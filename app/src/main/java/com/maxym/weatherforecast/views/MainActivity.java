package com.maxym.weatherforecast.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.maxym.weatherforecast.MainContract;
import com.maxym.weatherforecast.R;
import com.maxym.weatherforecast.adapters.DataAdapter;
import com.maxym.weatherforecast.adapters.StartSnapHelper;
import com.maxym.weatherforecast.model.Forecast;
import com.maxym.weatherforecast.model.List;
import com.maxym.weatherforecast.presenters.MainPresenter;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements MainContract.MainView, DataAdapter.OnCardClickListener {
    MainContract.MainPresenter mMainPresenter;
    public DataAdapter mAdapter;

    SharedPreferences savedCoordinates;
    public static final String APP_PREFERENCES = "coords";
    public static final String APP_PREFERENCES_LAT = "lat";
    public static final String APP_PREFERENCES_LON = "lon";

    public

    RecyclerView rvForecast;
    Button btnChangeCity;
    Button btnChangeCoords;

    TextView tvHeader;
    TextView tvMornTemp;
    TextView tvEveTemp;
    TextView tvNigTemp;
    TextView tvMornFeel;
    TextView tvEveFeel;
    TextView tvNigFeel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();
        mMainPresenter = new MainPresenter(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mMainPresenter.start();
        getDataFromPreferences();
    }

    private void initViews() {
        btnChangeCity = findViewById(R.id.btn_change_city);
        btnChangeCoords = findViewById(R.id.btn_change_coords);
        tvHeader = findViewById(R.id.tvHeader);
        tvEveFeel = findViewById(R.id.tvEveFeel);
        tvEveTemp = findViewById(R.id.tvEveTemp);
        tvMornFeel = findViewById(R.id.tvMornFeel);
        tvMornTemp = findViewById(R.id.tvMornTemp);
        tvNigFeel = findViewById(R.id.tvNigFeel);
        tvNigTemp = findViewById(R.id.tvNigTemp);
        rvForecast = findViewById(R.id.rvForecast);

        SnapHelper startSnapHelper = new StartSnapHelper();
        startSnapHelper.attachToRecyclerView(rvForecast);


        mAdapter = new DataAdapter();
        mAdapter.setOnCardClickListener(this);
        rvForecast.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                RecyclerView.HORIZONTAL, false));
        rvForecast.setAdapter(mAdapter);

        savedCoordinates = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    private void initListeners() {
        btnChangeCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CityActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        btnChangeCoords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapsActivity.class);
                startActivityForResult(intent, 2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Log.d("onActivityResult", data.getStringExtra("city"));
                    mMainPresenter.fetchWeatherByCity(data.getStringExtra("city"));
                    break;
                case 2:
                    Log.d("onActivityResult",String.valueOf(data.getDoubleExtra("lat", 0)));
                    Log.d("onActivityResult",String.valueOf(data.getDoubleExtra("lon", 0)));
                    mMainPresenter.fetchWeatherByCoords(data.getDoubleExtra("lat", 0),
                            data.getDoubleExtra("lon", 0));
                    setDataToPreferences(data.getDoubleExtra("lat", 0),
                            data.getDoubleExtra("lon", 0));
            }
        }
        else
            Snackbar.make(btnChangeCity, "Error", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showWeather(Forecast forecast) {
        Log.d("ll", String.valueOf(forecast.getCnt()));
        mAdapter.setData(forecast.getList());
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void showCityError() {
        Snackbar.make(btnChangeCity, "Wrong city", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showHeaderCity(String city, String country) {
        String header = city + "; " + country;
        tvHeader.setText(header);
    }
    @Override
    public void showHeaderCoords(double lat, double lon) {
        tvHeader.setText(lat + "; " + lon);
    }

    @Override
    public void getDataFromPreferences() {
        String lat = "0";
        double laat;
        double loon;
        String lon = "0";
        if(savedCoordinates.contains(APP_PREFERENCES_LAT)) {
            lat = savedCoordinates.getString(APP_PREFERENCES_LAT, "0");
            lon = savedCoordinates.getString(APP_PREFERENCES_LON, "0");
        }
        if(!lat.equals("0")) {
            Log.d("getData", lat);
            laat = Double.parseDouble(lat);
            loon = Double.parseDouble(lon);
            Log.d("getDataDouble", Double.toString(laat));
            mMainPresenter.fetchWeatherByCoords((double) laat, (double) loon);
        }
    }

    @Override
    public void setDataToPreferences(double lat, double lon) {
        SharedPreferences.Editor editor = savedCoordinates.edit();
        Log.d("getDataSet", Double.toString(lat));
        Log.d("getDataSet", Double.toString(lon));
        editor.clear();
        editor.putString(APP_PREFERENCES_LAT, Double.toString(lat));
        editor.putString(APP_PREFERENCES_LON, Double.toString(lon));
        editor.apply();
    }

    @Override
    public void onCardClick(int position, java.util.List<List> data) {
        showForecast(data.get(position));
    }

    @Override
    public void showForecast(List data) {
        tvMornTemp.setText( String.valueOf(data.getTemp().getMorn()));
        tvEveTemp.setText( String.valueOf(data.getTemp().getEve()));
        tvNigTemp.setText( String.valueOf(data.getTemp().getNight()));
        tvMornFeel.setText( String.valueOf(data.getFeelsLike().getMorn()));
        tvEveFeel.setText( String.valueOf(data.getFeelsLike().getMorn()));
        tvNigFeel.setText( String.valueOf(data.getFeelsLike().getMorn()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMainPresenter.stop();
    }

}