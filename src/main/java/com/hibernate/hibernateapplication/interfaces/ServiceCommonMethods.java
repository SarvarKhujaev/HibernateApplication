package com.hibernate.hibernateapplication.interfaces;

public interface ServiceCommonMethods {
    default void close( final Throwable throwable ) {}

    void close();
}
