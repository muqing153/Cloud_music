package com.muqingbfq.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.muqingbfq.MP3;
import com.muqingbfq.XM;
import com.muqingbfq.databinding.FragmentSearchBinding;
import com.muqingbfq.list.MyViewHoder;
import com.muqingbfq.main;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class search extends Fragment {
    public static RecyclerView.Adapter<MyViewHoder> lbspq;
    List<MP3> list = new ArrayList<>();
    List<XM> xmList = new ArrayList<>();
    public String name;

    public FragmentSearchBinding inflate;
    public int i = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        inflate = FragmentSearchBinding.inflate(inflater, container, false);
        lbspq = new mp3.adaper(list);
        View view = inflate.getRoot();
        TypedValue typedValue = new TypedValue();
        requireContext().getTheme().resolveAttribute(android.R.attr.windowBackground, typedValue, true);
        // 设置背景颜色
        view.setBackgroundColor(typedValue.data);
        inflate.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                当用户再次选择已选择的选项卡时调用。
                search.this.i = tab.getPosition();
                setStart(name);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                当选项卡退出选定状态时调用。
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//                当选项卡进入选定状态时调用。


            }
        });
        setVisibility(false);
        return view;
    }

    public void setVisibility(boolean bool) {
        if (bool) {
            inflate.getRoot().setVisibility(View.VISIBLE);
        } else {
            inflate.getRoot().setVisibility(View.GONE);
        }
    }

    public int k;

    public void setStart(String name) {
        if (i == 0) {
            new mp3.adaper(list);
            inflate.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
            inflate.recyclerview.setAdapter(lbspq);
        } else if (i == 1) {
            k = (int) (main.k / getResources().getDisplayMetrics().density + 0.5f) / 120;
            inflate.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
            inflate.recyclerview.setAdapter(new gd.baseadapter(getContext(),
                    xmList, true, inflate.recyclerview));

        }
        new start(name);
    }

    public class start extends Thread {
        public start(String name) {
            list.clear();
            xmList.clear();
            search.this.name = name;
            start();
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void run() {
            super.run();
            if (i == 0) {
                mp3();
            } else if (i == 1) {
                gd();
            }
            main.handler.post(() -> inflate.recyclerview.getAdapter().notifyDataSetChanged());
        }
    }

    private void mp3() {
        try {
            Long.parseLong(name);
            com.muqingbfq.api.playlist.hq(list, name);
            return;
        } catch (NumberFormatException e) {
            gj.sc(e);
        }
        String hq = wl.hq("/search?keywords=" + name + "&type=1");
        try {
            JSONArray jsonArray = new JSONObject(hq).getJSONObject("result")
                    .getJSONArray("songs");
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                JSONArray artists = jsonObject.getJSONArray("artists");
                int length1 = artists.length();
                StringBuilder zz = null;
                for (int j = 0; j < length1; j++) {
                    JSONObject josn = artists.getJSONObject(j);
                    String name_zz = josn.getString("name");
                    if (zz == null) {
                        zz = new StringBuilder(name_zz);
                    } else {
                        zz.append("/").append(name_zz);
                    }
                }
                list.add(new MP3(id, name, zz.toString(), ""));
            }
        } catch (Exception e) {
            gj.sc(e);
        }
    }

    private void gd() {
        try {
            Long.parseLong(name);
            String hq = wl.hq("/playlist/detail?id=" + name);
            JSONObject js = new JSONObject(hq).getJSONObject("playlist");
                String id = js.getString("id");
                String name = js.getString("name");
                String coverImgUrl = js.getString("coverImgUrl");
//                gj.sc(name);
                xmList.add(new XM(id, name, coverImgUrl));
            return;
        } catch (Exception e) {
            gj.sc(e);
        }
        try {
            String hq = wl.hq("/search?keywords=" + name +"&type=1000");
            JSONArray jsonArray = new JSONObject(hq).getJSONObject("result")
                    .getJSONArray("playlists");
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                int trackCount = jsonObject.getInt("trackCount");

                String nickname = "by " + jsonObject.getJSONObject("creator")
                        .getString("nickname");
                long playCount = jsonObject.getLong("playCount");
                String name = jsonObject.getString("name");
                String coverImgUrl = jsonObject.getString("coverImgUrl");
//                gj.sc(name);
                String formattedNumber = String.valueOf(playCount);
                if (playCount > 9999) {
                    DecimalFormat df = new DecimalFormat("#,###.0万");
                    formattedNumber = df.format(playCount / 10000);
                }
                xmList.add(new XM(id, name, trackCount + "首，" + nickname + "，播放"
                        + formattedNumber + "次", coverImgUrl));
            }
        } catch (Exception e) {
            gj.sc(e);
        }
    }
/*
    class spq extends RecyclerView.Adapter<MyViewHoder> {
        public spq() {
            lbspq = this;
        }

        @NonNull
        @Override
        public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return new MyViewHoder(ListMp3Binding.
                    inflate(getLayoutInflater(),parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {
            MP3 x = list.get(position);
            holder.binding.name.setText(x.name);
            holder.binding.zz.setText(x.zz);
            int color = ContextCompat.getColor(holder.getContext(), R.color.text);
            if (bfqkz.xm != null && x.id.equals(bfqkz.xm.id)) {
                color = ContextCompat.getColor(holder.getContext(), R.color.text_cz);
            }
            holder.binding.name.setTextColor(color);
            holder.binding.zz.setTextColor(color);
            holder.itemView.setOnClickListener(view1 -> {
                if (bfqkz.xm == null || !bfqkz.xm.id.equals(x.id)) {
                    bfqkz.xm = x;
                    new url(x);
                }
//                if (!com.muqingbfq.fragment.gd.gdid.equals(bflb_db.gdid)) {
                gd.gdid = null;
                bfqkz.list.clear();
                bfqkz.list.addAll(list);
                bfq.start(search.this.getContext());
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }*/
}
