package com.muqingbfq;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.muqingbfq.api.url;
import com.muqingbfq.fragment.bfq_db;
import com.muqingbfq.fragment.mp3;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MediaPlayer extends android.media.MediaPlayer {
    public MediaPlayer() {
        this.setOnCompletionListener(mediaPlayer -> {
/*            if (!home.db.view.isShown()) {
                home.db.view.setVisibility(View.VISIBLE);
            }*/
            int i = bfqkz.getmti(bfqkz.ms);
            bfqkz.xm = bfqkz.list.get(i);
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    bfqkz.mp3(url.hq(bfqkz.xm));
                }
            }.start();
        });
        this.setOnErrorListener((mediaPlayer, i, i1) -> {
            bfqkz.list.remove(bfqkz.xm);
            return false;
        });
        resumeTimer();
    }

    @Override
    public void pause() throws IllegalStateException {
        if (isPlaying()) {
            super.pause();
            //暂停
            if (bfq.kg != null) {
                bfq.kg.setImageResource(R.drawable.zt);
            }
            bfq_db.txa.setImageResource(R.drawable.zt);
            bfqkz.updateNotification();
        }
    }

    public Timer timer;
    public TimerTask timerTask;
    public void pauseTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }
    public void resumeTimer() {
        timer = new Timer();//定时器
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (bfqkz.mt.isPlaying() && bfq.getVisibility()) {
                    int currentPosition = bfqkz.mt.getCurrentPosition();
                    bfq.tdt.setProgress(currentPosition);
                    bfq.lrcView.updateTime(currentPosition, true);
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 500);
    }

    @Override
    public void start() throws IllegalStateException {
        super.start();
        if (bfqkz.xm == null) {
            bfq_an.xyq();
            return;
        }
        //开始
        if (bfq.kg != null) {
            bfq.kg.setImageResource(R.drawable.bf);
        }
        bfq_db.txa.setImageResource(R.drawable.bf);
        bfqkz.updateNotification();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void setDataSource(String path) throws IOException, IllegalArgumentException, IllegalStateException, SecurityException {
        super.setDataSource(path);
        prepare();
        bfqkz.tdt_max = getDuration();
        bfqkz.tdt_wz = getCurrentPosition();
        Glide.with(home.appCompatActivity)
                .asBitmap()
                .load(bfqkz.xm.picurl)
                .addListener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                @NonNull Target<Bitmap> target, boolean isFirstResource) {
                        bfqkz.notify.setBitmap(null);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(@NonNull Bitmap bitmap, @NonNull Object model, Target<Bitmap> target,
                                                   @NonNull DataSource dataSource, boolean isFirstResource) {
                        bfqkz.notify.setBitmap(bitmap);
                        return false;
                    }
                })
                .submit();
        start();
        main.handler.post(() -> {
            if (bfq.name != null) {
                bfq.tdt.setMax((int) bfqkz.tdt_max);
                bfq.tdt.setProgress((int) bfqkz.tdt_wz);
                bfq.time_a.setText(bfq_an.getTime(bfqkz.tdt_max));
                bfq.name.setText(bfqkz.xm.name);
                bfq.zz.setText(bfqkz.xm.zz);
                bfq_an.islike(bfq.like.getContext());
            }
            bfq_db.name.setText(bfqkz.xm.name);
            bfq_db.zz.setText(bfqkz.xm.zz);
            if (mp3.lbspq != null) {
                mp3.lbspq.notifyDataSetChanged();
            }
        });
    }
}
