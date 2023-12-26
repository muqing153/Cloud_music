package com.muqingbfq.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.muqingbfq.R;
import com.muqingbfq.api.resource;
import com.muqingbfq.databinding.FragmentWdBinding;
import com.muqingbfq.login.user_editing;
import com.muqingbfq.login.user_logs;
import com.muqingbfq.main;
import com.muqingbfq.xm;

import java.util.ArrayList;
import java.util.List;

public class gd_adapter extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout layout = new FrameLayout(getContext());
        List<xm> list = new ArrayList<>();
        RecyclerView recyclerView = new RecyclerView(layout.getContext());
        int k = (int) (main.k / getResources().getDisplayMetrics().density + 0.5f);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(layout.getContext(), k / 120);
        recyclerView.setLayoutManager(gridLayoutManager);
        layout.addView(recyclerView);
        gd.baseadapter baseadapter = new gd.baseadapter(getContext(), list);
        recyclerView.setAdapter(baseadapter);
        new Thread() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                super.run();
                resource.recommend(list);
                main.handler.post(new sx(baseadapter));
            }
        }.start();
        return layout;
    }
    private class sx implements Runnable {
        gd.baseadapter baseadapter;

        public sx(gd.baseadapter baseadapter) {
            this.baseadapter = baseadapter;
        }
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void run() {
            baseadapter.notifyDataSetChanged();
        }
    }

}
