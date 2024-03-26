package com.hibernate.hibernateapplication.constans;

import java.text.MessageFormat;

public final class PostgresBufferMethods {
    public static final String PG_PREWARM = "pg_prewarm";

    /*
    Список страниц сбрасывается в файл autoprewarm.blocks.
    Чтобы его увидеть, можно просто подождать,
    пока процесс autoprewarm master отработает в первый раз, но мы инициируем это вручную:
     */
    public static final String PREWARM_TABLE = "SELECT autoprewarm_dump_now();";

    /*
    Если мы предполагаем, что все ее содержимое очень важно, мы можем прочитать ее в буферный кеш с помощью вызова следующей функции:
     */
    public static final String INSERT_TABLE_CONTENT_INTO_BUFFER = MessageFormat.format(
            "SELECT {0}( '%s' );",
            PG_PREWARM
    );
}
