package com.muqingbfq;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.muqingbfq.login.visitor;
import com.muqingbfq.mq.wj;
import com.muqingbfq.mq.wl;

public class start extends AppCompatActivity {
    Intent home;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        home = new Intent(this, home.class);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        main.k = dm.widthPixels;
        main.g = dm.heightPixels;
        startApp();
    }
    private void startApp() {
        SharedPreferences theme = getSharedPreferences("theme", MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor edit = theme.edit();
        int i = theme.getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        if (i == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
            edit.putInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            edit.apply();
        }
        AppCompatDelegate.setDefaultNightMode(i);
        wl.Cookie = main.sp.getString(main.Cookie, "");
        if (wj.filesdri == null) {
            new wj(this);
        }
        if (wl.Cookie.equals("")) {
            new visitor(this, home);
        } else {
            startActivity(home);
            finish();
        }
    }
}