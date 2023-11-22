package com.muqingbfq;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class yc extends AppCompatActivity {
    public static Object exception;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yc);
        TextView text = findViewById(R.id.text);

        String deviceModel = Build.MODEL;
        String deviceManufacturer = Build.MANUFACTURER;
        String osVersion = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        float density = metrics.density;
        int densityDpi = metrics.densityDpi;
// 假设你已经获取到了手机信息，保存在相应的变量中
        String wb = "设备型号：" + deviceModel + "\n"
                + "制造商：" + deviceManufacturer + "\n"
                + "设备ID：" + deviceId + "\n"
                + "操作系统版本：" + osVersion + "\n"
                + "SDK版本：" + sdkVersion + "\n"
                + "屏幕尺寸：" + widthPixels + "x" + heightPixels + "\n"
                + "屏幕密度：" + density + "\n"
                + "密度DPI：" + densityDpi + "\n" +
                "异常信息： " + exception.toString();
        text.setText(wb);
    }

    public static void start(Object e) {
        start(home.appCompatActivity, e);
    }

    public static void start(Context context, Object e) {
        yc.exception = e;
        context.startActivity(new Intent(context, yc.class));
    }

    public static void tc(Context context, Object exception) {
        new MaterialAlertDialogBuilder(context)
                .setTitle("不是特别重要的警告")
                .setMessage(exception.toString())
                .setNegativeButton("无视", null)
                .setPositiveButton("分享", null)
                .show();
    }
}
