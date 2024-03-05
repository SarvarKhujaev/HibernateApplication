package com.hibernate.hibernateapplication.test;

import junit.framework.TestCase;

public class TestJUnit extends TestCase {
    public int value;

    @Override
    public void setUp () {
        this.value = 56;
    }

    @Override
    public void tearDown () {
        this.value = 58;
        System.out.println( "tests count: " + super.countTestCases() );
    }

    @org.junit.Test
    public void testCheck () {
        final String str = "Junit is working fine";
        assertEquals( "Junit is working fine",str );
    }
}
