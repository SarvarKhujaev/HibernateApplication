package com.hibernate.hibernateapplication.entities;

import java.util.Date;

public final class ProductDescription {
    public Long getId() {
        return this.id;
    }

    public void setId ( final Long id ) {
        this.id = id;
    }

    public Long getPrice() {
        return this.price;
    }

    public void setPrice ( final Long price ) {
        this.price = price;
    }

    public Date getCreateaDate() {
        return this.createaDate;
    }

    public void setCreateaDate ( final Date createaDate ) {
        this.createaDate = createaDate;
    }

    public String getProductPriceSize() {
        return this.productPriceSize;
    }

    public void setProductPriceSize ( final String productPriceSize ) {
        this.productPriceSize = productPriceSize;
    }

    private Long id;
    private Long price;

    private Date createaDate;
    private String productPriceSize;

    public ProductDescription () {}

    public ProductDescription(
            final Long id,
            final Long price,
            final Date createaDate,
            final String productPriceSize
    ) {
        this.id = id;
        this.price = price;
        this.createaDate = createaDate;
        this.productPriceSize = productPriceSize;
    }
}
