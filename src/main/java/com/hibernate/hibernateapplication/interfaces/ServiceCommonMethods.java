package com.hibernate.hibernateapplication.interfaces;

public interface ServiceCommonMethods {
    /*
    переносим все накопленные данные
    в Буферный кэш
    */
    default void insertTableContentToBuffer () {}

    default void prewarmTable () {}

    /*
    очищаем таблицу от старых и не используемых записей
    */
    default void vacuumTable () {}

    void close();
}
