package com.hibernate.hibernateapplication.inspectors;

import com.hibernate.hibernateapplication.HibernateApplication;
import org.hibernate.cfg.Environment;
import java.util.Map;

/*
CacheMode.NORMAL -> CacheStoreMode.USE and CacheRetrieveMode.USE -> Default. Reads/writes data from/into the cache

CacheMode.REFRESH -> CacheStoreMode.REFRESH and CacheRetrieveMode.BYPASS -> Doesn’t read from cache, but writes to the cache upon loading from the database

CacheMode.PUT -> CacheStoreMode.USE and CacheRetrieveMode.BYPASS -> Doesn’t read from cache, but writes to the cache as it reads from the database

CacheMode.GET -> CacheStoreMode.BYPASS and CacheRetrieveMode.USE -> Read from the cache, but doesn’t write to cache

CacheMode.IGNORE -> CacheStoreMode.BYPASS and CacheRetrieveMode.BYPASS -> Doesn’t read/write data from/into the cache
*/
public class Archive extends LogInspector {
    protected final byte BATCH_SIZE = 30;
    protected final Map< String, Object > dbSettings;

    protected Archive() {
        /*
        https://vladmihalcea.com/how-to-batch-insert-and-update-statements-with-hibernate/ <- docs

        https://docs.jboss.org/hibernate/orm/6.4/userguide/html_single/Hibernate_User_Guide.html#batch <- docs
        */
        this.dbSettings = super.newMap();

        this.dbSettings.put(
                Environment.URL,
                HibernateApplication
                        .context
                        .getEnvironment()
                        .getProperty( "variables.HIBERNATE_VALUES.URL" )
        );

        this.dbSettings.put(
                Environment.USER,
                HibernateApplication
                        .context
                        .getEnvironment()
                        .getProperty( "variables.HIBERNATE_VALUES.USER" )
        );

        this.dbSettings.put(
                Environment.PASS,
                HibernateApplication
                        .context
                        .getEnvironment()
                        .getProperty( "variables.HIBERNATE_VALUES.PASSWORD" )
        );

        this.dbSettings.put( Environment.DRIVER, "org.postgresql.Driver" );
        this.dbSettings.put( Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect" );
        this.dbSettings.put( Environment.SHOW_SQL, true );
        this.dbSettings.put( Environment.FORMAT_SQL, true );
        this.dbSettings.put( Environment.HBM2DDL_AUTO, "update" );

        /*
        There’s the hibernate.jdbc.batch_versioned_data configuration property we need to set,
        in order to enable UPDATE batching

        Some JDBC drivers return incorrect row counts when a batch is executed.
        If your JDBC driver falls into this category this setting should be set to false.
        Otherwise, it is safe to enable this which will allow Hibernate to still batch the DML for versioned entities
        and still use the returned row counts for optimistic lock checks. Since 5.0, it defaults to true.
        Previously (versions 3.x and 4.x), it used to be false.
         */
        this.dbSettings.put( Environment.BATCH_VERSIONED_DATA, true );

        /*
        A non-zero value enables use of JDBC2 batch updates by Hibernate (e.g. recommended values between 5 and 30)

        Controls the maximum number of statements Hibernate will batch together before asking the driver to execute the batch.
        Zero or a negative number disables this feature.
        */
        this.dbSettings.put( Environment.STATEMENT_BATCH_SIZE, this.BATCH_SIZE );

        /*
        Optimizes second-level cache operations to minimize writes, at the cost of more frequent reads.
        Providers typically set this appropriately.
         */
        this.dbSettings.put( Environment.USE_MINIMAL_PUTS, true );

        /*
        Defines a name to be used as a prefix to all second-level cache region names.
         */
        this.dbSettings.put( Environment.CACHE_REGION_PREFIX, "hibernate" );

        /*
        Enable or disable second level caching overall. By default,
        if the currently configured RegionFactory is not the NoCachingRegionFactory,
        then the second-level cache is going to be enabled.
        Otherwise, the second-level cache is disabled.
         */
        this.dbSettings.put( Environment.USE_SECOND_LEVEL_CACHE, true );

        /*
        If you enable the hibernate.generate_statistics configuration property,
        Hibernate will expose a number of metrics via SessionFactory.getStatistics().
        Hibernate can even be configured to expose these statistics via JMX.

        This way, you can get access to the Statistics class which comprises all sort of second-level cache metrics.
         */
        this.dbSettings.put( Environment.GENERATE_STATISTICS, true );
    }
}
