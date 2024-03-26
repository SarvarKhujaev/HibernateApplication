package com.hibernate.hibernateapplication.interfaces;

import com.hibernate.hibernateapplication.constans.PostgreSqlTables;

public interface ServiceCommonMethods {
    /*
    переносим все накопленные данные
    в Буферный кэш
    */
    default void insertTableContentToBuffer () {}

    /*
    очищаем таблицу от старых и не используемых записей
    */
    default void vacuumTable () {}

    default void prewarmTable () {};

    void close();
}
