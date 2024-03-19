package com.hibernate.hibernateapplication.inspectors;

import com.hibernate.hibernateapplication.constans.PostgreSqlSchema;

public class StringOperations extends CollectionsInspector {
    protected StringOperations () {}

    protected StringBuilder newStringBuilder ( final String s ) {
        return new StringBuilder( s );
    }

    protected String generateCacheName (
            final String table
    ) {
        return String.join(
                ".",
                "hibernate.cache",
                PostgreSqlSchema.ENTITIES.toLowerCase(),
                table.toLowerCase()
        );
    }
}
