package com.muqingbfq.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.muqingbfq.R;
import com.muqingbfq.databinding.FragmentWdBinding;
import com.muqingbfq.login.user_editing;
import com.muqingbfq.login.user_logs;
import com.muqingbfq.login.user_message;
import com.muqingbfq.main;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;
import com.muqingbfq.XM;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class wode extends Fragment {

    @SuppressLint("StaticFieldLeak")
    public static TextView name, jieshao;
    @SuppressLint("StaticFieldLeak")
    public static ImageView imageView;
    FragmentWdBinding binding;
    private final Object[][] lista = {
            {R.drawable.bf, "最近播放", "mp3_hc.json"},
            {R.drawable.download, "下载音乐", "mp3_xz.json"},
            {R.drawable.like, "喜欢音乐", "mp3_like.json"},
            {R.drawable.icon, "本地搜索", ""},
            {R.drawable.icon, "我的歌单", ""},
            {R.drawable.icon, "导入歌单", ""},
            {R.drawable.paihangbang, "排行榜", "排行榜"},
            {R.drawable.icon, "开发中", ""}
    };
    private final List<XM> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWdBinding.inflate(inflater, container, false);
        name = binding.text1;
        jieshao = binding.text2;
        imageView = binding.imageView;
        binding.cardview.setOnClickListener(v -> {
            if (main.getToken() == null) {
                startActivity(new Intent(getContext(), user_logs.class));
            } else {
                startActivity(new Intent(getContext(), user_editing.class));
            }
        });
        new user_message();
//        int k = (int) (main.k / getResources().getDisplayMetrics().density + 0.5f);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4) {
            @Override
            public boolean canScrollVertically() {
                return false;//禁止滑动
            }
        };
        binding.recyclerview1.setLayoutManager(gridLayoutManager);

        binding.recyclerview1.setFocusable(false);
        binding.recyclerview1.setAdapter(new RecyclerView.Adapter<VH>() {
            @NonNull
            @Override
            public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View inflate = View.inflate(getContext(), R.layout.view_button, null);
                return new VH(inflate);
            }

            @Override
            public void onBindViewHolder(@NonNull VH holder, int position) {
                String s = lista[position][1].toString();
                holder.textView.setText(s);
                Glide.with(getContext())
                        .load(lista[position][0])
                        .into(holder.imageView);
                String data = lista[position][2].toString();
                holder.itemView.setOnClickListener(view -> {
                    switch (data) {
                        case "mp3_hc.json":
                        case "mp3_xz.json":
                        case "mp3_like.json":
                            Intent a = new Intent(getContext(), com.muqingbfq.fragment.mp3.class);
                            a.putExtra("id", data);
                            a.putExtra("name", s);
                            getContext().startActivity(a);
                            break;
                        case "排行榜":
                            Intent b = new Intent(getContext(), com.muqingbfq.fragment.gd.class);
                            b.putExtra("id", data);
                            b.putExtra("name", s);
                            getContext().startActivity(b);
                            break;
                    }
                });
            }

            @Override
            public int getItemCount() {
                return lista.length;
            }
        });
        binding.recyclerview2.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;//禁止滑动
            }
        });
        binding.recyclerview2.setFocusable(false);
        binding.recyclerview2.setAdapter(new gd.baseadapter(getContext(), list, true));
        sx();
        return binding.getRoot();
    }

    class VH extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public VH(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.text1);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void sx() {
        try {
            list.clear();
            JSONObject date = new JSONObject(wj.dqwb(wj.gd_xz));
            for (Iterator<String> it = date.keys(); it.hasNext(); ) {
                String id = it.next();
                JSONObject jsonObject = date.getJSONObject(id);
                String name = jsonObject.getString("name");
                String picUrl = jsonObject.getString("picUrl");
                list.add(new XM(id, name, picUrl));
            }
            main.handler.post(() -> binding.recyclerview2.getAdapter().notifyDataSetChanged());
        } catch (Exception e) {
            gj.sc(e);
        }
    }

    public static void setname(String string) {
        main.handler.post(() -> name.setText(string));
    }

    public static void setqianming(String string) {
        main.handler.post(() -> {
            if (string == null) {
                jieshao.setText("");
            } else {
                jieshao.setText(string);
            }
        });
    }
}
