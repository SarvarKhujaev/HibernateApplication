package com.hibernate.hibernateapplication.inspectors;

import java.util.Date;

public class TimeInspector extends StringOperations {
    protected TimeInspector () {}

    protected Date newDate () {
        return new Date();
    }
}
