package com.ameer.weatherjo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class settings_activity extends AppCompatActivity {

    Switch aSwitch;
    boolean switchOnOROff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_activity);
        //aSwitch = findViewById(R.id.switchNight);

       // saveData();
       // loadData();

        /*if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            aSwitch.setChecked(true);
        }*/
       /* aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    saveData();
                    restartApp();
                } else
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    saveData();
                    restartApp();
            }
        });*/

    }

    /*public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("DarkSwitch", false);
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        switchOnOROff = sharedPreferences.getBoolean("DarkSwitch", false);
        //aSwitch.setChecked(switchOnOROff);
    }

    private void restartApp() {
        startActivity(new Intent(getApplicationContext(), settings_activity.class));
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));

    }
}
