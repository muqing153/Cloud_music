package com.muqingbfq.api;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.muqingbfq.MP3;
import com.muqingbfq.R;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FileDownloader {
    private static final String CHANNEL_ID = "download_channel";
    private static final int NOTIFICATION_ID = 3;

    public static void downloadFile(Context context, String url, MP3 x) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        // 创建通知渠道（仅适用于Android 8.0及以上版本）
        createNotificationChannel(context);
        // 发起请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // 下载失败处理
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    // 下载失败处理
                    return;
                }

                File outputFile = new File(wj.mp3, "nihao");
                File parentFile = outputFile.getParentFile();
                if (!parentFile.isDirectory()) {
                    parentFile.mkdirs();
                }
                InputStream inputStream = null;
                FileOutputStream outputStream = null;
                try {
                    byte[] buffer = new byte[4096];
                    long fileSize = response.body().contentLength();
                    long fileSizeDownloaded = 0;

                    inputStream = response.body().byteStream();
                    outputStream = new FileOutputStream(outputFile);

                    int read;
                    while ((read = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, read);
                        fileSizeDownloaded += read;
                        // 更新通知栏进度
                        updateNotificationProgress(context, fileSize, fileSizeDownloaded);
                    }
                    try {
                        Mp3File mp3file = new Mp3File(outputFile);
                        if (mp3file.hasId3v2Tag()) {
                            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                            // 设置新的ID值
                            id3v2Tag.setTitle(x.name);
                            id3v2Tag.setArtist(x.zz);
                            id3v2Tag.setAlbum(x.zz);
                            id3v2Tag.setUrl(x.picurl.toString());
                            mp3file.save(wj.mp3 + x.id);
                            outputFile.delete();
                        }
                        // 保存修改后的音乐文件，删除原来的文件
                    } catch (Exception e) {
                        gj.sc(e);
                    }
                    // 下载完成处理
                } catch (IOException e) {
                    e.printStackTrace();
                    // 下载失败处理
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                    notificationManager.cancel(NOTIFICATION_ID);
                }
            }
        });
    }

    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Download Channel";
            String description = "Channel for file download";
            int importance = NotificationManager.IMPORTANCE_LOW;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setShowBadge(false);
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private static void updateNotificationProgress(Context context, long fileSize,
                                                   long fileSizeDownloaded) {
        int progress = (int) ((fileSizeDownloaded * 100) / fileSize);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("Downloading File")
                .setContentText(progress + "% downloaded")
                .setProgress(100, progress, false)
                .setOngoing(true)
                .setOnlyAlertOnce(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
