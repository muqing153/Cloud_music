package com.muqingbfq.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.muqingbfq.R;
import com.muqingbfq.databinding.ActivityUserEditingBinding;
import com.muqingbfq.fragment.sz;
import com.muqingbfq.main;
import com.muqingbfq.mq.FragmentActivity;
import com.muqingbfq.mq.gj;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class user_editing extends FragmentActivity {
    //头像，用户名，签名，性别，背景
    ImageView imageViewa;
    EditText edit_name, edit_qianming;
    ImageView imageViewb;
    String file_a, file_b;
    ActivityUserEditingBinding binding;
    @SuppressLint("CheckResult")
    ActivityResultLauncher<Intent> setimagea =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Uri uri = data.getData();
                                file_a = getImagePath(uri);
                                Glide.with(user_editing.this)
                                        .load(file_a)
                                        .error(R.drawable.icon)
                                        .into(imageViewa);
                                // 处理选择的图片
                            }

                        }
                    }), setimageb =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Uri imageUri = data.getData();
                                file_b = getImagePath(imageUri);
                                Glide.with(user_editing.this)
                                        .load(file_b)
                                        .error(R.drawable.icon)
                                        .into(imageViewb);
                                // 处理选择的图片
                            }
                        }
                    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserEditingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        imageViewa = findViewById(R.id.image1);
        edit_name = findViewById(R.id.edit_name);
        edit_qianming = findViewById(R.id.edit_qianming);
        imageViewb = findViewById(R.id.image2);
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
//                    user_message.string string = user_message.get();
/*
                    main.handler.post(() -> {
                        Glide.with(user_editing.this)
                                .load(string.headIcon())
                                .into(imageViewa);
                        edit_name.setText(string.userName());
                        edit_qianming.setText(string.introduce());
                        Glide.with(user_editing.this)
                                .load(string.cover())
                                .into(imageViewb);
                        String gender = string.gender();
                        if (gender.equals("1")) {
                            gender = "男";
                        } else {
                            gender = "女";
                        }
                        binding.autoComplete.setText(gender);
                    });*/
                } catch (Exception e) {
                    gj.sc(e);
                }
            }
        }.start();

        View.OnClickListener onClickListener = view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/jpeg", "image/png", "image/jpg", "image/gif"}); // 指定可选的文件类型
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true); // 仅显示本地存储的文件
            // 检查权限
            if (ContextCompat.checkSelfPermission(user_editing.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有写入存储的权限，则请求权限
                ActivityCompat.requestPermissions(user_editing.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            } else {
                if (view.getId() == R.id.image1) {
                    setimagea.launch(new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
                } else if (view.getId() == R.id.image2) {
                    setimageb.launch(new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
                }
            }
        };
        imageViewa.setOnClickListener(onClickListener);
        imageViewb.setOnClickListener(onClickListener);
        binding.userEnd.setOnClickListener(view ->{
            finish();
//            main.settoken(null, null);
            com.muqingbfq.fragment.wode.setname("未登录");
            com.muqingbfq.fragment.wode.setqianming(null);
            com.muqingbfq.fragment.wode.imageView.setImageResource(R.drawable.icon);
        });
    }

    // 获取图片的实际路径
    private String getImagePath(Uri uri) {
        String path = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.moveToFirst()) {
                path = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return path;
    }
}