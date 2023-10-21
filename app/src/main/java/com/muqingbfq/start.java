package com.muqingbfq;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.muqingbfq.login.visitor;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;
import com.muqingbfq.mq.wl;

public class start extends AppCompatActivity {
    public static int ztl, dhl;
    Intent home;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            home = new Intent(this, home.class);
            ztl = getNavigationBarHeight(this);
            dhl = getStatusBarHeight(this);
            DisplayMetrics dm = getResources().getDisplayMetrics();
            main.k = dm.widthPixels;
            main.g = dm.heightPixels;
        } catch (Exception e) {
            yc.start(this, e);
        }
        if (Build.VERSION.SDK_INT >= 33) {
            int checkPermission =
                    ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                //动态申请
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.POST_NOTIFICATIONS}, REQUEST_EXTERNAL_STORAGE);
            } else {
                startApp();
            }
        } else {
            startApp();
        }
//        checkPermission();
    }

    public static long time;

    @Override
    protected void onResume() {
        super.onResume();

    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        @SuppressLint({"InternalInsetResource", "DiscouragedApi"}) int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        @SuppressLint({"InternalInsetResource", "DiscouragedApi"}) int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private AlertDialog dialog;

    @SuppressLint("ObsoleteSdkInt")
    private void checkPermission() {
        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                if (dialog != null) {
                    dialog.dismiss();
                    dialog = null;
                }
                dialog = new AlertDialog.Builder(this).setTitle("提示")//设置标题
                        .setMessage("请开启文件访问权限，否则无法正常使用本应用！").setNegativeButton("取消", (dialog, i) -> dialog.dismiss()).setPositiveButton("确定", (dialog, which) -> {
                            dialog.dismiss();
                            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                            startActivity(intent);
                        }).create();
                dialog.show();
            } else {
                gj.sc("Android 11以上，当前已有权限");
                startApp();
            }
        } else {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    if (dialog != null) {
                        dialog.dismiss();
                        dialog = null;
                    }
                    dialog = new AlertDialog.Builder(this).setTitle("提示")//设置标题
                            .setMessage("请开启文件访问权限，否则无法正常使用本应用！").setPositiveButton("确定", (dialog, which) -> {
                                dialog.dismiss();
                                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                            }).create();
                    dialog.show();
                } else {
                    gj.sc("Android 6.0以上，11以下，当前已有权限");
                    startApp();
                }
            } else {
                gj.sc("Android 6.0以下，已获取权限");
                startApp();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "授权成功！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权被拒绝！", Toast.LENGTH_SHORT).show();
            }
        }
        startApp();
    }

    private void startApp() {

        SharedPreferences theme = getSharedPreferences("theme", MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor edit = theme.edit();
        int i = theme.getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        if (i == -1) {
            edit.putInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
        AppCompatDelegate.setDefaultNightMode(i);

        wl.Cookie = main.sp.getString(main.Cookie, "");
        if (wl.Cookie.equals("")) {
            new visitor(this, home);
        } else {
            if (wj.filesdri == null) {
                new wj(this);
            }
            startActivity(home);
            finish();
        }
    }
}