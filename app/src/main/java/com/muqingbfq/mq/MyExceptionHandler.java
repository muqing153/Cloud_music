package com.muqingbfq.mq;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.muqingbfq.yc;

public class MyExceptionHandler implements Thread.UncaughtExceptionHandler {
    public static Throwable throwable;
    private Context mContext;
    public MyExceptionHandler(Context context) {
        mContext = context;
    }
    @Override
    public void uncaughtException(@NonNull Thread thread, @NonNull Throwable throwable) {
        // 将异常信息打印到日志中
        MyExceptionHandler.throwable = throwable;
        String TAG = "MyExceptionHandler";
        Log.e(TAG, "UncaughtException: ", throwable);
        // 在这里执行生成错误报告的逻辑，例如将错误信息保存到文件或发送给服务器
        // 可以使用第三方库，如ACRA、Bugsnag等，或者自行实现错误报告的处理逻辑
        // 生成错误报告的逻辑
        // 发送 Intent 重启应用
        yc.start(mContext, throwable);
        // 这里可以进行一些其他的操作，例如记录错误日志、弹出错误提示框等
        // 终止程序
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(0);
    }
}
