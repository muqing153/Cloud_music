package com.muqingbfq.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.muqingbfq.R;
import com.muqingbfq.XM;
import com.muqingbfq.activity_search;
import com.muqingbfq.api.playlist;
import com.muqingbfq.api.resource;
import com.muqingbfq.bfqkz;
import com.muqingbfq.databinding.FragmentWdBinding;
import com.muqingbfq.databinding.ListGdBBinding;
import com.muqingbfq.home;
import com.muqingbfq.login.user_logs;
import com.muqingbfq.login.visitor;
import com.muqingbfq.main;
import com.muqingbfq.mq.EditViewDialog;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            {R.drawable.fuwuzhongxing, "更换接口", "API"},
            {R.drawable.gd, "导入歌单", "gd"},
            {R.drawable.paihangbang, "排行榜", "排行榜"},
            {R.drawable.icon, "开发中", ""}
    };
    @SuppressLint("StaticFieldLeak")
    public static baseadapter adaper;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        // 其他初始化代码...
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentWdBinding.inflate(inflater, container, false);
        //初始化工具栏
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar);
        DrawerLayout drawerLayout = home.appCompatActivity.findViewById(R.id.chct);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawerLayout, binding.toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.appbar.setPadding(0, gj.getztl(getContext()), 0, 0);
        name = binding.text1;
        binding.toolbar.setTitle("");
        binding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isCollapsed = false; // 标记是否处于折叠状态

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    // 完全折叠时的逻辑
                    if (!isCollapsed) {
                        // 处理完全折叠时的操作
                        isCollapsed = true;
                        binding.aaa.setVisibility(View.VISIBLE);
                        binding.aaa.setText(name.getText());
                    }
                } else {
                    // 非折叠状态的逻辑
                    if (isCollapsed) {
                        // 处理非折叠状态的操作
                        isCollapsed = false;
                        binding.aaa.setVisibility(View.GONE);
                    }
                }
            }
        });
        jieshao = binding.text2;
        imageView = binding.imageView;
        binding.cardview.setOnClickListener(v -> {
            File file = new File(wj.filesdri, "user.mq");
            if (file.exists()) {
                String[] a = new String[]{"退出登录"};
                new MaterialAlertDialogBuilder(getContext())
                        .setItems(a, (dialogInterface, i) -> {
                            file.delete();
                            setname(getString(R.string.app_name));
                            setqianming(getString(R.string.app_name));
                            imageView.setImageResource(R.drawable.ic_launcher_foreground);
                            new visitor();
                            new com.muqingbfq.login.user_message();
                        }).show();
            } else {
                startActivity(new Intent(getContext(), user_logs.class));
            }
        });
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
                holder.imageView.setImageResource((Integer) lista[position][0]);
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
                        case "API":
                            EditViewDialog editViewDialog = new EditViewDialog(getContext(), "更换接口API")
                                    .setMessage("当前接口：\n" + main.api);
                            editViewDialog.setPositive(view1 -> {
                                String str = editViewDialog.getEditText();
                                boolean http = str.startsWith("http");
                                if (str.isEmpty() || !http) {
                                    gj.ts(getContext(), "请输入正确的api");
                                } else {
                                    gj.ts(getContext(), "更换成功");
                                    main.api = str;
                                    wj.xrwb(wj.filesdri + "API.mq", main.api);
                                    editViewDialog.dismiss();
                                }
                            }).show();
                            break;
                        case "gd":
                            EditViewDialog editViewDialog1 = new EditViewDialog(getContext(),
                                    "导入歌单")
                                    .setMessage("请用网易云https链接来进行导入或者歌单id");
                            editViewDialog1.setPositive(view1 -> {
                                String str = editViewDialog1.getEditText();
                                // 使用正则表达式提取链接
                                Pattern pattern = Pattern.compile("https?://[\\w./?=&]+");
                                Matcher matcher = pattern.matcher(str);
                                if (matcher.find())
                                    str = matcher.group();
                                if (!str.isEmpty()) {
                                    // 使用截取方法获取歌单 ID
                                    str = str.substring(str.indexOf("id=") + 3, str.indexOf("&"));
                                }
                                String finalStr = str;
                                gj.ts(getContext(), "导入中");
                                new Thread() {
                                    @Override
                                    public void run() {
                                        super.run();
                                        String hq = playlist.gethq(finalStr);
                                        if (hq != null) {
                                            wj.xrwb(wj.gd + finalStr, hq);
                                            try {
                                                JSONObject jsonObject = new JSONObject();
                                                if (wj.cz(wj.gd_xz)) {
                                                    jsonObject = new JSONObject(Objects.requireNonNull(wj.dqwb(wj.gd_xz)));
                                                }
                                                XM fh = resource.Playlist_content(finalStr);
                                                JSONObject json = new JSONObject();
                                                json.put("name", fh.name);
                                                json.put("picUrl", fh.picurl);
                                                jsonObject.put(fh.id, json);
                                                wj.xrwb(wj.gd_xz, jsonObject.toString());
                                                sx();
                                                gj.xcts(getContext(), "成功");
                                            } catch (JSONException e) {
                                                gj.sc("list gd onclick thear " + e);
                                                gj.xcts(getContext(), "失败");
                                            }
                                        }
                                    }
                                }.start();
                                editViewDialog1.dismiss();
                            }).show();
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
        adaper = new baseadapter();
        binding.recyclerview2.setAdapter(adaper);
        sx();
        denglu();
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
            adaper.list.clear();
            JSONObject date = new JSONObject(wj.dqwb(wj.gd_xz));
            for (Iterator<String> it = date.keys(); it.hasNext(); ) {
                String id = it.next();
                JSONObject jsonObject = date.getJSONObject(id);
                String name = jsonObject.getString("name");
                String picUrl = jsonObject.getString("picUrl");
                adaper.list.add(new XM(id, name, picUrl));
            }
            main.handler.post(() -> binding.recyclerview2.getAdapter().notifyDataSetChanged());
        } catch (Exception e) {
            gj.sc(e);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public static void addlist(XM xm) {
        if (adaper != null&&!adaper.list.contains(xm)) {
            adaper.list.add(xm);
            adaper.notifyDataSetChanged();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public static void removelist(XM xm) {
        if (adaper != null) {
            adaper.list.remove(xm);
            adaper.notifyDataSetChanged();
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

    public void denglu() {
        if (!wj.cz(wj.filesdri + "user.mq")) {
            return;
        }
        String dqwb = wj.dqwb(wj.filesdri + "user.mq");
        user_logs.USER user = new Gson().fromJson(dqwb, user_logs.USER.class);
        setname(user.name);
        setqianming(user.qianming);
        Glide.with(getContext())
                .load(user.picUrl)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.imageView);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_search) {
            Intent intent = new Intent(getContext(), activity_search.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    class baseadapter extends RecyclerView.Adapter<gd.VH> {
        public List<XM> list = new ArrayList<>();
        @NonNull
        @Override
        public gd.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new gd.VH(ListGdBBinding.bind(LayoutInflater.from(getContext())
                        .inflate(R.layout.list_gd_b, parent, false)));
        }

        public void setList(List<XM> list) {
            this.list = list;
        }

        @Override
        public void onBindViewHolder(@NonNull gd.VH holder, @SuppressLint("RecyclerView") int position) {
            XM xm = list.get(position);
                holder.itemView.setOnClickListener(v -> {
                    XM xm1 = list.get(position);
                    Intent intent = new Intent(getContext(), mp3.class);
                    intent.putExtra("id", xm1.id);
                    intent.putExtra("name", xm1.name);
                    getContext().startActivity(intent);
                });
                holder.itemView.setOnLongClickListener(v -> {
                    String[] stringArray = {"下载歌单", "删除歌单"};
                    new MaterialAlertDialogBuilder(getContext()).
                            setItems(stringArray, (dialog, id) -> {
                                switch (stringArray[id]) {
                                    case "下载歌单":
                                        new Thread() {
                                            @SuppressLint("NotifyDataSetChanged")
                                            @Override
                                            public void run() {
                                                super.run();
                                                String hq = playlist.gethq(xm.id);
                                                if (hq != null) {
                                                    wj.xrwb(wj.gd + xm.id, hq);
                                                    try {
                                                        JSONObject jsonObject = new JSONObject();
                                                        if (wj.cz(wj.gd_xz)) {
                                                            jsonObject = new JSONObject(Objects.requireNonNull(wj.dqwb(wj.gd_xz)));
                                                        }
                                                        XM fh = resource.Playlist_content(xm.id);
                                                        JSONObject json = new JSONObject();
                                                        json.put("name", fh.name);
                                                        json.put("picUrl", fh.picurl);
                                                        jsonObject.put(fh.id, json);
                                                        wj.xrwb(wj.gd_xz, jsonObject.toString());
                                                        main.handler.post(() -> notifyItemChanged(position));
                                                    } catch (JSONException e) {
                                                        gj.sc("list gd onclick thear " + e);
                                                    }
                                                }
                                            }
                                        }.start();
                                        break;
                                    case "删除歌单":
//                                        删除项目
                                        try {
                                            wj.sc(wj.gd + xm.id);
                                            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(wj.dqwb(wj.gd_xz)));
                                            jsonObject.remove(xm.id);
                                            list.remove(xm);
                                            wj.xrwb(wj.gd_xz, jsonObject.toString());
                                            notifyItemRemoved(position);
                                        } catch (JSONException e) {
                                            gj.sc(e);
                                        }
                                        break;
                                }
                                // 在这里处理菜单项的点击事件
                            }).show();
                    return false;
                });

                holder.bindingB.text2.setText(xm.message);
            holder.title.setText(xm.name);
            holder.kg.setOnClickListener(view1 -> {
                ImageView tx = (ImageView) view1;
                new Thread() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        super.run();
                        boolean an = playlist.hq(bfqkz.list, xm.id);
                        if (bfqkz.ms == 2) {
                            Collections.shuffle(bfqkz.list);
                        }
                        main.handler.post(() -> {
                            if (an) {
                                com.muqingbfq.bfq_an.xyq();
                                tx.setImageResource(R.drawable.bf);
                                com.muqingbfq.fragment.gd.gdid = xm.id;
                            }
                            notifyDataSetChanged();
                        });
                    }
                }.start();
            });
            Drawable color_kg = ContextCompat.getDrawable(getContext(), R.drawable.zt);
            if (xm.id.equals(com.muqingbfq.fragment.gd.gdid)) {
                color_kg = ContextCompat.getDrawable(getContext(), R.drawable.bf);
            }
            holder.kg.setImageDrawable(color_kg);
//            xm.picurl
            holder.image.setImageapply(xm.picurl);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

}
