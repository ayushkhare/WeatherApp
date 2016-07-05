package com.ayushkhare.weather.background;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.ayushkhare.weather.R;
import com.ayushkhare.weather.database.Country;
import com.ayushkhare.weather.network.WeatherAPI;
import com.ayushkhare.weather.network.WeatherData;
import com.ayushkhare.weather.preference.CountryPref;
import com.ayushkhare.weather.ui.weather.WeatherActivity;

import java.io.IOException;
import java.util.List;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * @author ayush
 * @since 17 Jan 2016, 7:53 PM.
 */
public class ReminderService extends IntentService {

    private CountryPref mPreference;
    private WeatherAPI mWeatherAPI;

    public ReminderService() {
        super("Background Service");
        mPreference = new CountryPref();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WeatherAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mWeatherAPI = retrofit.create(WeatherAPI.class);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        List<Country> countries = Country.find(Country.class, "zmw = ?", mPreference.getMainCountryZmw(ReminderService.this));
        if (countries.size() <= 0) {
            return;
        }

        Country mainCountry = countries.get(0);
        Call<WeatherData> call = mWeatherAPI.getWeather(mainCountry.getZmw());
        try {
            Response<WeatherData> response = call.execute();
            if (response.isSuccess() && response.body() != null) {
                WeatherData data = response.body();
                String result = String.format(getResources().getString(R.string.notification_placeholder),
                        mainCountry.getName(),
                        data.getCondition(),
                        data.getTemperatureC(),
                        data.getTemperatureF());

                Intent resultIntent = new Intent(ReminderService.this, WeatherActivity.class);
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                ReminderService.this,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                Notification notification = new NotificationCompat.Builder(ReminderService.this)
                        .setTicker(result)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentText(result)
                        .setContentIntent(resultPendingIntent)
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentTitle(getResources().getString(R.string.label_weather_update))
                        .build();

                NotificationManager mNotifyMgr =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // Builds the notification and issues it.
                mNotifyMgr.notify(1, notification);
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
