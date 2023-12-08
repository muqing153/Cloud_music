package com.muqingbfq.api;

import android.annotation.SuppressLint;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import com.muqingbfq.fragment.gd;
import com.muqingbfq.fragment.mp3;
import com.muqingbfq.main;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;
import com.muqingbfq.mq.wl;
import com.muqingbfq.xm;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class playlist extends Thread {
    public static final String api = "/playlist/track/all?id=";
    @SuppressLint("NotifyDataSetChanged")
    public static boolean hq(List<xm> list, String uid) {
        if (uid.equals("mp3_xz.json")) {
            return playlist.hq_xz(list);
        } else if (uid.equals("mp3_like.json")) {
            return playlist.hq_like(list);
        }
        list.clear();
        try {
            String hq;
            if (wj.cz(wj.gd + uid)) {
                hq = wj.dqwb(wj.gd + uid);
            } else {
                hq = wl.hq(api + uid + "&limit=30");
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
                list.add(new xm(id, name, zz.toString(), picUrl));
            }
            return true;
        } catch (Exception e) {
            gj.sc("失败的错误 " + e);
        }
        return false;
    }

    public static boolean hq_like(List<xm> list) {
        list.clear();
        try {
            JSONObject json = gd.like;
            if (json == null || json.length() < 1) {
                return false;
            }
            for (Iterator<String> it = json.keys(); it.hasNext(); ) {
                String id = it.next();
                JSONObject jsonObject = json.getJSONObject(id);
                String name = jsonObject.getString("name");
                String zz = jsonObject.getString("zz");
                String picUrl = jsonObject.getString("picUrl");
                list.add(new xm(id, name, zz, picUrl));
            }
            return true;
        } catch (Exception e) {
            gj.sc("失败的错误 " + e);
        }
        return false;
    }

    public static boolean hq_xz(List<xm> list) {
        list.clear();
        try {
            File file = new File(wj.filesdri + "mp3");
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                ID3v2 mp3File = new Mp3File(files[i]).getId3v2Tag();
                String id = files[i].getName();
                String name = mp3File.getTitle();
                String zz = mp3File.getArtist();
                String picUrl = mp3File.getUrl();
                list.add(new xm(id, name, zz, picUrl));
            }
            return true;
        } catch (Exception e) {
            gj.sc("失败的错误 " + e);
            wj.sc(wj.mp3_xz);
        }
        return false;
    }
}
