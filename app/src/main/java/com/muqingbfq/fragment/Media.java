package com.muqingbfq.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.muqingbfq.R;
import com.muqingbfq.bfq;
import com.muqingbfq.bfq_an;
import com.muqingbfq.bfqkz;
import com.muqingbfq.databinding.FragmentBfqBinding;
import com.muqingbfq.home;
import com.muqingbfq.main;
import com.muqingbfq.mq.gj;

import org.json.JSONException;
import org.json.JSONObject;

import me.wcy.lrcview.LrcView;

public class Media extends Fragment {
    @SuppressLint("StaticFieldLeak")
    public static LinearLayout view;
    private static bfq bfq;

    public void setBfq(bfq bfq) {
        Media.bfq = bfq;
    }

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
        if (bfq != null) {
            if (bool) {
                //开始
                bfq.kgsetImageResource(R.drawable.bf);
            } else {
                //暂停
                bfq.kgsetImageResource(R.drawable.zt);
            }
        }
        bfq_db.setkg(bool);
        bfqkz.updateNotification();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (view != null) {
            return view;
        }
        FragmentBfqBinding inflate = FragmentBfqBinding.inflate(inflater, container, false);
        view = inflate.getRoot();
        inflate.cardview.addView(home.imageView);
        tdt = inflate.tdt;
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

        time_a = inflate.timeA;
        time_b = inflate.timeB;
        //初始化歌词组件
        lrcview.setDraggable(true, (view, time) -> {
            bfqkz.mt.seekTo(Math.toIntExact(time));
            return false;
        });
        if (!isTablet(com.muqingbfq.bfq.context)) {
            lrcview.setOnTapListener((view, x, y) -> {
                View kp = inflate.kp1;
                if (kp.getVisibility() == View.VISIBLE) {
                    kp.setVisibility(View.GONE);
                } else {
                    kp.setVisibility(View.VISIBLE);
                }
            });
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT,2);
            inflate.linearlayout.addView(lrcview,layoutParams);
        }else{
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(0,
                            LinearLayout.LayoutParams.MATCH_PARENT,1);
            inflate.linearlayout.addView(lrcview,layoutParams);
        }
        //初始化播放器列表
        if (bfqkz.xm != null) {
            main.handler.removeCallbacks(bfqkz.mt.updateSeekBar); // 在播放开始时启动更新进度
            long duration = bfqkz.mt.getDuration();
            tdt.setMax((int) bfqkz.mt.getDuration());
            setTime_a(bfq_an.getTime(duration));
            long position = bfqkz.mt.getCurrentPosition();
            setProgress((int) position);
            main.handler.post(bfqkz.mt.updateSeekBar); // 在播放开始时启动更新进度
        }
        return view;
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

    public static void setlike(boolean bool) {
        if (bfq == null || bfq.inflate == null) {
            return;
        }
        int color = R.color.text;
        if (bool) {
            color = android.R.color.holo_red_dark;
        }
        gj.sc(color);
        bfq.inflate.like.setImageTintList(ContextCompat.
                getColorStateList(com.muqingbfq.bfq.context, color));
    }

    public static void setImageBitmap() {
        if (home.imageView == null) {
            return;
        }
        main.handler.post(() -> home.imageView.setImageBitmap(com.muqingbfq.bfq.bitmap));
    }

    public static void setname(String str) {
        if (com.muqingbfq.bfq.context == null) {
            return;
        }
        TextView name = com.muqingbfq.bfq.context.findViewById(R.id.name);
        name.setText(str);
    }

    public static void setzz(String str) {
        if (com.muqingbfq.bfq.context == null) {
            return;
        }
        TextView zz = com.muqingbfq.bfq.context.findViewById(R.id.zz);
        zz.setText(str);
    }

    public static Context Context() {
        if (view == null) {
            return null;
        }
        return view.getContext();
    }
}
