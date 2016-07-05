package com.ayushkhare.weather.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.ayushkhare.weather.database.Country;

/**
 * @author ayush
 * @since 17 Jan 2016, 4:41 PM.
 */
public class CountryPref {

    public String getMainCountryZmw(Context context) {
        SharedPreferences settings = context.getSharedPreferences("country_pref", 0);
        return settings.getString("main_country_zmw", "");
    }

    public String getMainCountryTmz(Context context) {
        SharedPreferences settings = context.getSharedPreferences("country_pref", 0);
        return settings.getString("main_country_tz", "Asia/Singapore");
    }

    public void setMainCountry(Context context, Country country) {
        SharedPreferences settings = context.getSharedPreferences("country_pref", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("main_country_zmw", country.getZmw());
        editor.putString("main_country_tz", country.getTz());
        editor.apply();
    }

    public boolean hasMainCountry(Context context) {
        return !TextUtils.isEmpty(getMainCountryZmw(context));
    }

    public boolean isMainCountry(Context context, Country country) {
        return country.getZmw().equals(getMainCountryZmw(context));
    }
}
