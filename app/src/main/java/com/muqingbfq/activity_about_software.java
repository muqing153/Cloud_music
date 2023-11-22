package com.muqingbfq;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;

public class activity_about_software extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_software);
        Toolbar toolbar = findViewById(R.id.toolbar);
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            toolbar.setSubtitle(versionName + " Bate");
        } catch (PackageManager.NameNotFoundException e) {
            yc.start(this, e);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.button1).setOnClickListener(view -> {
            wj.sc(wj.filesdri + "gx.mq");
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    int jianchagengxin = gj.jianchagengxin(activity_about_software.this);
                    if (jianchagengxin == 400) {
                        gj.xcts(activity_about_software.this, "无网络");
                    } else if (jianchagengxin == 0) {
                        gj.xcts(activity_about_software.this, "已经是最新的客户端了");
                    }
                }
            }.start();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}