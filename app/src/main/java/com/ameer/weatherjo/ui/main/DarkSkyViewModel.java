package com.ameer.weatherjo.ui.main;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ameer.weatherjo.API.ApiClient;
import com.ameer.weatherjo.API.ApiInterface;
import com.ameer.weatherjo.models.DarkSky;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DarkSkyViewModel extends ViewModel {
    MutableLiveData<DarkSky> darkSkyMutableLiveData = new MutableLiveData<>();
    private static final String TAG = "DarkSkyViewModel";

    public void getWeather(String location) {
       /* ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<DarkSky> call;
        call = apiInterface.getDarkSky(location);
        call.enqueue(new Callback<DarkSky>() {
            @Override
            public void onResponse(Call<DarkSky> call, Response<DarkSky> response) {
                darkSkyMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<DarkSky> call, Throwable t) {
            }
        });*/


        //RxJava:

        //1-method 1:
        //===================================================================
        Observable observable = ApiClient.getInstance().getWeather(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Observer<DarkSky> observer = new Observer<DarkSky>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(DarkSky darkSky) {
                darkSkyMutableLiveData.setValue(darkSky);
            }

            @Override
            public void onError(Throwable e) {
                // Toast.makeText(MainActivity.class(), "Error: " + e, Toast.LENGTH_LONG).show();
                Log.d(TAG, "onError: " + e);
            }

            @Override
            public void onComplete() {

            }
        };

        observable.subscribe(observer);



        //===================================================================
        //2- method 2
       /* Observable<DarkSky> observable=ApiClient.getInstance().getWeather(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(o->darkSkyMutableLiveData.setValue(o),e-> Log.d(TAG, "getWeather: "+e));
        */


    }
}
