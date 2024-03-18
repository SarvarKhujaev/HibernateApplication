package com.hibernate.hibernateapplication.entities;

import com.hibernate.hibernateapplication.constans.hibernate.HibernateNativeNamedQueries;
import com.hibernate.hibernateapplication.inspectors.TimeInspector;
import com.hibernate.hibernateapplication.constans.ErrorMessages;
import com.hibernate.hibernateapplication.constans.OrderStatus;

import org.hibernate.annotations.CacheModeType;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity( name = "orders" )
@Table( name = "orders", schema = "entities" )
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
        resultClass = OrderStatusCount.class
)
public class Order extends TimeInspector {
    public Long getId() {
        return this.id;
    }

    public List< Product > getProductList() {
        return this.productList;
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

    public void setUser( final User user ) {
        this.user = user;
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

    public void setProductList ( final List< Product > productList ) {
        this.productList = productList.stream().filter( product -> product.getTotalCount() > 0 ).toList();
    }

    public void setTotalOrderSum ( final long totalOrderSum ) {
        this.totalOrderSum = totalOrderSum;
    }

    public int getTotalCountOfProductsInOrder() {
        return this.totalCountOfProductsInOrder;
    }

    public void setTotalCountOfProductsInOrder ( final int totalCountOfProductsInOrder ) {
        this.totalCountOfProductsInOrder = totalCountOfProductsInOrder;
    }

    public OrderStatus getOrderStatus() {
        return this.orderStatus;
    }

    public void setOrderStatus ( final OrderStatus orderStatus ) {
        this.orderStatus = orderStatus;
    }

    public void setId ( final Long id ) {
        this.id = id;
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
    @Column( nullable = false, columnDefinition = "TIMESTAMP DEFAULT now()", name = "created_date" )
    private final Date createdDate = super.newDate(); // дата создания аккаунта

    @NotNull( message = ErrorMessages.NULL_VALUE )
    @ManyToOne( fetch = FetchType.LAZY )
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
    private List< Product > productList = super.newList();

    public Order () {}
}
