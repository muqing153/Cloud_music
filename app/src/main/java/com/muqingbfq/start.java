package com.muqingbfq;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.muqingbfq.login.visitor;
import com.muqingbfq.mq.wj;
import com.muqingbfq.mq.wl;

public class start {
    public start() {
        DisplayMetrics dm = home.appCompatActivity.getResources().getDisplayMetrics();
        main.k = dm.widthPixels;
        main.g = dm.heightPixels;
        if (wj.filesdri == null) {
            new wj(home.appCompatActivity);
        }
    }
}