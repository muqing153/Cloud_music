package com.muqingbfq.login;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.muqingbfq.mq.wj;
import com.muqingbfq.mq.wl;
import com.muqingbfq.yc;

import org.json.JSONException;
import org.json.JSONObject;

public class visitor extends Thread {

    AppCompatActivity activity;
    Intent intent;
    public visitor(AppCompatActivity activity, Intent intent) {
        this.activity = activity;
        this.intent = intent;
        start();
    }

    public visitor() {
        start();
    }
    @Override
    public void run() {
        super.run();
        String hq = wl.hq("/register/anonimous");
        try {
            JSONObject jsonObject = new JSONObject(hq);
            wl.setcookie(jsonObject.getString("cookie"));
            if (wj.filesdri == null) {
                new wj(activity);
            }
            if (activity != null) {
                activity.startActivity(intent);
                activity.finish();
            }
        } catch (JSONException e) {
            yc.start(activity, e);
        }
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            yc.start(activity,e);
        }
        if (activity != null) {
            activity.finish();
        }
    }
}
