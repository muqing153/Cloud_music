package com.muqingbfq.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.muqingbfq.R;
import com.muqingbfq.activity_about_software;
import com.muqingbfq.clean.fragment_clean;
import com.muqingbfq.login.cookie;
import com.muqingbfq.mq.gj;

public class sz {
    @SuppressLint("NonConstantResourceId")
    public static void switch_sz(Context context, int id) {
        if (id == R.id.a) {
            gj.llq(context, "https://rust.coldmint.top/ftp/muqing/");
        } else if (id == R.id.b) {
            context.startActivity(new Intent(context, com.muqingbfq.sz.class));
//                    设置中心
        } else if (id == R.id.c) {
            context.startActivity(new Intent(context, fragment_clean.class));
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
            context.startActivity(new Intent(context, cookie.class));
            //绑定网易云
        }
    }
}