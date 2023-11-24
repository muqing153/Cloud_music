package com.muqingbfq;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class bfq extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        setContentView(R.layout.activity_bfq);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.end);
        toolbar.setNavigationOnClickListener(view1 -> finish());
        toolbar.inflateMenu(R.menu.bfq);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.fx) {
                if (bfqkz.xm != null) {
                    com.muqingbfq.mq.gj.fx(this,
                            "音乐名称：" + bfqkz.xm.name +
                                    "\n 作者：" + bfqkz.xm.zz +
                                    "\n 链接：https://music.163.com/#/song?id=" + bfqkz.xm.id);
                }
            }
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
}