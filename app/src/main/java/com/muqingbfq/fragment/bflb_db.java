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
import com.muqingbfq.R;
import com.muqingbfq.api.url;
import com.muqingbfq.bfqkz;
import com.muqingbfq.list.MyViewHoder;
import com.muqingbfq.main;
import com.muqingbfq.xm;
import com.muqingbfq.yc;

public class bflb_db extends BottomSheetDialog {
    public static String gdid;

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
                lb.smoothScrollToPosition(bfqkz.list.indexOf(bfqkz.xm));
            }
            findViewById(R.id.xxbj).
                    setOnClickListener(v -> {
                        if (bfqkz.xm != null) {
                            lb.smoothScrollToPosition(bfqkz.list.indexOf(bfqkz.xm));
                        }
                    });
        } catch (Exception e) {
            yc.start(getContext(), e);
        }
    }

    public bflb_db(Context context) {
        super(context);
    }

    public static void start(Context context) {
        new bflb_db(context).show();
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
            xm x = bfqkz.list.get(position);
            holder.name.setText(x.name);
            holder.zz.setText(x.zz);
            int color = ContextCompat.getColor(holder.getContext(), R.color.text);
            if (bfqkz.xm != null && x.id.equals(bfqkz.xm.id)) {
                color = ContextCompat.getColor(holder.getContext(), R.color.text_cz);
            }
            holder.name.setTextColor(color);
            holder.zz.setTextColor(color);
            holder.view.setOnClickListener(view -> {
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

}
