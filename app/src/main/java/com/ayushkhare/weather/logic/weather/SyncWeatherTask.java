package com.ayushkhare.weather.logic.weather;

import android.os.AsyncTask;
import android.util.Log;

import com.ayushkhare.weather.cache.WeatherCache;
import com.ayushkhare.weather.database.Country;
import com.ayushkhare.weather.network.WeatherAPI;
import com.ayushkhare.weather.network.WeatherData;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Response;

/**
 * @author ayush
 * @since 17 Jan 2016, 11:46 AM.
 */
public class SyncWeatherTask extends AsyncTask<Country, Void, Void> {

    private final WeatherAPI mWeatherAPI;
    private final WeatherCache mWeatherCache;

    public SyncWeatherTask(WeatherCache weatherCache,
                           WeatherAPI weatherAPI) {
        mWeatherCache = weatherCache;
        mWeatherAPI = weatherAPI;
    }

    @Override
    protected Void doInBackground(Country... params) {
        if (params != null) {
            for (Country country : params) {
                try {
                    WeatherData weatherData = mWeatherCache.get(country);
                    if (weatherData == null) {
                        // if not found in the cache, make network request
                        Call<WeatherData> call = mWeatherAPI.getWeather(country.getZmw());
                        Response<WeatherData> response = call.execute();
                        if (response.isSuccess() && response.body() != null) {
                            weatherData = response.body();
                            mWeatherCache.put(country, weatherData);
                        }
                    }
                    SyncWeatherEvent event = new SyncWeatherEvent();
                    event.country = country;
                    event.weatherData = weatherData;
                    EventBus.getDefault().post(event);
                } catch (IOException e) {
                    Log.d("SyncWeatherTask", "IOException", e);
                    return null;
                }
            }
        }
        return null;
    }

    public static class SyncWeatherEvent {
        public Country country;
        public WeatherData weatherData;
    }
}
