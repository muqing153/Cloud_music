package com.muqingbfq.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
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
import androidx.appcompat.widget.Toolbar;

import com.muqingbfq.R;
import com.muqingbfq.main;
import com.muqingbfq.mq.EditViewDialog;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class user_logs extends AppCompatActivity {

    EditText edituser, editpassword;
    Toolbar toolbar;
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
    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_logs);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        UUID = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);

        edituser = findViewById(R.id.edit_user);
        editpassword = findViewById(R.id.edit_password);
        findViewById(R.id.login).setOnClickListener(view -> new CloudUser(edituser.getText().toString()
                , editpassword.getText().toString()));
        findViewById(R.id.enroll).setOnClickListener(view -> {
            Intent intent = new Intent(user_logs.this, enroll.class);
            intent.putExtra("user", edituser.getText().toString());
            intent.putExtra("appID", UUID);
            enroll.launch(intent);
        });
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

    String isEmail="false";
    public String account, password;
    class CloudUser extends Thread {

        public CloudUser(String account, String password) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            View v = getWindow().peekDecorView();
            if (null != v) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
            user_logs.this.account = account;
            user_logs.this.password = password;
            Pattern pattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
            if (pattern.matcher(account).matches()) {
                isEmail = "true";
            }
            start();
        }

        @Override
        public void run() {
            super.run();
            try {
                String post = wl.post("/php/user.php?action=login",
                        new String[]{
                                "account", "passWord", "appID", "isEmail"
                        },
                        new String[]{
                                account, password, UUID, isEmail
                        });
                gj.sc(post);
                if (TextUtils.isEmpty(post)) {
                    return;
                }
                JSONObject jsonObject = new JSONObject(post);
                if (jsonObject.getInt("code") == 0) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    gj.sc(data);
                    //用户token
                    String token = data.getString("token");
                    //用户名称account
                    String account = data.getString("account");
                    main.settoken(token, account);
                    new user_message(account);
                    user_logs.this.finish();
                } else {
                    String message = jsonObject.getString("message");
                    gj.xcts(user_logs.this, message);
                    if (message.equals("请更改登录设备")) {
                        JSONObject jsonpost = wl.jsonpost("/php/user.php?action=verification",
                                new String[]{
                                        "account", "passWord", "appID", "isEmail"
                                },
                                new String[]{
                                        account, password, UUID, isEmail
                                });
                        gj.sc(jsonpost);
                        if (!TextUtils.isEmpty(jsonpost.toString()) &&
                                jsonpost.getInt("code") != 0) {
                            return;
                        }
                        String message1 = jsonpost.getString("message");
                        gj.xcts(user_logs.this, message1);
                        main.handler.post(() -> {
                            EditViewDialog editViewDialog = new EditViewDialog(user_logs.this,
                                    "请输入验证码");
                            editViewDialog.setMessage("验证码在你账号锁绑定的邮箱绘制垃圾桶中请及时查看");
                            editViewDialog.setPositive(view -> {
                                new Thread() {
                                    @Override
                                    public void run() {
                                        JSONObject jsonpost = wl.jsonpost("/php/user.php?action=changeAppId",
                                                new String[]{
                                                        "account", "key", "appID", "isEmail"
                                                },
                                                new String[]{
                                                        account, editViewDialog.getEditText(), UUID, isEmail
                                                });
                                        
                                        gj.sc(jsonpost.toString());
                                        if (!TextUtils.isEmpty(jsonpost.toString())) {
                                            try {
                                                int code = jsonpost.getInt("code");
                                                if (code == 0) {
                                                    gj.xcts(user_logs.this,
                                                            "验证成功请重新登录");
                                                    editViewDialog.dismiss();
                                                } else {
                                                    gj.xcts(user_logs.this,
                                                            jsonpost.getString("message"));
                                                }
                                            } catch (JSONException e) {
                                                editViewDialog.dismiss();
                                                gj.sc(e);
                                            }
                                        }
                                    }
                                }.start();
//                                    editViewDialog.dismiss();
                            });
                            editViewDialog.setEditinputType("number");
                            editViewDialog.show();
                        });
                    } else if (message.equals("请先激活您的账户")) {
                        jihuo();
                    }
                }
            } catch (Exception e) {
                gj.sc(e);
            }
        }
    }

    public void jihuo() {
        main.handler.post(new Runnable() {
            @Override
            public void run() {
                EditViewDialog editViewDialog = new EditViewDialog(user_logs.this, "激活用户")
                        .setMessage("验证码在你账号锁绑定的邮箱绘制垃圾桶中请及时查看");
                editViewDialog.setPositive(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String editText = editViewDialog.getEditText();
                                new Thread() {
                                    @Override
                                    public void run() {
                                        super.run();
                                        JSONObject post = wl.jsonpost("/php/user.php?action=enableAccount",
                                                new String[]{
                                                        "account", "key"
                                                }
                                                , new String[]{
                                                        account, editText
                                                });
                                        if (TextUtils.isEmpty(post.toString())) {
                                            return;
                                        }
                                        try {
                                            int code = post.getInt("code");
                                            gj.xcts(user_logs.this, post.getString("message"));
                                            if (code == 1) {
                                                return;
                                            }
                                            editViewDialog.dismiss();
                                        } catch (JSONException e) {
                                            gj.sc(e);
                                        }

                                    }
                                }.start();
                            }
                        }).setEditinputType("number")
                        .show();
            }
        });
    }

}