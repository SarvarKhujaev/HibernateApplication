package com.hibernate.hibernateapplication.entities;

import com.beeline.beelineapplication.inspectors.LogInspector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class User extends LogInspector {
    public UUID getId() {
        return this.id;
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

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setId( final UUID id ) {
        this.id = id;
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

    public void setCreatedDate( final Date createdDate ) {
        this.createdDate = createdDate;
    }

    public void setPhoneNumber ( final String phoneNumber ) {
        this.phoneNumber = phoneNumber;
    }

    private String name;
    private String email;
    private String surname;
    private String phoneNumber;

    private UUID id;

    // дата создания аккаунта
    private Date createdDate;

    public static User generate (
            final ResultSet resultSet
    ) {
        return new User( resultSet );
    }

    private User (
            final ResultSet resultSet
    ) {
        try {
            this.setId( resultSet.getObject( "id", UUID.class ) );
            this.setCreatedDate( resultSet.getDate( "created_date" ) );

            this.setName( resultSet.getString( "name" ) );
            this.setEmail( resultSet.getString( "email" ) );
            this.setSurname( resultSet.getString( "surname" ) );
            this.setPhoneNumber( resultSet.getString( "phoneNumber" ) );
        } catch ( final SQLException exception ) {
            super.logging( exception );
        }
    }

    public User () {}
}
