package com.hibernate.hibernateapplication.entities;

import com.hibernate.hibernateapplication.constans.hibernate.HibernateNativeNamedQueries;
import com.hibernate.hibernateapplication.constans.PostgreSqlSchema;
import com.hibernate.hibernateapplication.constans.PostgreSqlTables;
import com.hibernate.hibernateapplication.inspectors.TimeInspector;
import com.hibernate.hibernateapplication.constans.ErrorMessages;
import com.hibernate.hibernateapplication.constans.Categories;

import org.hibernate.annotations.CacheModeType;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;

import java.util.Date;

@Entity( name = PostgreSqlTables.PRODUCTS )
@Table( name = PostgreSqlTables.PRODUCTS, schema = PostgreSqlSchema.ENTITIES )
@Cacheable
@SqlResultSetMappings(
        value = {
                @SqlResultSetMapping(
                        name = HibernateNativeNamedQueries.PRODUCTS_GET_PRODUCT_WITH_RIGHT_STATUS_DUE_TO_COUNT_SETTER,
                        classes = {
                                @ConstructorResult(
                                        targetClass = ProductDescription.class,
                                        columns = {
                                                @ColumnResult(
                                                        name = "id"
                                                ),
                                                @ColumnResult(
                                                        name = "price"
                                                ),
                                                @ColumnResult(
                                                        name = "createdDate"
                                                ),
                                                @ColumnResult(
                                                        name = "productPriceSize"
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
                        name = HibernateNativeNamedQueries.PRODUCTS_GET_PRODUCT_WITH_RIGHT_STATUS_DUE_TO_COUNT,

                        query = """
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
                            """,

                        timeout = 1,
                        readOnly = true,
                        cacheable = true,
                        cacheMode = CacheModeType.GET,
                        resultSetMapping = HibernateNativeNamedQueries.PRODUCTS_GET_PRODUCT_WITH_RIGHT_STATUS_DUE_TO_COUNT_SETTER
                )
        }
)
public class Product extends TimeInspector {
    public void setId ( final Long id ) {
        this.id = id;
    }

    public void setPrice ( final long price ) {
        this.price = price;
    }

    public void setCategory ( final Categories category ) {
        this.category = category;
    }

    public void setTotalCount ( final long totalCount ) {
        this.totalCount = totalCount;
    }

    public void setDescription ( final String description ) {
        this.description = description;
    }

    public void setProductName ( final String productName ) {
        this.productName = productName;
    }

    public void setProductWasSoldCount ( final long productWasSoldCount ) {
        this.productWasSoldCount = productWasSoldCount;
    }

    public long getId() {
        return this.id;
    }

    public long getPrice() {
        return this.price;
    }

    public long getTotalCount() {
        return this.totalCount;
    }

    public Date getCreatedDate() {
        return this.createdDate;
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

    public long getProductWasSoldCount() {
        return this.productWasSoldCount;
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
