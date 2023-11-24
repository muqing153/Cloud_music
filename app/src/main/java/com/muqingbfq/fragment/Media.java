package com.muqingbfq.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.dirror.lyricviewx.LyricViewX;
import com.muqingbfq.R;
import com.muqingbfq.api.url;
import com.muqingbfq.bfq;
import com.muqingbfq.bfq_an;
import com.muqingbfq.bfqkz;
import com.muqingbfq.main;
import com.muqingbfq.xm;

import org.json.JSONObject;

public class Media extends Fragment {
    @SuppressLint("StaticFieldLeak")
    public static View view;
    @SuppressLint("StaticFieldLeak")
    private static TextView time_a, time_b;
    @SuppressLint("StaticFieldLeak")
    private static SeekBar tdt;
    private static LyricViewX lrcview;

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
        lrcview.updateTime(progress, true);
    }

    public static void setbf(boolean bool) {
        if (bool) {
            //开始
            kgsetImageResource(R.drawable.bf);
        } else {
            //暂停
            kgsetImageResource(R.drawable.zt);
        }
        bfq_db.setkg(bool);
        bfqkz.updateNotification();
    }

    private static void kgsetImageResource(int a) {
        if (view == null) {
            return;
        }
        ImageView imageView = view.findViewById(R.id.kg);
        imageView.setImageResource(a);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (view != null) {
//            main.handler.post(() -> setImageBitmap(bfq.bitmap));
            return view;
        }
        view = inflater.inflate(R.layout.fragment_bfq, container, false);
        bfq_an.kz kz = new bfq_an.kz();
        ImageView kg = view.findViewById(R.id.kg);
        kg.setOnClickListener(kz);
        view.findViewById(R.id.xyq).setOnClickListener(kz);
        view.findViewById(R.id.syq).setOnClickListener(kz);
        ImageView tx = view.findViewById(R.id.mttx);
        tdt = view.findViewById(R.id.tdt);
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

        time_a = view.findViewById(R.id.time_a);
        time_b = view.findViewById(R.id.time_b);
        //初始化歌词组件
        View kp = view.findViewById(R.id.kp1);
        lrcview = getlrcView();
        lrcview.setDraggable(true, (time) -> {
            com.muqingbfq.bfqkz.mt.build.seekTo(Math.toIntExact(time));
            return true;
        });
        if (!isTablet(bfq.context)) {
            lrcview.setOnSingerClickListener(() -> {
                if (kp.getVisibility() == View.VISIBLE) {
                    kp.setVisibility(View.GONE);
                } else {
                    kp.setVisibility(View.VISIBLE);
                }
            });
        }
        //初始化播放器列表
        view.findViewById(R.id.bfq_list_mp3).
                setOnClickListener(view1 -> com.muqingbfq.fragment.bflb_db.start(bfq.context));

        view.findViewById(R.id.like).setOnClickListener(view1 -> {
            ImageView like = (ImageView) view1;
            try {
                if (bfqkz.like_bool) {
                    like.setImageTintList(ContextCompat.getColorStateList(view.getContext(), R.color.text));
                    com.muqingbfq.fragment.gd.like.remove(String.valueOf(bfqkz.xm.id));
                } else {
                    like.setImageTintList(ContextCompat.
                            getColorStateList(view.getContext(), android.R.color.holo_red_dark));
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
        ImageView control = view.findViewById(R.id.control);
        control.setOnClickListener(new bfq_an.control(control));
        if (bfqkz.xm != null) {
            setname(bfqkz.xm.name);
            setzz(bfqkz.xm.zz);
            main.handler.removeCallbacks(bfqkz.mt.updateSeekBar); // 在播放开始时启动更新进度
            long duration = bfqkz.mt.build.getDuration();
            tdt.setMax((int) bfqkz.mt.build.getDuration());
            setTime_a(bfq_an.getTime(duration));

            long position = bfqkz.mt.build.getCurrentPosition();
            setProgress((int) position);

            main.handler.post(bfqkz.mt.updateSeekBar); // 在播放开始时启动更新进度
            if (bfqkz.mt.build.isPlaying()) {
                kg.setImageResource(R.drawable.bf);
            }
            Glide.with(getContext())
                    .load(bfqkz.xm.picurl)
                    .error(R.drawable.icon)//图片加载失败后，显示的图片
                    .into(tx);
            bfq_an.islike(bfq.context);
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    url.gc(bfqkz.xm.id);
                }
            }.start();
        }
        return view;
    }

    private boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static LyricViewX getlrcView() {
        if (view == null) {
            return null;
        }
        return view.findViewById(R.id.gc);
    }

    public static void loadLyric(String a, String b) {
        lrcview.loadLyric(a, b);
    }

    public static void setlike(boolean bool) {
        ImageView imageView = view.findViewById(R.id.like);
        int color = R.color.text;
        if (bool) {
            color = android.R.color.holo_red_dark;
        }
        imageView.setImageTintList(ContextCompat.
                getColorStateList(view.getContext(), color));
    }

    public static void setImageBitmap(Bitmap bitmap) {
        if (view == null) {
            return;
        }
        ImageView imageView = view.findViewById(R.id.mttx);
        if (imageView != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    public static void setname(String str) {
        if (view == null) {
            return;
        }
        TextView name = view.findViewById(R.id.name);
        name.setText(str);
    }

    public static void setzz(String str) {
        if (view == null) {
            return;
        }
        TextView zz = view.findViewById(R.id.zz);
        zz.setText(str);
    }

    @Nullable
    @Override
    public Context getContext() {
        return view.getContext();
    }
}
