package com.muqingbfq.login;

import android.text.TextUtils;

import com.muqingbfq.R;
import com.muqingbfq.main;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wl;

import org.json.JSONObject;

public class user_message extends Thread {
    public user_message(String account) {
        main.account = account;
        main.token = main.getToken();
        start();
    }

    public user_message() {
        main.account = main.getAccount();
        main.token = main.getToken();
        if (!TextUtils.isEmpty(main.account)) {
            start();
        }
    }
    @Override
    public void run() {
        super.run();
        try {
            string strings = get();
            main.handler.post(() -> {
                com.muqingbfq.fragment.
                        wode.setname(strings.userName());
                com.muqingbfq.fragment.
                        wode.setqianming(strings.introduce());
                com.bumptech.glide.Glide.with(com.muqingbfq.fragment.
                                wode.imageView)
                        .load(strings.headIcon())
                        .placeholder(R.drawable.icon)//图片加载出来前，显示的图片
                        .error(R.drawable.icon)//图片加载失败后，显示的图片
                        .into(com.muqingbfq.fragment.
                                        wode.imageView);
            });
        } catch (Exception e) {
            gj.sc(e);
        }
    }

    public static string get() throws Exception {
        JSONObject post = wl.jsonpost("/php/user.php?action=getSpaceInfo",
                new String[]{
                        "account"
                },
                new String[]{
                        main.account
                });
        gj.sc(post);
        if (!TextUtils.isEmpty(post.toString()) &&
                post.getInt("code") == 0) {
            JSONObject data = post.getJSONObject("data");
            String headIcon = data.getString("headIcon");//头像
            String account = data.getString("account");//账号
            String userName = data.getString("userName");//名称
            String introduce = data.getString("introduce");//签名
            String cover = data.getString("cover");//背景
            if (headIcon.startsWith("..")) {
                headIcon = "https://rust.coldmint.top" + headIcon.substring(2);
            }
            if (cover.startsWith("..")) {
                cover = "https://rust.coldmint.top" + cover.substring(2);
            }
            String gender = data.getString("gender");
            return new string(new String[]{
                    headIcon, account, userName, introduce, cover, gender
            });
        }
        return null;
    }

    static class string {
        private final String[] strings;
        public string(String[] strings) {
            this.strings = strings;
        }

        public String headIcon() {
            return strings[0];
        }

        public String account() {
            return strings[1];
        }

        public String userName() {
            return strings[2];
        }

        public String introduce() {
            return strings[3];
        }

        public String cover() {
            return strings[4];
        }
        public String gender() {
            return strings[5];
        }
    }
}
