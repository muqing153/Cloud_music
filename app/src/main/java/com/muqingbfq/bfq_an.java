package com.muqingbfq;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.muqingbfq.api.url;
import com.muqingbfq.fragment.Media;
import com.muqingbfq.fragment.gd;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
        if (bfqkz.list.isEmpty()) {
            return;
        }
        bfqkz.mt.pause();
        int i = bfqkz.list.indexOf(bfqkz.xm) - 1;
        if (i < 0) {
            i = 0;
        }
        bfqkz.xm = bfqkz.list.get(i);
        new url(bfqkz.xm);
    }

    public static void xyq() {
        if (bfqkz.list.isEmpty()) {
            return;
        }
        bfqkz.mt.pause();
        int ms = bfqkz.ms;
        if (bfqkz.ms == 0) {
            ms = 1;
        }
        bfqkz.xm = bfqkz.list.get(bfqkz.getmti(ms));
        new url(bfqkz.xm);
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

    public static void islike() {
        boolean contains = false;
        String dqwb = wj.dqwb(wj.gd + "mp3_like.json");
        if (dqwb != null) {
            try {
                Type type = new TypeToken<List<MP3>>() {
                }.getType();
                List<MP3> o = new Gson().fromJson(dqwb, type);
                if (o != null) {
                    contains = o.contains(bfqkz.xm);
                }
            } catch (Exception e) {
                wj.sc(wj.gd + "mp3_like.json");
            }
        }
        bfqkz.like_bool = contains;
        bfq.setlike(contains);
    }
}
