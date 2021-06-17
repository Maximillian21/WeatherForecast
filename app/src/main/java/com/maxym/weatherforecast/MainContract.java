package com.maxym.weatherforecast;

import android.content.Context;

import com.maxym.weatherforecast.model.Forecast;
import com.maxym.weatherforecast.model.List;

import java.util.Map;

public interface MainContract extends BaseContract {
    interface MainView extends BaseView {
        void showWeather(Forecast forecast);
        Context getContext();
        void showHeaderCity(String city, String country);
        void showHeaderCoords(double lat, double lon);
        void getDataFromPreferences();
        void setDataToPreferences(double lat, double lon);
        void showForecast(List data);
        void showCityError();
    }

    interface ChoiceView extends BaseView {
        
    }

    interface MainPresenter extends BasePresenter {
        void fetchWeatherByCity(String city);
        void fetchWeatherByCoords(double lat, double lon);
    }
}
