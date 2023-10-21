package com.muqingbfq.list;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muqingbfq.R;

public class MyViewHoder extends RecyclerView.ViewHolder {
    public TextView name, zz;
    public View view;

    public MyViewHoder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        name = itemView.findViewById(R.id.wb1);
        zz = itemView.findViewById(R.id.zz);
    }

    public Context getContext() {
        return view.getContext();
    }
}