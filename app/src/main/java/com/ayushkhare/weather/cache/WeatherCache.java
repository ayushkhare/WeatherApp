package com.ayushkhare.weather.cache;

import android.util.LruCache;

import com.ayushkhare.weather.database.Country;
import com.ayushkhare.weather.network.WeatherData;

/**
 * @author ayush
 * @since 17 Jan 2016, 6:56 PM.
 */
public class WeatherCache {

    private LruCache<String, CacheObject> cache;
    private long CACHE_TIME = 1000 * 60; // cache for 60 sec

    private WeatherCache() {
        cache = new LruCache<>(50);
    }

    public static WeatherCache mInstance;
    public static synchronized WeatherCache getInstance() {
        if (mInstance == null) {
            mInstance = new WeatherCache();
        }
        return mInstance;
    }

    public void put(Country country, WeatherData weatherData) {
        CacheObject cacheObject = new CacheObject();
        cacheObject.data = weatherData;
        cacheObject.timestamp = System.currentTimeMillis();
        cache.put(country.getZmw(), cacheObject);
    }

    public WeatherData get(Country country) {
        CacheObject object = cache.get(country.getZmw());
        if (object != null) {
            if (!hasExpired(object.timestamp)) {
                return object.data;
            }
        }
        return null;
    }

    private boolean hasExpired(long timestamp) {
        return (System.currentTimeMillis() - timestamp) > CACHE_TIME;
    }

    private static class CacheObject {
        public WeatherData data;
        public long timestamp;
    }
}
