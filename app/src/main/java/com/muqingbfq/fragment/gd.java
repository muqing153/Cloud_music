package com.muqingbfq.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.muqingbfq.databinding.ListGdBBinding;
import com.muqingbfq.databinding.ListGdBinding;
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
        adapter = new baseadapter(this, list);
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
                resource.leaderboard(list);
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

    public static class baseadapter extends RecyclerView.Adapter<VH> {
        Context context;
        public List<XM> list;

        public baseadapter(Context context, List<XM> list) {
            this.context = context;
            this.list = list;
        }

        boolean bool = false;

        public baseadapter(Context context, List<XM> list, boolean bool, RecyclerView recyclerView) {
            this.context = context;
            this.list = list;
            this.bool = bool;
            setrecycle(recyclerView);
        }

        public void setrecycle(RecyclerView recyclerView) {

            recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                final GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public void onLongPress(@NonNull MotionEvent motionEvent) {
                        View childView = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
                        if (childView != null) {
                            int position = recyclerView.getChildAdapterPosition(childView);
                            setonlong(position);
                            // 处理长按事件，使用正确的位置position
                            // ...

                        }
                    }
                });

                @Override
                public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                    return gestureDetector.onTouchEvent(e);
                }

                @Override
                public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                }

            });
        }

        @SuppressLint("NotifyDataSetChanged")
        public void setonlong(int position) {
            XM xm = list.get(position);
            gj.sc(xm.name);
            String[] stringArray = context.getResources()
                    .getStringArray(R.array.gd_list);
            if (!wj.cz(wj.gd + xm.id)) {
                stringArray = new String[]{"下载歌单"};
            }
            String[] finalStringArray = stringArray;
            new MaterialAlertDialogBuilder(context).
                    setItems(stringArray, (dialog, id) -> {
                        switch (finalStringArray[id]) {
                            case "下载歌单":
                                new Thread() {
                                    @SuppressLint("NotifyDataSetChanged")
                                    @Override
                                    public void run() {
                                        super.run();
                                        String hq = playlist.gethq(xm.id);
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
                                                main.handler.post(() -> {
                                                    notifyItemChanged(position);
                                                    wode.addlist(xm);
                                                });
                                            } catch (JSONException e) {
                                                gj.sc("list gd onclick thear " + e);
                                            }
                                        }
                                    }
                                }.start();
                                break;
                            case "删除歌单":
//                                        删除项目
                                try {
                                    wj.sc(wj.gd + xm.id);
                                    JSONObject jsonObject = new JSONObject(Objects.requireNonNull(wj.dqwb(wj.gd_xz)));
                                    jsonObject.remove(xm.id);
                                    list.remove(xm);
                                    wj.xrwb(wj.gd_xz, jsonObject.toString());
                                    wode.removelist(xm);
                                } catch (JSONException e) {
                                    gj.sc(e);
                                }
                                break;
                        }
                        // 在这里处理菜单项的点击事件
                    }).show();
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (bool) {
                return new VH(ListGdBBinding.bind(LayoutInflater.from(context)
                        .inflate(R.layout.list_gd_b, parent, false)));
            }
            return new VH(ListGdBinding.bind(LayoutInflater.from(context)
                    .inflate(R.layout.list_gd, parent, false)));
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
                holder.bindingB.text2.setText(xm.message);
            } else {
                holder.binding.image.setOnClickListener(card);
                holder.binding.image.setOnLongClickListener(v -> {
                    setonlong(position);
                    return false;
                });
            }
            holder.title.setText(xm.name);
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

        class CARD implements View.OnClickListener {
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
        }

    }

    static class VH extends RecyclerView.ViewHolder {
        public ListGdBinding binding;
        public ImageView kg;
        public CardImage image;
        public TextView title;

        public VH(@NonNull ListGdBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            title = binding.wb1;
            kg = binding.kg;
            image = binding.image;
        }

        ListGdBBinding bindingB;

        public VH(@NonNull ListGdBBinding itemView) {
            super(itemView.getRoot());
            bindingB = itemView;
            title = bindingB.text1;
            kg = bindingB.kg;
            image = bindingB.image;
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