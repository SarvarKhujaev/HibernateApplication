package com.hibernate.hibernateapplication.inspectors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.Set;

public class DataValidateInspector {
    protected DataValidateInspector () {}

    protected boolean objectIsNotNull (
            final Object o
    ) {
        return o != null;
    }

    protected <T> Set< ConstraintViolation< T > > checkEntityValidation (
            final Validator validator,
            final T object
    ) {
        return validator.validate( object );
    }
}