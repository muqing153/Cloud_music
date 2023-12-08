package com.muqingbfq.clean;

import android.os.Bundle;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.muqingbfq.R;
import com.muqingbfq.databinding.CleanBinding;
import com.muqingbfq.mq.ActivityToolbar;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class fragment_clean extends ActivityToolbar {
    List<String[]> list = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        list.add(new String[]{"音乐", wj.mp3});
        list.add(new String[]{"歌单",wj.gd});
        CleanBinding binding = CleanBinding.inflate(getLayoutInflater());
        binding.toolbar.setTitle("储存清理");
        setContentView(binding.getRoot());
        binding.recyclerview.setAdapter(adapter);
    }

    private final RecyclerView.Adapter<VH> adapter = new RecyclerView.Adapter<VH>() {
        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(fragment_clean.this).
                    inflate(R.layout.list_clean, parent, false);
            return new VH(inflate);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            String[] s = list.get(position);
            File file = new File(s[1]);
            long leng = 0;
            if (file.isDirectory()) {
                gj.sc(file.toString());
                for (File a : file.listFiles()) {
                    leng += a.length();
                }
            }
            String s1 = Formatter.formatFileSize(fragment_clean.this, leng);
            holder.checkBox.setText(s[0] + ":" + s1);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    };

    class VH extends RecyclerView.ViewHolder {
        public CheckBox checkBox;
        public VH(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.box);
        }
    }
}
