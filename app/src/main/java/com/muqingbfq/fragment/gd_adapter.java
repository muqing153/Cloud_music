package com.muqingbfq.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.muqingbfq.MP3;
import com.muqingbfq.R;
import com.muqingbfq.XM;
import com.muqingbfq.api.resource;
import com.muqingbfq.api.url;
import com.muqingbfq.bfqkz;
import com.muqingbfq.databinding.FragmentGdBinding;
import com.muqingbfq.databinding.ListMp3ImageBinding;
import com.muqingbfq.main;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;
import com.muqingbfq.mq.wl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class gd_adapter extends Fragment {
    List<XM> list = new ArrayList<>();
    List<MP3> listmp3 = new ArrayList<>();

    FragmentGdBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGdBinding.inflate(getLayoutInflater(), container, false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.recyclerview1.setHasFixedSize(true);
        binding.recyclerview1.setNestedScrollingEnabled(false);
        binding.recyclerview1.setLayoutManager(linearLayoutManager);
        binding.recyclerview1.setAdapter(new gd.baseadapter(getContext(), list));
        new Thread() {
            @Override
            public void run() {
                super.run();
                resource.recommend(list);
                main.handler.post(new sx());
            }
        }.start();

        binding.recyclerview2.setFocusable(false);
        binding.recyclerview2.setLayoutManager(new LinearLayoutManager(getContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        binding.recyclerview2.setAdapter(new RecyclerView.Adapter<VH_MP3>() {
            @NonNull
            @Override
            public VH_MP3 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new VH_MP3(ListMp3ImageBinding.inflate(getLayoutInflater(), parent, false));
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onBindViewHolder(@NonNull VH_MP3 holder, int position) {
                MP3 x = listmp3.get(position);
                holder.binding.wb1.setText(x.name);
                holder.binding.zz.setText(x.zz);
/*                int color = ContextCompat.getColor(getContext(), R.color.text);
                if (bfqkz.xm != null && x.id.equals(bfqkz.xm.id)) {
                    color = ContextCompat.getColor(getContext(), R.color.text_cz);
                }
                holder.binding.wb1.setTextColor(color);
                holder.binding.zz.setTextColor(color);*/
                holder.itemView.setOnClickListener(view -> {
                    if (bfqkz.xm == null || !bfqkz.xm.id.equals(x.id)) {
                        bfqkz.xm = x;
                        new url(x);
                        notifyDataSetChanged();
                    } else if (!bfqkz.mt.isPlaying()) {
                        bfqkz.mt.start();
                    }
                    if (!bfqkz.list.contains(x)) {
                        bfqkz.list.add(0, x);
                    }
//                    bfqkz.list.addAll(list);
//                    bfq.start(getContext());
                });
                Glide.with(getContext()).load(x.picurl)
                        .apply(new RequestOptions().placeholder(R.drawable.ic_launcher_foreground))
                        .error(R.drawable.ic_launcher_foreground)
                        .into(holder.binding.imageView);
            }

            @Override
            public int getItemCount() {
                return listmp3.size();
            }
        });
        mp3list();
        return binding.getRoot();
    }

    class VH_MP3 extends RecyclerView.ViewHolder {
        ListMp3ImageBinding binding;
        public VH_MP3(@NonNull ListMp3ImageBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    private class sx implements Runnable {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void run() {
            binding.recyclerview1.getAdapter().notifyDataSetChanged();
        }
    }

    public void mp3list() {
        new Thread(){
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                super.run();
                String hq = wl.hq("/recommend/songs" + "?cookie=" + wl.Cookie);
                if (hq == null) {
                    hq = wj.dqwb(wj.filesdri + "songs.josn");
                }
                try {
                    JSONObject jsonObject = new JSONObject(hq);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray dailySongs = data.getJSONArray("dailySongs");
                    for (int i = 0; i < dailySongs.length(); i++) {
                        JSONObject jsonObject1 = dailySongs.getJSONObject(i);
                        String id = jsonObject1.getString("id");
                        String name = jsonObject1.getString("name");
                        JSONArray ar = jsonObject1.getJSONArray("ar");
                        StringBuilder zz = new StringBuilder();
                        for (int j = 0; j < ar.length(); j++) {
                            zz.append(ar.getJSONObject(j).getString("name")).append(' ');
                        }
                        JSONObject al = jsonObject1.getJSONObject("al");
                        String picUrl = al.getString("picUrl");
                        listmp3.add(new MP3(id, name, zz.toString(), picUrl));
                    }
                    wj.xrwb(wj.filesdri + "songs.josn", hq);
                    main.handler.post(() -> binding.recyclerview2.getAdapter().notifyDataSetChanged());
                } catch (Exception e) {
                    gj.sc(e);
                }
            }
        }.start();
    }
}
