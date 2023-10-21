package com.muqingbfq;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

public class bfq extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static SeekBar tdt;
    @SuppressLint("StaticFieldLeak")
    public static TextView name, zz, time_a, time_b;
    @SuppressLint("StaticFieldLeak")
    public static ImageView tx;
    @SuppressLint("StaticFieldLeak")
    public static ImageView kg, syq, xyq, like;
    public static com.dirror.lyricviewx.LyricViewX lrcView;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frameLayout = new FrameLayout(this);
        // 设置 FrameLayout 的布局参数（可以根据自己的需要进行设置）
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,   // 宽度为 Match Parent
                FrameLayout.LayoutParams.MATCH_PARENT);  // 高度为 Match Parent
        frameLayout.setLayoutParams(params);
        frameLayout.setId(1);
        setContentView(frameLayout);
        getSupportFragmentManager().beginTransaction()
                .add(frameLayout.getId(), new fragment(this))
                .commit();
/*        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.windowBackground, typedValue, true);
        // 设置背景颜色
        bj = typedValue.data;*/
    }
    @SuppressLint("StaticFieldLeak")
    public static View inflate;
    private static AppCompatActivity context;
    public static Bitmap bitmap;
    public static class fragment extends Fragment {

        public fragment(AppCompatActivity context) {
            bfq.context = context;
        }
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            if (inflate != null) {
                tx.setImageBitmap(bitmap);
                return inflate;
            }
            inflate = inflater.inflate(R.layout.fragment_bfq, container, false);
            lrcView = inflate.findViewById(R.id.gc);
            Toolbar toolbar = inflate.findViewById(R.id.toolbar);
            name = inflate.findViewById(R.id.name);
            zz = inflate.findViewById(R.id.zz);
            kg = inflate.findViewById(R.id.kg);
            xyq = inflate.findViewById(R.id.xyq);
            syq = inflate.findViewById(R.id.syq);
            tx = inflate.findViewById(R.id.mttx);
            tdt = inflate.findViewById(R.id.tdt);
            time_a = inflate.findViewById(R.id.time_a);
            time_b = inflate.findViewById(R.id.time_b);

//        lrcView.setIsEnableBlurEffect(true);
            View kp = inflate.findViewById(R.id.kp1);
            kp.setOnClickListener(v -> {
                if (lrcView != null) {
                    v.setVisibility(View.GONE);
                    lrcView.setVisibility(View.VISIBLE);
                    if (com.muqingbfq.api.url.lrc == null) {
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                com.muqingbfq.api.url.gc(bfqkz.xm.id);
                            }
                        }.start();
                    }
                }
            });
            toolbar.setNavigationIcon(R.drawable.end);
            toolbar.setNavigationOnClickListener(view1 -> context.finish());
            toolbar.inflateMenu(R.menu.bfq);
            toolbar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.fx) {
                    com.muqingbfq.mq.gj.fx(context,
                            "音乐名称：" + name.getText().toString() +
                                    "\n 作者：" + zz.getText().toString() +
                                    "\n 链接：https://music.163.com/#/song?id=" + bfqkz.id);
                }
                return false;
            });
            lrcView.setDraggable(true, (time) -> {
                com.muqingbfq.bfqkz.mt.seekTo(Math.toIntExact(time));
                return true;
            });
            lrcView.setOnSingerClickListener(() -> {
                lrcView.setVisibility(View.GONE);
                kp.setVisibility(View.VISIBLE);
            });
            inflate.findViewById(R.id.layout).setOnClickListener(view1 -> {
                lrcView.setVisibility(View.GONE);
                kp.setVisibility(View.VISIBLE);
            });
            inflate.findViewById(R.id.bfq_list_mp3).
                    setOnClickListener(view1 -> com.muqingbfq.fragment.bflb_db.start(context));

            bfq_an.kz kz = new bfq_an.kz();
            kg.setOnClickListener(kz);
            syq.setOnClickListener(kz);
            xyq.setOnClickListener(kz);
            like = inflate.findViewById(R.id.like);
            ImageView control = inflate.findViewById(R.id.control);
            control.setOnClickListener(new bfq_an.control(control));
            UI(inflate);
            return inflate;
        }
        private void UI(View view) {
//        tdt.getProgressDrawable().
//                setColorFilter(ContextCompat.getColor(this, R.color.text_tm), PorterDuff.Mode.MULTIPLY);
//        tdt.getThumb().
//                setColorFilter(ContextCompat.getColor(this, R.color.text), PorterDuff.Mode.SRC_IN);
            tdt.setOnSeekBarChangeListener(new bfq_an.tdt());

            like.setOnClickListener(view1 -> {
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
            if (bfqkz.xm != null) {
                xm xm = bfqkz.xm;
                name.setText(xm.name);
                zz.setText(xm.zz);
                time_a.setText(bfq_an.getTime(bfqkz.tdt_max));
                tdt.setMax((int) bfqkz.tdt_max);
                if (bfqkz.mt.isPlaying()) {
                    kg.setImageResource(R.drawable.bf);
                }
                bfq_an.islike(context);
                Glide.with(context).load(xm.picurl)
                        .placeholder(R.drawable.icon)
                        .into(tx);
            }
        }
    }

    public static void start(AppCompatActivity context) {
        Intent intent = new Intent(context, bfq.class);
        context.startActivity(intent);
//        home.dialog.show(context.getSupportFragmentManager(), "bfq");
    }

    public static boolean getVisibility() {
        if (inflate == null) {
            return false;
        }
        return inflate.isShown();
    }
}