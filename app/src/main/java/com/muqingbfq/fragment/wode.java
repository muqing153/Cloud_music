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
import com.muqingbfq.xm;

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
    private final List<com.muqingbfq.xm> list = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWdBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
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
        int k = (int) (main.k / getResources().getDisplayMetrics().density + 0.5f);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), k / 120);
        binding.recyclerview1.setLayoutManager(gridLayoutManager);
        final Object[][] lista ={
                {R.drawable.bf,"最近播放"},
                {R.drawable.download,"下载音乐"},
                {R.drawable.like,"喜欢音乐"},
                {R.drawable.icon,"本地搜索"},
                {R.drawable.icon,"我的歌单"},
                {R.drawable.icon,"导入歌单"},
                {R.drawable.icon,"开发中"},
                {R.drawable.icon,"开发者"}
        };
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
                holder.itemView.setOnClickListener(view -> {
                    Intent a = new Intent(getContext(), com.muqingbfq.fragment.mp3.class);
                    switch (position) {
                        case 0:
                            a.putExtra("id", "mp3_hc.json");
                            a.putExtra("name", s);
                            getContext().startActivity(a);
                            break;
                        case 1:
                            a.putExtra("id", "mp3_xz.json");
                            a.putExtra("name", s);
                            getContext().startActivity(a);
                            break;
                        case 2:
                            a.putExtra("id", "mp3_like.json");
                            a.putExtra("name", s);
                            getContext().startActivity(a);
                            break;
                        case 3:
                            break;

                    }
                });
            }

            @Override
            public int getItemCount() {
                return lista.length;
            }
        });
        sx();
        binding.recyclerview2.setLayoutManager(new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;//禁止滑动
            }
        });
        binding.recyclerview2.setFocusable(false);
        binding.recyclerview2.setAdapter(new gd.baseadapter(getContext(),list));
        return view;
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

    public void sx(){
        try {
            list.clear();
            JSONObject date = new JSONObject(wj.dqwb(wj.gd_xz));
            for (Iterator<String> it = date.keys(); it.hasNext(); ) {
                String id = it.next();
                boolean cz = wj.cz(wj.gd + id);
                JSONObject jsonObject = date.getJSONObject(id);
                String name = jsonObject.getString("name");
                String picUrl = jsonObject.getString("picUrl");
                list.add(new xm(id, name, picUrl, cz));
            }
            binding.recyclerview2.getAdapter().notifyDataSetChanged();
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
