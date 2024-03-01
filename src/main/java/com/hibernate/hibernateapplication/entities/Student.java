package com.hibernate.hibernateapplication.entities;

import com.hibernate.hibernateapplication.constans.Status;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity( name = "student" )
@Table( name = "student", schema = "school" )
public class Student {
    @Column( nullable = false, length = 50 )
    private String name;
    @Column( nullable = false, length = 50 )
    private String surname;
    @Column( nullable = false, length = 50 )
    private String groupName;
    @Column( nullable = false, length = 50 )
    private String fatherName;

    @Column( nullable = false, columnDefinition = "SMALLINT DEFAULT 18" )
    private byte age;

    @Column( nullable = false, columnDefinition = "FLOAT4 DEFAULT 5.0" )
    private float averagePoint;

    @Id
    @Column( nullable = false, updatable = false, columnDefinition = "UUID DEFAULT uuid_generate_v4()" )
    private UUID studentId;

    @Column( nullable = false, columnDefinition = "TIMESTAMP DEFAULT now()", name = "registered_date" )
    private Date registeredDate;

    // https://www.baeldung.com/jpa-default-column-values
    @Enumerated( value = EnumType.STRING )
    private Status status;
}
