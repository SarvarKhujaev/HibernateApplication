package com.hibernate.hibernateapplication.database;

import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.MetadataSources;

import com.hibernate.hibernateapplication.inspectors.Archieve;
import com.hibernate.hibernateapplication.entities.*;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.Validation;

import java.util.List;
import java.util.Set;

public final class HibernateConnector extends Archieve {
    private final Session session;
    private final SessionFactory sessionFactory;
    private final ValidatorFactory validatorFactory;
    private final StandardServiceRegistry registry;

    private static HibernateConnector connector = new HibernateConnector();

    public static HibernateConnector getInstance() {
        return connector != null ? connector : ( connector = new HibernateConnector() );
    }

    private Transaction newTransaction () {
        return this.session.beginTransaction();
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
        this.sessionFactory = new MetadataSources( this.registry )
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
        this.session = this.sessionFactory.openSession();

        /*
        создаем instance для валидации объектов
         */
        this.validatorFactory = Validation.buildDefaultValidatorFactory();

        /*
        настраиваем Second Level Cache
        */
        this.sessionFactory.getCache().evictEntityData( User.class );
        this.sessionFactory.getCache().evictEntityData( Order.class );
        this.sessionFactory.getCache().evictEntityData( Product.class );
        this.sessionFactory.getCache().evictEntityData( Student.class );

        super.logging( this.getClass() );
    }

    public void save ( final User user ) {
        final Set< ConstraintViolation< User > > violations = super.checkEntityValidation(
                this.validatorFactory.getValidator(),
                user
        );

        if ( !super.isCollectionNotEmpty( violations ) ) {
            final Transaction transaction = this.newTransaction();

            this.session.persist( user );

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
                this.validatorFactory.getValidator(),
                object
        );

        if ( !super.isCollectionNotEmpty( violations ) ) {
            final Transaction transaction = this.newTransaction();

            this.session.save( object );

            transaction.commit();
        } else {
            super.analyze(
                    violations,
                    userConstraintViolation -> super.logging( userConstraintViolation.getMessage() )
            );
        }

    }

    public void update ( final Order order ) {
        final User user = this.session.get( User.class, 1L );
        order.setUser( user );

        final List< Product > products = this.session.createQuery(
                """
                FROM products
                """
        ).list();

        order.setProductList( products );
        order.initializeProductList();

        final Transaction transaction = this.newTransaction();

        this.session.persist( order );

        super.analyze(
                order.getProductList(),
                this.session::update
        );

        transaction.commit();
    }

    public void update () {
        final User user = this.session.get( User.class, 1L );

        final Order order = this.session.get( Order.class, 1L );

        user.removeNewOrder( order );

        final Transaction transaction = this.newTransaction();

        this.session.persist( user );

        transaction.commit();
    }

    public void delete () {
        final User user = this.session.get( User.class, 1L );

        final Transaction transaction = this.newTransaction();

        this.session.delete( user );

        transaction.commit();

        System.out.println( transaction.getStatus() );
    }

    public void test () {
        final Transaction transaction = this.newTransaction();

        final ScrollableResults< Product > scrollableResults = this.session.createQuery(
                        """
                        FROM products
                        """
                ).setCacheMode( CacheMode.IGNORE )
                .scroll( ScrollMode.FORWARD_ONLY );

        while ( scrollableResults.next() ) {
            final Product product = scrollableResults.get();
            System.out.println( product.getId() );
        }

        transaction.commit();
        scrollableResults.close();
    }

    public void close () {
        this.session.clear();
        this.session.close();
        this.sessionFactory.close();
        StandardServiceRegistryBuilder.destroy( this.registry );

        super.logging( this );
    }
}
