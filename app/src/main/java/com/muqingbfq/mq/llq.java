package com.muqingbfq.mq;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.muqingbfq.R;

import java.io.File;

public class llq extends AppCompatActivity {
    WebView web;
    Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llq);
        Intent intent = getIntent();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        web = findViewById(R.id.webview);
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String title = view.getTitle();
                toolbar.setTitle(title);
                toolbar.setSubtitle(url);
                // 在这里获取到了网页的标题
            }
        });
        web.setDownloadListener((url1, userAgent, contentDisposition, mimetype, contentLength) -> {
            String size = "0B";
            if (contentLength > 0) {
                final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
                int digitGroups = (int) (Math.log10(contentLength) / Math.log10(1024));
                size = String.format("%.1f %s", contentLength
                        / Math.pow(1024, digitGroups), units[digitGroups]);
            }
            final String filename = url1.substring(url1.lastIndexOf('/') + 1);
            new MaterialAlertDialogBuilder(llq.this)
                    .setTitle(filename)
                    .setMessage("文件链接：" + url1 +
                            "\n文件大小：" + size)
                    .setNegativeButton("取消", null)
                    .setNegativeButton("下载", (dialogInterface, i) -> {
                        // 检查权限
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                                ContextCompat.checkSelfPermission(llq.this,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        != PackageManager.PERMISSION_GRANTED) {
                            // 如果没有写入存储的权限，则请求权限
                            ActivityCompat.requestPermissions(llq.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    1);
                        } else {
                            // 执行文件下载操作
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url1));
                            // 设置下载保存路径和文件名
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                            // 允许使用的网络类型，手机、WIFI
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                            // 设置通知栏标题和描述信息
                            request.setTitle(filename);
                            request.setDescription("正在下载");
                            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                            // 注册下载完成的广播接收器
                            long enqueue = downloadManager.enqueue(request);
                            BroadcastReceiver receiver = new BroadcastReceiver() {
                                @Override
                                public void onReceive(Context context, Intent intent) {
                                    if (filename.endsWith(".apk")) {
                                        // 打开安装界面
                                        Cursor cursor = downloadManager.query(
                                                new DownloadManager.Query().setFilterById(enqueue));
                                        if (cursor.moveToFirst()) {
                                            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                                            int status = cursor.getInt(columnIndex);
                                            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                                // 下载成功
                                                columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                                                String localUri = cursor.getString(columnIndex);
                                                if (localUri != null) {
                                                    Uri uri = Uri.parse(localUri);
                                                    String filePath = uri.getPath();
                                                    File file = new File(filePath);
                                                    // 获取下载文件的路径
                                                    Intent installIntent = new Intent(Intent.ACTION_VIEW);
                                                    installIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                                                    installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                    startActivity(installIntent);
                                                }
                                            }
                                        }
                                    }
                                }
                            };
                            registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                        }
                    }).show();

        });
        final ProgressBar progressBar = findViewById(R.id.webViewProgressBar);
        web.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setProgress(newProgress);
                    if (!progressBar.isShown()) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        loadUrl(intent.getStringExtra("url"));
    }

    private void loadUrl(String url) {
        web.loadUrl(url);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // 权限已授予，执行文件下载操作
            gj.ts(this, "权限已授予，请重新执行文件下载操作");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.llq, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (web.canGoBack()) {
            web.goBack();
        } else {
            finish();
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        } else if (itemId == R.id.fx) {
            gj.fx(this, web.getUrl());
//                    服务中心
        } else if (itemId == R.id.sx) {
            web.reload();
        } else if (itemId == R.id.menu_web) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(web.getUrl())));
        }
        return super.onOptionsItemSelected(item);
    }
}
