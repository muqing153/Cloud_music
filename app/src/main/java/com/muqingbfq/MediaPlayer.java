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
            if (isPlaying() && bfq.lrcView != null) {
                long position = getCurrentPosition();
                Media.setProgress((int) position);
            }
            main.handler.postDelayed(this, 1000); // 每秒更新一次进度
        }
    };

    @SuppressLint("UnsafeOptInUsageError")
    public MediaPlayer() {
        setOnErrorListener((mediaPlayer, i, i1) -> {
            if (bfqkz.list.isEmpty()) {
                return false;
            }
            //针对错误进行相应的处理
            bfqkz.list.remove(bfqkz.xm);
            bfqkz.xm = bfqkz.list.get(bfqkz.getmti(bfqkz.ms));
            new bfqkz.mp3(com.muqingbfq.api.
                            url.hq(bfqkz.xm));
            return false;
        });
        setOnCompletionListener(mediaPlayer -> {
            if (bfqkz.list.isEmpty()) {
                return;
            }
            bfq_an.xyq();
        });
        setAudioAttributes(new AudioAttributes
                .Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build());
//        main.handler.post(updateSeekBar); // 在播放开始时启动更新进度
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
            if (bfq.view != null) {
                main.handler.removeCallbacks(updateSeekBar); // 在播放开始时启动更新进度
                long duration = getDuration();
                Media.setMax(getDuration());
                Media.setTime_a(bfq_an.getTime(duration));
                long position = getCurrentPosition();
                Media.setProgress((int) position);
                main.handler.post(updateSeekBar); // 在播放开始时启动更新进度
            }
            // 在这里将进度更新到UI上
        });
        new Thread(){
            @Override
            public void run() {
                super.run();
                if (bfqkz.lishi_list.size() >= 100) {
                    bfqkz.lishi_list.remove(0);
                }
                bfqkz.lishi_list.remove(bfqkz.xm);
                if (!bfqkz.lishi_list.contains(bfqkz.xm)) {
                    bfqkz.lishi_list.add(0, bfqkz.xm);
                    wj.xrwb(wj.gd + "mp3_hc.json", new com.google.gson.Gson().toJson(bfqkz.lishi_list));
                }
                wj.setMP3ToFile(bfqkz.xm);
            }
        }.start();
    }

    public void DataSource(String path) throws Exception {
        reset();
        super.setDataSource(path);
        prepare();
        setTX();
    }

    public void setTX() {
        Glide.with(main.application)
                .asBitmap()
                .load(bfqkz.xm.picurl)
                .error(R.drawable.icon)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                @NonNull Target<Bitmap> target,
                                                boolean isFirstResource) {
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
                            gj.sc("yc:"+a);
                        }
                        if (bfqkz.notify != null) {
                            bfqkz.notify.tzl();
                        }
                        Media.setImageBitmap();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(@NonNull Bitmap bitmap, @NonNull Object model, Target<Bitmap> target,
                                                   @NonNull DataSource dataSource,
                                                   boolean isFirstResource) {
                        bfq.bitmap = bitmap;
                        if (bfqkz.notify != null) {
                            bfqkz.notify.tzl();
                        }
                        Media.setImageBitmap();
                        return false;
                    }
                })
                .submit();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void bfui() {
        String name = xm.name, zz = bfqkz.xm.zz;
        setTX();
        if (bfq.view != null) {
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