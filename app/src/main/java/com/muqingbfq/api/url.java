package com.muqingbfq.api;

import com.muqingbfq.fragment.Media;
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
        if (Media.getlrcView() != null) {
            gc(x.id);
        }
        try {
            if (wj.cz(wj.mp3 + x.id)) {
                return wj.mp3 + x.id;
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
            JSONArray data = json.getJSONArray("data");
            JSONObject jsonObject = data.getJSONObject(0);
            String url = jsonObject.getString("url");
            if (wiFiConnected) {
                new wl.xz(url, x);
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


    public static void gc(String id) {
        String lrc = null, tlyric = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(wl.hq("/lyric?id=" + id));
            lrc = jsonObject.getJSONObject("lrc").getString("lyric");
            tlyric = jsonObject.getJSONObject("tlyric").getString("lyric");
        } catch (JSONException e) {
            gj.sc("url gc(int id) lrc: " + e);
        }
        Media.loadLyric(lrc, tlyric);
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
