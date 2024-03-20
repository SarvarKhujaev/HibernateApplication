package com.hibernate.hibernateapplication.entities;

import com.hibernate.hibernateapplication.constans.OrderStatus;

public final class OrderStatusCount {
    public Long getTotalCount() {
        return this.totalCount;
    }

    public OrderStatus getOrderStatus() {
        return this.orderStatus;
    }

    public void setTotalCount ( final Long totalCount ) {
        this.totalCount = totalCount;
    }

    public void setOrderStatus ( final OrderStatus orderStatus ) {
        this.orderStatus = orderStatus;
    }

    private Long totalCount;

    private OrderStatus orderStatus;

    public OrderStatusCount () {}

    public OrderStatusCount(
            final OrderStatus orderStatus,
            final Long totalCount
    ) {
        this.totalCount = totalCount;
        this.orderStatus = orderStatus;
    }
}
