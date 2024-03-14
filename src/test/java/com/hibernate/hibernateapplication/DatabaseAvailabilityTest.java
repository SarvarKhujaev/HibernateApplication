package com.hibernate.hibernateapplication;

import com.hibernate.hibernateapplication.database.HibernateConnector;
import junit.framework.TestCase;

/*
проверяет соединение с БД и работу Hibernate
*/
public class DatabaseAvailabilityTest extends TestCase {
    private HibernateConnector connector;

    /*
    запускается при инициализации самого класса
    перед началом самого тестиирования
    */
    @Override
    public void setUp () {
        this.connector = HibernateConnector.getInstance();
    }

    /*
    запускается в конце тестиирования
    */
    @Override
    public void tearDown () {
        this.connector = null;
        System.out.println( "closed" );
    }

    /*
    проверяет что Hibernate и БД работают
    */
    public void testHibernateConnectionEstablishment () {
        assertNotNull( this.connector );
        assertNotNull( this.connector.getSession() );
        assertNotNull( this.connector.getSessionFactory() );

        assertTrue( this.connector.getSession().isConnected() );
    }
}
