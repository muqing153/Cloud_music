package com.muqingbfq;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import com.muqingbfq.mq.MyExceptionHandler;
import com.muqingbfq.mq.wj;
import com.muqingbfq.mq.wl;

public class main extends Application {
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    public static Handler handler = new Handler(Looper.getMainLooper());
    public static String api = "http://139.196.224.229:3000";
    public static String http = "http://139.196.224.229/muqing";
    public static int k, g;
    public static SharedPreferences sp;
    public static SharedPreferences.Editor edit;

    public static String mp3 = "mp3", mp3_csh,
            Time = "Time", Cookie = "Cookie";

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        new wj(this);
        sp = getSharedPreferences("Set_up", MODE_PRIVATE);
        edit = sp.edit();
        if (sp.getLong(Time, 0) == 0) {
            edit.putLong(Time, System.currentTimeMillis());
        }
        start.time = sp.getLong(Time, System.currentTimeMillis());
        boolean bj = false;
        try {
            mp3_csh = sp.getString(mp3, "");
        } catch (Exception e) {
            edit.putString(mp3, "");
            edit.commit();
            mp3_csh = "";
        }
        try {
            com.muqingbfq.bfqkz.ms = sp.getInt("ms", 1);
        } catch (Exception e) {
            edit.putInt("ms", 1);
            bj = true;
            com.muqingbfq.bfqkz.ms = 1;
        }
        try {
            wl.Cookie = sp.getString(Cookie, "");
        } catch (Exception e) {
            edit.putString(Cookie, "");
            wl.Cookie = "";
            bj = true;
        }
        if (bj) {
            edit.commit();
        }
        // 创建全局异常处理器实例 设置全局异常处理器
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
    }
}
