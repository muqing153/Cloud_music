package com.muqingbfq;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.muqingbfq.api.FileDownloader;
import com.muqingbfq.api.url;
import com.muqingbfq.databinding.ActivityBfqBinding;
import com.muqingbfq.fragment.Media;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;
import com.muqingbfq.mq.wl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class bfq extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static AppCompatActivity context;
    public ActivityBfqBinding inflate;
    public static String lrc;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context = this;
        inflate = ActivityBfqBinding.inflate(getLayoutInflater());
        LinearLayout root = inflate.getRoot();
        TypedValue typedValue = new TypedValue();
        home.appCompatActivity.getTheme().resolveAttribute(android.R.attr.windowBackground, typedValue, true);
        // 设置背景颜色
        root.setBackgroundColor(typedValue.data);
        setContentView(root);
        Media media = (Media) getSupportFragmentManager().findFragmentById(R.id.fragment_bfq);
        media.setBfq(this);
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
        bfq_an.kz kz = new bfq_an.kz();
        inflate.kg.setOnClickListener(kz);
        inflate.xyq.setOnClickListener(kz);
        inflate.syq.setOnClickListener(kz);
        inflate.bfqListMp3.
                setOnClickListener(view1 -> com.muqingbfq.fragment.bflb_db.start(this));
        inflate.control.setOnClickListener(new bfq_an.control(inflate.control));
        if (bfqkz.mt != null && bfqkz.mt.isPlaying()) {
            inflate.kg.setImageResource(R.drawable.bf);

        }
        text();
        inflate.like.setOnClickListener(view1 -> {
            try {
                Gson gson = new Gson();
                Type type = new TypeToken<List<MP3>>() {
                }.getType();
                List<MP3> list = gson.fromJson(wj.dqwb(wj.gd + "mp3_like.json"), type);
                if (list == null) {
                    list = new ArrayList<>();
                }
                if (bfqkz.like_bool) {
                    list.remove(bfqkz.xm);
                    inflate.like
                            .setImageTintList(ContextCompat.getColorStateList(bfq.this, R.color.text));
                } else {
                    if (!list.contains(bfqkz.xm)) {
                        list.add(bfqkz.xm);
                        inflate.like.setImageTintList(ContextCompat.
                                getColorStateList(bfq.this, android.R.color.holo_red_dark));
                    }
                }
                bfqkz.like_bool = !bfqkz.like_bool;
                wj.xrwb(wj.gd + "mp3_like.json", gson.toJson(list));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        if (bfqkz.xm != null) {
            Media.setname(bfqkz.xm.name);
            Media.setzz(bfqkz.xm.zz);
            bfq_an.islike();
            bfqkz.mt.setTX();
        }
        inflate.download.setOnClickListener(view -> {
            if (wj.cz(wj.mp3 + bfqkz.xm.id)) {
                gj.ts(this, "你已经下载过这首歌曲了");
                return;
            }
            if (bfqkz.xm != null) {
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        MP3 x = bfqkz.xm;
                        String hq = wl.hq(url.api + "?id=" + x.id + "&level=exhigh" + "&cookie=" + wl.Cookie);
                        if (hq == null) {
                            return;
                        }
                        try {
                            JSONObject json = new JSONObject(hq);
                            JSONArray data = json.getJSONArray("data");
                            JSONObject jsonObject = data.getJSONObject(0);
                            String url = jsonObject.getString("url");
                            FileDownloader.downloadFile(bfq.this, url, bfqkz.xm);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }.start();
            }
        });
    }


    public static Bitmap bitmap;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, bfq.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public void kgsetImageResource(int a) {
        if (inflate == null) {
            return;
        }
        inflate.kg.setImageResource(a);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    float downY, moveY;

    @SuppressLint("ClickableViewAccessibility")
    public void text() {
        inflate.toolbar.setOnTouchListener((view, motionEvent) -> {
            LinearLayout root = inflate.getRoot();
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downY = motionEvent.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    //长按事件，可以移动
                    moveY = motionEvent.getRawY();
                    //移动的距离
                    float dy = moveY - downY;
                    //重新设置控件的位置。移动
                    if (dy <= 0) {
                        return true;
                    } else if (dy > main.g - main.g / 5.0) {
                        finish();
                        return true;
                    }
                    root.setTranslationY(dy);
                    break;
                case MotionEvent.ACTION_UP:
                    if (inflate.getRoot().getY() > main.g - main.g / 1.5) {
                        finish();
                        return true;
                    }
                    ObjectAnimator animator = ObjectAnimator.ofFloat(root, "y", root.getY(), 0);
                    animator.setDuration(300);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            root.setY(0);
                        }
                    });
                    animator.start();
                    break;
            }
            return true;
        });
    }
}