package com.hibernate.hibernateapplication.constans.hibernate;

public final class HibernateCacheRegions {
    /*
    Holding the cached query results
     */
    public static final String DEFAULT_QUERY_RESULTS_REGION = "default-query-results-region";

    /*
    Holding timestamps of the most recent updates to queryable tables.
    These are used to validate the results as they are served from the query cache.

    If you configure your underlying cache implementation to use expiration,
    it’s very important that the timeout of the underlying cache region
    for the default-update-timestamps-region is set to a higher value than the timeout setting of any of the query caches.

    In fact, we recommend that the default-update-timestamps-region region is not configured
    for expiration (time-based) or eviction (size/memory-based) at all.
    Note that an LRU (Least Recently Used) cache eviction policy is never appropriate for this particular cache region.
     */
    public static final String DEFAULT_UPDATE_TIMESTAMPS_REGION = "default-update-timestamps-region";
}
