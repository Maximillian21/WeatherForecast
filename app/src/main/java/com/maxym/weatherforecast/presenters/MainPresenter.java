package com.maxym.weatherforecast.presenters;

import android.util.Log;
import android.widget.Toast;

import com.maxym.weatherforecast.MainContract;
import com.maxym.weatherforecast.MainContract.MainView;
import com.maxym.weatherforecast.model.Forecast;
import com.maxym.weatherforecast.network.APIUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


public class MainPresenter implements MainContract.MainPresenter {

    public APIUtils mApiUtils;
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private MainView mMainView;

    public MainPresenter (MainView mainView) {
        mMainView = mainView;
    }

    @Override
    public void start() {
        mApiUtils = new APIUtils();
    }

    @Override
    public void fetchWeatherByCity(String city) {
        Log.d("fetchWeatherByCity", city);
        Disposable disposable = mApiUtils.getAnswers(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Forecast>() {
                    @Override
                    public void onSuccess(Forecast forecast) {
                        Log.d("onSuccess", String.valueOf(forecast.getCnt()));
                        mMainView.showHeaderCity(city.toUpperCase(), forecast.getCity().getCountry());
                        mMainView.showWeather(forecast);
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.d("onError", "ne");
                        e.printStackTrace();
                        mMainView.showCityError();
                    }
                });
        mDisposable.add(disposable);
    }

    @Override
    public void fetchWeatherByCoords(double lat, double lon) {
        Log.d("fetchCoords", Double.toString(lat));
        Log.d("fetchCoords", Double.toString(lon));
        Disposable disposable = mApiUtils.getAnswers(lat, lon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Forecast>() {
                    @Override
                    public void onSuccess(Forecast forecast) {
                        Log.d("onSuccess", String.valueOf(forecast.getCnt()));
                        String city = forecast.getCity().getName();
                        mMainView.showForecast(forecast.getList().get(0));
                        if(city.equals(""))
                            mMainView.showHeaderCoords(forecast.getCity().getCoord().getLat(),
                                    forecast.getCity().getCoord().getLat());
                        else
                            mMainView.showHeaderCity(forecast.getCity().getName().toUpperCase(),
                                    forecast.getCity().getCountry());
                        mMainView.showWeather(forecast);
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.d("onError", "ne");
                        e.printStackTrace();
                        Toast.makeText(mMainView.getContext(), "Kekich", Toast.LENGTH_SHORT).show();
                    }
                });
        mDisposable.add(disposable);
    }



    @Override
    public void stop() {
        mApiUtils = null;
        mDisposable.clear();
        mDisposable = null;
    }
}
