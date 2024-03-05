package com.hibernate.hibernateapplication.testing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith( value = Suite.class )
@Suite.SuiteClasses( {
        DatabaseAvailabilityTest.class,
} )
public final class JavaTest {}
