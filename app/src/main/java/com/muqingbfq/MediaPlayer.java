package com.muqingbfq;

import static com.muqingbfq.bfqkz.xm;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.media3.common.MediaItem;
import androidx.media3.common.PlaybackException;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.muqingbfq.fragment.Media;
import com.muqingbfq.fragment.bflb_db;
import com.muqingbfq.fragment.bfq_db;
import com.muqingbfq.fragment.search;
import com.muqingbfq.mq.gj;

public class MediaPlayer {
    public ExoPlayer build;
    // 每秒更新一次进度
    public Runnable updateSeekBar = new Runnable() {
        @Override
        public void run() {
            if (build != null && build.isPlaying() && Media.getlrcView() != null) {
                long position = build.getCurrentPosition();
                Media.setProgress((int) position);
            }
            main.handler.postDelayed(this, 1000); // 每秒更新一次进度
        }
    };

    @SuppressLint("UnsafeOptInUsageError")
    public MediaPlayer() {
        build = new ExoPlayer.Builder(home.appCompatActivity).build();
        build.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(@Player.State int state) {
                switch (state) {
                    case Player.STATE_READY:
                        bfui();
                        break;

                    case Player.STATE_ENDED:
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
                        break;

                    case Player.STATE_BUFFERING:
                    case Player.STATE_IDLE:
                        break;
                }
                if (Media.view != null) {
                    main.handler.removeCallbacks(updateSeekBar); // 在播放开始时启动更新进度
                    long duration = build.getDuration();
                    Media.setMax((int) build.getDuration());
                    Media.setTime_a(bfq_an.getTime(duration));
                    long position = build.getCurrentPosition();
                    Media.setProgress((int) position);
                    main.handler.post(updateSeekBar); // 在播放开始时启动更新进度
                }
                // 在这里将进度更新到UI上
            }


            @Override
            public void onPlayerError(@NonNull PlaybackException error) {
                gj.sc(error);
                bfqkz.list.remove(bfqkz.xm);
                Player.Listener.super.onPlayerError(error);
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

            @Override
            public void onIsPlayingChanged(boolean isPlaying) {
                Media.setbf(isPlaying);
            }
        });
    }

    public void pause(){
        if (build.isPlaying()) {
            build.pause();
        }
    }

    public void start(){
        if (bfqkz.xm == null) {
            if (bfqkz.list != null && bfqkz.list.size() > 0) {
                bfq_an.xyq();
            }
            return;
        }
        build.play();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDataSource(String path) {
        MediaItem mediaItem = MediaItem.fromUri(path);
        main.handler.post(() -> {
            build.setMediaItem(mediaItem);
            build.prepare();
            build.setPlayWhenReady(true);
            start();
        });
    }

    public boolean isPlaying() {
        if (build == null) {
            return false;
        }
        return build.isPlaying();
    }

    public void seekTo(long a) {
        build.seekTo(a);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void bfui() {
        Glide.with(main.application)
                .asBitmap()
                .load(bfqkz.xm.picurl)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                @NonNull Target<Bitmap> target, boolean isFirstResource) {
                        bfq.bitmap = null;
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
        String name = xm.name, zz = bfqkz.xm.zz;
        if (Media.view != null) {
            Media.setProgress(0);
            Media.setname(name);
            Media.setzz(zz);
            bfq_an.islike(Media.view.getContext());
        }
        bfq_db.setname(name);
        bfq_db.setzz(zz);
        if (bfqkz.notify.notificationManager != null) {
            bfqkz.notify.notificationBuilder.setContentTitle(name);
            bfqkz.notify.notificationBuilder.setContentText(zz);
            bfqkz.notify.notificationManager_notify();

        }
        if (com.muqingbfq.fragment.mp3.lbspq != null) {
            com.muqingbfq.fragment.mp3.lbspq.notifyDataSetChanged();
        }
        if (search.lbspq != null) {
            search.lbspq.notifyDataSetChanged();
        }
        if (bflb_db.adapter != null) {
            bflb_db.adapter.notifyDataSetChanged();
            gj.sc(1);
        }
    }
}