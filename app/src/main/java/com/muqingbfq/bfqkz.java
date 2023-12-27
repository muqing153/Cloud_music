package com.muqingbfq;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media.MediaBrowserServiceCompat;

import com.muqingbfq.api.url;
import com.muqingbfq.mq.BluetoothMusicController;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class bfqkz extends MediaBrowserServiceCompat {
    public static MediaPlayer mt = new MediaPlayer();
    public static List<MP3> list = new ArrayList<>();
    public static List<MP3> lishi_list = new ArrayList<>();
    public static int ms;
    //    0 循环 1 顺序 2 随机
    public static MP3 xm;
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
            if (TextUtils.isEmpty(id)) {
                //针对错误进行相应的处理
/*                if (bfqkz.list.size() < 1) {
                    return;
                }
                bfqkz.list.remove(bfqkz.xm);
                bfqkz.xm = bfqkz.list.get(bfqkz.getmti(bfqkz.ms));
                new mp3(id);*/
                return;
            }
            if (TextUtils.isEmpty(xm.picurl)) {
                xm.picurl = url.picurl(xm.id);
            }
            if (bfqkz.lishi_list.size() >= 100) {
                bfqkz.lishi_list.remove(0);
            }
            bfqkz.lishi_list.remove(bfqkz.xm);
            if (!bfqkz.lishi_list.contains(bfqkz.xm)) {
                bfqkz.lishi_list.add(0, bfqkz.xm);
                wj.xrwb(wj.gd + "mp3_hc.json", new com.google.gson.Gson().toJson(bfqkz.lishi_list));
            }
            mt.setDataSource(id);
        } catch (Exception e) {
            yc.start(home.appCompatActivity, "bfqkz mp3(" + id + ") :" + e);
        }
    }

    public static class mp3 extends Thread {
        String id;

        public mp3(String id) {
            this.id = id;
            start();
        }

        @Override
        public void run() {
            super.run();
            mp3(id);
        }
    }


    public static MediaSessionCompat mSession;
    public static PlaybackStateCompat playback;

    @Override
    public void onCreate() {
        super.onCreate();
        com.muqingbfq.api.playlist.hq_hc(bfqkz.lishi_list);
        new BluetoothMusicController(this);
        playback = new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_NONE, 0, 1.0f)
                .build();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(new ComponentName(this, home.class));//用ComponentName得到class对象
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);// 关键的一步，设置启动模式，两种情况
        PendingIntent pendingIntent = com.muqingbfq.mq.NotificationManagerCompat.getActivity(this, intent);
        mSession = new MediaSessionCompat(this, "MusicService",
                home.componentName, pendingIntent);
        mSession.setCallback(new callback());
        mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mSession.setPlaybackState(playback);
//            mSession.setActive(true);
        setSessionToken(mSession.getSessionToken());

        notify = new com.muqingbfq.mq.NotificationManagerCompat(this);
    }

    class callback extends MediaSessionCompat.Callback {
        @Override
        public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
            mediaButtonEvent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            return true;
        }

        @Override
        public void onPlay() {
            super.onPlay();
            if (playback.getState() == PlaybackStateCompat.STATE_PAUSED) {
                mt.start();
                playback = new PlaybackStateCompat.Builder()
                        .setState(PlaybackStateCompat.STATE_PLAYING, 0, 1.0f)
                        .build();
                mSession.setPlaybackState(playback);
            }
        }

        @Override
        public void onPause() {
            super.onPause();
            if (playback.getState() == PlaybackStateCompat.STATE_PLAYING) {
                mt.pause();
                playback = new PlaybackStateCompat.Builder()
                        .setState(PlaybackStateCompat.STATE_PAUSED, 0, 1.0f)
                        .build();
                mSession.setPlaybackState(playback);
            }
        }

        @SuppressLint("SwitchIntDef")
        @Override
        public void onPlayFromUri(Uri uri, Bundle extras) {
            try {
                switch (playback.getState()) {
                    case PlaybackStateCompat.STATE_PLAYING:
                    case PlaybackStateCompat.STATE_PAUSED:
                    case PlaybackStateCompat.STATE_NONE:
//                        mp3(uri);/
                        playback = new PlaybackStateCompat.Builder()
                                .setState(PlaybackStateCompat.STATE_CONNECTING, 0, 1.0f)
                                .build();
                        mSession.setPlaybackState(playback);
                        //我们可以保存当前播放音乐的信息，以便客户端刷新UI
                        mSession.setMetadata(new MediaMetadataCompat.Builder()
                                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, extras.getString("title"))
                                .build()
                        );
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        return null;
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {

    }
/*

    public class MyBinder extends Binder {
        bfqkz getService() {
            return bfqkz.this;
        }
    }
*/

    public static void updateNotification() {
        try {
            // 更新通知栏的播放状态
            if (bfqkz.notify != null && notify.notificationBuilder != null) {
                notify.tzl();
            }
        } catch (Exception e) {
            gj.sc("bfqkz updateNotification:" + e);
        }
    }
}