package com.hibernate.hibernateapplication.test;

import com.hibernate.hibernateapplication.database.HibernateConnector;
import junit.framework.TestCase;

public class DatabaseAvailabilityTest extends TestCase {
    private HibernateConnector connector;

    @Override
    public void setUp () {
        this.connector = HibernateConnector.getInstance();
    }

    @Override
    public void tearDown () {
        this.connector.close();
        this.connector = null;
    }

    public void testHibernateConnectionEstablishment () {
        assertNotNull( this.connector );
        assertNotNull( this.connector.getSession() );
        assertNotNull( this.connector.getSessionFactory() );

        assertTrue( this.connector.getSession().isConnected() );
    }
}
