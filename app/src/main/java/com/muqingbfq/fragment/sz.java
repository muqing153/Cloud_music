package com.muqingbfq.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.muqingbfq.R;
import com.muqingbfq.activity_about_software;
import com.muqingbfq.home;
import com.muqingbfq.login.user_logs;
import com.muqingbfq.login.user_message;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wl;

public class sz {

    @SuppressLint("StaticFieldLeak")
    public static TextView name, jieshao;
    @SuppressLint("StaticFieldLeak")
    public static ImageView imageView;
    Context context;

    public sz(Context context, View view) {
        this.context = context;
        name = view.findViewById(R.id.sz_text1);
        jieshao = view.findViewById(R.id.sz_text2);
        imageView = view.findViewById(R.id.image);
        view.findViewById(R.id.xdbj).
                setOnClickListener(v -> {
                    if (name.getText().equals("登录")) {
                        context.startActivity(new Intent(context, user_logs.class));
                    }
                });
        new user_message();
    }

    public static void switch_sz(Context context, int id) {
        if (id == R.id.a) {
            gj.llq(context, "https://rust.coldmint.top/ftp/muqing/");
        } else if (id == R.id.b) {
            context.startActivity(new Intent(context, com.muqingbfq.sz.class));
//                    设置中心
        } else if (id == R.id.c) {
//                    储存清理
        } else if (id == R.id.d) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("mqqapi://card/show_pslcard?card_type=group&uin="
                                + 674891685));
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(context, "无法打开 QQ", Toast.LENGTH_SHORT).show();
            }
            // 如果没有安装 QQ 客户端或无法打开 QQ，您可以在此处理异常
//                    官方聊群
        } else if (id == R.id.e) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "mqqwpa://im/chat?chat_type=wpa&uin=" + "1966944300"));
                context.startActivity(intent);
            } catch (Exception e) {
                // 如果没有安装 QQ 或无法跳转，则会抛出异常
                Toast.makeText(context, "无法打开 QQ", Toast.LENGTH_SHORT).show();
            }
//                    联系作者
        } else if (id == R.id.f) {
            context.startActivity(new Intent(context, activity_about_software.class));
//                    关于软件
        } else if (id == R.id.g) {
            new com.muqingbfq.login.visitor();
            home.appCompatActivity.finish();
        }
    }
}