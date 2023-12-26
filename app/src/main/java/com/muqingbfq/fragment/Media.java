package com.muqingbfq.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.muqingbfq.R;
import com.muqingbfq.bfq;
import com.muqingbfq.bfq_an;
import com.muqingbfq.bfqkz;
import com.muqingbfq.databinding.ActivityBfqBinding;
import com.muqingbfq.main;
import com.muqingbfq.mq.gj;
import com.muqingbfq.view.CardImage;

import org.json.JSONObject;

import me.wcy.lrcview.LrcView;

public class Media{
    @SuppressLint("StaticFieldLeak")
    private static TextView time_a, time_b;
    @SuppressLint("StaticFieldLeak")
    private static SeekBar tdt;
    public static LrcView lrcview;

    public static void setTime_a(String str) {
        if (time_a == null) {
            return;
        }
        time_a.setText(str);
    }

    public static void setTime_b(String str) {
        if (time_b == null) {
            return;
        }
        time_b.setText(str);
    }

    public static void setMax(int max) {
        tdt.setMax(max);
    }

    public static void setProgress(int progress) {
        tdt.setProgress(progress);
        lrcview.updateTime(progress);
    }

    public static void setbf(boolean bool) {
        if (bool) {
            //开始
            bfq.kgsetImageResource(R.drawable.bf);
        } else {
            //暂停
            bfq.kgsetImageResource(R.drawable.zt);
        }
        bfq_db.setkg(bool);
        bfqkz.updateNotification();
    }

    public Media(ActivityBfqBinding binding) {
        imageView = binding.cardview;
        lrcview = binding.lrcView;
        tdt = binding.tdt;
        tdt.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setTime_b(bfq_an.getTime(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 拖动条移动中
                main.handler.removeCallbacks(bfqkz.mt.updateSeekBar);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 播放音乐到指定位置
                main.handler.post(bfqkz.mt.updateSeekBar);
                bfqkz.mt.seekTo(seekBar.getProgress());
            }
        });
        time_a = binding.timeA;
        time_b = binding.timeB;
        //初始化歌词组件
        lrcview.setDraggable(true, (view, time) -> {
            bfqkz.mt.seekTo(Math.toIntExact(time));
            return false;
        });
        if (!isTablet(binding.getRoot().getContext())) {
            lrcview.setOnTapListener((view, x, y) -> {
                View kp = binding.kp1;
                if (kp.getVisibility() == View.VISIBLE) {
                    kp.setVisibility(View.GONE);
                } else {
                    kp.setVisibility(View.VISIBLE);
                }
            });
        }
        //初始化播放器列表
        if (bfqkz.xm != null) {
//            main.handler.removeCallbacks(bfqkz.mt.updateSeekBar); // 在播放开始时启动更新进度
            long duration = bfqkz.mt.getDuration();
            tdt.setMax((int) bfqkz.mt.getDuration());
            setTime_a(bfq_an.getTime(duration));
            long position = bfqkz.mt.getCurrentPosition();
//            main.handler.post(bfqkz.mt.updateSeekBar); // 在播放开始时启动更新进度
            loadLyric();
            setProgress((int) position);
        }
    }

    private boolean isTablet(Context context) {
        return (context.getResources().
                getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static void loadLyric() {
        if (lrcview == null || com.muqingbfq.bfq.lrc == null) {
            return;
        }
        JSONObject jsonObject;
        String a = null, b = null;
        try {
            jsonObject = new JSONObject(com.muqingbfq.bfq.lrc);
            a = jsonObject.getJSONObject("lrc").getString("lyric");
            b = jsonObject.getJSONObject("tlyric").getString("lyric");
        } catch (Exception e) {
            gj.sc(e);
        }
        lrcview.loadLrc(a, b);
    }


    @SuppressLint("StaticFieldLeak")
    public static CardImage imageView;

    public static void setImageBitmap() {
        if (imageView == null) {
            return;
        }
        main.handler.post(() -> imageView.setImage(com.muqingbfq.bfq.bitmap));
    }
}
