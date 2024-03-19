package com.hibernate.hibernateapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.hibernate.hibernateapplication.database.HibernateConnector;

@SpringBootApplication
public class HibernateApplication {
    public static ApplicationContext context;

    public static void main( final String[] args ) {
        context = SpringApplication.run( HibernateApplication.class, args );
//        HibernateConnector.getInstance().insertUsers();
//        HibernateConnector.getInstance().insertProducts();
//        HibernateConnector.getInstance().insertOrders();
//        HibernateConnector.getInstance().getUserOrders();
        HibernateConnector.getInstance().checkNewNativeQuery();
        HibernateConnector.getInstance().close();
    }
}
