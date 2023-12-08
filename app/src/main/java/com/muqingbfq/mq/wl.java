package com.muqingbfq.mq;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.nfc.Tag;
import android.provider.MediaStore;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.muqingbfq.home;
import com.muqingbfq.main;
import com.muqingbfq.xm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.FormBody;
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

    public static String post(String str, String[] a, String[] b) {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        FormBody.Builder builder = new FormBody.Builder();
        for (int i = 0; i < a.length; i++) {
            builder.add(a[i], b[i]);
        }
        Request request = new Request.Builder()
                .url("https://rust.coldmint.top" + str)
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

    public static JSONObject jsonpost(String str, String[] a, String[] b) {
        try {
            return new JSONObject(post(str, a, b));
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
        xm x;

        public xz(String url, xm x) {
            this.url = url;
            this.x = x;
            start();
        }

        @Override
        public void run() {
            super.run();
            try {
                if (new File(wj.mp3).isDirectory()) {
                    File[] aa = new File(wj.mp3).listFiles();
                    if (aa.length >= 30) {
                        aa[0].delete();
                    }
                }
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
                        File file = new File(wj.mp3, x.id+".mp3");
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                        File parentFile = file.getParentFile();
                        if (!parentFile.isDirectory()) {
                            parentFile.mkdirs();
                        }
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
                        Mp3File mp3file = new Mp3File(file);
                        if (mp3file.hasId3v2Tag()) {
                            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                            // 设置新的ID值
                            id3v2Tag.setTitle(x.name);
                            id3v2Tag.setArtist(x.zz);
                            id3v2Tag.setAlbum(x.zz);
                            id3v2Tag.setUrl(x.picurl.toString());
                            mp3file.save(wj.mp3 + x.id);
                            file.delete();
                            // 保存修改后的音乐文件，删除原来的文件
                        }
                    }
                }
            } catch (Exception e) {
                gj.sc("wl xz " + e);
            }
        }
    }

}
