package com.hibernate.hibernateapplication.test;

import junit.framework.TestCase;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder( value = MethodSorters.NAME_ASCENDING )
public class StringTest extends TestCase {
    public void testCheck () {
        final String str = "Junit is working fine";
        assertEquals( "Junit is working fine",str );
    }
}
