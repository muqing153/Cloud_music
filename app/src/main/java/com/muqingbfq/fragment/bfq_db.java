package com.muqingbfq.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.gson.reflect.TypeToken;
import com.muqingbfq.R;
import com.muqingbfq.bfq;
import com.muqingbfq.bfq_an;
import com.muqingbfq.bfqkz;
import com.muqingbfq.home;
import com.muqingbfq.mq.wj;
import com.muqingbfq.xm;

import java.lang.reflect.Type;
import java.util.List;

public class bfq_db extends Fragment {
    @SuppressLint("StaticFieldLeak")
    public static View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            String jsonList = this.getContext().getSharedPreferences("list", Context.MODE_PRIVATE)
                    .getString("listData", null); // 获取保存的 JSON 字符串
            if (jsonList != null) {
                Type type = new TypeToken<List<xm>>() {
                }.getType();
                bfqkz.list = new com.google.gson.Gson().fromJson(jsonList, type);
                // 将 JSON 字符串转换回列表数据
            }
        }
        view = inflater.inflate(R.layout.fragment_bfq_db, container, false);

        TextView name = view.findViewById(R.id.name);
        view.findViewById(R.id.kg).setOnClickListener(new bfq_an.kz());
        view.findViewById(R.id.txb).setOnClickListener(view -> bflb_db.start(getContext()));
        view.setOnClickListener(vw -> bfq.start(home.appCompatActivity));
// 恢复列表数据
        if (bfqkz.xm != null) {
            name.setText(bfqkz.xm.name + "/" + bfqkz.xm.zz);
        }
        if (bfqkz.mt != null) {
            Media.setbf(bfqkz.mt.isPlaying());
        }
        return view;
    }

    private static <T extends View> T findViewById(int id) {
        return view.findViewById(id);
    }

    public static void setkg(boolean bool) {
        if (view != null) {
            ImageView imageView = findViewById(R.id.kg);
            if (bool) {
                imageView.setImageResource(R.drawable.bf);
            } else {
                imageView.setImageResource(R.drawable.zt);
            }
        }
    }

    public static void setname(String str) {
        if (view != null) {
            TextView textView = findViewById(R.id.name);
            textView.setText(str);
        }
    }
}