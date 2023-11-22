package com.muqingbfq.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;
import com.muqingbfq.R;
import com.muqingbfq.api.playlist;
import com.muqingbfq.api.resource;
import com.muqingbfq.bfq_an;
import com.muqingbfq.bfqkz;
import com.muqingbfq.list.list_gd;
import com.muqingbfq.main;
import com.muqingbfq.mq.wj;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.muqingbfq.xm;

public class gd extends Fragment {
    public static String gdid;
    public static BaseAdapter lbspq;
    public static List<xm> list;
    public static JSONObject like = new JSONObject();
    GridView gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gd, container, false);
        list = new ArrayList<>();
        lbspq = new baseadapter(view.getContext());
        gridView = view.findViewById(R.id.wgbj);
        gridView.setAdapter(lbspq);
        if (gdid == null) {
            gdid = main.mp3_csh;
        }
        TabLayout tabLayout = view.findViewById(R.id.tablayout);
        for (String name : new String[]{"推荐", "排行榜", "下载"}) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(name);
            tabLayout.addTab(tab);
        }
        new thread("推荐");
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                list.clear();
                lbspq.notifyDataSetChanged();
                new thread(tab.getText().toString());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        try {
            if (wj.cz(wj.mp3_like)) {
                like = new JSONObject(wj.dqwb(wj.mp3_like));
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return view;
    }

    class baseadapter extends BaseAdapter {
        Context context;
        LayoutInflater layoutInflater;

        public baseadapter(Context context) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @SuppressLint({"ResourceAsColor", "InflateParams", "ClickableViewAccessibility"})
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHoder viewHoder;
            xm xm = list.get(i);
            if (view == null) {
                viewHoder = new ViewHoder();
                view = layoutInflater.inflate(R.layout.list_gd, null, false);
                viewHoder.textView = view.findViewById(R.id.wb1);
                viewHoder.imageView = view.findViewById(R.id.fh);
                viewHoder.cardView = view.findViewById(R.id.cardview);
                viewHoder.kg = view.findViewById(R.id.kg);
                view.setTag(viewHoder);
            } else {
                viewHoder = (ViewHoder) view.getTag();
            }
            list_gd gd = new list_gd(xm);
            viewHoder.cardView.setOnClickListener(gd);
            viewHoder.cardView.setOnLongClickListener(gd);
            viewHoder.textView.setText(xm.name);
            viewHoder.kg.setOnClickListener(view1 -> {
                ImageView tx = (ImageView) view1;
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        boolean an=playlist.hq(bfqkz.list, xm.id);
                            main.handler.post(() -> {
                                if (an) {
                                    bfq_an.xyq();
                                    tx.setImageResource(R.drawable.bf);
                                    main.edit.putString(main.mp3, xm.id);
                                    main.edit.commit();
                                    main.mp3_csh = gdid = xm.id;
                                }
                                com.muqingbfq.fragment.gd.lbspq.notifyDataSetChanged();
                            });
                        }
                }.start();
            });
            int color = ContextCompat.getColor(context, R.color.text);
            Drawable color_kg = ContextCompat.getDrawable(context, R.drawable.zt);
            if (xm.id.equals(gdid)) {
                color = ContextCompat.getColor(context, R.color.text_cz);
                color_kg = ContextCompat.getDrawable(context, R.drawable.bf);
            } else if (xm.cz) {
                color = ContextCompat.getColor(context, R.color.text_cz_tm);
            }
            viewHoder.kg.setImageDrawable(color_kg);
            viewHoder.textView.setTextColor(color);
            Glide.with(context).load(xm.picurl).apply(new RequestOptions().placeholder(R.drawable.icon))
                    .into(viewHoder.imageView);
//            new wl(xm.picurl, Glide.with(context)).loadImage(bitmap -> viewHoder.imageView.setImageBitmap(bitmap));
            return view;
        }
    }

    class ViewHoder {
        TextView textView;
        ImageView imageView, kg;
        MaterialCardView cardView;
    }

    class thread extends Thread {
        String name;

        public thread(String name) {
            this.name = name;
            start();
        }

        @Override
        public void run() {
            super.run();
            switch (name) {
                case "推荐":
                    resource.recommend(list);
                    break;
                case "下载":
                    resource.下载(list);
                    break;
                case "排行榜":
                    resource.排行榜(list);
                    break;
            }
            main.handler.post(() -> lbspq.notifyDataSetChanged());
        }
    }

}