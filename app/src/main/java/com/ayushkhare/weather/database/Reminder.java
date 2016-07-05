package com.ayushkhare.weather.database;

import com.orm.SugarRecord;

/**
 * @author ayush
 * @since 17 Jan 2016, 7:25 PM.
 */
public class Reminder extends SugarRecord {

    private int hour;

    private int minute;

    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
