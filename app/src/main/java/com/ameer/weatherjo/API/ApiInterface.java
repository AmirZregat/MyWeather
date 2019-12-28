package com.ameer.weatherjo.API;

import com.ameer.weatherjo.models.DarkSky;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

   @GET("forecast/e4166bc8630ebca5d3c7a30e90f5956c/{location}")
    Call<DarkSky> getDarkSky(@Path("location")String location);

}
