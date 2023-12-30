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
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
    public static ActivityBfqBinding binding;
    @SuppressLint("StaticFieldLeak")
    public static View view;
    public static String lrc;
    public static com.muqingbfq.view.LrcView lrcView;
    private void setLrc(){
        lrcView = binding.lrcView;/*
        lrcview.setCurrentColor(ContextCompat.getColor(this,R.color.text));
        lrcview.setLabel(getString(R.string.app_name));
        lrcview.setCurrentTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
//            lrcView.setLrcPadding(16);
        lrcview.setCurrentTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics()));
        lrcview.setTimelineTextColor(ContextCompat.getColor(this,R.color.text_tm));
        lrcview.setDraggable(true, (view, time) -> {
            bfqkz.mt.seekTo(Math.toIntExact(time));
            return false;
        });
        if (!gj.isTablet(this)) {
            lrcview.setOnTapListener((view, x, y) -> {
                View kp = binding.kp1;
                if (kp.getVisibility() == View.VISIBLE) {
                    kp.setVisibility(View.GONE);
                } else {
                    kp.setVisibility(View.VISIBLE);
                }
            });
        } else {
            lrcview.setOnTapListener((view, x, y) -> {
            });
        }*/
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        binding = ActivityBfqBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setLrc();
        new Media(binding);
        TypedValue typedValue = new TypedValue();
        home.appCompatActivity.getTheme().resolveAttribute(android.R.attr.windowBackground, typedValue, true);
        // 设置背景颜色
        view.setBackgroundColor(typedValue.data);
        setContentView(view);
        binding.toolbar.setNavigationOnClickListener(view1 -> finish());
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.fx && bfqkz.xm != null) {
                com.muqingbfq.mq.gj.fx(this,
                        "音乐名称：" + bfqkz.xm.name +
                                "\n 作者：" + bfqkz.xm.zz +
                                "\n 链接：https://music.163.com/#/song?id=" + bfqkz.xm.id);
            }
            return false;
        });
        bfq_an.kz kz = new bfq_an.kz();
        binding.kg.setOnClickListener(kz);
        binding.xyq.setOnClickListener(kz);
        binding.syq.setOnClickListener(kz);
        binding.bfqListMp3.
                setOnClickListener(view1 -> com.muqingbfq.fragment.bflb_db.start(this));
        binding.control.setOnClickListener(new bfq_an.control(binding.control));
        if (bfqkz.mt != null && bfqkz.mt.isPlaying()) {
            binding.kg.setImageResource(R.drawable.bf);
        }
        binding.toolbar.setOnTouchListener(new Touch());
        view.setOnTouchListener(new Touch());

        binding.like.setOnClickListener(view1 -> {
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
                    binding.like
                            .setImageTintList(ContextCompat.getColorStateList(bfq.this, R.color.text));
                } else {
                    if (!list.contains(bfqkz.xm)) {
                        list.add(bfqkz.xm);
                        binding.like.setImageTintList(ContextCompat.
                                getColorStateList(bfq.this, android.R.color.holo_red_dark));
                    }
                }
                bfqkz.like_bool = !bfqkz.like_bool;
                wj.xrwb(wj.gd + "mp3_like.json", gson.toJson(list));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        binding.download.setOnClickListener(view -> {
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
        if (bfqkz.xm != null) {
            setname(bfqkz.xm.name);
            setzz(bfqkz.xm.zz);
            bfq_an.islike();
            bfqkz.mt.setTX();
        }
    }

    public static void setname(String str) {
        if (binding == null) {
            return;
        }
        binding.name.setText(str);
    }

    public static void setzz(String str) {
        if (binding == null) {
            return;
        }
        binding.zz.setText(str);
    }

    public static Bitmap bitmap;

    public static void start(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, bfq.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void kgsetImageResource(int a) {
        if (binding == null) {
            return;
        }
        binding.kg.setImageResource(a);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    public class Touch implements View.OnTouchListener {
        private float downY;

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {LinearLayout root = binding.getRoot();
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downY = motionEvent.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    //长按事件，可以移动
                    float moveY = motionEvent.getRawY();
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
                    if (binding.getRoot().getY() > main.g - main.g / 1.5) {
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
        }
    }
    public static void setlike(boolean bool) {
        if (binding == null) {
            return;
        }
        int color = R.color.text;
        if (bool) {
            color = android.R.color.holo_red_dark;
        }
        binding.like.setImageTintList(ContextCompat.
                getColorStateList(binding.getRoot().getContext(), color));
    }

    @Override
    public void finish() {
        super.finish();
        view = null;
        lrcView = null;
        main.handler.removeCallbacks(bfqkz.mt.updateSeekBar); // 在播放开始时启动更新进度
    }
}