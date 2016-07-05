package com.ayushkhare.weather.network;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * @author ayush
 * @since 15 Jan 2016, 2:29 PM.
 */
public interface CountryAPI {

    String BASE_URL = "http://autocomplete.wunderground.com/";

    @GET("aq")
    Call<CountryData> getCountryList(@Query("query") String query);
}
