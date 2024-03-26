package com.hibernate.hibernateapplication.database;

import com.hibernate.hibernateapplication.constans.PostgresBufferMethods;
import com.hibernate.hibernateapplication.constans.PostgresVacuumMethods;
import com.hibernate.hibernateapplication.constans.hibernate.HibernateNativeNamedQueries;
import com.hibernate.hibernateapplication.interfaces.ServiceCommonMethods;
import com.hibernate.hibernateapplication.constans.PostgreSqlTables;
import com.hibernate.hibernateapplication.constans.OrderStatus;
import com.hibernate.hibernateapplication.inspectors.Archive;
import com.hibernate.hibernateapplication.entities.*;

import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.MetadataSources;

import org.hibernate.stat.CacheRegionStatistics;

import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.*;

import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.Validation;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

public final class HibernateConnector extends Archive implements ServiceCommonMethods {
    public Session getSession() {
        return this.session;
    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    public StandardServiceRegistry getRegistry() {
        return this.registry;
    }

    public ValidatorFactory getValidatorFactory() {
        return this.validatorFactory;
    }

    private final Session session;
    private final SessionFactory sessionFactory;
    private final StandardServiceRegistry registry;
    private final ValidatorFactory validatorFactory;

    private static HibernateConnector connector = new HibernateConnector();

    public static HibernateConnector getInstance() {
        return connector != null ? connector : ( connector = new HibernateConnector() );
    }

    private Transaction newTransaction () {
        return this.getSession().beginTransaction();
    }

    private HibernateConnector () {
        /*
        оегистрируем все настройки
        */
        this.registry = new StandardServiceRegistryBuilder()
                .applySettings( super.dbSettings )
                .build();

        /*
        подключаемся к самой БД
        */
        this.sessionFactory = new MetadataSources( this.getRegistry() )
                .addAnnotatedClass( User.class )
                .addAnnotatedClass( Order.class )
                .addAnnotatedClass( Product.class )
                .addAnnotatedClass( Student.class )
                .getMetadataBuilder()
                .build()
                .getSessionFactoryBuilder()
                .build();

        /*
        открываем сессию
        */
        this.session = this.getSessionFactory().openSession();

        /*
        создаем instance класса Validation для валидации объектов
         */
        this.validatorFactory = Validation.buildDefaultValidatorFactory();

        /*
        настраиваем Second Level Cache
        */
        this.getSessionFactory().getCache().evictEntityData( User.class );
        this.getSessionFactory().getCache().evictEntityData( Order.class );
        this.getSessionFactory().getCache().evictEntityData( Product.class );

        /*
        Hibernate specific JDBC batch size configuration on a per-Session basis
         */
        this.getSession().setJdbcBatchSize( super.BATCH_SIZE );

        this.prewarmTable();

        this.insertTableContentToBuffer();

        super.logging( this.getClass() );
    }

    @Override
    public void vacuumTable () {
        super.analyze(
                super.getTablesList(),
                table -> super.logging(
                        table
                                + " was cleaned: "
                                + this.getSession().createNativeQuery(
                                MessageFormat.format(
                                        """
                                        VACUUM( {0}, {1} ) {2};
                                        """,
                                        PostgresVacuumMethods.ANALYZE,
                                        PostgresVacuumMethods.VERBOSE,
                                        table
                                )
                        ).executeUpdate()
                )
        );
    }

    @Override
    public void prewarmTable () {
        /*
        загружаем список таблиц
        */
        this.getSession().createNativeQuery(
                PostgresBufferMethods.PREWARM_TABLE
        ).executeUpdate();
    }

    @Override
    public void insertTableContentToBuffer () {
        final Transaction transaction = this.newTransaction();

        super.analyze(
                super.getTablesList(),
                table -> super.logging(
                        table
                                + " was inserted into buffer: "
                                + this.getSession().createNativeQuery(
                                PostgresBufferMethods.INSERT_TABLE_CONTENT_INTO_BUFFER.formatted(
                                        table
                                )
                        ).executeUpdate()
                )
        );

        transaction.commit();
        super.logging( transaction );
    }

    private void checkBatchLimit () {
        if ( super.isBatchLimitNotOvercrowded() ) {
            /*
            если да, то освобождаем пространство в кеше
            на уровне first-level cache

            When you make new objects persistent, employ methods flush() and clear() to the session regularly,
            to control the size of the first-level cache.
             */
            this.getSession().flush();
            this.getSession().clear();
        }
    }

    public void save ( final User user ) {
        final Set< ConstraintViolation< User > > violations = super.checkEntityValidation(
                this.getValidatorFactory().getValidator(),
                user
        );

        if ( !super.isCollectionNotEmpty( violations ) ) {
            final Transaction transaction = this.newTransaction();

            this.getSession().persist( user );

            transaction.commit();
        } else {
            super.analyze(
                    violations,
                    userConstraintViolation -> super.logging( userConstraintViolation.getMessage() )
            );
        }
    }

    public void save ( final Product object ) {
        final Set< ConstraintViolation< Product > > violations = super.checkEntityValidation(
                this.getValidatorFactory().getValidator(),
                object
        );

        if ( !super.isCollectionNotEmpty( violations ) ) {
            final Transaction transaction = this.newTransaction();

            this.getSession().save( object );

            transaction.commit();
        } else {
            super.analyze(
                    violations,
                    userConstraintViolation -> super.logging( userConstraintViolation.getMessage() )
            );
        }

    }

    public void update ( final Order order ) {
        final User user = this.getSession().get( User.class, 1L );

        order.setUser( user );

        final List< Product > products = this.getSession().createQuery(
                """
                FROM products
                """
        ).list();

        order.setProductList( products );
        order.initializeProductList();

        final Transaction transaction = this.newTransaction();

        this.getSession().persist( order );

        transaction.commit();
    }

    public void update () {
        final User user = this.getSession().get( User.class, 1L );

        final Order order = this.getSession().get( Order.class, 1L );

        user.removeNewOrder( order );

        final Transaction transaction = this.newTransaction();

        this.getSession().persist( user );

        transaction.commit();
    }

    public void delete () {
        final User user = this.getSession().get( User.class, 1L );

        final Transaction transaction = this.newTransaction();

        this.getSession().delete( user );

        transaction.commit();

        super.logging( transaction.getStatus() );
    }

    public void test () {
        final Transaction transaction = this.newTransaction();

        final ScrollableResults< Product > scrollableResults = this.getSession().createQuery(
                        """
                        FROM products
                        """
                ).setCacheMode( CacheMode.GET )
                .scroll( ScrollMode.FORWARD_ONLY );

        int operationsCounter = 0;

        while ( scrollableResults.next() ) {
            this.checkBatchLimit();

            super.logging( scrollableResults.get().getId() );
        }

        transaction.commit();
        scrollableResults.close();
    }

    public void get () {
        final Query< Order > orderQuery = this.getSession().createQuery(
                "FROM orders WHERE user.id = :user_id ORDER BY createdDate",
                Order.class
        );

        orderQuery.setFirstResult( 5 );
        orderQuery.setMaxResults( 100 );

        orderQuery.setCacheMode( CacheMode.GET );
        orderQuery.setComment( "Select all orders for current user" );

        orderQuery.setParameter( "user_id", 10 );

        super.analyze(
                orderQuery.getResultList(),
                order -> super.logging( order.getId() )
        );
    }

    public void getWithCriteria () {
        final CriteriaBuilder builder = this.getSession().getCriteriaBuilder();

        final CriteriaQuery< Order > criteriaQuery = builder.createQuery( Order.class );

        final Root< Order > root = criteriaQuery.from( Order.class );

        /*
        готовим параметры для запроса
         */
        final ParameterExpression< Long > idParam = builder.parameter( Long.class );
        final ParameterExpression< OrderStatus > orderStatusParam = builder.parameter( OrderStatus.class );

        criteriaQuery
                .distinct( true )
                .multiselect( root.get( "id" ), root.get( "orderStatus" ) )
                .where(
                        builder.or(
                                builder.and(
                                        root.get( "totalOrderSum" ).isNotNull(),
                                        builder.equal( root.get( "id" ), idParam )
                                ),
                                builder.and(
                                        builder.equal( root.get( "orderStatus" ), orderStatusParam )
                                )
                        )
                );

        super.analyze(
                this.getSession()
                        .createQuery( criteriaQuery )
                        .setParameter( idParam, 5L )
                        .setParameter( orderStatusParam, OrderStatus.CREATED )
                        .getResultList(),
                order -> super.logging( order.getCreatedDate().toString() )
        );
    }

    public void getWithNativeQuery () {
        /*
        To avoid the overhead of using ResultSetMetadata, or simply to be more explicit in what is returned, one can use addScalar():

        Example;
            List<Object[]> persons = session.createNativeQuery(
                "SELECT * FROM Person", Object[].class)
            .addScalar("id", StandardBasicTypes.LONG)
            .addScalar("name", StandardBasicTypes.STRING)
            .list();
         */
        super.analyze(
                this.getSession().createNativeQuery(
                        MessageFormat.format(
                                """
                                SELECT * FROM {0}.{1}
                                WHERE id = {2};
                                """,
                                "entities",
                                "orders",
                                5
                        ),
                        Object[].class
                ).addScalar( "id", Long.class )
                        .addScalar( "total_order_sum", Long.class )
                        .getResultList(),
                objects -> super.logging( objects[0] + " : " + objects[1] )
        );
    }

    public void joinExample () {
        final NativeQuery< Order > nativeQuery = this.getSession().createNativeQuery(
                """
                SELECT {o.*}, {u.*}
                FROM entities.orders o
                JOIN entities.users u ON u.id = o.user_id;
                """,
                Order.class,
                "o"
        );

        nativeQuery.addJoin( "u", "o.user" );

        nativeQuery.setTupleTransformer( ( tuple, aliases ) -> (Order) tuple[0] );

        super.analyze(
                nativeQuery.list(),
                order -> super.logging( order.getUser().getName() )
        );
    }

    public void insertUsers () {
        for ( int i = 0; i < 10; i++ ) {
            final User user = new User();

            user.setName( "test" );
            user.setEmail( "sarvar@gmail.com" + i );
            user.setSurname( "test" );
            user.setPhoneNumber( "+99897" + i );

            this.save( user );
        }
    }

    public void insertProducts () {
        for ( int i = 0; i < 10; i++ ) {
            final Product product = new Product();

            product.setProductName( "test" );
            product.setDescription( "test" );

            product.setPrice( i * 10000 + 1 );
            product.setTotalCount( i * 10000 + 1 );

            this.save( product );
        }
    }

    public void insertOrders () {
        final List< User > users = this.getSession().createQuery(
                """
                FROM USERS WHERE id IN ( 1, 2, 3, 4 )
                """
        ).getResultList();

        final List< Product > products = this.getSession().createQuery(
                """
                FROM PRODUCTS WHERE id IN ( 2, 3, 4, 5, 6, 7 )
                """
        ).getResultList();

        final Transaction transaction = this.newTransaction();

        int operationsCounter = 0;

        for ( final User user : users ) {
            for ( int j = 0; j < 5; j++ ) {
                final Order order = new Order();

                order.setProductList( products );
                order.initializeProductList();

                user.addNewOrder( order );

                this.checkBatchLimit();

                this.getSession().persist( order );

                super.analyze(
                        order.getProductList(),
                        this.session::update
                );
            }
        }

        transaction.commit();

        super.logging( transaction.getStatus() );
    }

    public void getUserOrders () {
        final Transaction transaction = this.newTransaction();

        final User user = this.getSession().get( User.class, 1L );

        super.analyze(
                user.getOrders(),
                order -> super.logging( order.getId() + " : " + order.getProductList().size() )
        );

        transaction.commit();
    }

    public void checkNewNativeQuery () {
        super.analyze(
                this.getSession().createNamedQuery(
                        HibernateNativeNamedQueries.PRODUCTS_GET_PRODUCT_WITH_RIGHT_STATUS_DUE_TO_COUNT,
                        ProductDescription.class
                ).setParameter( "order", "price DESC, createdDate ASC" )
                        .setParameter( "limit", 30 )
                        .setCacheable( true )
                        .setCacheMode( CacheMode.GET )
                        .setCacheRegion(
                                super.generateCacheName( PostgreSqlTables.ORDERS )
                        ).list(),

                productDescription -> super.logging(
                        productDescription.getId()
                        + " : "
                        + productDescription.getProductPriceSize()
                )
        );
    }

    public void getWithNaturalId () {
        final Student student = this.getSession()
                .byNaturalId( Student.class )
                .using( "email", "sarvar@gmail.com" )
                .load();

        this.getSession().find(
                Student.class, 1L
        );

        this.getSession().get( Student.class, 1L );
    }

    /*
    If you enable the hibernate.generate_statistics configuration property,
    Hibernate will expose a number of metrics via SessionFactory.getStatistics().
    Hibernate can even be configured to expose these statistics via JMX.

    This way, you can get access to the Statistics class which comprises all sort of second-level cache metrics.
    */
    public void readCacheStatistics () {
        final CacheRegionStatistics regionStatistics = this.getSession()
                .getSessionFactory()
                .getStatistics()
                .getDomainDataRegionStatistics(
                        super.generateCacheName(
                                PostgreSqlTables.ORDERS
                        )
                );

        super.logging(
                regionStatistics.getRegionName()
        );

        super.logging(
                regionStatistics.getHitCount()
        );

        super.logging(
                regionStatistics.getMissCount()
        );

        super.logging(
                regionStatistics.getSizeInMemory()
        );
    }

    /*
    закрывам все соединения и instance
    */
    @Override
    public synchronized void close () {
        this.vacuumTable();
        this.getSession().clear();
        this.getSession().close();
        this.getSessionFactory().close();
        this.getValidatorFactory().close();
        StandardServiceRegistryBuilder.destroy( this.getRegistry() );

        connector = null;

        super.logging( this );
    }
}
