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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muqingbfq.R;
import com.muqingbfq.activity_search;
import com.muqingbfq.api.url;
import com.muqingbfq.bfq;
import com.muqingbfq.bfqkz;
import com.muqingbfq.list.MyViewHoder;
import com.muqingbfq.main;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wl;
import com.muqingbfq.xm;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class search extends Fragment {
    View view;
    RecyclerView.Adapter<MyViewHoder> lbspq;
    List<xm> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);
        TypedValue typedValue = new TypedValue();
        requireContext().getTheme().resolveAttribute(android.R.attr.windowBackground, typedValue, true);
        // 设置背景颜色
        view.setBackgroundColor(typedValue.data);
        RecyclerView lb = view.findViewById(R.id.recyclerview);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        lb.setLayoutManager(manager);
        lbspq = new spq();
        lb.setAdapter(lbspq);
        setVisibility(false);
        return view;
    }

    public void setVisibility(boolean bool) {
        if (bool) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public boolean getVisibility() {
        return view.isShown();
    }

    public void setStart(String name) {
        setVisibility(true);
        list.clear();
        new start(name);
    }

    public class start extends Thread {
        String name;

        public start(String name) {
            this.name = name;
            start();
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void run() {
            super.run();
            String hq = wl.hq("/search?keywords=" + name);
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
                    list.add(new xm(id, name, zz.toString(), null));
                }
                main.handler.post(() -> lbspq.notifyDataSetChanged());
            } catch (Exception e) {
                gj.sc(e);
            }
        }
    }

    class spq extends RecyclerView.Adapter<MyViewHoder> {
        @NonNull
        @Override
        public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_mp3, parent, false);
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
            holder.view.setOnClickListener(view1 -> {
                if (bfqkz.xm == null || !bfqkz.xm.id.equals(x.id)) {
                    bfqkz.xm = x;
                    new url(x);
                }
                if (!com.muqingbfq.fragment.gd.gdid.equals(bflb_db.gdid)) {
                    bfqkz.list.clear();
                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        bfqkz.list.add(list.get(i));
                    }
                }
                bfqkz.mt.start();
                bfq.start(activity_search.appCompatActivity);
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
