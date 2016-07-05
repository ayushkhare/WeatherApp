package com.ayushkhare.weather.ui.reminder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ayushkhare.weather.R;
import com.ayushkhare.weather.database.Reminder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author ayush
 * @since 17 Jan 2016, 11:47 PM.
 */
class ReminderAdapter extends BaseAdapter {

    private List<Reminder> mDataSource = new ArrayList<>();

    public void setDataSource(List<Reminder> dataList) {
        mDataSource = dataList;
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Reminder getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.reminder_item_view_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SimpleDateFormat fmtOut = new SimpleDateFormat("hh:mma", Locale.ENGLISH);
        String date = fmtOut.format(new Date(getItem(position).getTimestamp()));
        holder.mTitleView.setText(date);
        return convertView;
    }

    private static class ViewHolder {
        public TextView mTitleView;

        public ViewHolder(View convertView) {
            mTitleView = (TextView) convertView.findViewById(R.id.time);
        }
    }
}
