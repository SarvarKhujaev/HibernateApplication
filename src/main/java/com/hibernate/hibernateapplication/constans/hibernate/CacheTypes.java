package com.hibernate.hibernateapplication.constans.hibernate;

/*
https://www.geeksforgeeks.org/hibernate-cache-expiration/

https://www.geeksforgeeks.org/hibernate-cache-eviction-with-example/ <- docs

https://www.geeksforgeeks.org/hibernate-enable-and-implement-first-and-second-level-cache/
*/
public final class CacheTypes {
    /*
    Time-based eviction is a cache eviction strategy that involves removing data from the cache after
    a certain period has elapsed since the data was last accessed or added to the cache.
    The idea behind this strategy is that the data in the cache may become stale or irrelevant after a
    certain amount of time, and it is better to remove it from the cache to make room for more relevant data.

    Depending on your system’s requirements, you may be able to implement time-based eviction in different ways.
    For instance, some systems use a fixed TTL for all cache data, while others
    use a TTI based on how long it’s been since the last time you accessed the data.
    */
    public static final String Time_Based_Eviction = "Time-Based Eviction";

    /*
    Count-Based Eviction is a technique used in cache management,
    where items are removed from the cache based on the number of times they have been accessed.
    The basic idea is that items that are accessed frequently are more likely to be accessed again shortly,
    so they should be kept in the cache. On the other hand, items that are accessed less frequently
    are less likely to be accessed again, so they can be evicted to make room for more frequently accessed items.

    Each item in your cache is associated with a counter.
    Every time you access an item, the counter on that item goes up.
    When your cache gets too full, the item that has the lowest counter will be evicted.
    */
    public static final String Count_Based_Eviction = "Count-Based Eviction";

    /*
    Query result eviction refers to the process of removing or expiring cached
    query results from a database or data storage system.
    When a query is executed in a database, the result of that query is typically stored in a cache to improve performance.
    This cache can be set to expire after a certain period or when new data is added to the database.

    When the cached query result is evicted, it is removed from the cache and the next time the same query executed,
    the database will have to retrieve the data again from its primary storage location.
    */
    public static final String Query_Based_Eviction = "Query-Based Eviction";

    /*
    is a temporary storage area that stores frequently accessed data to improve the performance of the system.
    When cache data is cleared, it is removed from the cache memory, and the system needs
    to retrieve the data again from the source, which may take more time.

    Cache regions can be cleared in various ways, depending on the system or device being used.
    For example, web browsers usually have options to clear the cache, either for specific regions or for the entire cache.
    Similarly, mobile devices and computers may have built-in tools for clearing the cache,
    or users may need to use third-party applications or software to perform the task.
    */
    public static final String Cache_Based_Eviction = "Time-Based Eviction";
}
