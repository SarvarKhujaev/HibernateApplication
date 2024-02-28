package com.hibernate.hibernateapplication.entities;

import com.beeline.beelineapplication.constants.OrderStatus;
import com.beeline.beelineapplication.inspectors.LogInspector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public final class Order extends LogInspector {
    public UUID getUserId() {
        return this.userId;
    }

    public List< Product > getProductList() {
        return this.productList;
    }

    public void setId ( final UUID id ) {
        this.id = id;
    }

    public void setUserId ( final UUID userId ) {
        this.userId = userId;
    }

    public void setCreatedDate ( final Date createdDate ) {
        this.createdDate = createdDate;
    }

    public void setOrderStatus( final OrderStatus orderStatus ) {
        this.orderStatus = orderStatus;
    }

    public void setProductList ( final List< Product > productList ) {
        this.productList = productList;
    }

    public void setTotalOrderSum( final int totalOrderSum ) {
        this.totalOrderSum = totalOrderSum;
    }

    public void setTotalCountOfProductsInOrder( final int totalCountOfProductsInOrder ) {
        this.totalCountOfProductsInOrder = totalCountOfProductsInOrder;
    }

    // общая стоимость заказа
    private int totalOrderSum;

    // общее количество товаров в заказе
    private int totalCountOfProductsInOrder;

    private UUID id;

    // id пользователя который сделал заказ
    private UUID userId;

    // дата создания заказа
    private Date createdDate;

    private OrderStatus orderStatus = OrderStatus.CREATED;

    private List< Product > productList;

    public static Order generate (
            final ResultSet resultSet
    ) {
        return new Order( resultSet );
    }

    private Order ( final ResultSet resultSet ) {
        try {
            this.setId( resultSet.getObject( "id", UUID.class ) );
            this.setUserId( resultSet.getObject( "user_id", UUID.class ) );

            this.setCreatedDate( resultSet.getDate( "created_date" ) );

            this.setTotalOrderSum( resultSet.getInt( "total_order_sum" ) );
            this.setTotalCountOfProductsInOrder( resultSet.getInt( "total_count_of_products_in_order" ) );

            this.setProductList( resultSet.getObject( "product_list", List.class ) );
            this.setOrderStatus( resultSet.getObject( "orderStatus", OrderStatus.class ) );
        } catch ( final SQLException exception ) {
            super.logging( exception );
        }
    }
}
