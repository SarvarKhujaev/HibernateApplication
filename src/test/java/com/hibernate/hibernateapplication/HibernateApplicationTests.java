package com.hibernate.hibernateapplication;

import junit.extensions.RepeatedTest;
import junit.framework.JUnit4TestAdapter;

import org.junit.runner.JUnitCore;
import org.springframework.boot.test.context.SpringBootTest;

import com.hibernate.hibernateapplication.testing.JavaTest;

@SpringBootTest
public final class HibernateApplicationTests {
    public static void main( final String[] args ) {
        new JUnitCore().run(
                new RepeatedTest(
                        new JUnit4TestAdapter( JavaTest.class ), 3
                )
        );
    }
}
