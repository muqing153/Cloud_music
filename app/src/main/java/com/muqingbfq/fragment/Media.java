package com.muqingbfq.fragment;

import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.google.android.material.slider.Slider;
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
        bfq.binding.tdt.setValueTo(max);
    }

    public static void setProgress(int progress) {
        if (bfq.view == null) {
            return;
        }
//        gj.sc(progress);
        bfq.binding.tdt.setValue(progress);
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
        if (bfqkz.notify != null) {
            bfqkz.notify.tzl_button();
        }

    }

    public Media(ActivityBfqBinding binding) {
        binding.tdt.addOnChangeListener((slider, value, fromUser) -> setTime_b(bfq_an.getTime((long) value)));
        binding.tdt.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {

                // 拖动条移动中
                main.handler.removeCallbacks(bfqkz.mt.updateSeekBar);
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                // 播放音乐到指定位置
                main.handler.post(bfqkz.mt.updateSeekBar);
                bfqkz.mt.seekTo((int) slider.getValue());
            }
        });
        /*
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
        });*/
        //初始化播放器列表
        if (bfqkz.xm != null) {
            long duration = bfqkz.mt.getDuration();
            binding.tdt.setValueTo(bfqkz.mt.getDuration());
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
            gj.sc("Media loadLyric "+e);
        }
        LrcView.setLrc(a, b);
//        bfq.lrcView.getLrc();
//        bfq.lrcView.loadLrc(a, b);
    }


    public static void setImageBitmap() {
        if (bfq.view == null) {
            return;
        }
        if (bfq.bitmap == null) {
            bfq.bitmap= BitmapFactory.decodeResource(bfq.view.getResources(), R.drawable.icon);
        }
        main.handler.post(() -> bfq.binding.cardview.setImage(com.muqingbfq.bfq.bitmap));
    }
}
