package com.ayushkhare.weather.network;

import java.util.List;

/**
 * @author ayush
 * @since 15 Jan 2016, 1:19 PM.
 */
public class WeatherData {

    public List<Forecast> hourly_forecast;

    public String getCondition() {
        if (hourly_forecast != null
                && hourly_forecast.size() > 0) {
            return hourly_forecast.get(0).condition;
        }
        return "";
    }

    public String getTemperatureC() {
        if (hourly_forecast != null
                && hourly_forecast.size() > 0) {
            return hourly_forecast.get(0).temp.metric;
        }
        return "";
    }

    public String getTemperatureF() {
        if (hourly_forecast != null
                && hourly_forecast.size() > 0) {
            return hourly_forecast.get(0).temp.english;
        }
        return "";
    }

    public int getFCTCode() {
        if (hourly_forecast != null
                && hourly_forecast.size() > 0) {
            return hourly_forecast.get(0).fctcode;
        }
        return 0;
    }

    public static class Forecast {
        public Temperature temp;
        public String condition;
        public int fctcode;
    }

    public static class Temperature {
        public String metric;
        public String english;
    }
}
