package com.hibernate.hibernateapplication.constans.hibernate;

public enum CacheEvictionStrategies {
    /*
    Least Recently Used (LRU): Least Recently Unused (LRU) is an algorithm that removes data
    from the cache that has not been used for the longest period.
    LRU is based on the premise that data that has been utilized for a long period is unlikely to be required in the future.
    */
    LRU,

    /*
    First-In-First-Out (FIFO): First-in-first-out (FIFO) is an algorithm that prioritizes
    the removal of previously added data from the cache.
    The rationale behind this is that the longer the data has resided in the cache, the less likely it is to be required.
    */
    LFU,

    /*
    Least Frequently Used (LFU): Least Frequently Utilized (LFU) is an algorithm that removes data from
    the cache based on the premise that the least frequently accessed data is unlikely to be required in the future.
    */
    FIFO,
}
