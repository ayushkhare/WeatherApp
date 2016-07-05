package com.ayushkhare.weather.ui.weather;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayushkhare.weather.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author ayush
 * @since 17 Jan 2016, 11:06 AM.
 */
public class WeatherItemView extends FrameLayout {

    @Bind(R.id.icon)
    ImageView mCouldIconView;

    @Bind(R.id.country)
    TextView mNameTextView;

    @Bind(R.id.condition)
    TextView mConditionTextView;

    public WeatherItemView(Context context) {
        super(context);
        initView(context);
    }

    public WeatherItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public WeatherItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WeatherItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.weather_preview_item_view_layout, this);
        ButterKnife.bind(this);
    }

    public void setCloudIcon(Drawable drawable) {
        mCouldIconView.setImageDrawable(drawable);
    }

    public void setCountryName(String name) {
        mNameTextView.setText(name);
    }

    public void setCondition(String condition) {
        mConditionTextView.setText(condition);
    }
}
