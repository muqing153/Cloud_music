package com.muqingbfq;

import static com.muqingbfq.bfqkz.xm;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import com.muqingbfq.fragment.Media;
import com.muqingbfq.fragment.bflb_db;
import com.muqingbfq.fragment.bfq_db;
import com.muqingbfq.fragment.search;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;

import java.io.IOException;

public class MediaPlayer extends android.media.MediaPlayer {
    // 每秒更新一次进度
    public Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            if (isPlaying() && Media.lrcview != null) {
                long position = getCurrentPosition();
                Media.setProgress((int) position);
            }
            main.handler.postDelayed(this, 1000); // 每秒更新一次进度
        }
    };

    @SuppressLint("UnsafeOptInUsageError")
    public MediaPlayer() {
        setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(android.media.MediaPlayer mediaPlayer, int i, int i1) {
                //针对错误进行相应的处理
                bfqkz.list.remove(bfqkz.xm);
                bfqkz.xm = bfqkz.list.get(bfqkz.getmti(bfqkz.ms));
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        bfqkz.mp3(com.muqingbfq.api.
                                url.hq(bfqkz.xm));
                    }
                }.start();
                return false;
            }
        });
        setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(android.media.MediaPlayer mediaPlayer) {
                int i = bfqkz.getmti(bfqkz.ms);
                bfqkz.xm = bfqkz.list.get(i);
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        bfqkz.mp3(com.muqingbfq.api.
                                url.hq(bfqkz.xm));
                    }
                }.start();
            }
        });
        setAudioAttributes(new AudioAttributes
                .Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());
        main.handler.post(updateSeekBar); // 在播放开始时启动更新进度
    }

    @Override
    public void pause() throws IllegalStateException {
        if (isPlaying()) {
            super.pause();
            Media.setbf(false);
        }
    }

    @Override
    public void start() throws IllegalStateException {
        if (bfqkz.xm == null) {
            if (bfqkz.list != null && bfqkz.list.size() > 0) {
                bfq_an.xyq();
            }
            return;
        }
        super.start();
        Media.setbf(true);
    }

    // 创建 MediaItem 列表
//    public static List<MediaItem> list = new ArrayList<>();
    @Override
    public void setDataSource(String path) throws IOException {
        reset();
        super.setDataSource(path);
        prepare();
        start();
        main.handler.post(() -> {
            bfui();
            if (bfq.inflate != null) {
                main.handler.removeCallbacks(updateSeekBar); // 在播放开始时启动更新进度
                long duration = getDuration();
                Media.setMax((int) getDuration());
                Media.setTime_a(bfq_an.getTime(duration));
                long position = getCurrentPosition();
                Media.setProgress((int) position);
                main.handler.post(updateSeekBar); // 在播放开始时启动更新进度
            }
            // 在这里将进度更新到UI上
        });
    }

    public void DataSource(String path) throws Exception {
        reset();
        super.setDataSource(path);
        prepare();
    }

    public void setTX() {
        Glide.with(main.application)
                .asBitmap()
                .load(bfqkz.xm.picurl)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                @NonNull Target<Bitmap> target, boolean isFirstResource) {
                        bfq.bitmap = null;
                        try {
                            Mp3File mp3file = new Mp3File(wj.mp3 + bfqkz.xm.id);
                            if (mp3file.hasId3v2Tag()) {
                                ID3v2 id3v2Tag = mp3file.getId3v2Tag();
                                byte[] albumImage = id3v2Tag.getAlbumImage();
                                bfq.bitmap=
                                        BitmapFactory.decodeByteArray(albumImage, 0, albumImage.length);
                            }
                        } catch (Exception a) {
                            gj.sc(a);
                        }
                        bfqkz.notify.setBitmap();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Bitmap bitmap, @NonNull Object model, Target<Bitmap> target,
                                                   @NonNull DataSource dataSource, boolean isFirstResource) {
                        bfq.bitmap = bitmap;
                        bfqkz.notify.setBitmap();
                        return false;
                    }
                })
                .submit();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void bfui() {
        setTX();
        String name = xm.name, zz = bfqkz.xm.zz;
        if (bfq.inflate != null) {
            Media.setProgress(0);
            bfq.setname(name);
            bfq.setzz(zz);
            bfq_an.islike();
        }
        bfq_db.setname(name + "/" + zz);
        if (com.muqingbfq.fragment.mp3.lbspq != null) {
            com.muqingbfq.fragment.mp3.lbspq.notifyDataSetChanged();
        }
        if (search.lbspq != null) {
            search.lbspq.notifyDataSetChanged();
        }
        if (bflb_db.adapter != null) {
            bflb_db.adapter.notifyDataSetChanged();
        }
    }
}