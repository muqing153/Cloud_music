package com.muqingbfq;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.muqingbfq.databinding.ActivityHomeBinding;
import com.muqingbfq.fragment.bfq_db;
import com.muqingbfq.fragment.gd_adapter;
import com.muqingbfq.fragment.wode;
import com.muqingbfq.mq.gj;

import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static AppCompatActivity appCompatActivity;
    ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appCompatActivity = this;
        setTheme(R.style.Theme_muqing);
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DisplayMetrics dm = getResources().getDisplayMetrics();
        main.k = dm.widthPixels;
        main.g = dm.heightPixels;
        try {
            //初始化工具栏
            setSupportActionBar(binding.toolbar);
            DrawerLayout drawerLayout = findViewById(R.id.chct);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawerLayout, binding.toolbar, R.string.app_name, R.string.app_name);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            //初始化侧滑
            binding.chb.setNavigationItemSelectedListener(item -> {
                com.muqingbfq.fragment.sz.switch_sz(home.this, item.getItemId());
                return false;
            });
            //初始化播放器组件
            // 启动Service
            if (serviceIntent == null) {
                serviceIntent = new Intent(this, bfqkz.class);
                startService(serviceIntent);
            }
            //检测更新
            new gj.jianchagengxin(this);
            binding.editView.setOnClickListener(view ->
                    startActivity(new Intent(this, activity_search.class)));
            UI();
        } catch (Exception e) {
            yc.tc(this, e);
        }
    }

    List<Fragment> list = new ArrayList<>();
    private class adaper extends FragmentStateAdapter {
        public adaper(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
            list.add(new gd_adapter());
            list.add(new wode());
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return list.get(position);
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public void UI() {
        binding.viewPager.setAdapter(new adaper(this));

// 将 ViewPager2 绑定到 TabLayout
        binding.tablayout.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.a) {
                binding.viewPager.setCurrentItem(0);
            } else if (itemId == R.id.c) {
                binding.viewPager.setCurrentItem(1);
            }
            return true;
        });
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        binding.tablayout.setSelectedItemId(R.id.a);
                        break;
                    case 1:
                        binding.tablayout.setSelectedItemId(R.id.c);
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                try {
                                    sleep(1000);
                                } catch (InterruptedException e) {
                                    e.toString();
                                }
                                wode fragment = (wode) list.get(position);
                                fragment.sx();
                            }
                        }.start();
                        break;
                }
            }
        });
    }

    private static Intent serviceIntent;
    @Override
    protected void onPause() {
        super.onPause();
        //在销毁 Activity 之前，系统会先调用 onDestroy()。系统调用此回调的原因如下：
        // 保存列表数据
        SharedPreferences sharedPreferences = getSharedPreferences("list", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonList = new com.google.gson.Gson().toJson(bfqkz.list);
        editor.putString("listData", jsonList);
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.bfq_db, new bfq_db())
                .commit();
    }
    private long time;

    @Override
    public void onBackPressed() {
        if (bfqkz.mt.isPlaying()) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        } else {
            if (time < System.currentTimeMillis() - 1000) {
                time = System.currentTimeMillis();
                gj.ts(this, "再按一次退出软件");
            } else {
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_search) {
            Intent intent = new Intent(this, activity_search.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}