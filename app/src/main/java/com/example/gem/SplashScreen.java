package com.example.gem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //thread creation(asynchronous:multiple threads or processes will be executed at a given time background
        // processes keep running we need to use handler)
        //(alt+enter)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 3000);//give delay time in milliseconds always
    }
}
       /* new CountDownTimer(3000, 1000) {

            This method will be invoked on finishing or expiring the timer
            @Override
            public void onFinish() {
                Creates an intent to start new activity
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);

                startActivity(intent);

                finish();

            }*/

            /** This method will be invoked in every 1000 milli seconds until
             * this timer is expired.Because we specified 1000 as tick time
             * while creating this CountDownTimer
             */
           /* @Override
            public void onTick(long millisUntilFinished) {

            }
        }.start();
    }
}*/
    /*protected void onStart()
    {
        super.onStart();
        if (mUser.getUid() != null) {
            Toast.makeText(SplashScreen.this, "You are already logged In", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SplashScreen.this, HomeActivity.class));

        } else
        {
            Toast.makeText(SplashScreen.this, "You can log In Now", Toast.LENGTH_LONG).show();
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
        }

    }
}*/