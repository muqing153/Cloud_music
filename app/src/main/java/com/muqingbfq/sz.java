package com.muqingbfq;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.materialswitch.MaterialSwitch;

public class sz extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sz);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        UI();
        kaifazhe();
    }

    @SuppressLint("ApplySharedPref")
    private void UI() {
        MaterialSwitch a1 = findViewById(R.id.switch_a1);
        MaterialSwitch a2 = findViewById(R.id.switch_a2);
        SharedPreferences theme = getSharedPreferences("theme", MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor edit = theme.edit();
        int i = theme.getInt("theme", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        if (i == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
            a1.setChecked(true);
            a2.setEnabled(false);
        } else {
            a1.setChecked(false);
            a2.setEnabled(true);
            a2.setChecked(i == AppCompatDelegate.MODE_NIGHT_YES);
        }
        a1.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
// 跟随系统设置切换颜色模式
                int ms = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
                AppCompatDelegate.setDefaultNightMode(ms);
                edit.putInt("theme", ms);
                edit.commit();
            }
            a2.setEnabled(!b);
        });
        a2.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isEnabled()) {
                int ms;
                if (b) {
                    ms = AppCompatDelegate.MODE_NIGHT_YES;
                } else {
                    ms = AppCompatDelegate.MODE_NIGHT_NO;
                }
                AppCompatDelegate.setDefaultNightMode(ms);
                edit.putInt("theme", ms);
                edit.commit();
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    ActivityResultLauncher<Intent> intent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (Settings.canDrawOverlays(this)) {
                    com.muqingbfq.mq.floating.start(sz.this);
                }
            });
    public void kaifazhe() {
        MaterialSwitch materialSwitch = findViewById(R.id.switch_kfz);
        materialSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                if (!Settings.canDrawOverlays(this)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    this.intent.launch(intent);
                } else {
                    com.muqingbfq.mq.floating.start(sz.this);
                }
            } else {
                com.muqingbfq.mq.floating.end(sz.this);
            }
        });
    }
}