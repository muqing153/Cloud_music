package com.muqingbfq.fragment;

import android.widget.SeekBar;

import com.muqingbfq.R;
import com.muqingbfq.bfq;
import com.muqingbfq.bfq_an;
import com.muqingbfq.bfqkz;
import com.muqingbfq.databinding.ActivityBfqBinding;
import com.muqingbfq.main;
import com.muqingbfq.mq.gj;
import com.muqingbfq.view.LrcView;

import org.json.JSONObject;

public class Media {
    public static void setTime_a(String str) {
        if (bfq.view == null) {
            return;
        }
        bfq.binding.timeA.setText(str);
    }

    public static void setTime_b(String str) {
        if (bfq.view == null) {
            return;
        }
        bfq.binding.timeB.setText(str);
    }

    public static void setMax(int max) {
        if (bfq.view == null) {
            return;
        }
        bfq.binding.tdt.setMax(max);
    }

    public static void setProgress(int progress) {
        if (bfq.view == null) {
            return;
        }
        bfq.binding.tdt.setProgress(progress);
//        bfq.lrcview.updateTime(progress);
        bfq.lrcView.setTimeLrc(progress);
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
        binding.tdt.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        //初始化播放器列表
        if (bfqkz.xm != null) {
            long duration = bfqkz.mt.getDuration();
            binding.tdt.setMax(bfqkz.mt.getDuration());
            setTime_a(bfq_an.getTime(duration));
            long position = bfqkz.mt.getCurrentPosition();
            main.handler.post(bfqkz.mt.updateSeekBar); // 在播放开始时启动更新进度
            loadLyric();
            setProgress((int) position);
        }
    }


    public static void loadLyric() {
        if (com.muqingbfq.bfq.lrc == null) {
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
        LrcView.setLrc(a, b);
//        bfq.lrcView.getLrc();
//        bfq.lrcView.loadLrc(a, b);
    }


    public static void setImageBitmap() {
        if (bfq.view == null) {
            return;
        }
        main.handler.post(() -> bfq.binding.cardview.setImage(com.muqingbfq.bfq.bitmap));
    }
}
