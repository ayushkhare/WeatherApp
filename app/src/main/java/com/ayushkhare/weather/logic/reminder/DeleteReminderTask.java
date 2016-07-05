package com.ayushkhare.weather.logic.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.ayushkhare.weather.background.ReminderReceiver;
import com.ayushkhare.weather.database.Reminder;

import de.greenrobot.event.EventBus;

/**
 * @author ayush
 * @since 17 Jan 2016, 11:32 PM.
 */
public class DeleteReminderTask extends AsyncTask<Reminder, Void, Void> {

    private final Context mContext;

    public DeleteReminderTask(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    protected Void doInBackground(Reminder... params) {
        if (params != null) {
            for (Reminder reminder : params) {

                // cancel the ALARM MANAGER alarm
                Intent myIntent = new Intent(mContext, ReminderReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,
                        (int) ((long) reminder.getId()),
                        myIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                am.cancel(pendingIntent);

                Reminder.delete(reminder);

                DeleteReminderEvent event = new DeleteReminderEvent();
                event.reminder = reminder;
                EventBus.getDefault().post(event);
            }
        }
        return null;
    }

    public static class DeleteReminderEvent {
        public Reminder reminder;
    }
}
