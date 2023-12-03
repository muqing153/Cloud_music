package com.muqingbfq.mq;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.media.MediaMetadataCompat;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.muqingbfq.MyButtonClickReceiver;
import com.muqingbfq.R;
import com.muqingbfq.bfq;
import com.muqingbfq.bfqkz;
import com.muqingbfq.fragment.Media;
import com.muqingbfq.home;
import com.muqingbfq.yc;

public class NotificationManagerCompat {
    Service context;
    public NotificationCompat.Builder notificationBuilder;
    public androidx.core.app.NotificationManagerCompat notificationManager;
    private String name, zz;

    public NotificationManagerCompat(Service context) {
        this.context = context;
        name = context.getString(R.string.app_name);
        zz = context.getString(R.string.zz);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name,
                        NotificationManager.IMPORTANCE_LOW);
                channel.setDescription(zz);
                NotificationManager systemService = context.getSystemService(NotificationManager.class);
                systemService.createNotificationChannel(channel);
                if (!systemService.areNotificationsEnabled()) {
                    return;
                }
            }
            // 适配12.0及以上

            // 设置启动的程序，如果存在则找出，否则新的启动
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setComponent(new ComponentName(context, home.class));//用ComponentName得到class对象
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);// 关键的一步，设置启动模式，两种情况
            pendingIntent = getActivity(context, intent);
            Intent my = new Intent(context, MyButtonClickReceiver.class);
            pendingIntent_kg = getBroadcast(context, my.
                    setAction("kg"));
            pendingIntent_syq = getBroadcast(context, my.
                    setAction("syq"));
            pendingIntent_xyq = getBroadcast(context, my.
                    setAction("xyq"));
            // 取消操作的PendingIntent
// 取消操作的PendingIntent
/*            PendingIntent cancelIntent = MediaButtonReceiver.buildMediaButtonPendingIntent(context,
                    PlaybackStateCompat.ACTION_STOP);*/
            style = new androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
                    .setMediaSession(bfqkz.mSession.getSessionToken());
//                            .setShowCancelButton(true)
//                            .setCancelButtonIntent(cancelIntent);
            notificationManager = androidx.core.app.NotificationManagerCompat.from(context);
            notificationBuilder = getNotificationBuilder(context)
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle(name).setContentText(zz)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setOngoing(true).setAutoCancel(false).setOnlyAlertOnce(true)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setContentIntent(pendingIntent)
                    .setStyle(style);
            tzl();
//            context.startForeground(1, notificationBuilder.build());
        } catch (Exception e) {
            yc.start(context, e);
        }
    }

    PendingIntent pendingIntent;
    androidx.media.app.NotificationCompat.MediaStyle style;

    @SuppressLint("RestrictedApi")
    public void tzl() {
        if (bfqkz.xm != null) {
            name = bfqkz.xm.name;
            zz = bfqkz.xm.zz;
        }
        bfqkz.build = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, name)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, zz)
                .build();
        bfqkz.mSession.setMetadata(bfqkz.build);
        notificationBuilder.mActions.clear();
        notificationBuilder.addAction(android.R.drawable.ic_media_previous, "syq", pendingIntent_syq) // #0
                .addAction(bfqkz.mt.isPlaying() ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play
                        , "kg", pendingIntent_kg)  // #1
                .addAction(android.R.drawable.ic_media_next, "xyq", pendingIntent_xyq)
                .setContentTitle(name)
                .setContentText(zz)
                .setOngoing(bfqkz.mt.isPlaying());
        notificationManager_notify();
    }

    private PendingIntent pendingIntent_kg,
            pendingIntent_syq,
            pendingIntent_xyq;
    private final String CHANNEL_ID = "muqing_yy_id";

    public void notificationManager_notify() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, notificationBuilder.build());
    }

    @SuppressLint({"MissingPermission", "RestrictedApi", "NotifyDataSetChanged"})
    public void setBitmap() {
        if (bfq.bitmap == null) {
            bfq.bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.icon);
        }
        if (notificationManager != null) {
            notificationBuilder.setLargeIcon(bfq.bitmap);
            notificationManager.notify(1, notificationBuilder.build());
        }
        Media.setImageBitmap();
    }

    private NotificationCompat.Builder getNotificationBuilder(Context context) {
        // 适用于Android 8.0及以上版本
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        return new NotificationCompat.Builder(context, CHANNEL_ID);
    }

    private PendingIntent getBroadcast(Context context, Intent intent) {
        int flag;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            flag = PendingIntent.FLAG_IMMUTABLE;
        } else {
            flag = PendingIntent.FLAG_UPDATE_CURRENT;
        }
        return PendingIntent.getBroadcast(context, 0, intent, flag);
    }

    private PendingIntent getActivity(Context context, Intent intent) {
        int flag;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            flag = PendingIntent.FLAG_IMMUTABLE;
        } else {
            flag = PendingIntent.FLAG_UPDATE_CURRENT;
        }
        return PendingIntent.getActivity(context, 0, intent, flag);
    }

}
