package com.hibernate.hibernateapplication.entities;

import com.hibernate.hibernateapplication.constans.PostgreSqlSchema;
import com.hibernate.hibernateapplication.constans.PostgreSqlTables;
import com.hibernate.hibernateapplication.inspectors.TimeInspector;
import com.hibernate.hibernateapplication.constans.ErrorMessages;

import org.hibernate.type.NumericBooleanConverter;
import org.hibernate.annotations.*;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Table;
import jakarta.persistence.*;

import java.util.Date;

@Entity( name = PostgreSqlTables.LOG )
@Table( name = PostgreSqlTables.LOG, schema = PostgreSqlSchema.ENTITIES )
@Cacheable
@org.hibernate.annotations.Cache(
        usage = CacheConcurrencyStrategy.READ_ONLY
)
@Immutable
@SoftDelete(
        /*
        YesNoConverter -> Defines conversion using 'N' for false and 'Y' for true
        TrueFalseConverter -> Defines conversion using 'F' for false and 'T' for true
        NumericBooleanConverter -> Defines conversion using 0 for false and 1 for true
         */
        strategy = SoftDeleteType.DELETED,
        converter = NumericBooleanConverter.class,
        columnName = "order_is_dead"
)
public class Logs extends TimeInspector {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    private Long id;

    @PartitionKey
    @NotNull( message = ErrorMessages.NULL_VALUE )
    @Column( nullable = false, columnDefinition = "TIMESTAMP DEFAULT now()", name = "created_date" )
    private final Date createdDate = super.newDate(); // дата создания аккаунта

    @NotNull
    @Column( nullable = false, columnDefinition = "TEXT NOT NULL" )
    private String message;
}
