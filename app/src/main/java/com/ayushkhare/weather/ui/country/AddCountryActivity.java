package com.ayushkhare.weather.ui.country;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.ayushkhare.weather.R;
import com.ayushkhare.weather.database.Country;
import com.ayushkhare.weather.preference.CountryPref;
import com.ayushkhare.weather.logic.country.SaveCountryTask;
import com.ayushkhare.weather.network.CountryAPI;
import com.ayushkhare.weather.network.CountryData;
import com.ayushkhare.weather.util.ColorUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTextChanged;
import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class AddCountryActivity extends AppCompatActivity implements Callback<CountryData> {

    @Bind(R.id.suggestion_list)
    ListView mListView;

    @Bind(R.id.container)
    View mContainerView;

    private CountryPref mPreference;
    private AutoCompleteAdapter mAdapter;
    private CountryAPI mCountryAPI;
    private Call<CountryData> mRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_country);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        mAdapter = new AutoCompleteAdapter();
        mListView.setAdapter(mAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CountryAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mCountryAPI = retrofit.create(CountryAPI.class);
        mPreference = new CountryPref();
        mContainerView.setBackgroundColor(ColorUtil.colorFromTime(System.currentTimeMillis(),
                mPreference.getMainCountryTmz(this)));
    }

    @OnClick(R.id.close_button)
    void onCloseClick() {
        finish();
    }

    @OnItemClick(R.id.suggestion_list)
    void onItemClick(int position) {
        Country country = mAdapter.getItem(position);
        if (!mPreference.hasMainCountry(this)) {
            mPreference.setMainCountry(this, country);
        }
        new SaveCountryTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, country);
    }

    @OnTextChanged(R.id.country_input)
    void onTextChanged(CharSequence text) {
        if (mRequest != null) {
            mRequest.cancel();
        }
        if (!TextUtils.isEmpty(text)) {
            mRequest = mCountryAPI.getCountryList(text.toString());
            mRequest.enqueue(this);
        } else {
            mAdapter.setDataSource(new ArrayList<Country>());
            mAdapter.notifyDataSetChanged();
        }
    }

    /*
     * NETWORK CALLBACKS
     */
    @Override
    public void onResponse(Response<CountryData> response, Retrofit retrofit) {
        CountryData data = response.body();
        if (response.isSuccess() && data != null) {
            List<Country> cityList = new ArrayList<>();
            for (Country region : data.RESULTS) {
                if (region.isCity()) {
                    cityList.add(region);
                }
            }
            mAdapter.setDataSource(cityList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        Log.e("WEATHER", "Error", t);
    }

    /*
     * EVENT HANDLERS
     */
    public void onEventMainThread(SaveCountryTask.SaveCountryEvent event) {
        finish();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
