package com.muqingbfq.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.muqingbfq.R;
import com.muqingbfq.XM;
import com.muqingbfq.api.playlist;
import com.muqingbfq.api.resource;
import com.muqingbfq.bfq_an;
import com.muqingbfq.bfqkz;
import com.muqingbfq.databinding.FragmentMp3Binding;
import com.muqingbfq.main;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;
import com.muqingbfq.mq.wl;
import com.muqingbfq.view.CardImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class gd extends com.muqingbfq.mq.FragmentActivity {
    public static String gdid;
    private final List<XM> list = new ArrayList<>();
    public static RecyclerView.Adapter<VH> adapter;
    int k;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentMp3Binding binding = FragmentMp3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        binding.title.setText(intent.getStringExtra("name"));
        adapter = new baseadapter(this,list);
        k = (int) (main.k / getResources().getDisplayMetrics().density + 0.5f);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, k / 120);
        binding.lb.setLayoutManager(gridLayoutManager);
        binding.lb.setAdapter(adapter);
        String id = intent.getStringExtra("id");
        new start(id);
    }

    @SuppressLint("NotifyDataSetChanged")
    class start extends Thread {
        String id;
        public start(String id) {
            this.id = id;
            list.clear();
            adapter.notifyDataSetChanged();
            start();
        }

        @Override
        public void run() {
            super.run();
            if (id.equals("排行榜")) {
                resource.排行榜(list);
            } else {
                String hq = wl.hq("/search?keywords=" + id + "&limit=" + (k * 3) + "&type=1000");
                try {
                    JSONArray jsonArray = new JSONObject(hq).getJSONObject("result")
                            .getJSONArray("playlists");
                    int length = jsonArray.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id = jsonObject.getString("id");
                        String name = jsonObject.getString("name");
                        String coverImgUrl = jsonObject.getString("coverImgUrl");
                        list.add(new XM(id, name, coverImgUrl));
                    }
                } catch (Exception e) {
                    gj.sc(e);
                }
            }
            main.handler.post(new lbspq_sx());
        }
    }

    public static class baseadapter extends RecyclerView.Adapter<VH>{
        Context context;
        List<XM> list;

        public baseadapter(Context context, List<XM> list) {
            this.context = context;
            this.list = list;
        }

        boolean bool = false;
        public baseadapter(Context context, List<XM> list, boolean bool) {
            this.context = context;
            this.list = list;
            this.bool = bool;
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (bool) {
                return new VH(LayoutInflater.from(context)
                        .inflate(R.layout.list_gd_b, parent, false));
            }
            return new VH(LayoutInflater.from(context)
                    .inflate(R.layout.list_gd, parent, false));
        }

        public void setList(List<XM> list) {
            this.list = list;
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            XM xm = list.get(position);
            CARD card = new CARD(position);
            if (bool) {
                holder.itemView.setOnClickListener(card);
                holder.itemView.setOnLongClickListener(card);
            } else {
                holder.image.setOnClickListener(card);
                holder.image.setOnLongClickListener(card);
            }
            holder.textView.setText(xm.name);
            holder.kg.setOnClickListener(view1 -> {
                ImageView tx = (ImageView) view1;
                new Thread() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        super.run();
                        boolean an = playlist.hq(bfqkz.list, xm.id);
                        if (bfqkz.ms == 2) {
                            Collections.shuffle(bfqkz.list);
                        }
                        main.handler.post(() -> {
                            if (an) {
                                bfq_an.xyq();
                                tx.setImageResource(R.drawable.bf);
                                gdid = xm.id;
                            }
                            notifyDataSetChanged();
                        });
                    }
                }.start();
            });
            Drawable color_kg = ContextCompat.getDrawable(context, R.drawable.zt);
            if (xm.id.equals(gdid)) {
                color_kg = ContextCompat.getDrawable(context, R.drawable.bf);
            }
            holder.kg.setImageDrawable(color_kg);
//            xm.picurl
            holder.image.setImageapply(xm.picurl);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class CARD implements View.OnClickListener
                , View.OnLongClickListener {
            int position;

            public CARD(int position) {
                this.position = position;
            }

            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                XM xm = list.get(position);
                Intent intent = new Intent(context, mp3.class);
                intent.putExtra("id", xm.id);
                intent.putExtra("name", xm.name);
                context.startActivity(intent);
            }

            @Override
            public boolean onLongClick(View view) {
                XM xm = list.get(position);
                String[] stringArray = view.getResources()
                        .getStringArray(R.array.gd_list);
                if (!wj.cz(wj.gd + xm.id)) {
                    stringArray = new String[]{"下载歌单"};
                }
                new MaterialAlertDialogBuilder(view.getContext()).
                        setItems(stringArray, (dialog, id) -> {
                            new Thread() {
                                @Override
                                public void run() {
                                    if (id == 0) {
                                        String hq = wl.hq(playlist.api + xm.id + "&limit=100");
                                        if (hq != null) {
                                            wj.xrwb(wj.gd + xm.id, hq);
                                            try {
                                                JSONObject jsonObject = new JSONObject();
                                                if (wj.cz(wj.gd_xz)) {
                                                    jsonObject = new JSONObject(Objects.requireNonNull(wj.dqwb(wj.gd_xz)));
                                                }
                                                XM fh = resource.Playlist_content(xm.id);
                                                JSONObject json = new JSONObject();
                                                json.put("name", fh.name);
                                                json.put("picUrl", fh.picurl);
                                                jsonObject.put(fh.id, json);
                                                wj.xrwb(wj.gd_xz, jsonObject.toString());
                                                main.handler.post(() -> notifyItemChanged(position));
                                            } catch (JSONException e) {
                                                gj.sc("list gd onclick thear " + e);
                                            }
                                        }

                                    } else if (id == 2) {
                                        wj.sc(wj.gd + xm.id);
                                        try {
                                            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(wj.dqwb(wj.gd_xz)));
                                            jsonObject.remove(xm.id);
                                            list.remove(xm);
                                            wj.xrwb(wj.gd_xz, jsonObject.toString());
                                        } catch (JSONException e) {
                                            gj.sc(e);
                                        }
                                    }
                                    main.handler.post(() -> notifyItemChanged(position));
                                }
                            }.start();
                            // 在这里处理菜单项的点击事件
                            dialog.dismiss();
                        }).show();
                return false;
            }

        }

    }

    static class VH extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView kg;
        CardImage image;

        public VH(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.wb1);
            image = itemView.findViewById(R.id.image);
            kg = itemView.findViewById(R.id.kg);
        }
    }
    private class lbspq_sx implements Runnable {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void run() {
            adapter.notifyDataSetChanged();
        }
    }
}