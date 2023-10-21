package com.muqingbfq.mq;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.muqingbfq.main;
import com.muqingbfq.yc;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;

public class gj {
    public static void ts(Context a, Object b) {
        Toast.makeText(a, b.toString(), Toast.LENGTH_SHORT).show();
    }

    public static void xcts(Context context, Object b) {
        main.handler.post(() -> Toast.makeText(context, b.toString(), Toast.LENGTH_SHORT).show());
    }

    public static void sc(Object a) {
        if (a == null) {
            a = "null";
        }
        Log.d("云音乐", String.valueOf(a));
    }

    public static void llq(Context context, String str) {
        context.startActivity(new Intent(context, llq.class).putExtra("url", str));
    }

    public static void fx(Context context, String str) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, str);
        context.startActivity(shareIntent);
    }

    public static int isDarkTheme(Context context) {
        return context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
//        return flag == Configuration.UI_MODE_NIGHT_YES;
    }

    public static boolean isWiFiConnected() {
        try {
            for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (networkInterface.isUp() && !networkInterface.isLoopback()) {
                    if (networkInterface.getDisplayName().contains("wlan")) {
                        return true;  // Wi-Fi网络
                    } else if (networkInterface.getDisplayName().contains("rmnet")) {
                        return false;  // 流量网络
                    }
                }
            }
        } catch (SocketException e) {
            yc.start(main.context, e);
        }
        return false;  // 默认为流量网络
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int cornerRadius) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

}
