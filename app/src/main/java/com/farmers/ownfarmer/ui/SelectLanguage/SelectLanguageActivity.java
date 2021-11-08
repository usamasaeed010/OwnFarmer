package com.farmers.ownfarmer.ui.SelectLanguage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.farmers.ownfarmer.Languages.data.PreferencesHelper;
import com.farmers.ownfarmer.Languages.util.LocaleHelper;
import com.farmers.ownfarmer.MainActivity;
import com.farmers.ownfarmer.R;
import com.farmers.ownfarmer.ui.login.LogInActivity;


public class SelectLanguageActivity extends AppCompatActivity {

    String language;

    SharedPreferences user_language_preference;
    SharedPreferences.Editor user_language_editor;
    String defLanguage ;

    Button urduLanguage_BTN, englishLanguage_BTN;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.green_borders, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.green_borders));
        }

        user_language_preference = getSharedPreferences("select_language", MODE_PRIVATE);
        language = user_language_preference.getString("my_language",null);

        if((language!=null && !language.isEmpty()))
        {
            // startActivity(new Intent(getApplicationContext() , MainActivity.class));
            startActivity(new Intent(getApplicationContext() , LogInActivity.class));

        } else {

            setContentView(R.layout.activity_select_language);


            /////// shared preference
            user_language_preference = getSharedPreferences("select_language", MODE_PRIVATE);
            user_language_editor = user_language_preference.edit();


            ////////////// initialization
            urduLanguage_BTN = findViewById(R.id.urdu_language_btn);
            englishLanguage_BTN = findViewById(R.id.english_language_btn);


            ////// urdu selected
            urduLanguage_BTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ////for urdu choose ////////

                    language = "ur";

                    user_language_editor.putString("my_language", "ur");
                    user_language_editor.apply();

                    select_language(language);

                }
            });



            //////// english _ selected
            englishLanguage_BTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ////for english choose ////////

                    language = "en";

                    user_language_editor.putString("my_language", "eng");
                    user_language_editor.apply();

                    select_language(language);

                }
            });


        }

    }


    public void select_language(String language)
    {
        PreferencesHelper.setLanguage(SelectLanguageActivity.this, language);
        LocaleHelper.setLocale(SelectLanguageActivity.this, language);

        Intent intent = new Intent(getApplicationContext(), SelectLanguageActivity.class);
        startActivity(intent);
        finish();

    }

}