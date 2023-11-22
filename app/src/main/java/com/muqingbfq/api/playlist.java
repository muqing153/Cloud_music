package com.muqingbfq.api;

import android.annotation.SuppressLint;

import com.muqingbfq.fragment.gd;
import com.muqingbfq.fragment.mp3;
import com.muqingbfq.main;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;
import com.muqingbfq.mq.wl;
import com.muqingbfq.xm;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

public class playlist extends Thread {
    public static final String api = "/playlist/track/all?id=";
    @SuppressLint("NotifyDataSetChanged")
    public static boolean hq(List<xm> list, String uid) {
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
//            main.handler.post(new mp3.lbspq_sx());
            return true;
        } catch (Exception e) {
            gj.sc("失败的错误 " + e);
        }
        return false;
    }

    public static void hq_like(List<xm> list) {
        list.clear();
        try {
            JSONObject json = gd.like;
            for (Iterator<String> it = json.keys(); it.hasNext(); ) {
                String id = it.next();
                JSONObject jsonObject = json.getJSONObject(id);
                String name = jsonObject.getString("name");
                String zz = jsonObject.getString("zz");
                String picUrl = jsonObject.getString("picUrl");
                list.add(new xm(id, name, zz, picUrl));
            }
            main.handler.post(new mp3.lbspq_sx());
        } catch (Exception e) {
            gj.sc("失败的错误 " + e);
        }
    }

    public static void hq_xz(List<xm> list) {
        list.clear();
        try {
            JSONArray json = new JSONObject(wj.dqwb(wj.mp3_xz))
                    .getJSONArray("songs");
            int length = json.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = json.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String zz = jsonObject.getString("zz");
                String picUrl = jsonObject.getString("picUrl");
                list.add(new xm(id, name, zz, picUrl));
            }
//            main.handler.post(new mp3.lbspq_sx());
        } catch (Exception e) {
            gj.sc("失败的错误 " + e);
            wj.sc(wj.mp3_xz);
        }
    }
}
