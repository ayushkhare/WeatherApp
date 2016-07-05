package com.ayushkhare.weather.logic.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.ayushkhare.weather.background.ReminderReceiver;
import com.ayushkhare.weather.database.Reminder;

import java.util.Calendar;

import de.greenrobot.event.EventBus;

/**
 * @author ayush
 * @since 17 Jan 2016, 11:32 PM.
 */
public class SaveReminderTask extends AsyncTask<Reminder, Void, Void> {

    private final Context mContext;

    public SaveReminderTask(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    protected Void doInBackground(Reminder... params) {
        if (params != null) {
            for (Reminder reminder : params) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, reminder.getHour());
                calendar.set(Calendar.MINUTE, reminder.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                reminder.setTimestamp(calendar.getTimeInMillis());
                reminder.save();

                // Use ALARM MANAGER to set an alarm
                Intent myIntent = new Intent(mContext, ReminderReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,
                        (int) ((long) reminder.getId()),
                        myIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                am.setRepeating(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY,
                        pendingIntent);

                SaveReminderEvent event = new SaveReminderEvent();
                event.reminder = reminder;
                EventBus.getDefault().post(event);
            }
        }
        return null;
    }

    public static class SaveReminderEvent {
        public Reminder reminder;
    }
}
