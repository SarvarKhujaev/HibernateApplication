package com.hibernate.hibernateapplication.entities;

import com.hibernate.hibernateapplication.constans.hibernate.HibernateNativeNamedQueries;
import com.hibernate.hibernateapplication.constans.PostgreSqlSchema;
import com.hibernate.hibernateapplication.constans.PostgreSqlTables;
import com.hibernate.hibernateapplication.inspectors.TimeInspector;
import com.hibernate.hibernateapplication.constans.ErrorMessages;
import com.hibernate.hibernateapplication.constans.OrderStatus;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CacheModeType;

import org.hibernate.annotations.PartitionKey;
import org.hibernate.annotations.Immutable;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity( name = PostgreSqlTables.ORDERS )
@Table( name = PostgreSqlTables.ORDERS, schema = PostgreSqlSchema.ENTITIES )
@SqlResultSetMappings(
        value = {
                @SqlResultSetMapping(
                        name = HibernateNativeNamedQueries.ORDERS_GET_ORDERS_GROUP_VALUES_SETTER,
                        classes = {
                                @ConstructorResult(
                                        targetClass = OrderStatusCount.class,
                                        columns = {
                                                @ColumnResult(
                                                        name = "orderStatus"
                                                ),
                                                @ColumnResult(
                                                        name = "totalCount"
                                                )
                                        }
                                )
                        }
                )
        }
)
@org.hibernate.annotations.NamedNativeQueries(
        value = {
                @org.hibernate.annotations.NamedNativeQuery(
                        name = HibernateNativeNamedQueries.ORDERS_GET_ORDERS_GROUP_VALUES,

                        query = """
                            SELECT status AS orderStatus, COUNT() AS totalCount
                            FROM entities.orders
                            WHERE created_date < now()
                            GROUP BY ( orderStatus )
                            """,

                        timeout = 1,
                        readOnly = true,
                        cacheable = true,
                        cacheMode = CacheModeType.GET,
                        resultSetMapping = HibernateNativeNamedQueries.ORDERS_GET_ORDERS_GROUP_VALUES_SETTER
                )
        }
)
@Cacheable
@org.hibernate.annotations.Cache(
        usage = CacheConcurrencyStrategy.READ_WRITE
)
@Immutable
public class Order extends TimeInspector {
    public Long getId() {
        return this.id;
    }

    public User getUser() {
        return this.user;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public long getTotalOrderSum() {
        return this.totalOrderSum;
    }

    public OrderStatus getOrderStatus() {
        return this.orderStatus;
    }

    public List< Product > getProductList() {
        return this.productList;
    }

    public int getTotalCountOfProductsInOrder() {
        return this.totalCountOfProductsInOrder;
    }

    public void setUser( final User user ) {
        this.user = user;
    }

    public void setProductList ( final List< Product > productList ) {
        this.productList = productList.stream().filter( product -> product.getTotalCount() > 0 ).toList();
    }

    public void setOrderStatus ( final OrderStatus orderStatus ) {
        this.orderStatus = orderStatus;
    }

    public void setTotalOrderSum ( final long totalOrderSum ) {
        this.totalOrderSum = totalOrderSum;
    }

    public void setTotalCountOfProductsInOrder ( final int totalCountOfProductsInOrder ) {
        this.totalCountOfProductsInOrder = totalCountOfProductsInOrder;
    }

    public void initializeProductList () {
        this.setTotalCountOfProductsInOrder( this.getProductList().size() );

        this.getProductList()
                .stream()
                .map( Product::getPrice )
                .forEach( value -> this.setTotalOrderSum( this.getTotalOrderSum() + value ) );

        super.analyze(
                this.getProductList(),
                product -> {
                    product.setTotalCount( product.getTotalCount() - 1 );
                    product.setProductWasSoldCount( product.getProductWasSoldCount() + 1 );
                }
        );
    }

    // общая стоимость заказа
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Positive( message = ErrorMessages.VALUE_MUST_BE_POSITIVE)
    @Column( nullable = false, columnDefinition = "BIGINT", name = "total_order_sum" )
    private long totalOrderSum = 0;

    // общее количество товаров в заказе
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Positive( message = ErrorMessages.VALUE_MUST_BE_POSITIVE )
    @Column( nullable = false, columnDefinition = "SMALLINT", name = "total_count_of_products_in_order" )
    private int totalCountOfProductsInOrder = 0;

    // https://www.baeldung.com/jpa-default-column-values
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Enumerated( value = EnumType.STRING )
    @Column( name = "order_status", nullable = false )
    private OrderStatus orderStatus = OrderStatus.CREATED;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Column( nullable = false, columnDefinition = "TIMESTAMP DEFAULT now()", name = "created_date", updatable = false )
    @PartitionKey
    private final Date createdDate = super.newDate(); // дата создания аккаунта

    @NotNull( message = ErrorMessages.NULL_VALUE )
    @ManyToOne( fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    @PartitionKey
    private User user;

    @NotNull( message = ErrorMessages.NULL_VALUE )
    @OneToMany(
            orphanRemoval = true,
            targetEntity = Product.class,
            cascade = CascadeType.REFRESH,
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "order_id"
    )
    @OrderBy( value = "productName DESC, price DESC" )
    /*
    Hibernate can also cache collections, and the @Cache annotation must be on added to the collection property.

    If the collection is made of value types (basic or embeddables mapped with @ElementCollection),
    the collection is stored as such.
    If the collection contains other entities (@OneToMany or @ManyToMany),
    the collection cache entry will store the entity identifiers only.
    */
    @org.hibernate.annotations.Cache( usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE )
    private List< Product > productList = super.newList();

    public Order () {}
}
