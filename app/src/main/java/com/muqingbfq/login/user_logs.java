package com.muqingbfq.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.muqingbfq.R;
import com.muqingbfq.main;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wl;

import org.json.JSONObject;

import java.util.Objects;

public class user_logs extends AppCompatActivity {

    EditText edituser, editpassword;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_logs);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        edituser = findViewById(R.id.edit_user);
        editpassword = findViewById(R.id.edit_password);
        findViewById(R.id.login).setOnClickListener(view -> new CloudUser(edituser.getText().toString()
                , editpassword.getText().toString()));
        findViewById(R.id.enroll).setOnClickListener(view -> {
            Intent intent = new Intent(user_logs.this, enroll.class);
            intent.putExtra("user", edituser.getText().toString());
            startActivityForResult(intent, 0);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && requestCode == 0 && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            String user = bundle.getString("user");
            String password = bundle.getString("password");
            edituser.setText(user);
            editpassword.setText(password);
        }
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

    class CloudUser extends Thread {
        String user, password;

        public CloudUser(String user, String password) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            View v = getWindow().peekDecorView();
            if (null != v) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
            this.user = user;
            this.password = password;
            start();
        }

        @Override
        public void run() {
            super.run();
            String s = wl.get(main.http + "/user.php?" + "user=" + user + "&password=" + password);
            try {
                JSONObject jsonObject = new JSONObject(s);
                int code = jsonObject.getInt("code");
                String msg = jsonObject.getString("msg");
                main.handler.post(() -> gj.ts(user_logs.this, msg));
                if (code == 200) {
                    String cookie = jsonObject.getString("cookie");
                    if (wl.iskong()) {
                        new visitor();
                    }
                    wl.setcookie(cookie);
                    new user_message();
                    user_logs.this.finish();
                }
            } catch (Exception e) {
                gj.sc(e);
            }
        }
    }

    public static class erweima extends Thread {
        int code = 800;
        String unikey, qrimg, hq;
        private long time=0;
        ImageView imageView;
        TextView textView;
        MaterialAlertDialogBuilder materialAlertDialogBuilder;

        public erweima(Context context) {
            View inflate = LayoutInflater.from(context).inflate(R.layout.erweima, null);
            imageView = inflate.findViewById(R.id.image);
            textView = inflate.findViewById(R.id.text);
// 创建布局参数对象
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(main.g, main.k);
// 设置视图的布局参数
            imageView.setLayoutParams(layoutParams);
            materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context) {
            };
            materialAlertDialogBuilder.setOnDismissListener(dialog -> {
                // 对话框消失时触发的操作
                // 可以在这里处理一些额外的逻辑
                code = 0;
            });
            materialAlertDialogBuilder.setView(inflate).setTitle("请使用网易云音乐扫码");
            materialAlertDialogBuilder.show();
            start();
        }

        @Override
        public void run() {
            super.run();
            while (code != 0) {
                try {
                    hq = wl.hq("/login/qr/check?key=" + unikey + Time());
                    if (hq != null) {
                        JSONObject json = new JSONObject(hq);
                        code = json.getInt("code");
                        switch (code) {
                            case 800:
                            case 400:
                                setwb("二维码过期");
                                hqkey();
                                break;
                            case 801:
                                setwb("等待扫码");
                                break;
                            case 802:
                                setwb("等待确认");
                                break;
                            case 803:
                                setwb("登录成功");
                                wl.setcookie(json.getString("cookie"));
                                main.handler.postDelayed(() -> materialAlertDialogBuilder.create().cancel(),
                                        500);
                                code = 0;
                                break;
                            default:
                                code = 0;
                                // 默认情况下的操作
                                break;
                        }
                    }
                    sleep(1000);
                } catch (Exception e) {
                    gj.sc(e);
                }
            }
        }

        private void hqkey() throws Exception {
            unikey = new JSONObject(Objects.requireNonNull(wl.hq("/login/qr/key"))).
                    getJSONObject("data").getString("unikey");
            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(wl.hq("/login/qr/create?key=" +
                    unikey +
                    "&qrimg=base64")));
            qrimg = jsonObject.getJSONObject("data").getString("qrimg");
            main.handler.post(() -> imageView.setImageBitmap(stringToBitmap(qrimg)));
        }

        private String Time() {
            if (time < System.currentTimeMillis() - 1000) {
                time = System.currentTimeMillis();
            }
            return "&timestamp" + time;
        }

        private void setwb(String wb) {
            main.handler.post(() -> textView.setText(wb));
        }
    }
}