package com.ameer.weatherjo.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ameer.weatherjo.API.ApiClient;
import com.ameer.weatherjo.API.ApiInterface;
import com.ameer.weatherjo.R;
import com.ameer.weatherjo.models.DarkSky;
import com.ameer.weatherjo.ui.settings_activity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.text.DateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView temp, summary, windSpeed, humidity, feelsLike, todayDate;
    ImageView icon;
    SwipeRefreshLayout refreshLayout;

    //ViewModel
    DarkSkyViewModel darkSkyViewModel;

    String location;
    //boolean switchOnOROff;

    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // loadDarkOrNot();

        temp = findViewById(R.id.temp);
        summary = findViewById(R.id.summary);
        windSpeed = findViewById(R.id.windSpeed);
        humidity = findViewById(R.id.humidity);
        feelsLike = findViewById(R.id.feelsLike);
        icon = findViewById(R.id.icon);
        todayDate = findViewById(R.id.todayDate);
        refreshLayout = findViewById(R.id.swipeToRefresh);

        //ViewModel
        darkSkyViewModel = ViewModelProviders.of(this).get(DarkSkyViewModel.class);


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                InternetPermission();
                refreshLayout.setRefreshing(false);

            }
        });
        InternetPermission();
        setDate();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.settings) {
            Intent i = new Intent(getApplicationContext(), settings_activity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


    private void InternetPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            //GPS is off
                            TurnOnGPS();
                        } else {
                            //GPS is on
                            getLocation();
                        }

                        loadJson();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }

    private void getLocation() {
        try {
            Location locationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double lat = locationGps.getLatitude();
            double longi = locationGps.getLongitude();
            location = lat + "," + longi;
            // location="32.532155, 35.863619";
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), "Security Exception", Toast.LENGTH_SHORT).show();
        }
    }

    private void TurnOnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GO TO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void setDate() {
        Calendar calendar = Calendar.getInstance();
        String CurrentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        todayDate.setText(CurrentDate);
    }


    public void loadJson() {
        darkSkyViewModel.getWeather(location);

        darkSkyViewModel.darkSkyMutableLiveData.observe(this, new Observer<DarkSky>() {
            @Override
            public void onChanged(DarkSky darkSky) {

                int t = (int) (Math.ceil((darkSky.getCurrently().getTemperature() - 32) * 5 / 9));

                temp.setText("" + t + "°");

                int f = (int) (Math.ceil((darkSky.getCurrently().getApparentTemperature() - 32) * 5 / 9));
                feelsLike.setText("" + f + "°");

                summary.setText(darkSky.getCurrently().getSummary());
                windSpeed.setText((int) (Math.ceil(darkSky.getCurrently().getWindSpeed() * 1.609344)) + " Km/h");
                humidity.setText(darkSky.getCurrently().getHumidity() + "");


                String i = darkSky.getCurrently().getIcon();
                switch (i) {
                    case "partly-cloudy-day":
                        icon.setImageResource(R.drawable.partlycloudyday);
                        break;
                    case "rain":
                        icon.setImageResource(R.drawable.rain);
                        break;
                    case "cloudy":
                        icon.setImageResource(R.drawable.cloudy);
                        break;
                    case "clear-night":
                        icon.setImageResource(R.drawable.clearnight);
                        break;
                    case "partly-cloudy-night":
                        icon.setImageResource(R.drawable.partlycloudynight);
                        break;
                    case "clear-day":
                        icon.setImageResource(R.drawable.clearday);
                        break;
                    case "fog":
                        icon.setImageResource(R.drawable.fog);
                        break;
                    case "sleet":
                        icon.setImageResource(R.drawable.sleet);
                        break;
                    case "snow":
                        icon.setImageResource(R.drawable.snow);
                        break;
                    default:
                        icon.setImageResource(R.drawable.weatherjo);
                        break;
                }

            }
        });

        /*ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<DarkSky> call;
        call = apiInterface.getDarkSky(location);
        call.enqueue(new Callback<DarkSky>() {
            @Override
            public void onResponse(Call<DarkSky> call, Response<DarkSky> response) {
                if (!response.isSuccessful()) {

                    temp.setText("Code: " + response.code());
                    return;
                }

                DarkSky darkSky = response.body();
                int t = (int) (Math.ceil((darkSky.getCurrently().getTemperature() - 32) * 5 / 9));

                temp.setText("" + t + "°");

                int f = (int) (Math.ceil((darkSky.getCurrently().getApparentTemperature() - 32) * 5 / 9));
                feelsLike.setText("" + f + "°");

                summary.setText(darkSky.getCurrently().getSummary());
                windSpeed.setText((int) (Math.ceil(darkSky.getCurrently().getWindSpeed() * 1.609344)) + " Km/h");
                humidity.setText(darkSky.getCurrently().getHumidity() + "");


                String i = darkSky.getCurrently().getIcon();
                switch (i) {
                    case "partly-cloudy-day":
                        icon.setImageResource(R.drawable.partlycloudyday);
                        break;
                    case "rain":
                        icon.setImageResource(R.drawable.rain);
                        break;
                    case "cloudy":
                        icon.setImageResource(R.drawable.cloudy);
                        break;
                    case "clear-night":
                        icon.setImageResource(R.drawable.clearnight);
                        break;
                    case "partly-cloudy-night":
                        icon.setImageResource(R.drawable.partlycloudynight);
                        break;
                    case "clear-day":
                        icon.setImageResource(R.drawable.clearday);
                        break;
                    case "fog":
                        icon.setImageResource(R.drawable.fog);
                        break;
                    case "sleet":
                        icon.setImageResource(R.drawable.sleet);
                        break;
                    case "snow":
                        icon.setImageResource(R.drawable.snow);
                        break;
                    default:
                        icon.setImageResource(R.drawable.weatherjo);
                        break;
                }


            }

            @Override
            public void onFailure(Call<DarkSky> call, Throwable t) {
                temp.setText(t.getMessage());
            }
        });
        */
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
