package com.muqingbfq.list;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muqingbfq.R;
import com.muqingbfq.databinding.ListMp3Binding;

public class MyViewHoder extends RecyclerView.ViewHolder {
    public ListMp3Binding binding;
    public MyViewHoder(@NonNull ListMp3Binding itemView) {
        super(itemView.getRoot());
        binding = itemView;
    }
    public Context getContext() {
        return itemView.getContext();
    }
}