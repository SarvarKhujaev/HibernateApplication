package com.hibernate.hibernateapplication.entities;

import com.hibernate.hibernateapplication.inspectors.TimeInspector;
import com.hibernate.hibernateapplication.constans.ErrorMessages;
import com.hibernate.hibernateapplication.constans.Categories;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;

import java.util.Date;

@Entity( name = "products" )
@Table( name = "products", schema = "entities" )
@Cacheable
public class Product extends TimeInspector {
    public long getId() {
        return this.id;
    }

    public long getPrice() {
        return this.price;
    }

    public void setPrice ( final long price ) {
        this.price = price;
    }

    public long getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount ( final long totalCount ) {
        this.totalCount = totalCount;
    }

    public long getProductWasSoldCount() {
        return this.productWasSoldCount;
    }

    public void setProductWasSoldCount ( final long productWasSoldCount ) {
        this.productWasSoldCount = productWasSoldCount;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription ( final String description ) {
        this.description = description;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName ( final String productName ) {
        this.productName = productName;
    }

    public void setId ( final Long id ) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public Categories getCategory() {
        return this.category;
    }

    public void setCategory ( final Categories category ) {
        this.category = category;
    }

    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Positive( message = ErrorMessages.VALUE_MUST_BE_POSITIVE )
    @Column( nullable = false )
    private long price;

    // количество оставшихся товаров в хранилице
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Min( value = 0, message = ErrorMessages.VALUE_MUST_BE_POSITIVE )
    @Column( nullable = false, name = "total_count", columnDefinition = "BIGINT DEFAULT 0" )
    private long totalCount = 0;

    // количество проданных товаров
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Min( value = 0, message = ErrorMessages.VALUE_MUST_BE_POSITIVE )
    @Column( nullable = false, name = "product_was_sold_count", columnDefinition = "BIGINT DEFAULT 0" )
    private long productWasSoldCount = 0;

    @Size( min = 3, max = 50, message = ErrorMessages.VALUE_OUT_OF_RANGE )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @NotBlank( message = ErrorMessages.NULL_VALUE )
    @Column( nullable = false, length = 50 )
    private String description; // описание товара

    @Size( min = 3, max = 50, message = ErrorMessages.VALUE_OUT_OF_RANGE )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @NotBlank( message = ErrorMessages.NULL_VALUE )
    @Column( nullable = false, length = 20, name = "product_name" )
    private String productName; // название товара

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    // дата создания товара
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Column( nullable = false, columnDefinition = "TIMESTAMP DEFAULT now()", name = "created_date" )
    private final Date createdDate = super.newDate();

    // https://www.baeldung.com/jpa-default-column-values
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Column( nullable = false )
    @Enumerated( value = EnumType.STRING )
    private Categories category = Categories.FOR_MEN;

    public Product() {}
}
