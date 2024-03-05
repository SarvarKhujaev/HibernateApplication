package com.hibernate.hibernateapplication.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith( value = Suite.class )
@Suite.SuiteClasses( {
        TestJUnit.class,
        StringTest.class,
        DatabaseAvailabilityTest.class,
} )
public final class JavaTest {
}
