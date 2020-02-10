package com.ameer.weatherjo.API;

import com.ameer.weatherjo.models.DarkSky;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("forecast/e4166bc8630ebca5d3c7a30e90f5956c/{location}")
    Observable<DarkSky> getDarkSky(@Path("location") String location);
//use observable inested of call for RxJava..because i need it to retrive an observable
    //u can use single insted of Observable because it will return only 1 single value.. whatever you want
}
