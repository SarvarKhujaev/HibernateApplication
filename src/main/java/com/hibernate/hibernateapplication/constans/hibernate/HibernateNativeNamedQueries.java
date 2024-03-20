package com.hibernate.hibernateapplication.constans.hibernate;

public final class HibernateNativeNamedQueries {
    public static final String ORDERS_GET_ORDERS_GROUP_VALUES = "ORDERS_GET_ORDERS_GROUP_VALUES";

    public static final String ORDERS_GET_ORDERS_GROUP_VALUES_QUERY = """
                            SELECT status AS orderStatus, COUNT() AS totalCount
                            FROM entities.orders
                            WHERE created_date < now()
                            GROUP BY ( orderStatus )
                            """;

    public static final String ORDERS_GET_ORDERS_GROUP_VALUES_SETTER = "ORDERS_GET_ORDERS_GROUP_VALUES_SETTER";

    public static final String PRODUCTS_GET_PRODUCT_WITH_RIGHT_STATUS_DUE_TO_COUNT = "PRODUCTS_GET_PRODUCT_WITH_RIGHT_STATUS_DUE_TO_COUNT";

    public static final String PRODUCTS_GET_PRODUCT_WITH_RIGHT_STATUS_DUE_TO_COUNT_QUERY = """
                            SELECT id, price, created_date AS createdDate,

                            CASE
                            WHEN price > 1000 THEN 'expensive'
                            WHEN price BETWEEN 500 AND 100 THEN 'cheap'
                            WHEN price BETWEEN 1000 AND 500 THEN 'middle'
                            ELSE 'normal'
                            END productPriceSize

                            FROM entities.products
                            ORDER BY :order
                            LIMIT :limit
                            """;

    public static final String PRODUCTS_GET_PRODUCT_WITH_RIGHT_STATUS_DUE_TO_COUNT_SETTER = "PRODUCTS_GET_PRODUCT_WITH_RIGHT_STATUS_DUE_TO_COUNT_SETTER";
}
