package com.ayushkhare.weather.ui.reminder;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ListView;
import android.widget.TimePicker;

import com.ayushkhare.weather.R;
import com.ayushkhare.weather.database.Reminder;
import com.ayushkhare.weather.logic.reminder.DeleteReminderTask;
import com.ayushkhare.weather.logic.reminder.GetReminderListTask;
import com.ayushkhare.weather.logic.reminder.SaveReminderTask;
import com.ayushkhare.weather.preference.CountryPref;
import com.ayushkhare.weather.util.ColorUtil;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import de.greenrobot.event.EventBus;

public class ReminderActivity extends AppCompatActivity {

    @Bind(R.id.reminder_list)
    ListView mListView;

    @Bind(R.id.container)
    View mContainerView;

    private ReminderAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mAdapter = new ReminderAdapter();
        mListView.setAdapter(mAdapter);

        CountryPref countryPref = new CountryPref();
        mContainerView.setBackgroundColor(ColorUtil.colorFromTime(System.currentTimeMillis(),
                countryPref.getMainCountryTmz(this)));

        // refresh from DB
        refreshDataFromDB();
    }

    private void refreshDataFromDB() {
        new GetReminderListTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @OnClick(R.id.add_button)
    void onAddClick() {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (view.isShown()) {
                    setAlarm(hourOfDay, minute);
                }
            }
        }, hour, minute, DateFormat.is24HourFormat(this)).show();
    }

    @OnItemClick(R.id.reminder_list)
    void onItemClick(int position) {
        final Reminder reminder = mAdapter.getItem(position);
        new AlertDialog.Builder(this, R.style.AppDialog)
                .setTitle(R.string.label_remove_reminder)
                .setCancelable(true)
                .setNegativeButton(R.string.label_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                .setPositiveButton(R.string.label_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new DeleteReminderTask(getApplicationContext())
                                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, reminder);
                            }
                        })
                .show();
    }

    private void setAlarm(int hour, int minute) {
        Reminder reminder = new Reminder();
        reminder.setHour(hour);
        reminder.setMinute(minute);
        new SaveReminderTask(getApplicationContext()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, reminder);
    }

    /*
     * EVENT HANDLERS
     */
    public void onEventMainThread(SaveReminderTask.SaveReminderEvent event) {
        refreshDataFromDB();
    }

    public void onEventMainThread(DeleteReminderTask.DeleteReminderEvent event) {
        refreshDataFromDB();
    }

    public void onEventMainThread(GetReminderListTask.GetReminderEvent event) {
        mAdapter.setDataSource(event.reminderList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
