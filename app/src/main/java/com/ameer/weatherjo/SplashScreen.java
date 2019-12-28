package com.ameer.weatherjo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        Thread thread=new Thread()
        {
            @Override
            public void run() {
                try {
                    sleep(3000); //how long do u want the splash screen to stay
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();//to close the splash screen
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        thread.start();

    }
}
