package com.hibernate.hibernateapplication;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith( value = Suite.class )
@Suite.SuiteClasses( value = {
        DatabaseAvailabilityTest.class,
} )
public final class JavaTest {}
