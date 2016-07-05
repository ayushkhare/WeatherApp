package com.ayushkhare.weather.background;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * @author ayush
 * @since 17 Jan 2016, 7:53 PM.
 */
public class ReminderReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, ReminderService.class);
        context.startService(serviceIntent);
    }
}
