package com.muqingbfq.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muqingbfq.R;
import com.muqingbfq.api.playlist;
import com.muqingbfq.api.url;
import com.muqingbfq.bfq;
import com.muqingbfq.bfqkz;
import com.muqingbfq.databinding.FragmentMp3Binding;
import com.muqingbfq.home;
import com.muqingbfq.list.MyViewHoder;
import com.muqingbfq.main;
import com.muqingbfq.xm;

import java.util.ArrayList;
import java.util.List;

public class mp3 extends com.muqingbfq.mq.ActivityToolbar {
    private final List<xm> list = new ArrayList<>();
    public static RecyclerView.Adapter<MyViewHoder> lbspq;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentMp3Binding inflate = FragmentMp3Binding.inflate(getLayoutInflater());
        Intent intent = getIntent();
        inflate.toolbar.setTitle(intent.getStringExtra("name"));
        setContentView(inflate.getRoot());
        lbspq = new spq();
        RecyclerView lb = findViewById(R.id.lb);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        lb.setLayoutManager(layoutManager);
        lb.setAdapter(lbspq);
        String id = intent.getStringExtra("id");
        new start(id);
    }

    @SuppressLint("NotifyDataSetChanged")
    class start extends Thread {
        String id;

        public start(String id) {
            this.id = id;
            list.clear();
            mp3.lbspq.notifyDataSetChanged();
            start();
        }

        @Override
        public void run() {
            super.run();
            if (id.equals("mp3_xz.json")) {
                playlist.hq_xz(list);
            } else if (id.equals("mp3_like.json")) {
                playlist.hq_like(list);
            } else {
                playlist.hq(list, id);
            }
            main.handler.post(new lbspq_sx());
        }
    }

    public static class lbspq_sx implements Runnable {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void run() {
            lbspq.notifyDataSetChanged();
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
            holder.itemView.setOnClickListener(view -> {
                if (bfqkz.xm == null || !bfqkz.xm.id.equals(x.id)) {
                    bfqkz.xm = x;
                    new url(x);
                }
                bfqkz.list.clear();
                bfqkz.list.addAll(list);
                bfq.start(mp3.this);
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }


    public static void startactivity(Context context, String id) {
        context.startActivity(new Intent(context, mp3.class).putExtra("id", id));
    }
}
