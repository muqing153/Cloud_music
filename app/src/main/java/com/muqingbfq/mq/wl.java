package com.muqingbfq.mq;


import com.muqingbfq.main;
import com.muqingbfq.XM;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class wl {
    public static String Cookie;

    public static void setcookie(String cookie) {
        wl.Cookie = cookie;
        main.edit.putString("Cookie", cookie);
        main.edit.commit();
    }

    public static String hq(String url) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(main.api + url)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                return response.body().string();
            }
        } catch (Exception e) {
            gj.sc("wl hq(Strnig)  " + e);
        }
        return null;
    }

    public static String post(String str, String[][] a) {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        FormBody.Builder builder = new FormBody.Builder();
        for (String[] strings : a) {
//            gj.sc(strings[0] + ":" + strings[1]);
            builder.add(strings[0], strings[1]);
        }
        Request request = new Request.Builder()
                .url(main.api + str)
                .post(builder.build())
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            gj.sc(e);
        }
        return null;
    }

    public static JSONObject jsonpost(String str, String[][] a) {
        try {
            return new JSONObject(post(str, a));
        } catch (JSONException e) {
            gj.sc(e);
            return null;
        }
    }

    public static String get(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.body() != null) {
                return response.body().string();
            }
        } catch (Exception e) {
            gj.sc("wl get(Strnig)  " + e);
        }
        return null;
    }

    public static class xz extends Thread {
        String url;
        XM x;

        public xz(String url, XM x) {
            this.url = url;
            this.x = x;
            start();
        }

    }

}
