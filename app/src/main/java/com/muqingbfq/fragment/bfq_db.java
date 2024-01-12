package com.muqingbfq.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.reflect.TypeToken;
import com.muqingbfq.MP3;
import com.muqingbfq.R;
import com.muqingbfq.api.url;
import com.muqingbfq.bfq;
import com.muqingbfq.bfq_an;
import com.muqingbfq.bfqkz;
import com.muqingbfq.main;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;

import java.lang.reflect.Type;
import java.util.List;

public class bfq_db extends Fragment implements GestureDetector.OnGestureListener {
    @SuppressLint("StaticFieldLeak")
    public static View view;

    private GestureDetector gestureDetector;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            String jsonList = this.getContext().getSharedPreferences("list", Context.MODE_PRIVATE)
                    .getString("listData", null); // 获取保存的 JSON 字符串
            if (jsonList != null) {
                Type type = new TypeToken<List<MP3>>() {
                }.getType();
                bfqkz.list = new com.google.gson.Gson().fromJson(jsonList, type);
                // 将 JSON 字符串转换回列表数据
            }

            bfqkz.xm = wj.getMP3FromFile();
            if (bfqkz.xm != null) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        String hq = url.hq(bfqkz.xm);
                        if (hq == null) {
                            return;
                        }
                        try {
                            bfqkz.mt.DataSource(hq);
                        } catch (Exception e) {
                            gj.sc(e);
                        }
                    }
                }.start();
            }
        }
        view = inflater.inflate(R.layout.fragment_bfq_db, container, false);

        TextView name = view.findViewById(R.id.name);
        view.findViewById(R.id.kg).setOnClickListener(new bfq_an.kz());
        view.findViewById(R.id.txb).setOnClickListener(view -> bflb_db.start(getContext()));
//        view.setOnClickListener(vw -> bfq.start(getContext()));
        gestureDetector = new GestureDetector(getContext(), this);

        view.setOnTouchListener((view, motionEvent) -> {
/*            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.bj));
                return true;
            }*/
            return gestureDetector.onTouchEvent(motionEvent);

        });

// 恢复列表数据
        if (bfqkz.xm != null) {
            name.setText(bfqkz.xm.name + "/" + bfqkz.xm.zz);
        }
        if (bfqkz.mt != null) {
            Media.setbf(bfqkz.mt.isPlaying());
        }
        return view;
    }

    private static <T extends View> T findViewById(int id) {
        return view.findViewById(id);
    }

    public static void setkg(boolean bool) {
        if (view != null) {
            ImageView imageView = findViewById(R.id.kg);
            if (bool) {
                imageView.setImageResource(R.drawable.bf);
            } else {
                imageView.setImageResource(R.drawable.zt);
            }
        }
    }

    public static void setname(String str) {
        if (view != null) {
            TextView textView = findViewById(R.id.name);
            textView.setText(str);
        }
    }

    @Override
    public boolean onDown(@NonNull MotionEvent motionEvent) {
        view.setBackgroundColor(Color.parseColor("#80F2ECF6"));
        return true;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent motionEvent) {
    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
        bfq.start(getContext());
        view.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.bj));
//        view.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.bj));
        return true;
    }

    @Override
    public boolean onScroll(@Nullable MotionEvent motionEvent, @NonNull MotionEvent motionEvent1,
                            float v, float v1) {
        view.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.bj));
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(@Nullable MotionEvent e1,
                           @NonNull MotionEvent e2, float v, float v1) {
        view.setBackgroundColor(ContextCompat.getColor(getContext(),R.color.bj));
        float distance = e1.getX() - e2.getX();
        float threshold = main.k / 2.0f;
        // 判断手势方向并限制滑动距离
        if (distance > threshold) {
            // 向左滑动
            // 在这里添加你的逻辑代码
            bfq_an.xyq();
        } else if (distance < -threshold) {
            // 向右滑动
            // 在这里添加你的逻辑代码
            bfq_an.syq();
        }
        return true;
    }
}