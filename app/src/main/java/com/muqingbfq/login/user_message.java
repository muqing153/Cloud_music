package com.muqingbfq.login;

import com.bumptech.glide.Glide;
import com.muqingbfq.R;
import com.muqingbfq.fragment.sz;
import com.muqingbfq.main;
import com.muqingbfq.mq.wl;
import com.muqingbfq.yc;

import org.json.JSONObject;

public class user_message extends Thread {

    public user_message() {
        start();
    }

    @Override
    public void run() {
        super.run();
        String hq = wl.hq("/user/account" + "?cookie=" + wl.Cookie);
        try {
            JSONObject jsonObject = new JSONObject(hq).getJSONObject("profile");
            String nickname = jsonObject.getString("nickname");
            String signature = jsonObject.getString("signature");
            String avatarUrl = jsonObject.getString("avatarUrl");
            main.handler.post(() -> {
                sz.name.setText(nickname);
                sz.jieshao.setText(signature);
                Glide.with(sz.imageView)
                        .load(avatarUrl)
                        .placeholder(R.drawable.icon)//图片加载出来前，显示的图片
                        .error(R.drawable.deleat)//图片加载失败后，显示的图片
                        .into(sz.imageView);
            });
        } catch (Exception e) {
            yc.start(e);
        }
    }
}
