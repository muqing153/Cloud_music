package com.muqingbfq.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.muqingbfq.R;
import com.muqingbfq.api.url;
import com.muqingbfq.bfq;
import com.muqingbfq.bfqkz;
import com.muqingbfq.databinding.FragmentSearchBinding;
import com.muqingbfq.list.MyViewHoder;
import com.muqingbfq.main;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;
import com.muqingbfq.mq.wl;
import com.muqingbfq.xm;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class search extends Fragment {
    public static RecyclerView.Adapter<MyViewHoder> lbspq;
    List<xm> list = new ArrayList<>();
    gd.baseadapter adapter_gd;
    public String name;

    public FragmentSearchBinding inflate;
    public int i = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        inflate = FragmentSearchBinding.inflate(inflater, container, false);
        lbspq = new spq();
        View view = inflate.getRoot();
        TypedValue typedValue = new TypedValue();
        requireContext().getTheme().resolveAttribute(android.R.attr.windowBackground, typedValue, true);
        // 设置背景颜色
        adapter_gd = new gd.baseadapter(getContext(), list);
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
            new spq();
            inflate.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
            inflate.recyclerview.setAdapter(lbspq);
        } else if (i == 1) {
            k = (int) (main.k / getResources().getDisplayMetrics().density + 0.5f) / 120;
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),
                    k);
            inflate.recyclerview.setLayoutManager(gridLayoutManager);
            inflate.recyclerview.setAdapter(adapter_gd);
        }
        new start(name);
    }

    public class start extends Thread {
        public start(String name) {
            list.clear();
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
            main.handler.post(() -> {
                if (i == 0) {
                    lbspq.notifyDataSetChanged();
                } else if (i == 1) {
                    adapter_gd.notifyDataSetChanged();
                }
//                lbspq.notifyDataSetChanged();
            });
        }
    }

    private void mp3() {
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
                list.add(new xm(id, name, zz.toString(), ""));
            }
        } catch (Exception e) {
            gj.sc(e);
        }
    }

    private void gd() {
        String hq = wl.hq("/search?keywords=" + name + "&limit=" + (k * 3) + "&type=1000");
        try {
            JSONArray jsonArray = new JSONObject(hq).getJSONObject("result")
                    .getJSONArray("playlists");
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String coverImgUrl = jsonObject.getString("coverImgUrl");
//                gj.sc(name);
                list.add(new xm(id, name, coverImgUrl, wj.cz(wj.gd + id)));
            }

        } catch (Exception e) {
            gj.sc(e);
        }
    }

    class spq extends RecyclerView.Adapter<MyViewHoder> {
        public spq() {
            lbspq = this;
        }

        @NonNull
        @Override
        public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.
                    from(parent.getContext()).inflate(R.layout.list_mp3, parent, false);
            return new MyViewHoder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {
            xm x = list.get(position);
            holder.name.setText(x.name);
            holder.zz.setText(x.zz);
            int color = ContextCompat.getColor(holder.getContext(), R.color.text);
            if (bfqkz.xm != null && x.id.equals(bfqkz.xm.id)) {
                color = ContextCompat.getColor(holder.getContext(), R.color.text_cz);
            }
            holder.name.setTextColor(color);
            holder.zz.setTextColor(color);
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
    }
}
