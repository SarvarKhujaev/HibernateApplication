package com.hibernate.hibernateapplication;

import com.hibernate.hibernateapplication.entities.User;
import com.hibernate.hibernateapplication.entities.Order;
import com.hibernate.hibernateapplication.entities.Product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.hibernate.hibernateapplication.database.HibernateConnector;


@SpringBootApplication
public class HibernateApplication {
    public static void main( final String[] args ) {
//        SpringApplication.run( HibernateApplication.class, args );

        final User user = new User();

        user.setName( "tesr" );
        user.setEmail( "tesr@gmail.com" );
        user.setSurname( "tesr" );
        user.setPhoneNumber( "tesr" );

//        user.addNewOrder( new Order() );
//        user.addNewOrder( new Order() );
//        user.addNewOrder( new Order() );
//        user.addNewOrder( new Order() );

        final Product product = new Product();

        product.setDescription( "test" );
        product.setProductName( "test" );

        product.setPrice( 500L );
        product.setTotalCount( 500L );

        HibernateConnector.getInstance().save( user );
//        HibernateConnector.getInstance().test();

//        HibernateConnector.getInstance().update( new Order() );

        HibernateConnector.getInstance().close();
    }
}
