package com.ayushkhare.weather.logic.reminder;

import android.os.AsyncTask;

import com.ayushkhare.weather.database.Reminder;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * @author ayush
 * @since 17 Jan 2016, 7:27 PM.
 */
public class GetReminderListTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... params) {
        GetReminderEvent event = new GetReminderEvent();
        event.reminderList = Reminder.listAll(Reminder.class);
        EventBus.getDefault().post(event);
        return null;
    }

    public static class GetReminderEvent {
        public List<Reminder> reminderList;
    }
}
