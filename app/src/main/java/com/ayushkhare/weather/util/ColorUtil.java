package com.ayushkhare.weather.util;

import android.graphics.Color;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author ayush
 * @since 15 Jan 2016, 3:26 PM.
 */
public class ColorUtil {

    private static final int COLOR_MORNING = Color.parseColor("#06CDFF");
    private static final int COLOR_NIGHT = Color.parseColor("#06245F");

    public static int colorFromTime(long timestamp, String timezoneId) {
        Date date = new Date(timestamp);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone(timezoneId));
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        float percent;
        if (hour <= 12) {
            percent = (hour) / 12f;
        } else {
            percent = 1 - ((hour - 12) / 12f);
        }
        int r = colorVariation(Color.red(COLOR_NIGHT), Color.red(COLOR_MORNING), percent);
        int g = colorVariation(Color.green(COLOR_NIGHT), Color.green(COLOR_MORNING), percent);
        int b = colorVariation(Color.blue(COLOR_NIGHT), Color.blue(COLOR_MORNING), percent);
        return Color.argb(255, r, g, b);
    }

    private static int colorVariation(int colorStart, int colorEnd, float distance) {
        return colorStart + (int) ((colorEnd - colorStart) * distance);
    }
}
