package com.hibernate.hibernateapplication.inspectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogInspector extends TimeInspector {
    protected LogInspector() {}

    private final Logger LOGGER = LogManager.getLogger( "LOGGER_WITH_JSON_LAYOUT" );

    private Logger getLOGGER () {
        return this.LOGGER;
    }

    protected void logging ( final Class clazz ) {
        this.getLOGGER().info( clazz.getName() + " was created" );
    }

    protected void logging ( final Object o ) {
        this.getLOGGER().info( o.getClass().getName() + " was closed successfully" );
    }

    protected void logging ( final String message ) {
        this.getLOGGER().info( message );
    }

    protected void logging ( final Throwable error ) {
        this.getLOGGER().error( "Error: " + error );
    }
}
