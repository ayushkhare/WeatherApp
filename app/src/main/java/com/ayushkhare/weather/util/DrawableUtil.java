package com.ayushkhare.weather.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.ayushkhare.weather.R;

/**
 * @author ayush
 * @since 17 Jan 2016, 5:43 PM.
 */
public class DrawableUtil {
    /*
    fctcodes
	========
	1	Clear
	2	Partly Cloudy
	3	Mostly Cloudy
	4	Cloudy
	5	Hazy
	6	Foggy
	7	Very Hot
	8	Very Cold
	9	Blowing Snow
	10	Chance of Showers
	11	Showers
	12	Chance of Rain
	13	Rain
	14	Chance of a Thunderstorm
	15	Thunderstorm
	16	Flurries
	17	OMITTED
	18	Chance of Snow Showers
	19	Snow Showers
	20	Chance of Snow
	21	Snow
	22	Chance of Ice Pellets
	23	Ice Pellets
	24	Blizzard
	*/
    public static Drawable drawableByCondition(Context context, int fctCode) {
        int drawableId;
        switch (fctCode) {
            case 1:
                drawableId = R.drawable.sun;
                break;
            case 2:
            case 3:
                drawableId = R.drawable.cloud;
                break;
            case 4:
                drawableId = R.drawable.very_cloudy;
                break;
            case 5:
                drawableId = R.drawable.fog;
                break;
            case 6:
                drawableId = R.drawable.fog_cloudy;
                break;
            case 7:
                drawableId = R.drawable.sun;
                break;
            case 8:
            case 9:
                drawableId = R.drawable.snow;
                break;
            case 10:
            case 11:
            case 12:
            case 13:
                drawableId = R.drawable.rain;
                break;
            case 14:
            case 15:
            case 16:
            case 17:
                drawableId = R.drawable.thunderstorm;
                break;
            case 18:
            case 19:
            case 20:
            case 21:
                drawableId = R.drawable.snow;
                break;
            case 22:
            case 23:
                drawableId = R.drawable.hail;
                break;
            case 24:
                drawableId = R.drawable.thunderstorm;
                break;
            default:
                drawableId = R.drawable.sun;
                break;
        }
        Drawable normalDrawable = context.getResources().getDrawable(drawableId);
        Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
        DrawableCompat.setTint(wrapDrawable, context.getResources().getColor(R.color.white));
        return wrapDrawable;
    }

    private DrawableUtil() {

    }
}
