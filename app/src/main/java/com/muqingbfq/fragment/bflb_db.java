package com.muqingbfq.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.muqingbfq.MP3;
import com.muqingbfq.R;
import com.muqingbfq.api.url;
import com.muqingbfq.bfqkz;
import com.muqingbfq.databinding.FragmentBflbDbBinding;
import com.muqingbfq.databinding.ListMp3ABinding;
import com.muqingbfq.list.MyViewHoder;
import com.muqingbfq.main;
import com.muqingbfq.yc;

public class bflb_db extends BottomSheetDialog {
    public static RecyclerView.Adapter<MyViewHoder> adapter;
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentBflbDbBinding binding = FragmentBflbDbBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        int height = main.g - main.g / 2 / 2;
        getBehavior().setPeekHeight(height);
        getBehavior().setMaxHeight(height);
        try {
            binding.lb.setAdapter(new spq());
            if (bfqkz.xm != null) {
                binding.lb.smoothScrollToPosition(getI());
            }
            binding.xxbj.setOnClickListener(v -> {
                        if (bfqkz.xm != null) {
                            binding.lb.smoothScrollToPosition(getI());
                        }
                    });
            binding.sc.setOnClickListener(view -> new MaterialAlertDialogBuilder(getContext())
                    .setTitle("清空播放列表")
                    .setPositiveButton("确定", (dialogInterface, i) -> {
                        bfqkz.list.clear();
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("取消", null)
                    .show());
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
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_mp3, parent, false);
            return new MyViewHoder(ListMp3ABinding.
                    inflate(getLayoutInflater(),parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {
            MP3 x = bfqkz.list.get(position);
            holder.bindingA.name.setText(x.name);
            holder.bindingA.zz.setText(x.zz);
            int color = ContextCompat.getColor(holder.getContext(), R.color.text);
            if (bfqkz.xm != null && x.id.equals(bfqkz.xm.id)) {
                color = ContextCompat.getColor(holder.getContext(), R.color.text_cz);
            }
            holder.bindingA.name.setTextColor(color);
            holder.bindingA.zz.setTextColor(color);
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
