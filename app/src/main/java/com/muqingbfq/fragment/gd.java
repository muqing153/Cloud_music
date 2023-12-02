package com.muqingbfq.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.muqingbfq.xm;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class gd extends Fragment {
    public static String gdid;
    public static RecyclerView.Adapter<VH> lbspq;
    public List<xm> list = new ArrayList<>();
    public static JSONObject like = new JSONObject();
    RecyclerView gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gd, container, false);
        lbspq = new baseadapter(view.getContext(),list);
        gridView = view.findViewById(R.id.wgbj);
        int k = (int) (main.k / getResources().getDisplayMetrics().density + 0.5f);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), k / 120);
        gridView.setLayoutManager(gridLayoutManager);
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

    public static class baseadapter extends RecyclerView.Adapter<VH> {
        Context context;
        List<xm> list;
        public baseadapter(Context context, List<xm> list) {
            this.context = context;
            this.list = list;
        }
        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.list_gd, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            xm xm = list.get(position);
            list_gd gd = new list_gd(xm);
            holder.cardView.setOnClickListener(gd);
            holder.cardView.setOnLongClickListener(gd);
            holder.textView.setText(xm.name);
            holder.kg.setOnClickListener(view1 -> {
                ImageView tx = (ImageView) view1;
                new Thread() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        super.run();
                        boolean an = playlist.hq(bfqkz.list, xm.id);
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
            holder.kg.setImageDrawable(color_kg);
            holder.textView.setTextColor(color);
            Glide.with(context).load(xm.picurl).apply(new RequestOptions().placeholder(R.drawable.icon))
                    .into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

    }

    static class VH extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView, kg;
        MaterialCardView cardView;

        public VH(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.wb1);
            imageView = itemView.findViewById(R.id.fh);
            cardView = itemView.findViewById(R.id.cardview);
            kg = itemView.findViewById(R.id.kg);
        }
    }

    class thread extends Thread {
        String name;

        public thread(String name) {
            this.name = name;
            list.clear();
            start();
        }

        @SuppressLint("NotifyDataSetChanged")
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