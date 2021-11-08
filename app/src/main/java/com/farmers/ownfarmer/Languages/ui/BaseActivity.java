package com.farmers.ownfarmer.Languages.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.farmers.ownfarmer.Languages.util.LocaleHelper;


/**
 * This Base Activity is very useful to avoid repeating code :
 * Many apps has common functionality that is performed at all activities.
 * For example, initializing analytics service or even cross UI elements.
 * Having a base activity reuse the code efficiently and save maintaining time.
 * Its basic OO concept.
 *
 */
public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(LocaleHelper.onAttach(context));

    }
}
