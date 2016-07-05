package com.ayushkhare.weather.logic.country;

import android.os.AsyncTask;

import com.ayushkhare.weather.database.Country;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author aysuh
 * @since 17 Jan 2016, 10:49 AM.
 */
public class GetCountryListTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... params) {
        CountryListEvent event = new CountryListEvent();
        event.countryList = Country.listAll(Country.class);
        EventBus.getDefault().post(event);
        return null;
    }

    public static class CountryListEvent {
        public List<Country> countryList;
    }
}
