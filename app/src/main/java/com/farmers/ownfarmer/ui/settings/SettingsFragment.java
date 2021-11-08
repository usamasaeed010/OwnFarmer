package com.farmers.ownfarmer.ui.settings;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.farmers.ownfarmer.Languages.data.PreferencesHelper;
import com.farmers.ownfarmer.Languages.util.LocaleHelper;
import com.farmers.ownfarmer.MainActivity;
import com.farmers.ownfarmer.R;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


public class SettingsFragment extends Fragment {

    //for instance
    Activity activity;
    MainActivity mainActivity = new MainActivity();

    String defLanguage, language;
    SharedPreferences user_language_preference;
    SharedPreferences.Editor user_language_editor;
    CardView switch_language_CV;
    String my_language;

    TextView tv_ChangeLanguage;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ////
        activity = mainActivity.getInstance();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
            defLanguage = locale.getLanguage();
        } else {
            //noinspection deprecation
            locale = getResources().getConfiguration().locale;
            defLanguage = locale.getLanguage();
        }


        //// shared preference
        user_language_preference = getContext().getSharedPreferences("select_language", MODE_PRIVATE);
        language = user_language_preference.getString("my_language", null);

        /////// shared preference
        user_language_preference = getContext().getSharedPreferences("select_language", MODE_PRIVATE);
        user_language_editor = user_language_preference.edit();

        /// initialization
        tv_ChangeLanguage = view.findViewById(R.id.change_text);
        switch_language_CV = view.findViewById(R.id.switch_language_CV);
        switch_language_CV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (defLanguage.contains("ur")) {
                    language = "en";

                    user_language_editor.putString("my_language", "eng");
                    user_language_editor.apply();

                    select_language(language);
                } else {

                    language = "ur";

                    user_language_editor.putString("my_language", "ur");
                    user_language_editor.apply();

                    select_language(language);

                }
            }
        });

        if (defLanguage.contains("ur")) {

            my_language = "Urdu";
            tv_ChangeLanguage.setText("Switch Language " + "(" + "English" + ")");
            tv_ChangeLanguage.setGravity(Gravity.END);

        } else {

            my_language = "English";
            tv_ChangeLanguage.setText("زبان سوئچ " + "(" + "اردو " + ")");
            tv_ChangeLanguage.setGravity(Gravity.END);

        }

        return view;
    }

    //////// change language
    public void select_language(String language) {
        PreferencesHelper.setLanguage(activity, language);
        LocaleHelper.setLocale(activity, language);

        Intent intent = new Intent(activity, MainActivity.class);
        startActivity(intent);
        activity.finish();
    }
}