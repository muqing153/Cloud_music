package com.muqingbfq.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.muqingbfq.databinding.ActivityUserLogsBinding;
import com.muqingbfq.main;
import com.muqingbfq.mq.EditViewDialog;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wl;

import org.json.JSONException;
import org.json.JSONObject;



public class user_logs extends AppCompatActivity {
    public static class USER {
        public String name, qianming;
        public Object picUrl;

        public USER(String user, String qianming, Object picUrl) {
            this.name = user;
            this.qianming = qianming;
            this.picUrl = picUrl;
        }
    }
    EditText edituser, editpassword;
    public static String UUID;

    ActivityResultLauncher<Intent> enroll =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Bundle bundle = data.getExtras();
                                String user = bundle.getString("user");
                                String password = bundle.getString("password");
                                edituser.setText(user);
                                editpassword.setText(password);
                            }
                        }
                    });
    ActivityUserLogsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserLogsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        UUID = Settings.Secure.getString(getContentResolver(),
//                Settings.Secure.ANDROID_ID);
        binding.login.setOnClickListener(view -> new CloudUser());
/*        findViewById(R.id.enroll).setOnClickListener(view -> {
            Intent intent = new Intent(user_logs.this, enroll.class);
            intent.putExtra("user", edituser.getText().toString());
            intent.putExtra("appID", UUID);
            enroll.launch(intent);
        });*/
    }
    //some statement

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string.split(",")[1], Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public String account, password;
    class CloudUser extends Thread {
        public CloudUser() {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            View v = getWindow().peekDecorView();
            if (null != v) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
            user_logs.this.account = binding.editUser.getText().toString();
            user_logs.this.password = binding.editPassword.getText().toString();
            start();
        }

        @Override
        public void run() {
            super.run();
            try {
                String hq = wl.hq("/login/cellphone?phone=" + account + "&password=" + password);
                if (TextUtils.isEmpty(hq)) {
                    return;
                }
                JSONObject jsonObject = new JSONObject(hq);
                int code = jsonObject.getInt("code");
                if (code == 200) {
                    JSONObject data = jsonObject.getJSONObject("profile");
                    String nickname = data.getString("nickname");//用户名
                    String avatarUrl = data.getString("avatarUrl");//用户头像
                    String signature = data.getString("signature");//用户签名
                    String cookie = jsonObject.getString("cookie");
                    gj.xcts(user_logs.this, "登录成功");
                    wl.setcookie(cookie);
                    new user_message(nickname,signature,avatarUrl);
                    user_logs.this.finish();
                } else if (code == 502) {
                    gj.xcts(user_logs.this, jsonObject.getString("message"));
                } else {
                    gj.xcts(user_logs.this, "找不到此账号");
                }
            } catch (Exception e) {
                gj.sc(e);
            }
        }
    }
}