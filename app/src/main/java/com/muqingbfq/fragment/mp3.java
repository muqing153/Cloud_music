package com.muqingbfq.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.muqingbfq.MP3;
import com.muqingbfq.R;
import com.muqingbfq.XM;
import com.muqingbfq.api.FileDownloader;
import com.muqingbfq.api.playlist;
import com.muqingbfq.api.resource;
import com.muqingbfq.api.url;
import com.muqingbfq.bfq;
import com.muqingbfq.bfqkz;
import com.muqingbfq.databinding.FragmentMp3Binding;
import com.muqingbfq.databinding.ListMp3Binding;
import com.muqingbfq.list.MyViewHoder;
import com.muqingbfq.main;
import com.muqingbfq.mq.FragmentActivity;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class mp3 extends FragmentActivity {
    private List<MP3> list = new ArrayList<>();
    private List<MP3> list_ys = new ArrayList<>();
    public static adaper lbspq;
    FragmentMp3Binding binding;
//    private static String id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentMp3Binding.inflate(getLayoutInflater());
        Intent intent = getIntent();
        binding.title.setText(intent.getStringExtra("name"));
        String id = intent.getStringExtra("id");
        setContentView(binding.getRoot());
        lbspq = new adaper(list);
        binding.lb.setLayoutManager(new LinearLayoutManager(this));
        binding.lb.setAdapter(lbspq);
        new start(id);
        binding.edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.edittext.getVisibility() == View.VISIBLE) {
                    lbspq.getFilter().filter(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //添加Android自带的分割线
        binding.lb.addItemDecoration(
                new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem itemA = menu.add("搜索");
        itemA.setTitle("搜索");
        itemA.setIcon(R.drawable.sousuo);
        itemA.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            if (binding.edittext.getVisibility() == View.VISIBLE) {
                binding.title.setVisibility(View.VISIBLE);
                binding.edittext.setVisibility(View.GONE);
            } else {
                finish();
            }
            gj.ycjp(binding.edittext);
            lbspq.getFilter().filter("");
        } else if (itemId == 0) {
            binding.title.setVisibility(View.GONE);
            binding.edittext.setVisibility(View.VISIBLE);
            gj.tcjp(binding.edittext);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (binding.edittext.getVisibility() == View.VISIBLE) {
            binding.title.setVisibility(View.VISIBLE);
            binding.edittext.setVisibility(View.GONE);
        } else {
            finish();
        }
        gj.ycjp(binding.edittext);
        lbspq.getFilter().filter("");
    }

    @SuppressLint("NotifyDataSetChanged")
    class start extends Thread {
        String id;

        public start(String id) {
            this.id = id;
            list.clear();
            list_ys.clear();
            start();
        }

        @Override
        public void run() {
            super.run();
            if (id.equals("mp3_xz.json")) {
                playlist.hq_xz(list);
            } else if (id.equals("mp3_like.json")) {
                playlist.hq_like(list);
            } else {
                playlist.hq(list, id);
            }
            list_ys = list;
            main.handler.post(new lbspq_sx());
        }
    }

    public static class lbspq_sx implements Runnable {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void run() {
            lbspq.notifyDataSetChanged();
        }
    }

    public static class adaper extends RecyclerView.Adapter<MyViewHoder> implements Filterable {

        private List<MP3> list;
        private List<MP3> list_ys;
        public adaper(List list) {
            this.list = list;
            list_ys = list;
        }
        @NonNull
        @Override
        public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHoder(ListMp3Binding.bind(LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.list_mp3,
                    parent, false)));
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {
            MP3 x = list.get(position);
            holder.binding.text1.setText(String.valueOf(position + 1));
            holder.binding.name.setText(x.name);
            holder.binding.zz.setText(x.zz);
            int color = ContextCompat.getColor(holder.getContext(), R.color.text);
            if (bfqkz.xm != null && x.id.equals(bfqkz.xm.id)) {
                color = ContextCompat.getColor(holder.getContext(), R.color.text_cz);
            }
            holder.binding.name.setTextColor(color);
            holder.binding.zz.setTextColor(color);
            holder.itemView.setOnClickListener(view -> {
                if (bfqkz.xm == null || !bfqkz.xm.id.equals(x.id)) {
                    bfqkz.xm = x;
                    new url(x);
                }
                bfqkz.list.clear();
                bfqkz.list.addAll(list);
                bfq.start(holder.getContext());
            });
            holder.itemView.setOnLongClickListener(view -> {
                String a[] = new String[]{"喜欢歌曲", "下载歌曲", "复制名字"};
                new MaterialAlertDialogBuilder(view.getContext()).
                        setItems(a, (dialog, id) -> {
                            String title = a[id];
                            switch (title) {
                                case "下载歌曲":
                                    new FileDownloader(view.getContext()).downloadFile(x);
                                    break;
                                case "喜欢歌曲":
                                    try {
                                        Gson gson = new Gson();
                                        Type type = new TypeToken<List<MP3>>() {
                                        }.getType();
                                        List<MP3> list = gson.fromJson(wj.dqwb(wj.gd + "mp3_like.json"), type);
                                        if (list == null) {
                                            list = new ArrayList<>();
                                        }
                                        if (bfqkz.like_bool) {
                                            list.remove(bfqkz.xm);
                                            bfq.setlike(false);
                                        } else {
                                            if (!list.contains(bfqkz.xm)) {
                                                list.add(bfqkz.xm);
                                                bfq.setlike(true);
                                            }
                                        }
                                        bfqkz.like_bool = !bfqkz.like_bool;
                                        wj.xrwb(wj.gd + "mp3_like.json", gson.toJson(list));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case "复制名字":
                                    gj.fz(view.getContext(), x.name);
                                    break;

                            }
                        }).show();
                return false;
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        //没有过滤的内容，则使用源数据
                        list = list_ys;
                    } else {
                        List<MP3> filteredList = new ArrayList<>();
                        for (int i = 0; i < list_ys.size(); i++) {
                            MP3 mp3 = list_ys.get(i);
                            if (mp3.name.contains(charString)
                                    || mp3.zz.contains(charString)) {
                                filteredList.add(list_ys.get(i));
                            }
                        }
                        list = filteredList;
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = list;
                    return filterResults;
                }

                @SuppressLint("NotifyDataSetChanged")
                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    list = (List<MP3>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }
    }


    public static void startactivity(Context context, String id) {
        context.startActivity(new Intent(context, mp3.class).putExtra("id", id));
    }
}
