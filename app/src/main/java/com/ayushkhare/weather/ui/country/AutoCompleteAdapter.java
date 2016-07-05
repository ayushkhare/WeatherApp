package com.ayushkhare.weather.ui.country;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ayushkhare.weather.R;
import com.ayushkhare.weather.database.Country;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ayush
 * @since 17 Jan 2016, 11:43 PM.
 */
public class AutoCompleteAdapter extends BaseAdapter {

    private List<Country> mDataSource = new ArrayList<>();

    public void setDataSource(List<Country> dataList) {
        mDataSource = dataList;
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Country getItem(int position) {
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
            convertView = View.inflate(parent.getContext(), R.layout.city_suggestion_item_view, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTitleView.setText(getItem(position).getName());
        return convertView;
    }


    private static class ViewHolder {
        public TextView mTitleView;

        public ViewHolder(View convertView) {
            mTitleView = (TextView) convertView.findViewById(R.id.title);
        }
    }
}
