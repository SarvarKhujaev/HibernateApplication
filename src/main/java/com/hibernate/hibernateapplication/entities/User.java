package com.hibernate.hibernateapplication.entities;

import com.hibernate.hibernateapplication.constans.PostgreSqlFunctions;
import com.hibernate.hibernateapplication.constans.PostgreSqlSchema;
import com.hibernate.hibernateapplication.constans.PostgreSqlTables;
import com.hibernate.hibernateapplication.inspectors.TimeInspector;
import com.hibernate.hibernateapplication.constans.ErrorMessages;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.PartitionKey;
import org.hibernate.annotations.Immutable;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity( name = PostgreSqlTables.USERS )
@Table( name = PostgreSqlTables.USERS, schema = PostgreSqlSchema.ENTITIES )
@Cacheable // настраиваем Second Level Cache
@org.hibernate.annotations.Cache(
        usage = CacheConcurrencyStrategy.READ_ONLY
)
public class User extends TimeInspector {
    public List< Order > getOrders() {
        return this.orders;
    }

    public void setName ( final String name ) {
        this.name = name;
    }

    public void setEmail ( final String email ) {
        this.email = email;
    }

    public void setSurname ( final String surname ) {
        this.surname = surname;
    }

    public void setPhoneNumber ( final String phoneNumber ) {
        this.phoneNumber = phoneNumber;
    }

    public void addNewOrder ( final Order order ) {
        this.getOrders().add( order );
        order.setUser( this );
    }

    public void removeNewOrder ( final Order order ) {
        this.getOrders().remove( order );
        order.setUser( null );
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getSurname() {
        return this.surname;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Long getId() {
        return this.id;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    @Size(
            min = 3,
            max = 50,
            message = ErrorMessages.VALUE_OUT_OF_RANGE
    )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @NotBlank( message = ErrorMessages.NULL_VALUE )
    @Column( nullable = false, length = 20 )
    private String name;

    @Size(
            min = 3,
            max = 30,
            message = ErrorMessages.VALUE_OUT_OF_RANGE
    )
    @Email( message = ErrorMessages.WRONG_EMAIL )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @NotBlank( message = ErrorMessages.NULL_VALUE )
    @Column(
            nullable = false,
            unique = true,
            length = 30
    )
    private String email;

    @Size(
            min = 3,
            max = 20,
            message = ErrorMessages.VALUE_OUT_OF_RANGE
    )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @NotBlank( message = ErrorMessages.NULL_VALUE )
    @Column( nullable = false, length = 20 )
    private String surname;

    @Size(
            min = 3,
            max = 20,
            message = ErrorMessages.VALUE_OUT_OF_RANGE
    )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @NotBlank( message = ErrorMessages.NULL_VALUE )
    @Column(
            name = "phone_number",
            length = 20,
            unique = true,
            nullable = false
    )
    private String phoneNumber;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @Immutable
    @PartitionKey
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Column(
            name = "created_date",
            nullable = false,
            updatable = false,
            columnDefinition = PostgreSqlFunctions.NOW
    )
    private final Date createdDate = super.newDate(); // дата создания

    @OneToMany(
            orphanRemoval = true,
            targetEntity = Order.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @JoinColumn( name = "user_id" )
    /*
    Hibernate can also cache collections, and the @Cache annotation must be on added to the collection property.

    If the collection is made of value types (basic or embeddables mapped with @ElementCollection),
    the collection is stored as such.
    If the collection contains other entities (@OneToMany or @ManyToMany),
    the collection cache entry will store the entity identifiers only.
    */
    @org.hibernate.annotations.Cache(
            usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE
    )
    @OrderBy( value = "totalOrderSum DESC, totalCountOfProductsInOrder DESC" )
    private List< Order > orders = super.newList();

    public User () {}
}
