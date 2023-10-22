package com.muqingbfq.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.muqingbfq.R;
import com.muqingbfq.bfq;
import com.muqingbfq.bfq_an;
import com.muqingbfq.bfqkz;
import com.muqingbfq.home;
import com.muqingbfq.xm;

import java.lang.reflect.Type;
import java.util.List;

public class bfq_db extends Fragment {
    @SuppressLint("StaticFieldLeak")
    public static View view;
    public static TextView name, zz;
    public static ImageView txa;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bfq_db, container, false);
        name = view.findViewById(R.id.name);
        zz = view.findViewById(R.id.zz);
        txa = view.findViewById(R.id.kg);
        txa.setOnClickListener(new bfq_an.kz());

        view.findViewById(R.id.txb).setOnClickListener(view -> bflb_db.start(getContext()));
        view.setOnClickListener(view12 -> bfq.start(home.appCompatActivity));

// 恢复列表数据
        SharedPreferences sharedPreferences = this.getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String jsonList = sharedPreferences.getString("listData", ""); // 获取保存的 JSON 字符串
        if (!jsonList.isEmpty()) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<xm>>() {
            }.getType();
            bfqkz.list = gson.fromJson(jsonList, type); // 将 JSON 字符串转换回列表数据
        } else {
            view.setVisibility(View.GONE);
        }
        return view;
    }

}