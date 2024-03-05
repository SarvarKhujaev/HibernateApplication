package com.hibernate.hibernateapplication.inspectors;

import org.hibernate.cfg.Environment;
import java.util.Map;

public class Archieve extends LogInspector {
    protected Archieve () {}

    protected final Map< String, Object > dbSettings = Map.of(
            Environment.URL, "jdbc:postgresql://localhost:5432/postgres",
            Environment.USER, "postgres",
            Environment.PASS, "killerbee",
            Environment.DRIVER, "org.postgresql.Driver",
            Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect",
            Environment.SHOW_SQL, true,
            Environment.FORMAT_SQL, true,
            Environment.HBM2DDL_AUTO, "update",
            Environment.STATEMENT_BATCH_SIZE, 50
    );
}
