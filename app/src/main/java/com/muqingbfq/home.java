package com.muqingbfq;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.muqingbfq.fragment.Media;
import com.muqingbfq.fragment.bfq_db;
import com.muqingbfq.fragment.gd;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import me.wcy.lrcview.LrcView;

public class home extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static AppCompatActivity appCompatActivity;
    @SuppressLint("StaticFieldLeak")
    public static ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        appCompatActivity = this;
        setTheme(R.style.Theme_muqing);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        main.k = dm.widthPixels;
        main.g = dm.heightPixels;
        if (imageView == null) {
            imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            if (bfq.bitmap == null) {
                imageView.setImageResource(R.drawable.icon);
            } else {
                Media.setImageBitmap();
            }
        }
        if (Media.lrcview == null) {
            // 在合适的位置初始化LrcView
            Media.lrcview = new LrcView(this);
            // 请将Context替换为实际的上下文对象
            // 设置LrcView的属性
            Media.lrcview.setCurrentColor(getResources().getColor(R.color.text));
            Media.lrcview.setLabel(getString(R.string.app_name));
            Media.lrcview.setCurrentTextSize(TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
//            lrcView.setLrcPadding(16);
            Media.lrcview.setCurrentTextSize(TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics()));
            Media.lrcview.setTimelineTextColor(getResources().getColor(R.color.text_tm));
        }
        try {
            //初始化工具栏
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            DrawerLayout drawerLayout = findViewById(R.id.chct);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            //初始化侧滑
            NavigationView chb = findViewById(R.id.chb);
            chb.setNavigationItemSelectedListener(item -> {
                com.muqingbfq.fragment.sz.switch_sz(home.this, item.getItemId());
                return false;
            });
            new com.muqingbfq.fragment.sz(this, chb.getHeaderView(0));
            //初始化播放器组件
            // 启动Service
            if (serviceIntent == null) {
                serviceIntent = new Intent(this, bfqkz.class);
                startService(serviceIntent);
            }
            //检测更新
            new gj.jianchagengxin(this);

        } catch (Exception e) {
            yc.tc(this, e);
        }
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
        wj.setMP3ToFile(bfqkz.xm);
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