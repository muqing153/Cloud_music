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
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.muqingbfq.databinding.ActivityBfqBinding;
import com.muqingbfq.fragment.Media;

import org.json.JSONObject;

public class bfq extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static AppCompatActivity context;
    ActivityBfqBinding inflate;

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
        if (bfqkz.xm != null) {
            Media.setname(bfqkz.xm.name);
            Media.setzz(bfqkz.xm.zz);
        }
/*        inflate.name.setOnLongClickListener(view -> {
            gj.fz(bfq.this, inflate.name.getText().toString());
            gj.ts(bfq.this, "复制成功");
            return false;
        });*/
        bfq_an.kz kz = new bfq_an.kz();
        inflate.kg.setOnClickListener(kz);
        inflate.xyq.setOnClickListener(kz);
        inflate.xyq.setOnClickListener(kz);
        inflate.bfqListMp3.
                setOnClickListener(view1 -> com.muqingbfq.fragment.bflb_db.start(this));
        inflate.control.setOnClickListener(new bfq_an.control(inflate.control));
        if (bfqkz.mt != null && bfqkz.mt.build.isPlaying()) {
            inflate.kg.setImageResource(R.drawable.bf);
            bfq_an.islike(this);
        }
        text();

        inflate.like.setOnClickListener(view1 -> {
            ImageView like = (ImageView) view1;
            try {
                if (bfqkz.like_bool) {
                    like.setImageTintList(ContextCompat.getColorStateList(bfq.this, R.color.text));
                    com.muqingbfq.fragment.gd.like.remove(String.valueOf(bfqkz.xm.id));
                } else {
                    like.setImageTintList(ContextCompat.
                            getColorStateList(bfq.this, android.R.color.holo_red_dark));
                    JSONObject json = new JSONObject();
                    json.put("name", bfqkz.xm.name);
                    json.put("zz", bfqkz.xm.zz);
                    json.put("picUrl", bfqkz.xm.picurl);
                    com.muqingbfq.fragment.gd.like.put(String.valueOf(bfqkz.xm.id), json);
                }
                com.muqingbfq.mq.wj.xrwb(com.muqingbfq.mq.wj.mp3_like,
                        com.muqingbfq.fragment.gd.like.toString());
                bfqkz.like_bool = !bfqkz.like_bool;
            } catch (Exception e) {
                e.printStackTrace();
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