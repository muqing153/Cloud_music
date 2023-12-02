package com.muqingbfq;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.muqingbfq.databinding.ActivityBfqBinding;
import com.muqingbfq.fragment.Media;
import com.muqingbfq.mq.gj;

public class bfq extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static AppCompatActivity context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        ActivityBfqBinding inflate = ActivityBfqBinding.inflate(getLayoutInflater());
        setContentView(inflate.getRoot());
        Toolbar toolbar = inflate.toolbar;
        toolbar.setNavigationOnClickListener(view1 -> finish());
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.fx && bfqkz.xm != null) {
                com.muqingbfq.mq.gj.fx(this,
                        "音乐名称：" + bfqkz.xm.name +
                                "\n 作者：" + bfqkz.xm.zz +
                                "\n 链接：https://music.163.com/#/song?id=" + bfqkz.xm.id);
            }
            return false;
        });
        if (bfqkz.xm != null) {
            Media.setname(bfqkz.xm.name);
            Media.setzz(bfqkz.xm.zz);
        }
        inflate.name.setOnLongClickListener(view -> {
            gj.fz(bfq.this, inflate.name.getText().toString());
            gj.ts(bfq.this, "复制成功");
            return false;
        });
    }

    public static Bitmap bitmap;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, bfq.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Media.view = null;
    }
}