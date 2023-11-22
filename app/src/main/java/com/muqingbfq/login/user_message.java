package com.muqingbfq.login;

import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.muqingbfq.R;
import com.muqingbfq.fragment.sz;
import com.muqingbfq.main;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;
import com.muqingbfq.mq.wl;

import org.json.JSONObject;
public class user_message extends Thread {
    public user_message(String user) {
        main.user = user;
        start();
    }
    public user_message() {
        main.user=wj.dqwb(wj.filesdri + "user");
        start();
    }
    String name = "未登录", signature = "游客模式";
    @Override
    public void run() {
        super.run();
        try {
            if (main.user != null) {
                String hq = wl.get(main.http + "/getid.php?" + "user=" + main.user);
                if (!TextUtils.isEmpty(hq)) {
                    JSONObject json = new JSONObject(hq);
                    if (json.getInt("code") == 200) {
                        name = json.getString("name");
                        signature = json.getString("qianming");
                        json.put("name", name);
                        json.put("qianming", signature);
                        json.put("user", main.user);
                        wj.xrwb(wj.filesdri + "user", main.user);
                    }
                }
            }
                main.handler.post(() -> {
                    sz.name.setText(name);
                    sz.jieshao.setText(signature);
                    Glide.with(sz.imageView)
                        .load(main.http + "/picurl/" + main.user + ".jpg")
                        .placeholder(R.drawable.icon)//图片加载出来前，显示的图片
                        .error(R.drawable.icon)//图片加载失败后，显示的图片
                        .into(sz.imageView);
            });
        } catch (Exception e) {
            gj.sc(e);
        }
    }
}
