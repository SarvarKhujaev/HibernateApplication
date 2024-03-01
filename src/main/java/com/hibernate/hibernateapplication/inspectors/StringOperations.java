package com.hibernate.hibernateapplication.inspectors;

public class StringOperations extends CollectionsInspector {
    protected StringOperations () {}

    protected StringBuilder newStringBuilder ( final String s ) {
        return new StringBuilder( s );
    }

    /*
    принимает параметр для Cassandra, который является типом TIMESTAMP,
    и добавляет в начало и конец апострафы
    */
    protected String joinWithAstrix ( final Object value ) {
        return "'" + value + "'";
    }
}
