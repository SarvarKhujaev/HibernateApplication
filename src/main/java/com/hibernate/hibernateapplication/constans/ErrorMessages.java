package com.hibernate.hibernateapplication.constans;

/*
https://www.geeksforgeeks.org/hibernate-validator-with-example/
*/
/*
хранит сообщения об ошибках при работе с параметрами таблиц
*/
public final class ErrorMessages {
    public static final String WRONG_EMAIL = "WRONG EMAIL FORMAT WAS INSERTED";

    public static final String NULL_VALUE = "VALUE CANNOT BE EMPTY";

    public static final String VALUE_MUST_BE_POSITIVE = "VALUE MUST BE POSITIVE";

    public static final String VALUE_OUT_OF_RANGE = "YOUR VALUE IS OUT OF RANGE";
    public static final String DATE_IS_INVALID = "YOUR DATE MUST BE PRESENT OR FUTURE";
}
