package com.ayushkhare.weather.database;

import com.orm.SugarRecord;

/**
 * @author ayush
 * @since 15 Jan 2016, 2:34 PM.
 */
public class Country extends SugarRecord {

    private String name;
    private String type;
    private String zmw;
    private String tz;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCity() {
        return type.equals("city");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getZmw() {
        return zmw;
    }

    public void setZmw(String zmw) {
        this.zmw = zmw;
    }

    public String getTz() {
        return tz;
    }

    public void setTz(String tz) {
        this.tz = tz;
    }
}
