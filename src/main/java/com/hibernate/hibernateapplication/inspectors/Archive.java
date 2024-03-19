package com.hibernate.hibernateapplication.inspectors;

import com.hibernate.hibernateapplication.HibernateApplication;
import org.hibernate.cfg.Environment;
import java.util.Map;

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
        Hibernate can sort INSERT and UPDATE statements using the following configuration options
         */
        this.dbSettings.put( Environment.ORDER_INSERTS, true );
        this.dbSettings.put( Environment.ORDER_UPDATES, true );

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
    }
}
