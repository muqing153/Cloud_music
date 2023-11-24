package com.muqingbfq;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.muqingbfq.fragment.Media;
import com.muqingbfq.fragment.bfq_db;
import com.muqingbfq.mq.gj;

public class home extends AppCompatActivity {
    private final ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            bfqkz.MyBinder binder = (bfqkz.MyBinder) iBinder;
            bfqkz service = binder.getService();
            // 与Service建立连接后，可以通过myService调用Service中的方法
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
            //断开连接
        }
    };
    private boolean isBound = false;
    @SuppressLint("StaticFieldLeak")
    public static AppCompatActivity appCompatActivity;

    @SuppressLint({"CommitTransaction", "ObsoleteSdkInt"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        appCompatActivity = this;
        Media.view = null;
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
//                startService(serviceIntent);
                bindService(serviceIntent,serviceConnection, Context.BIND_AUTO_CREATE);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.bfq_db, new bfq_db()).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁之前 finish();
        // 解绑Service
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
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