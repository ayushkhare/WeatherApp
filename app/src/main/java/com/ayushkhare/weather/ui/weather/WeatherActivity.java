package com.ayushkhare.weather.ui.weather;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ayushkhare.weather.R;
import com.ayushkhare.weather.cache.WeatherCache;
import com.ayushkhare.weather.database.Country;
import com.ayushkhare.weather.logic.country.DeleteCountryTask;
import com.ayushkhare.weather.logic.country.GetCountryListTask;
import com.ayushkhare.weather.logic.country.SaveCountryTask;
import com.ayushkhare.weather.logic.weather.SyncWeatherTask;
import com.ayushkhare.weather.network.WeatherAPI;
import com.ayushkhare.weather.network.WeatherData;
import com.ayushkhare.weather.preference.CountryPref;
import com.ayushkhare.weather.ui.country.AddCountryActivity;
import com.ayushkhare.weather.ui.reminder.ReminderActivity;
import com.ayushkhare.weather.util.ColorUtil;
import com.ayushkhare.weather.util.DrawableUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    @Bind(R.id.container)
    View mContainerView;

    @Bind(R.id.cloud_image)
    ImageView mIconView;

    @Bind(R.id.country)
    TextView mCountryView;

    @Bind(R.id.condition)
    TextView mConditionView;

    @Bind(R.id.temperature)
    TextView mTemperatureView;

    @Bind(R.id.country_panel)
    LinearLayout mCountryPanel;

    @BindString(R.string.temp_holder)
    String tempHolder;

    private WeatherAPI mWeatherAPI;
    private WeatherCache mWeatherCache;
    private CountryPref mPreference;
    private HashMap<String, WeatherItemView> mCountryViewMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WeatherAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mWeatherAPI = retrofit.create(WeatherAPI.class);
        mWeatherCache = WeatherCache.getInstance();
        mPreference = new CountryPref();

        mCountryViewMap = new HashMap<>();
        if (!mPreference.hasMainCountry(this)) {
            Intent intent = new Intent(this, AddCountryActivity.class);
            intent.putExtra("set_main", true);
            startActivity(intent);
            return;
        }

        // load data
        refreshDataFromDB();
    }

    private void refreshDataFromDB() {
        new GetCountryListTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @OnClick(R.id.add_button)
    void onAddCountryClick() {
        Intent intent = new Intent(this, AddCountryActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.add_reminder)
    void onAddReminderClick() {
        Intent intent = new Intent(this, ReminderActivity.class);
        startActivity(intent);
    }

    /*
     * EVENT HANDLERS
     */
    public void onEventMainThread(GetCountryListTask.CountryListEvent event) {
        List<Country> notMainCountryList = new ArrayList<>();
        for (Country country : event.countryList) {
            if (mPreference.isMainCountry(this, country)) {
                setMainCountry(country);
            } else {
                notMainCountryList.add(country);
            }
        }
        setNotMainCountries(notMainCountryList);
    }

    public void onEventMainThread(SyncWeatherTask.SyncWeatherEvent event) {
        Country country = event.country;
        WeatherData weather = event.weatherData;
        if (mPreference.isMainCountry(this, country)) {
            mConditionView.setText(weather.getCondition());
            mIconView.setImageDrawable(DrawableUtil.drawableByCondition(this, weather.getFCTCode()));
            String temperatureString = String.format(tempHolder,
                    weather.getTemperatureC(),
                    weather.getTemperatureF());
            mTemperatureView.setText(temperatureString);
        } else {
            WeatherItemView itemView = mCountryViewMap.get(event.country.getZmw());
            if (itemView != null) {
                itemView.setCloudIcon(DrawableUtil.drawableByCondition(this, weather.getFCTCode()));
                itemView.setCondition(weather.getCondition());
            }
        }
    }

    public void onEventMainThread(SaveCountryTask.SaveCountryEvent event) {
        refreshDataFromDB();
    }

    public void onEventMainThread(DeleteCountryTask.DeleteCountryEvent event) {
        refreshDataFromDB();
    }

    /*
     * UI UPDATE METHODS
     */
    private void setMainCountry(Country mainCountry) {
        mCountryView.setText(mainCountry.getName());
        mContainerView.setBackgroundColor(ColorUtil.colorFromTime(System.currentTimeMillis(),
                mainCountry.getTz()));
        new SyncWeatherTask(mWeatherCache, mWeatherAPI).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mainCountry);
    }

    private void setNotMainCountries(List<Country> countryList) {
        mCountryPanel.removeAllViews();
        mCountryViewMap.clear();
        if (countryList != null) {
            for (Country country : countryList) {
                WeatherItemView itemView = new WeatherItemView(this);
                itemView.setCountryName(country.getName());
                itemView.setTag(country);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
                mCountryPanel.addView(itemView);
                mCountryViewMap.put(country.getZmw(), itemView);
                new SyncWeatherTask(mWeatherCache, mWeatherAPI).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, country);
            }
        }
    }

    /*
     * ITEM EVENT LISTENERS
     */
    @Override
    public void onClick(View v) {
        Country clickCountry = (Country) v.getTag();
        mPreference.setMainCountry(this, clickCountry);
        refreshDataFromDB();
    }

    @Override
    public boolean onLongClick(View v) {
        final Country clickCountry = (Country) v.getTag();
        new AlertDialog.Builder(this, R.style.AppDialog)
                .setTitle(R.string.label_remove_country)
                .setMessage(clickCountry.getName())
                .setCancelable(true)
                .setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new DeleteCountryTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, clickCountry);
                    }
                }).show();
        return true;
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
