package com.ameer.weatherjo.ui.main;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ameer.weatherjo.API.ApiClient;
import com.ameer.weatherjo.API.ApiInterface;
import com.ameer.weatherjo.models.DarkSky;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DarkSkyViewModel extends ViewModel {
    MutableLiveData<DarkSky> darkSkyMutableLiveData = new MutableLiveData<>();

    public void getWeather(String location) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
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
        });
    }
}
