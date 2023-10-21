package com.muqingbfq;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.core.content.ContextCompat;

import com.muqingbfq.api.url;
import com.muqingbfq.fragment.gd;
import com.muqingbfq.fragment.mp3;
import com.muqingbfq.mq.gj;

import java.text.SimpleDateFormat;
import java.util.Date;

public class bfq_an {
    public static class kz implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.kg) {
                MyButtonClickReceiver.playOrPause();
            } else if (id == R.id.syq) {
                syq();
            } else if (id == R.id.xyq) {
                xyq();
            }
        }
    }

    public static void syq() {
        bfqkz.mt.pause();
        int i = bfqkz.list.indexOf(bfqkz.xm) - 1;
        if (i < 0) {
            i = 0;
        }
        bfqkz.xm = bfqkz.list.get(i);
        new url(bfqkz.xm);
    }

    public static void xyq() {
        bfqkz.mt.pause();
        int ms = bfqkz.ms;
        if (bfqkz.ms == 0) {
            ms = 1;
        }
        bfqkz.xm = bfqkz.list.get(bfqkz.getmti(ms));
        new url(bfqkz.xm);
    }

    public static class tdt implements SeekBar.OnSeekBarChangeListener {
        //        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            bfq.time_b.setText(getTime(progress));

//                bfq.time_b.setText(simpleDateFormat.format(new Date(progress)));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // 暂停播放
             bfqkz.mt.pauseTimer();
//            bfqkz.mt.pause();
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // 播放音乐到指定位置
            bfqkz.mt.seekTo(seekBar.getProgress());
            bfqkz.mt.resumeTimer();
//            bfqkz.mt.start();
        }
    }

    public static class control implements View.OnClickListener {
        public control(ImageView imageView) {
            switch (bfqkz.ms) {
                case 0:
                    imageView.setImageResource(R.drawable.mt_xh);
                    break;
                case 1:
                    imageView.setImageResource(R.drawable.mt_sx);
                    break;
                case 2:
                    imageView.setImageResource(R.drawable.mt_sj);
                    break;
            }
        }

        @Override
        public void onClick(View v) {
            ImageView imageView = (ImageView) v;
            switch (bfqkz.ms) {
                case 0:
                    bfqkz.ms = 1;
                    imageView.setImageResource(R.drawable.mt_sx);
//                    顺序
                    break;
                case 1:
                    bfqkz.ms = 2;
                    imageView.setImageResource(R.drawable.mt_sj);
//                    随机
                    break;
                case 2:
                    bfqkz.ms = 0;
                    imageView.setImageResource(R.drawable.mt_xh);
//                    循环
                    break;
            }
            main.edit.putInt("ms", bfqkz.ms);
            main.edit.commit();
//            imageView.setImageDrawable();
        }
    }

    @SuppressLint("SimpleDateFormat")
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
    public static String getTime(long time) {
        return simpleDateFormat.format(new Date(time));
    }

    public static void UI(boolean bool) {
        if (bfq.getVisibility()) {
            bfq.xyq.setEnabled(bool);
            bfq.syq.setEnabled(bool);
            bfq.kg.setEnabled(bool);
            bfq.tdt.setEnabled(bool);
        }
    }
    public static void islike(Context context) {
        try {
            gd.like.getJSONObject(String.valueOf(bfqkz.xm.id));
            bfq.like.setImageTintList(ContextCompat.
                    getColorStateList(context, android.R.color.holo_red_dark));
            bfqkz.like_bool = true;
        } catch (Exception e) {
            bfq.like.setImageTintList(ContextCompat.getColorStateList(context, R.color.text));
            gj.sc("bfq_an islike() :" + e);
            bfqkz.like_bool = false;
        }
    }
}
