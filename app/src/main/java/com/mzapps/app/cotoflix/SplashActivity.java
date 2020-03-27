package com.mzapps.app.cotoflix;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    //SharedPreference sharedPref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        sharedPref = new SharedPreference(this);

        if (sharedPref.loadNightModeState()==true)
            setTheme(R.style.AppDarkTheme_NoActionBar);
*/
        startActivity(new Intent(this,MainActivity.class));
        finish();


    }
}
