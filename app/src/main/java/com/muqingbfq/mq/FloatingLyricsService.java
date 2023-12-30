package com.muqingbfq.mq;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.muqingbfq.bfq_an;
import com.muqingbfq.bfqkz;
import com.muqingbfq.databinding.FloatLrcviewBinding;
import com.muqingbfq.main;
import com.muqingbfq.view.LrcView;

import java.io.File;
import java.lang.reflect.Type;

public class FloatingLyricsService extends Service implements View.OnClickListener, View.OnTouchListener {
    private WindowManager windowManager;
    private View layout;
    public Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            if (bfqkz.mt.isPlaying() && lrcView != null) {
                long position = bfqkz.mt.getCurrentPosition();
                lrcView.setTimeLrc(position);
            }
            handler.postDelayed(this, 1000); // 每秒更新一次进度
        }
    };
    @SuppressLint("StaticFieldLeak")
    public static FloatingLyricsService lei;

    public static boolean get() {
        File file = new File(wj.filesdri + "FloatingLyricsService.json");
        if (file.exists() && file.isFile()) {
            String dqwb = wj.dqwb(file.toString());
            Gson gson = new Gson();
            Type type = new TypeToken<SETUP>() {
            }.getType();
            SETUP setup = gson.fromJson(dqwb, type);
            return setup.i != 0;
        } else {
            return true;
        }
    }
    Handler handler = new Handler();
    LrcView lrcView;
    WindowManager.LayoutParams params;

    public static class SETUP {
        //0是关闭 1是打开 2是锁定
        public int i;
        public float TOP;
        public int Y;
    }

    public SETUP setup = new SETUP();
    File file;

    public int lock() {
        if (setup != null && setup.i == 2) {
            return WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        }
        return WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        lei = this;
        try {
            file = new File(wj.filesdri + "FloatingLyricsService.json");
            if (file.exists() && file.isFile()) {
                String dqwb = wj.dqwb(file.toString());
                Gson gson = new Gson();
                Type type = new TypeToken<SETUP>() {
                }.getType();
                setup = gson.fromJson(dqwb, type);
            } else {
                setup.i = 1;
                setup.TOP = 0;
                setup.Y = -main.g;
            }


        } catch (Exception e) {
            wj.sc(file.toString());
            gj.sc(e);
        }
        // 创建悬浮窗歌词的 View
//        FloatLrcviewBinding
        FloatLrcviewBinding binding = FloatLrcviewBinding.inflate(LayoutInflater.from(this));
        layout = binding.getRoot();
        layout.setOnTouchListener(this);
//        ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
//        layout.setLayoutParams(layoutParams);

//        int i = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;FLAG_NOT_TOUCH_MODAL
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                        WindowManager.LayoutParams.TYPE_PHONE,
                lock(),
                PixelFormat.TRANSLUCENT
        );

        params.y = setup.Y;

        lrcView = binding.lrcView;
        bfq_an.kz bfqAn = new bfq_an.kz();
        binding.kg.setOnClickListener(bfqAn);
        binding.syq.setOnClickListener(bfqAn);
        binding.xyq.setOnClickListener(bfqAn);
        binding.lock.setOnClickListener(this);
//        params.gravity = Gravity.CENTER;


        // 获取 WindowManager 并将悬浮窗歌词添加到窗口中
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        if (setup.i == 2) {
            params.flags = lock();
            layout.setBackground(null);
            lrcView.setAlpha(0.5f);
            layout.findViewById(com.muqingbfq.R.id.controlLayout).setVisibility(View.GONE);
        }
        windowManager.addView(layout, params);
        gj.sc("添加成功");
        handler.post(updateSeekBar); // 在播放开始时启动更新进度
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 在 Service 销毁时移除悬浮窗歌词
        if (windowManager != null && layout != null) {
            windowManager.removeView(layout);
            handler.removeCallbacks(bfqkz.mt.updateSeekBar); // 在播放开始时启动更新进度
        }
        lei = null;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void baocun() {
        String s = new Gson().toJson(setup);
        wj.xrwb(new File(wj.filesdri + "FloatingLyricsService.json").toString(), s);
    }

    private int initialY;
    private float initialTouchY;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录触摸事件的初始位置和坐标
                initialY = params.y;
                initialTouchY = motionEvent.getRawY();
                return true;
            case MotionEvent.ACTION_MOVE:
                // 计算触摸事件的偏移量，将悬浮窗口的位置设置为初始位置加上偏移量
                int offsetY = (int) (motionEvent.getRawY() - initialTouchY);
                setup.Y = initialY + offsetY;
                params.y = setup.Y;
                windowManager.updateViewLayout(layout, params);
                return true;
            case MotionEvent.ACTION_UP:
                baocun();
                break;

        }
        return false;
    }

    @Override
    public void onClick(View view) {
        setyc();
    }

    public void setyc() {
        setup.i = 2;
        params.flags = lock();
        layout.setBackground(null);
        lrcView.setAlpha(0.5f);
        layout.findViewById(com.muqingbfq.R.id.controlLayout).setVisibility(View.GONE);
        windowManager.updateViewLayout(layout, params);
        baocun();
    }

    public void show() {
        setup.i = 1;
        params.flags = lock();
        layout.setBackgroundColor(Color.parseColor("#50000000"));
        lrcView.setAlpha(1.0f);
        layout.findViewById(com.muqingbfq.R.id.controlLayout).setVisibility(View.VISIBLE);
        windowManager.updateViewLayout(layout, params);
        baocun();
    }
}
