package com.hibernate.hibernateapplication.entities;

import com.hibernate.hibernateapplication.constans.PostgreSqlFunctions;
import com.hibernate.hibernateapplication.constans.PostgreSqlSchema;
import com.hibernate.hibernateapplication.constans.PostgreSqlTables;
import com.hibernate.hibernateapplication.inspectors.TimeInspector;
import com.hibernate.hibernateapplication.constans.ErrorMessages;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;

import jakarta.persistence.Table;
import jakarta.persistence.*;

import org.hibernate.annotations.*;
import java.util.Date;

@Entity( name = PostgreSqlTables.STUDENTS )
@Table( name = PostgreSqlTables.STUDENTS, schema = PostgreSqlSchema.ENTITIES )
@Cacheable
@org.hibernate.annotations.Cache(
        usage = CacheConcurrencyStrategy.READ_ONLY
)
public class Student extends TimeInspector {
    public Long getId() {
        return this.id;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }

    public String getEmail() {
        return this.email;
    }

    public void setId ( final Long id ) {
        this.id = id;
    }

    public void setEmail ( final String email ) {
        this.email = email;
    }

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
    private final Date createdDate = super.newDate(); // дата создания аккаунта

    @Email( message = ErrorMessages.WRONG_EMAIL )
    @NotNull( message = ErrorMessages.NULL_VALUE )
    /*
    Natural ids represent domain model unique identifiers that have a meaning in the real world too.
    Even if a natural id does not make a good primary key (surrogate keys being usually preferred),
    it’s still useful to tell Hibernate about it.
    As we will see later, Hibernate provides a dedicated,
    efficient API for loading an entity by its natural id much like it offers for loading by identifier (PK).

    All values used in a natural id must be non-nullable.

    For natural id mappings using a to-one association, this precludes the use of not-found mappings which effectively define a nullable mapping.
    */
    @NaturalId
    @Column( name = "email", unique = true, nullable = false, updatable = false )
    private String email;

    public Student () {}
}


