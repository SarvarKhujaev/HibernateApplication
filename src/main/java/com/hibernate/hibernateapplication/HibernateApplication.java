package com.hibernate.hibernateapplication;

import org.junit.runner.JUnitCore;
import junit.extensions.RepeatedTest;
import junit.framework.JUnit4TestAdapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.hibernate.hibernateapplication.testing.JavaTest;
import com.hibernate.hibernateapplication.inspectors.LogInspector;
import com.hibernate.hibernateapplication.database.HibernateConnector;

@SpringBootApplication
public class HibernateApplication {
    public static void main( final String[] args ) {
        /*
        запускаем тесты
        */
        new LogInspector(
                new JUnitCore().run(
                        new RepeatedTest(
                                new JUnit4TestAdapter( JavaTest.class ), 3
                        )
                )
        );

        HibernateConnector.getInstance().close();

        SpringApplication.run( HibernateApplication.class, args );
    }
}
