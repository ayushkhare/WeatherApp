package com.ayushkhare.weather.logic.country;

import android.os.AsyncTask;

import com.ayushkhare.weather.database.Country;

import de.greenrobot.event.EventBus;

/**
 * @author ayush
 * @since 17 Jan 2016, 10:52 AM.
 */
public class DeleteCountryTask extends AsyncTask<Country, Void, Void> {
    @Override
    protected Void doInBackground(Country... params) {
        if (params != null) {
            for (Country country : params) {
                country.delete();
                DeleteCountryEvent event = new DeleteCountryEvent();
                event.country = country;
                EventBus.getDefault().post(event);
            }
        }
        return null;
    }

    public static class DeleteCountryEvent {
        public Country country;
    }
}
