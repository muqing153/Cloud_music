package com.muqingbfq;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.muqingbfq.databinding.ListKaifazheBinding;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;

import java.util.ArrayList;
import java.util.List;

public class activity_about_software extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_software);
        Toolbar toolbar = findViewById(R.id.toolbar);
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            toolbar.setSubtitle(versionName + " Bate");
        } catch (PackageManager.NameNotFoundException e) {
            yc.start(this, e);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.button1).setOnClickListener(view -> {
            wj.sc(wj.filesdri + "gx.mq");
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    int jianchagengxin = gj.jianchagengxin(activity_about_software.this);
                    if (jianchagengxin == 400) {
                        gj.xcts(activity_about_software.this, "无网络");
                    } else if (jianchagengxin == 0) {
                        gj.xcts(activity_about_software.this, "已经是最新的客户端了");
                    }
                }
            }.start();
        });
    }

    MenuItem itemA;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        itemA= menu.add("特别鸣谢");
        itemA.setTitle("特别鸣谢");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        } else if (item == itemA) {
            new botton(this);
        }
        return super.onOptionsItemSelected(item);
    }

    class botton extends BottomSheetDialog {

        List<Object[]> list = new ArrayList<>();
        public botton(@NonNull Context context) {
            super(context);
            setTitle("特别鸣谢");
            list.add(new Object[]{"http://139.196.224.229/muqing/picurl/mint.jpg","薄荷今天吃什么?", "维护开发者", "QQ"});
            list.add(new Object[]{"http://139.196.224.229/muqing/picurl/weilian.jpg","威廉", "主要测试BUG", "QQ"});
            show();

        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            RecyclerView recyclerView = new RecyclerView(getContext());
            recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            recyclerView.setPadding(50,100,50,500);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new RecyclerView.Adapter<VH>() {
                @NonNull
                @Override
                public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    ListKaifazheBinding binding = ListKaifazheBinding.inflate(getLayoutInflater());
                    return new VH(binding.getRoot());
                }

                @Override
                public void onBindViewHolder(@NonNull VH holder, int position) {
                    Object[] objects = list.get(position);
                    holder.name.setText(objects[1].toString());
                    holder.zz.setText(objects[2].toString());
                    Glide.with(getContext())
                            .load(objects[0])
                            .error(R.drawable.icon)
                            .into(holder.imageView);
                }

                @Override
                public int getItemCount() {
                    return list.size();
                }
            });
            setContentView(recyclerView);

        }
    }

    class VH extends RecyclerView.ViewHolder {
        public TextView name, zz;
        public ImageView imageView;
        public VH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text1);
            zz = itemView.findViewById(R.id.text2);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }


}