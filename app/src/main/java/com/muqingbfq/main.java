package com.muqingbfq;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatDelegate;

import com.muqingbfq.login.visitor;
import com.muqingbfq.mq.FloatingLyricsService;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;
import com.muqingbfq.mq.wl;

import java.io.File;

public class main extends Application {
    public static Application application;

    public static Handler handler = new Handler(Looper.getMainLooper());
    public static String api = "http://139.196.224.229:3000";
    public static String http = "http://139.196.224.229/muqing";
    public static int k, g;
    public static SharedPreferences sp;
    public static SharedPreferences.Editor edit;

    public static String account,token;

    public int count = 0;
    @SuppressLint("HardwareIds")
    @Override
    public void onCreate() {
        super.onCreate();
        if (wj.filesdri == null) {
            new wj(this);
        }
        File file = new File(wj.filesdri + "API.mq");
        if (file.exists()&&file.isFile()) {
            String dqwb = wj.dqwb(file.toString());
            if (!TextUtils.isEmpty(dqwb) && dqwb.startsWith("http")) {
                api = dqwb;
            } else {
                file.delete();
            }
        } else {
            wj.xrwb(file.toString(), main.api);
        }
        application = this;
//        UUID.randomUUID().toString();
        sp = getSharedPreferences("Set_up", MODE_PRIVATE);
        edit = sp.edit();
        boolean bj = false;
        try {
            com.muqingbfq.bfqkz.ms = sp.getInt("ms", 1);
        } catch (Exception e) {
            edit.putInt("ms", 1);
            bj = true;
            com.muqingbfq.bfqkz.ms = 1;
        }
        try {
            wl.Cookie = sp.getString("Cookie", "");
        } catch (Exception e) {
            edit.putString("Cookie", "");
            wl.Cookie = "";
            bj = true;
        }
        if (bj) {
            edit.commit();
        }

        wl.Cookie = main.sp.getString("Cookie", "");
        if (wl.Cookie.equals("")) {
            new visitor();
        }

        SharedPreferences theme = getSharedPreferences("theme", MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor edit = theme.edit();
        int i = theme.getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        if (i == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
            edit.putInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            edit.apply();
        }
        AppCompatDelegate.setDefaultNightMode(i);


        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityStarted(Activity activity) {
                if (count == 0) { //后台切换到前台
                    gj.sc(">>>>>>>>>>>>>>>>>>>App切到前台");
                    if (FloatingLyricsService.lei != null) {
                        stopService(new Intent(main.this, FloatingLyricsService.class));
                    }
                }
                count++;
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
                count--;
                if (count == 0) { //后台切换到前台
                    gj.sc(">>>>>>>>>>>>>>>>>>>App切到后台");
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.toString();
                            }
//                            gj.sc(count);
                            if (count != 0) {
                                return;
                            }
                            if (!FloatingLyricsService.get()) {
                                return;
                            }
                            if (Settings.canDrawOverlays(main.this)) {
                                startService(new Intent(main.this, FloatingLyricsService.class));
                            }
                            super.run();
                        }
                    }.start();
                }
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
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
