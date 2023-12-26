package com.muqingbfq.api;

import android.text.TextUtils;

import com.muqingbfq.R;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;
import com.muqingbfq.mq.wl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import com.muqingbfq.xm;

public class resource {

    public static void recommend(List<xm> list) {
        try {
            list.clear();
            String hq;
            JSONObject json;
            hq = wl.hq("/recommend/resource?cookie=" + wl.Cookie);
            if (hq == null) {
                hq = wj.dqwb(wj.gd_json);
                if (hq != null) {
                    json = new JSONObject(hq);
                    if (json.getInt("code") == 200) {
                        wj.xrwb(wj.gd_json, hq);
                        JSONArray recommend = json.getJSONArray("recommend");
                        int length = recommend.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject jsonObject = recommend.getJSONObject(i);
                            add(jsonObject, list);
                        }
                    }
                }
                return;
            }
            json = new JSONObject(hq);
            if (json.getInt("code") == 200) {
                wj.xrwb(wj.gd_json, hq);
                JSONArray recommend = json.getJSONArray("recommend");
                int length = recommend.length();
                for (int i = 0; i < length; i++) {
                    JSONObject jsonObject = recommend.getJSONObject(i);
                    add(jsonObject, list);
                }
            }
        } catch (Exception e) {
            gj.sc("resource tuijian" + e);
        }
    }

    public static void 排行榜(List<xm> list) {
        String hq;
        try {
            if (wj.cz(wj.gd_phb)) {
                hq = wj.dqwb(wj.gd_phb);
            } else {
                hq = wl.hq("/toplist");
                if (hq == null) {
                    return;
                }
                wj.xrwb(wj.gd_phb, hq);
            }
            JSONObject jsonObject = new JSONObject(hq);
            if (jsonObject.getInt("code") == 200) {
                JSONArray list_array = jsonObject.getJSONArray("list");
                int length = list_array.length();
                for (int i = 0; i < length; i++) {
                    JSONObject get = list_array.getJSONObject(i);
                    String id = get.getString("id");
                    String name = get.getString("name") + "\n";
                    String description = get.getString("description");
                    if (!TextUtils.isEmpty(description) && !description.equals("null")) {
                        name += description;
                    }
                    boolean cz = wj.cz(wj.gd + id);
                    String coverImgUrl = get.getString("coverImgUrl");
                    list.add(new xm(id, name, coverImgUrl, cz));
                }
            }
        } catch (Exception e) {
            gj.sc(e);
        }
    }

    private static void add(JSONObject jsonObject, List<xm> list) throws Exception {
        String id = jsonObject.getString("id");
        boolean cz = wj.cz(wj.gd + id);
        String name = jsonObject.getString("name");
        String picUrl = jsonObject.getString("picUrl");
        list.add(new xm(id, name, picUrl, cz));
    }
}
