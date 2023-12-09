package com.muqingbfq.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.muqingbfq.R;
import com.muqingbfq.mq.FragmentActivity;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wl;

import org.json.JSONException;
import org.json.JSONObject;

public class enroll extends FragmentActivity {
    EditText edit_account, edit_username,
            edit_password, edit_email;

    public String account, username, password, email, appID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);
        Intent intent = getIntent();
        //获取UID编辑框
        edit_account = findViewById(R.id.edit_user);
        edit_username = findViewById(R.id.edit_name);
        //设置密码编辑框
        edit_password = findViewById(R.id.edit_password);
        edit_email = findViewById(R.id.edit_email);
        edit_username.setText(
                intent.getStringExtra("user"));
        appID = intent.getStringExtra("appID");
        findViewById(R.id.enroll).setOnClickListener(view -> a());
    }

    public void a() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        new thread();
    }

    private void end() {
        Intent intent = new Intent(); // 创建一个新意图
        Bundle bundle = new Bundle(); // 创建一个新包裹
        // 往包裹存入名叫response_time的字符串
        bundle.putString("user", account);
        // 往包裹存入名叫response_content的字符串
        bundle.putString("password", password);
        intent.putExtras(bundle); // 把快递包裹塞给意图
        // 携带意图返回上一个页面。RESULT_OK表示处理成功
        setResult(Activity.RESULT_OK, intent);
        finish(); // 结束当前的活动页面
    }

    class thread extends Thread {
        public thread() {
            account = edit_account.getText().toString();
            username = edit_username.getText().toString();
            password = edit_password.getText().toString();
            email = edit_email.getText().toString();
            start();
        }

        @Override
        public void run() {
            super.run();
            try {
                JSONObject jsonpost = wl.jsonpost("/php/user.php?action=register",
                        new String[]{
                                "account", "userName", "passWord"
                                , "email", "appID"
                        }
                        , new String[]{
                                account, username, password, email, appID
                        });
                if (TextUtils.isEmpty(jsonpost.toString())) {
                    return;
                }
                int code = jsonpost.getInt("code");
                String message = jsonpost.getString("message");
                gj.xcts(enroll.this, message);
                if (code == 0) {
                    end();
                }
            } catch (JSONException e) {
                gj.sc(e);
            }
        }
    }
}