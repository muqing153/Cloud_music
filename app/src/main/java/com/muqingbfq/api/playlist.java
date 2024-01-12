package com.muqingbfq.api;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import com.muqingbfq.MP3;
import com.muqingbfq.fragment.gd;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;
import com.muqingbfq.mq.wl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

public class playlist extends Thread {
    public static final String api = "/playlist/track/all?id=";

    @SuppressLint("NotifyDataSetChanged")
    public static boolean hq(List<MP3> list, String uid) {
        switch (uid) {
            case "mp3_xz.json":
                return playlist.hq_xz(list);
            case "mp3_like.json":
                return playlist.hq_like(list);
            case "mp3_hc.json":
                return hq_hc(list);
        }
        list.clear();
        try {
            String hq;
            if (wj.cz(wj.gd + uid)) {
                hq = wj.dqwb(wj.gd + uid);
            } else {
                if (wj.cz(wj.filesdri + "user.mq")) {
                    hq = wl.hq(api + uid + "&limit=100" + "&cookie=" + wl.Cookie);
                } else {
                    hq = wl.hq(api + uid + "&limit=100");
                }
            }
            JSONObject json = new JSONObject(hq);
            JSONArray songs = json.getJSONArray("songs");
            int length = songs.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = songs.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");

                JSONObject al = jsonObject.getJSONObject("al");
                JSONArray ar = jsonObject.getJSONArray("ar");
                StringBuilder zz = new StringBuilder();
                int length_a = ar.length();
                for (int j = 0; j < length_a; j++) {
                    zz.append(ar.getJSONObject(j).getString("name"))
                            .append("/");
                }
                zz.append("-").append(al.getString("name"));
                String picUrl = al.getString("picUrl");
                list.add(new MP3(id, name, zz.toString(), picUrl));
            }
            return true;
        } catch (Exception e) {
            gj.sc("失败的错误 " + e);
        }
        return false;
    }

    public static boolean hq_like(List<MP3> list) {
        list.clear();
        try {
            String dqwb = wj.dqwb(wj.gd + "mp3_like.json");
            if (dqwb == null) {
                return false;
            }
            Type type = new TypeToken<List<MP3>>() {
            }.getType();
            Gson gson = new Gson();
            list.addAll(gson.fromJson(dqwb, type));
            return true;
        } catch (Exception e) {
            gj.sc("失败的错误 " + e);
        }
        return false;
    }

    public static boolean hq_xz(List<MP3> list) {
        list.clear();
        try {
            File file = new File(wj.filesdri + "mp3");
            File[] files = file.listFiles();
            for (File value : files) {
                ID3v2 mp3File = new Mp3File(value).getId3v2Tag();
                String id = value.getName();
                String name = mp3File.getTitle();
                String zz = mp3File.getArtist();
                String picUrl = mp3File.getUrl();
                list.add(new MP3(id, name, zz, picUrl));
            }
            return true;
        } catch (Exception e) {
            gj.sc("失败的错误 " + e);
            wj.sc(wj.mp3_xz);
        }
        return false;
    }

    public static boolean hq_hc(List<MP3> list) {
        list.clear();
        try {
            String dqwb = wj.dqwb(wj.gd + "mp3_hc.json");
            if (dqwb == null) {
                return false;
            }
            Type type = new TypeToken<List<MP3>>() {
            }.getType();
            Gson gson = new Gson();
            list.addAll(gson.fromJson(dqwb, type));
            return true;
        } catch (Exception e) {
            gj.sc("失败的错误 " + e);
            wj.sc(wj.mp3_xz);
        }
        return false;
    }
}
