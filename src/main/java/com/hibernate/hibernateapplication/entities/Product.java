package com.hibernate.hibernateapplication.entities;

import com.beeline.beelineapplication.constants.Categories;
import com.beeline.beelineapplication.inspectors.LogInspector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public final class Product extends LogInspector {
    public UUID getId() {
        return this.id;
    }

    public long getPrice() {
        return this.price;
    }

    public String getDescription() {
        return this.description;
    }

    public String getProductName() {
        return this.productName;
    }

    public Categories getCategory() {
        return this.category;
    }

    public void setId ( final UUID id ) {
        this.id = id;
    }

    public void setPrice ( final long price ) {
        this.price = price;
    }

    public void setTotalCount ( final int totalCount ) {
        this.totalCount = totalCount;
    }

    public void setCreatedDate ( final Date createdDate ) {
        this.createdDate = createdDate;
    }

    public void setCategory ( final Categories category ) {
        this.category = category;
    }

    public void setDescription ( final String description ) {
        this.description = description;
    }

    public void setProductName ( final String productName ) {
        this.productName = productName;
    }

    public void setProductWasSoldCount ( final int productWasSoldCount ) {
        this.productWasSoldCount = productWasSoldCount;
    }

    private long price;

    // количество оставшихся товаров в хранилице
    private int totalCount;

    // количество проданных товаров
    private int productWasSoldCount;

    private String description; // описание товара
    private String productName; // название товара

    private UUID id;

    // дата создания товара
    private Date createdDate;

    private Categories category = Categories.FOR_MEN;

    public static Product generate (
            final ResultSet resultSet
    ) {
        return new Product( resultSet );
    }

    private Product (
            final ResultSet resultSet
    ) {
        try {
            this.setId( resultSet.getObject( "id", UUID.class ) );

            this.setPrice( resultSet.getLong( "price" ) );
            this.setTotalCount( resultSet.getInt( "totalCount" ) );
            this.setProductWasSoldCount( resultSet.getInt( "productWasSoldCount" ) );

            this.setProductName( resultSet.getString( "productName" ) );
            this.setDescription( resultSet.getString( "description" ) );

            this.setCreatedDate( resultSet.getDate( "created_date" ) );
            this.setCategory( resultSet.getObject( "category", Categories.class ) );
        } catch ( final SQLException exception ) {
            super.logging( exception );
        }
    }
}
