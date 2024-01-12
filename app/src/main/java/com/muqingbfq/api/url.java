package com.muqingbfq.api;

import android.content.Intent;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import com.muqingbfq.MP3;
import com.muqingbfq.bfq;
import com.muqingbfq.fragment.Media;
import com.muqingbfq.home;
import com.muqingbfq.login.user_logs;
import com.muqingbfq.main;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;
import com.muqingbfq.mq.wl;
import com.muqingbfq.yc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class url extends Thread {
    public static String api = "/song/url/v1";
    MP3 x;

    public url(MP3 x) {
        this.x = x;
        start();
    }

    public static String hq(MP3 x) {
        try {
            if (wj.cz(wj.mp3 + x.id)) {
                return wj.mp3 + x.id;
            }
            if (wj.cz(wj.filesdri + "hc/" + x.id)) {
                return wj.filesdri + "hc/" + x.id;
            }
            String level = "standard";
            boolean wiFiConnected = gj.isWiFiConnected();
            if (wiFiConnected) {
                level = "exhigh";
            }
            String hq = wl.hq(api + "?id=" + x.id + "&level=" +
                    level + "&cookie=" + wl.Cookie);
            if (hq == null) {
                return null;
            }
            JSONObject json = new JSONObject(hq);
            gj.sc(json);
            if (json.getInt("code") == -460) {
                String message = json.getString("message");
                main.handler.post(() -> {
                    gj.ts(home.appCompatActivity, message);
                    home.appCompatActivity.startActivity(new Intent(home.appCompatActivity
                            , user_logs.class));
                });
                return null;
            }
            getLrc(x.id);
            Media.loadLyric();
            JSONArray data = json.getJSONArray("data");
            JSONObject jsonObject = data.getJSONObject(0);
            String url = jsonObject.getString("url");
            if (wiFiConnected) {
                new FileDownloader(url, x,true);
            }
            return url;
        } catch (JSONException e) {
            yc.start("url hq :" + e);
        }
        return null;
    }

    @Override
    public void run() {
        super.run();
        com.muqingbfq.bfqkz.mp3(hq(x));
    }


    public static void getLrc(String id) {
        String file = wj.mp3 + id;
        if (wj.cz(file)) {
            try {
                Mp3File mp3file = new Mp3File(file);
                if (mp3file.hasId3v2Tag()) {
                    ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                    bfq.lrc = id3v2Tag.getLyrics();
                }
                if (bfq.lrc == null) {
                    bfq.lrc = wl.hq("/lyric?id=" + id);
                }
            } catch (Exception e) {
                gj.sc(e);
            }
        } else {
            bfq.lrc = wl.hq("/lyric?id=" + id);
        }
    }

    public static String Lrc(String id) {
        return wl.hq("/lyric?id=" + id);
    }

    public static String picurl(String id) {
        String hq = wl.hq("/song/detail?ids=" + id);
        try {
            return new JSONObject(hq).getJSONArray("songs").getJSONObject(0)
                    .getJSONObject("al").getString("picUrl");
        } catch (Exception e) {
            gj.sc(e);
        }
        return null;
    }
}