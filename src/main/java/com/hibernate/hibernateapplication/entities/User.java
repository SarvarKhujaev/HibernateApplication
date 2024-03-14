package com.hibernate.hibernateapplication.entities;

import com.hibernate.hibernateapplication.inspectors.TimeInspector;
import com.hibernate.hibernateapplication.constans.ErrorMessages;

import jakarta.validation.constraints.*;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity( name = "users" )
@Table( name = "users", schema = "entities" )
@Cacheable // настраиваем First Level Cache
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

    @Size( min = 3, max = 50, message = ErrorMessages.VALUE_OUT_OF_RANGE )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @NotBlank( message = ErrorMessages.NULL_VALUE )
    @Column( nullable = false, length = 20 )
    private String name;

    @Size( min = 3, max = 30, message = ErrorMessages.VALUE_OUT_OF_RANGE )
    @Email( message = ErrorMessages.WRONG_EMAIL )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @NotBlank( message = ErrorMessages.NULL_VALUE )
    @Column( nullable = false, length = 30 )
    private String email;

    @Size( min = 3, max = 20, message = ErrorMessages.VALUE_OUT_OF_RANGE )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @NotBlank( message = ErrorMessages.NULL_VALUE )
    @Column( nullable = false, length = 20 )
    private String surname;

    @Size( min = 3, max = 20, message = ErrorMessages.VALUE_OUT_OF_RANGE )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @NotBlank( message = ErrorMessages.NULL_VALUE )
    @Column( nullable = false, length = 20, name = "phone_number" )
    private String phoneNumber;

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Column( nullable = false, columnDefinition = "TIMESTAMP DEFAULT now()", name = "created_date" )
    private final Date createdDate = super.newDate(); // дата создания аккаунта

    @OneToMany(
            orphanRemoval = true,
            targetEntity = Order.class,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    @OrderBy( value = "totalOrderSum DESC, totalCountOfProductsInOrder DESC" )
    @JoinColumn( name = "user_id" )
    private List< Order > orders = super.newList();

    public User () {}
}
