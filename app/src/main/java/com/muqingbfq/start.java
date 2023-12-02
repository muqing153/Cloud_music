package com.muqingbfq;

import android.util.DisplayMetrics;

import com.muqingbfq.mq.wj;

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