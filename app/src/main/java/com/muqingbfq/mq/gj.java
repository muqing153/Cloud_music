package com.muqingbfq.mq;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.muqingbfq.main;
import com.muqingbfq.yc;

import org.json.JSONObject;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;

public class gj {
    public static void ts(Context a, Object b) {
        Toast.makeText(a, b.toString(), Toast.LENGTH_SHORT).show();
    }

    public static void xcts(Context context, Object b) {
        main.handler.post(() -> Toast.makeText(context, b.toString(), Toast.LENGTH_SHORT).show());

    }

    public static boolean isAppInForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Service.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = activityManager.getRunningAppProcesses();

        if (runningAppProcessInfoList == null) {
            Log.d("runningAppProcess:", "runningAppProcessInfoList is null!");
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcessInfoList) {
            if (processInfo.processName.equals(context.getPackageName()) && (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTablet(Context context) {
        boolean b;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        main.k = dm.widthPixels;
        main.g = dm.heightPixels;
        b = main.k > main.g;
        return b;
    }

    public static void sc(Object a) {
        if (a == null) {
            a = "null";
        }
        Log.d("云音乐", a.toString());
        floating.addtext(a.toString());
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

    /**
     * 复制文字到剪切板
     *
     */
    public static void fz(Context context, String text) {
        ClipboardManager systemService =
                (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建能够存入剪贴板的ClipData对象
        //‘Label’这是任意文字标签
        ClipData mClipData = ClipData.newPlainText("Label", text);
        //将ClipData数据复制到剪贴板：
        systemService.setPrimaryClip(mClipData);
        gj.ts(context, "复制成功");
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
            yc.start(e);
        }
        return false;  // 默认为流量网络
    }

    public static class jianchagengxin extends Thread {
        Context context;

        public jianchagengxin(Context context) {
            this.context = context;
            if (!wj.cz(wj.filesdri + "gx.mq")) {
                start();
            }
        }

        @Override
        public void run() {
            super.run();
            jianchagengxin(context);
        }
    }

    public static int jianchagengxin(Context context) {
        try {
            String versionName = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
            String hq = wl.get(main.http + "/muqingbfq?bb=" + versionName);
            final JSONObject jsonObject = new JSONObject(hq);
            boolean code = jsonObject.getInt("code") == 1;
            String msg = jsonObject.getString("msg");
            if (code) {
                String url = jsonObject.getString("url");
                String bb = jsonObject.getString("bb");
                main.handler.post(() -> new MaterialAlertDialogBuilder(context)
                        .setTitle("更新" + bb)
                        .setMessage(msg + "\n" + "取消后不再提示更新你需要到关于软件手动检测")
                        .setNegativeButton("取消", (dialogInterface, i) -> wj.xrwb(wj.filesdri + "gx.mq", null))
                        .setPositiveButton("更新", (dialogInterface, i) -> context.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse(url))))
//                                    new ApkDownloader(context).downloadAndInstall(url, wj.filesdri))
                        .show());
            }
            //1表示需要更新
            return code ? 1 : 0;
        } catch (Exception e) {
            sc(e);
        }
        return 400;
    }

    public static void tcjp(EditText editText) {
        editText.requestFocus();//获取焦点
        InputMethodManager imm = (InputMethodManager)
                editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        gj.sc(imm.isActive());
        //没有显示键盘，弹出
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }
    public static void ycjp(EditText editText) {
        InputMethodManager imm = (InputMethodManager)
                editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) //有显示键盘，隐藏
            imm.hideSoftInputFromWindow(editText.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static int getztl(Context context) {
        // 获得状态栏高度
        @SuppressLint({"InternalInsetResource", "DiscouragedApi"}) int resourceId =
                context.getResources().
                getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }
}