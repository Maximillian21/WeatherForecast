package com.maxym.weatherforecast.network;

import com.maxym.weatherforecast.model.Forecast;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NetworkService {
    @GET("/data/2.5/forecast/daily?appid=a7566f90e4ed0120ac27665a49f3bc9a&units=metric")
    Single<Forecast> getAnswers(@Query("q") String q);

    @GET("/data/2.5/forecast/daily?appid=a7566f90e4ed0120ac27665a49f3bc9a&units=metric")
    Single<Forecast> getAnswers(@Query("lat") double lat, @Query("lon") double lon);
}
