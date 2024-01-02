package com.muqingbfq;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.slider.Slider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.muqingbfq.databinding.ActivitySzSetlrcBinding;
import com.muqingbfq.mq.FloatingLyricsService;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wj;
import com.muqingbfq.view.LrcView;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Locale;

public class sz extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sz);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(getString(R.string.sz));
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

    public static class setlrc extends Fragment implements Slider.OnSliderTouchListener,
            Slider.OnChangeListener {
        FloatingLyricsService.SETUP setup;
        ActivitySzSetlrcBinding binding;
        public Runnable updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (bfqkz.mt.isPlaying()) {
                    int index = 0;
                    for (int i = 0; i < LrcView.lrclist.size(); i++) {
                        LrcView.LRC lineLrc = LrcView.lrclist.get(i);
                        if (lineLrc.time <= bfqkz.mt.getCurrentPosition()) {
                            index = i;
                        } else {
                            break;
                        }
                    }
                    if (index < LrcView.lrclist.size()) {
                        String text;
                        if (LrcView.lrclist.size() <= 3) {
                            for (LrcView.LRC a : LrcView.lrclist) {
                                if (a.time == 5940000 && a.lrc.equals("纯音乐，请欣赏")) {
                                    text = "纯音乐，请欣赏";
                                    binding.lrctext.setText(text);
                                    return;
                                }
                            }
                        }
                        LrcView.LRC currentLrc = LrcView.lrclist.get(index);
                        text = currentLrc.lrc;
                        if (currentLrc.tlyric != null) {
                            text += "\n" + currentLrc.tlyric;
                        }
                        binding.lrctext.setText(text);
                    }
                }
                gj.sc(getClass()+"执行");
                main.handler.postDelayed(this, 1000); // 每秒更新一次进度
            }
        };
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            binding = ActivitySzSetlrcBinding.inflate(inflater, container, false);
            File file = new File(wj.filesdri + "FloatingLyricsService.json");
            if (file.exists() && file.isFile()) {
                String dqwb = wj.dqwb(file.toString());
                Gson gson = new Gson();
                Type type = new TypeToken<FloatingLyricsService.SETUP>() {
                }.getType();
                setup = gson.fromJson(dqwb, type);
                binding.slide1.setValue(setup.size);
                binding.slide2.setValue(setup.Alpha);

                binding.lrctext.setAlpha(setup.Alpha);
                binding.lrctext.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                        setup.size,
                        getResources().getDisplayMetrics()));
                binding.lrctext.setTextColor(Color.parseColor(setup.Color));
                binding.lrctext.setOnClickListener(view -> ColorPickerDialogBuilder
                        .with(view.getContext())
                        .setTitle("调色盘")
                        .initialColor(Color.parseColor(setup.Color))
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(selectedColor -> {
                        })
                        .setPositiveButton("确定", (dialog, selectedColor, allColors) -> {
                            setup.Color = String.format("#%08X", selectedColor);
                            binding.lrctext.setTextColor(selectedColor);
                            FloatingLyricsService.baocun(setup);
                        })
                        .setNegativeButton("取消",null)
                        .build()
                        .show());
                binding.textSlide1.setText(String.valueOf(setup.size));
                binding.textSlide2.setText(String.format(Locale.US,"%.2f",setup.Alpha));

                main.handler.post(updateSeekBar);
            }
            if (setup.i != 0) {
                binding.switchA1.setChecked(true);
            }
            binding.switchA1.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b) {
                    setup.i = 1;
                } else {
                    setup.i = 0;
                    home.appCompatActivity
                            .stopService(new Intent(home.appCompatActivity,
                                    FloatingLyricsService.class));
                }
                FloatingLyricsService.baocun(setup);
            });
            binding.slide1.setLabelFormatter(value -> String.valueOf((int) value));
            binding.slide1.addOnChangeListener(this);
            binding.slide1.addOnChangeListener(this);
            binding.slide1.addOnSliderTouchListener(this);

            binding.slide2.addOnChangeListener(this);
            binding.slide2.addOnSliderTouchListener(this);

            return binding.getRoot();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            main.handler.removeCallbacks(updateSeekBar);
        }

        @Override
        public void onStartTrackingTouch(@NonNull Slider slider) {

        }

        @Override
        public void onStopTrackingTouch(@NonNull Slider slider) {
            FloatingLyricsService.baocun(setup);
        }

        @Override
        public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
            if (slider == binding.slide1) {
                setup.size = (int) value;
                binding.lrctext.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                        setup.size,
                        getResources().getDisplayMetrics()));
                binding.textSlide1.setText(String.valueOf(setup.size));
            }else if (slider == binding.slide2) {
                setup.Alpha = value;
                binding.lrctext.setAlpha(value);
                binding.textSlide2.setText(String.format(Locale.US,"%.2f", value));
            }
        }
    }

}