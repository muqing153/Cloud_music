package com.muqingbfq.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.muqingbfq.MP3;
import com.muqingbfq.R;
import com.muqingbfq.api.url;
import com.muqingbfq.bfqkz;
import com.muqingbfq.list.MyViewHoder;
import com.muqingbfq.main;
import com.muqingbfq.mq.gj;
import com.muqingbfq.xm;
import com.muqingbfq.yc;

public class bflb_db extends BottomSheetDialog {
    public static RecyclerView.Adapter<MyViewHoder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bflb_db);
        int height = main.g - main.g / 2 / 2;
        getBehavior().setPeekHeight(height);
        getBehavior().setMaxHeight(height);
        try {
            RecyclerView lb = findViewById(R.id.lb);
            lb.setAdapter(new spq());
            if (bfqkz.xm != null) {
                lb.smoothScrollToPosition(getI());
            }
            findViewById(R.id.xxbj).
                    setOnClickListener(v -> {
                        if (bfqkz.xm != null) {
                            lb.smoothScrollToPosition(getI());
                        }
                    });
        } catch (Exception e) {
            yc.start(getContext(), e);
        }
    }

    private int getI() {
        int i = bfqkz.list.indexOf(bfqkz.xm);
        if (i == -1) {
            i = 0;
        }
        return i;
    }
    public bflb_db(Context context) {
        super(context);
    }

    public static void start(Context context) {
        new bflb_db(context).show();
    }

    class spq extends RecyclerView.Adapter<MyViewHoder> {
        public spq() {
            adapter = this;
        }

        @NonNull
        @Override
        public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_mp3, parent, false);
            return new MyViewHoder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {
            MP3 x = bfqkz.list.get(position);
            holder.name.setText(x.name);
            holder.zz.setText(x.zz);
            int color = ContextCompat.getColor(holder.getContext(), R.color.text);
            if (bfqkz.xm != null && x.id.equals(bfqkz.xm.id)) {
                color = ContextCompat.getColor(holder.getContext(), R.color.text_cz);
            }
            holder.name.setTextColor(color);
            holder.zz.setTextColor(color);
            holder.itemView.setOnClickListener(view -> {
                if (bfqkz.xm != x) {
                    bfqkz.xm = x;
                    new url(x);
                }
            });
        }

        @Override
        public int getItemCount() {
            return bfqkz.list.size();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        adapter = null;
    }
}
