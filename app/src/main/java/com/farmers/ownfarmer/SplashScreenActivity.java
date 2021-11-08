package com.farmers.ownfarmer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.farmers.ownfarmer.ui.SelectLanguage.SelectLanguageActivity;
import com.farmers.ownfarmer.ui.login.LogInActivity;

public class SplashScreenActivity extends AppCompatActivity {

    SharedPreferences preferences;
    String user_login;

    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ////get value from sp
        preferences = getSharedPreferences("autologin_patient", MODE_PRIVATE);
        user_login = preferences.getString("log_status", null);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //////////// checking if user already logged in
                if (user_login == null) {

                    Intent intent = new Intent(SplashScreenActivity.this, LogInActivity.class);
                    startActivity(intent);
                    finish();

                } else if(user_login!=null && !user_login.isEmpty()){

                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else{

                    Intent intent = new Intent(SplashScreenActivity.this, LogInActivity.class);
                    startActivity(intent);
                    finish();

                }

            }

        }, SPLASH_DISPLAY_LENGTH);

    }
}