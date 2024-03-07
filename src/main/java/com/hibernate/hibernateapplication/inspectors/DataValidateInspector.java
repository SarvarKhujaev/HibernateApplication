package com.hibernate.hibernateapplication.inspectors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;

public class DataValidateInspector {
    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

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