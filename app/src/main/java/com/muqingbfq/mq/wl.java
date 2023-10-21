package com.muqingbfq.mq;


import com.muqingbfq.main;
import com.muqingbfq.xm;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class wl {
    public static String Cookie;

    public static void setcookie(String cookie) {
        wl.Cookie = cookie;
        main.edit.putString(main.Cookie, cookie);
        main.edit.commit();
    }

    public static boolean iskong() {
        return Cookie.equals("") || Cookie == null;
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
        xm x;

        public xz(String url, xm x) {
            this.url = url;
            this.x = x;
            start();
        }

        @Override
        public void run() {
            super.run();
            xz(url, x);
        }
    }

    public static boolean xz(String url, final xm x) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    //访问路径
                    .url(url)
                    .build();
            Call call = client.newCall(request);
            Response response = call.execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body != null) {
                    File file = new File(wj.mp3, String.valueOf(x.id));
                    InputStream inputStream = body.byteStream();
                    FileOutputStream fileOutputStream =
                            new FileOutputStream(file);
                    // 替换为实际要保存的文件路径
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }
                    fileOutputStream.close();
                    inputStream.close();
                }
            }
            JSONObject jsonObject = new JSONObject();
            if (wj.cz(wj.mp3_xz)) {
                jsonObject = new JSONObject(wj.dqwb(wj.mp3_xz));
            } else {
                jsonObject.put("songs", new JSONArray());
            }
            JSONArray songs = jsonObject.getJSONArray("songs");
            if (songs.length() > 30) {
                songs.remove(0);
            }
            JSONObject json = new JSONObject();
            json.put("id", x.id);
            json.put("name", x.name);
            json.put("zz", x.zz);
            json.put("picUrl", x.picurl);
            songs.put(json);
            wj.xrwb(wj.mp3_xz, jsonObject.toString());
            return true;
        } catch (Exception e) {
            gj.sc("wl xz " + e);
        }
        return false;
    }
}
