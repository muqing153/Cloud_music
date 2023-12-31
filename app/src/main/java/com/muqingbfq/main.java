package com.muqingbfq;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import com.muqingbfq.mq.wj;
import com.muqingbfq.mq.wl;

public class main extends Application {
    private static Application application;

    public static Handler handler = new Handler(Looper.getMainLooper());
    public static String api = "http://139.196.224.229:3000";
    public static String http = "http://139.196.224.229/muqing";
    public static int k, g;
    public static SharedPreferences sp;
    public static SharedPreferences.Editor edit;

    public static String mp3 = "mp3", mp3_csh,
     Cookie = "Cookie";
    public static String account,token;

    @SuppressLint("HardwareIds")
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
//        UUID.randomUUID().toString();
        new wj(this);
        sp = getSharedPreferences("Set_up", MODE_PRIVATE);
        edit = sp.edit();
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
    }

    public static SharedPreferences getSharedPreferences(String string) {
        return application.getSharedPreferences(string, MODE_PRIVATE);
    }

    public static String getToken() {
        SharedPreferences token1 = getSharedPreferences("token");
        return token1.getString("token", null);
    }
    public static String getAccount() {
        SharedPreferences token1 = getSharedPreferences("token");
        return token1.getString("account", null);
    }

    public static void settoken(String token, String account) {
        SharedPreferences token1 = getSharedPreferences("token");
        SharedPreferences.Editor edit1 = token1.edit();
        edit1.putString("token", token);
        edit1.putString("account", account);
        edit1.apply();
    }
}
