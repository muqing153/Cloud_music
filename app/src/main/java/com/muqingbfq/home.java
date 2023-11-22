package com.muqingbfq;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.muqingbfq.fragment.Media;
import com.muqingbfq.fragment.bfq_db;
import com.muqingbfq.mq.gj;

import java.util.List;

public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @SuppressLint("StaticFieldLeak")
    public static Context appCompatActivity;

    @SuppressLint({"CommitTransaction", "ObsoleteSdkInt"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        appCompatActivity = this;
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
            chb.setNavigationItemSelectedListener(this);
            new com.muqingbfq.fragment.sz(this, chb.getHeaderView(0));
            //初始化播放器组件
            if (mediaBrowser == null) {
                mediaBrowser = new MediaBrowserCompat(this,
                        new ComponentName(this, bfqkz.class),
                        connectionCallbacks,
                        null);
                mediaBrowser.connect();
            }
            //检测更新
            new gj.jianchagengxin(this);
        } catch (Exception e) {
            yc.tc(this, e);
        }
    }

    public static MediaBrowserCompat mediaBrowser;
    private MediaBrowserCompat.ConnectionCallback connectionCallbacks =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    // 已连接到服务，可以开始浏览媒体内容
                    // 订阅媒体内容
                    mediaBrowser.subscribe("mediaId", subscriptionCallback);
                    // 请求当前播放状态
                    MediaControllerCompat mediaController = new MediaControllerCompat(home.this, mediaBrowser.getSessionToken());
                    MediaControllerCompat.TransportControls transportControls = mediaController.getTransportControls();
                    // 执行媒体控制操作
                    transportControls.play();
                }

                @Override
                public void onConnectionSuspended() {
                    // 与服务的连接暂停或断开
                    // 取消订阅媒体内容
                    mediaBrowser.unsubscribe("mediaId", subscriptionCallback);
                    // 清除播放状态
                    MediaControllerCompat mediaController = new MediaControllerCompat(home.this, mediaBrowser.getSessionToken());
                    MediaControllerCompat.TransportControls transportControls = mediaController.getTransportControls();
                    // 执行媒体控制操作
                    transportControls.stop();
                }

                @Override
                public void onConnectionFailed() {
                    // 与服务的连接失败
                    // 尝试重新连接服务或显示错误信息等
                }
            };
    private MediaBrowserCompat.SubscriptionCallback subscriptionCallback =
            new MediaBrowserCompat.SubscriptionCallback() {
                @Override
                public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
                    // 媒体内容加载完成
                    // 处理每个媒体项
                    // ...
                }

                @Override
                public void onError(@NonNull String parentId) {
                    // 媒体内容加载失败
                    // 处理加载失败的情况
                }
            };

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

    private long time;
    @SuppressLint("StaticFieldLeak")
    public static com.muqingbfq.fragment.bfq_db bfq_db = new bfq_db();
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
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        com.muqingbfq.fragment.sz.switch_sz(this, item.getItemId());
        return false;
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Media.view = null;
    }
}