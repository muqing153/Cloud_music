package com.muqingbfq;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.media.MediaBrowserCompat;
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

    public MediaBrowserCompat mBrowser;

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
            if (componentName == null) {
                componentName = new ComponentName(getApplicationContext(), bfqkz.class);
                mBrowser = new MediaBrowserCompat(
                        getApplicationContext(), componentName
                        ,//绑定服务端
                        browserConnectionCallback,//设置连接回调
                        null
                );
                mBrowser.connect();
            }
            //检测更新
            new gj.jianchagengxin(this);
            binding.editView.setOnClickListener(view ->
                    startActivity(new Intent(this, activity_search.class)));
            UI();
//            startService(new Intent(this, FloatingLyricsService.class));
        } catch (Exception e) {
            yc.tc(this, e);
        }
    }

    public static ComponentName componentName;
    private Adaper adapter;

    private class Adaper extends FragmentStateAdapter {
        List<Fragment> list = new ArrayList<>();

        public Adaper(@NonNull FragmentActivity fragmentActivity) {
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
        adapter = new Adaper(this);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setSaveEnabled(false);
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
                                wode fragment = (wode) adapter.createFragment(position);
                                fragment.sx();
                            }
                        }.start();
                        break;
                }
            }
        });
    }
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


    /**
     * 连接状态的回调接口，连接成功时会调用onConnected()方法
     */
    private final MediaBrowserCompat.ConnectionCallback browserConnectionCallback =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    //必须在确保连接成功的前提下执行订阅的操作
                    if (mBrowser.isConnected()) {
                        //mediaId即为MediaBrowserService.onGetRoot的返回值
                        //若Service允许客户端连接，则返回结果不为null，其值为数据内容层次结构的根ID
                        //若拒绝连接，则返回null
                        String mediaId = mBrowser.getRoot();

                        //Browser通过订阅的方式向Service请求数据，发起订阅请求需要两个参数，其一为mediaId
                        //而如果该mediaId已经被其他Browser实例订阅，则需要在订阅之前取消mediaId的订阅者
                        //虽然订阅一个 已被订阅的mediaId 时会取代原Browser的订阅回调，但却无法触发onChildrenLoaded回调

                        //ps：虽然基本的概念是这样的，但是Google在官方demo中有这么一段注释...
                        // This is temporary: A bug is being fixed that will make subscribe
                        // consistently call onChildrenLoaded initially, no matter if it is replacing an existing
                        // subscriber or not. Currently this only happens if the mediaID has no previous
                        // subscriber or if the media content changes on the service side, so we need to
                        // unsubscribe first.
                        //大概的意思就是现在这里还有BUG，即只要发送订阅请求就会触发onChildrenLoaded回调
                        //所以无论怎样我们发起订阅请求之前都需要先取消订阅
                        mBrowser.unsubscribe(mediaId);
                        //之前说到订阅的方法还需要一个参数，即设置订阅回调SubscriptionCallback
                        //当Service获取数据后会将数据发送回来，此时会触发SubscriptionCallback.onChildrenLoaded回调
                        mBrowser.subscribe(mediaId, browserSubscriptionCallback);
                    }
                    gj.sc("连接成功");
                }

                @Override
                public void onConnectionFailed() {
                    gj.sc("连接失败！");
                }
            };
    /**
     * 向媒体服务器(MediaBrowserService)发起数据订阅请求的回调接口
     */
    private final MediaBrowserCompat.SubscriptionCallback browserSubscriptionCallback =
            new MediaBrowserCompat.SubscriptionCallback() {
                @Override
                public void onChildrenLoaded(@NonNull String parentId,
                                             @NonNull List<MediaBrowserCompat.MediaItem> children) {

                    gj.sc("onChildrenLoaded------");
                }
            };

    @Override
    public void finish() {
        super.finish();
        // 断开连接并释放资源
        if (mBrowser != null && mBrowser.isConnected()) {
            mBrowser.disconnect();
        }
        int i = Process.myPid();
        android.os.Process.killProcess(i);
    }
}