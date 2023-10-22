package com.muqingbfq.api;

import android.view.View;

import com.muqingbfq.bfq;
import com.muqingbfq.home;
import com.muqingbfq.main;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;
import com.muqingbfq.mq.wl;
import com.muqingbfq.xm;
import com.muqingbfq.yc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class url extends Thread {
    public static String api = "/song/url/v1";
    xm x;

    public url(xm x) {
        this.x = x;
        start();
    }

    public static String hq(xm x) {
        if (bfq.getVisibility() && bfq.lrcView != null && bfq.lrcView.getVisibility() == View.VISIBLE) {
            gc(x.id);
        } else {
            lrc = null;
        }
        try {
            if (wj.cz(wj.mp3 + x.id)) {
                return wj.mp3 + x.id;
            }
            String level = "standard";
            if (gj.isWiFiConnected()) {
                level = "exhigh";
            }
            String hq = wl.hq(api + "?id=" + x.id + "&level=" +
                    level + "&cookie=" + wl.Cookie);
            gj.sc(hq);
            if (hq == null) {
                return null;
            }
            JSONObject json = new JSONObject(hq);
            JSONArray data = json.getJSONArray("data");
            JSONObject jsonObject = data.getJSONObject(0);

            String url = jsonObject.getString("url");
            if (wl.xz(url, x)) {
                url = wj.mp3 + x.id;
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


    public static String lrc, tlyric;

    public static void gc(String id) {
        lrc = null;
        tlyric = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(wl.hq("/lyric?id=" + id));
            lrc = jsonObject.getJSONObject("lrc").getString("lyric");
        } catch (JSONException e) {
            gj.sc("url gc(int id) lrc: " + e);
        }
        try {
            tlyric = jsonObject.getJSONObject("tlyric").getString("lyric");
        } catch (JSONException e) {
            gj.sc("url gc(int id) tlyric: " + e);
        }
        bfq.lrcView.loadLyric(lrc, tlyric);
    }

    public static String picurl(String id) {
        String hq = wl.hq("/song/detail?ids=" + id);
        try {
            return new JSONObject(hq).getJSONArray("songs").getJSONObject(0)
                    .getJSONObject("al").getString("picUrl");
        } catch (Exception e) {
            yc.start(e);
        }
        return null;
    }
}
