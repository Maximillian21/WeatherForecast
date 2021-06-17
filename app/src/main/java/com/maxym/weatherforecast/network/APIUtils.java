package com.maxym.weatherforecast.network;

import android.util.Log;

import com.maxym.weatherforecast.model.Forecast;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIUtils implements NetworkService{
    private static String BASE_URL = "http://api.openweathermap.org/";

    @Override
    public Single<Forecast> getAnswers(String q) {
        Retrofit retrofit = getClient(BASE_URL);
        NetworkService service = retrofit.create(NetworkService.class);
        return service.getAnswers(q);
    }

    @Override
    public Single<Forecast> getAnswers(double lat, double lon) {
        Retrofit retrofit = getClient(BASE_URL);
        NetworkService service = retrofit.create(NetworkService.class);
        Log.d("getCoords", Double.toString(lat));
        return service.getAnswers(lat, lon);
    }

    public static Retrofit getClient(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }
}
