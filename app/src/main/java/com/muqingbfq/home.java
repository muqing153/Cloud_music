package com.muqingbfq;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.LeadingMarginSpan;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.muqingbfq.fragment.bfq_db;
import com.muqingbfq.mq.gj;

public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @SuppressLint("StaticFieldLeak")
    public static Toolbar toolbar;
    public static AppCompatActivity appCompatActivity;

    @SuppressLint("CommitTransaction")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        appCompatActivity = this;
        try {
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            DrawerLayout drawerLayout = findViewById(R.id.chct);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();
            NavigationView chb = findViewById(R.id.chb);
            chb.setNavigationItemSelectedListener(this);
            Menu menu = chb.getMenu();
            for (int i = 0; i < menu.size(); i++) {
                MenuItem item = menu.getItem(i);
                SpannableString spannableString = new SpannableString(item.getTitle());
                spannableString.setSpan(new LeadingMarginSpan.Standard(
                        26, 26), 0, spannableString.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                item.setTitle(spannableString);
            }
            new com.muqingbfq.fragment.sz(this, chb.getHeaderView(0));

            db = new bfq_db();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.bfq_db, db).commit();
            mediaBrowser = new MediaBrowserCompat(this,
                    new ComponentName(this, bfqkz.class), connectionCallbacks, null);
            mediaBrowser.connect();
        } catch (Exception e) {
            gj.sc(e);
        }
    }

    private MediaBrowserCompat mediaBrowser;
    private final MediaBrowserCompat.ConnectionCallback connectionCallbacks =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    // 连接成功后执行的操作
                    MediaControllerCompat mediaController;
                    try {
                        mediaController = new MediaControllerCompat(home.this,
                                mediaBrowser.getSessionToken());
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    MediaControllerCompat.setMediaController(home.this, mediaController);

                }

                @Override
                public void onConnectionSuspended() {
                    // 连接暂停时执行的操作
//                    gj.ts(home.this,"zangting");
                }

                @Override
                public void onConnectionFailed() {
                    // 连接失败时执行的操作
//                    gj.ts(home.this,"shibai");
                }
            };

    @SuppressLint("StaticFieldLeak")
    public static bfq_db db;

    @Override
    protected void onPause() {
        super.onPause();
        // 保存列表数据
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonList = gson.toJson(bfqkz.list); // 将列表数据转换为 JSON 字符串
        editor.putString("listData", jsonList); // 保存 JSON 字符串到 SharedPreferences
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaBrowser.disconnect();
    }
    @Override
    public void onResume() {
        super.onResume();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void onStop() {
        super.onStop();
        // (see "stay in sync with the MediaSession")
        if (MediaControllerCompat.getMediaController(home.this) != null) {
//            MediaControllerCompat.getMediaController(home.this).unregisterCallback(controllerCallback);
        }
        mediaBrowser.disconnect();
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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        com.muqingbfq.fragment.sz.switch_sz(this, item.getItemId());
        return false;
    }

}