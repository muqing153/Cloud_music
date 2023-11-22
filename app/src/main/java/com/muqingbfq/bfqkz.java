package com.muqingbfq;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

import com.muqingbfq.api.url;
import com.muqingbfq.mq.BluetoothMusicController;
import com.muqingbfq.mq.gj;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class bfqkz extends MediaBrowserServiceCompat {
    public static com.muqingbfq.MediaPlayer mt;
    public static List<xm> list = new ArrayList<>();
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
            mt.setDataSource(id);
        } catch (Exception e) {
            yc.start(home.appCompatActivity, "bfqkz mp3(" + id + ") :" + e);
        }
    }

    public static MediaSessionCompat mSession;
    public static MediaMetadataCompat build;
    public static PlaybackStateCompat playback;
    @Override
    public void onCreate() {
        super.onCreate();
        if (mt == null) {
            mt = new com.muqingbfq.MediaPlayer();
            new BluetoothMusicController(this);
        }
        mSession = new MediaSessionCompat(this, "MusicService");
        playback=new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_NONE,0,1.0f)
                .build();
/*        mSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                mt.start();
            }
            @Override
            public void onPause() {
                // 处理暂停音乐逻辑
                mt.pause();
                if(playback.getState() == PlaybackStateCompat.STATE_PLAYING){
                    playback = new PlaybackStateCompat.Builder()
                            .setState(PlaybackStateCompat.STATE_PAUSED,0,1.0f)
                            .build();
                    mSession.setPlaybackState(playback);
                }
            }

            @Override
            public void onSkipToNext() {
                // 处理切换到下一首音乐逻辑
            }

            @Override
            public void onSkipToPrevious() {
                // 处理切换到上一首音乐逻辑
            }
        });*/
        build = new MediaMetadataCompat.Builder()
                .build();
        mSession.setMetadata(build);
        mSession.setPlaybackState(playback);
        mSession.setActive(true);
        setSessionToken(mSession.getSessionToken());
        notify = new com.muqingbfq.mq.NotificationManagerCompat(this);

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