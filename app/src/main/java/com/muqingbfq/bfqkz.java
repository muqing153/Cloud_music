package com.muqingbfq;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

import com.muqingbfq.api.url;
import com.muqingbfq.mq.gj;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class bfqkz extends MediaBrowserServiceCompat {
    public static com.muqingbfq.MediaPlayer mt = new com.muqingbfq.MediaPlayer();
    public static String id;
    public static List<xm> list = new ArrayList<>();
    public static long tdt_max, tdt_wz;
    public static int ms;
    //    0 循环 1 顺序 2 随机
    public static xm xm;
    public static boolean like_bool;
    @SuppressLint("StaticFieldLeak")
    public static com.muqingbfq.mq.NotificationManagerCompat notify;
    public static int getmti(int s) {
        int i = bfqkz.list.indexOf(xm);
        if (s == 1) {
            i = bfqkz.list.indexOf(xm) + 1;
            if (i >= bfqkz.list.size()) {
                i = 0;
            }
        } else if (s == 2) {
            i = new Random().nextInt(bfqkz.list.size());
        }
        return i;
    }

    @SuppressLint("NotifyDataSetChanged")
    public static void mp3(String id) {
        try {
            if (id == null) {
                return;
            }
            if (xm.picurl == null || xm.picurl.equals("")) {
                xm.picurl = url.picurl(xm.id);
            }
            mt.reset();
            mt.setDataSource(id);
        } catch (Exception e) {
            gj.sc("bfqkz mp3(String id) :" + e);
        }
    }
    public static MediaSessionCompat mSession;

    @Override
    public void onCreate() {
        super.onCreate();
        mSession = new MediaSessionCompat(this,"MusicService");
        mSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                mt.start();
                // 处理播放音乐逻辑
            }

            @Override
            public void onPause() {
                // 处理暂停音乐逻辑
                mt.pause();
            }

            @Override
            public void onSkipToNext() {
                // 处理切换到下一首音乐逻辑
            }
            @Override
            public void onSkipToPrevious() {
                // 处理切换到上一首音乐逻辑
            }
        });//设置回调
/*        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(new ComponentName(this, start.class));//用ComponentName得到class对象
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);// 关键的一步，设置启动模式，两种情况
        MediaButtonReceiver.handleIntent(mSession,intent);*/

        MediaMetadataCompat build = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, "歌手名称")
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, "专辑名称")
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, "歌曲名称")
                .build();
        mSession.setMetadata(build);
        mSession.setActive(true);
        setSessionToken(mSession.getSessionToken());
        notify = new com.muqingbfq.mq.NotificationManagerCompat(this);
/*        ;
// 激活MediaSessionCompat
        */
        // 初始化通知栏
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        return null;
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {

    }

    public static void updateNotification() {
        try {
            // 更新通知栏的播放状态
            if (notify.notificationBuilder != null) {
                notify.tzl_an();
            }
        } catch (Exception e) {
            gj.sc("bfqkz updateNotification:" + e);
        }
    }
}