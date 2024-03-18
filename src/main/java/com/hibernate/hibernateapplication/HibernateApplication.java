package com.hibernate.hibernateapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.hibernate.hibernateapplication.database.HibernateConnector;

@SpringBootApplication
public class HibernateApplication {
    public static void main( final String[] args ) {
//        SpringApplication.run( HibernateApplication.class, args );
        HibernateConnector.getInstance().checkNewNativeQuery();
        HibernateConnector.getInstance().close();
    }
}
