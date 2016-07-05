package com.ayushkhare.weather.network;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * @author ayush
 * @since 15 Jan 2016, 1:19 PM.
 */
public interface WeatherAPI {

    String BASE_URL = "http://api.wunderground.com/api/974f49f7176dead1/";

    @GET("hourly/q/zmw:{zmw}.json")
    Call<WeatherData> getWeather(@Path("zmw") String zmwCode);
}
