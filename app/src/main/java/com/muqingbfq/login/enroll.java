package com.muqingbfq.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import com.muqingbfq.R;
import com.muqingbfq.main;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wl;
import com.muqingbfq.yc;

import org.json.JSONObject;

public class enroll extends AppCompatActivity {
    EditText eduser,edpassword;
    String user, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        eduser = findViewById(R.id.edit_user);
        eduser.setText(
                intent.getStringExtra("user"));
        edpassword = findViewById(R.id.edit_password);
        findViewById(R.id.edit_cookie).setOnClickListener(view -> new user_logs.erweima(view.getContext()));
        findViewById(R.id.enroll).setOnClickListener(view -> a());
    }

    public void a() {
        user = eduser.getText().toString();
        password = edpassword.getText().toString();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        new thread().start();
    }

        private void end() {
        Intent intent = new Intent(); // 创建一个新意图
        Bundle bundle = new Bundle(); // 创建一个新包裹
        // 往包裹存入名叫response_time的字符串
        bundle.putString("user", user);
        // 往包裹存入名叫response_content的字符串
        bundle.putString("password", password);
        intent.putExtras(bundle); // 把快递包裹塞给意图

        // 携带意图返回上一个页面。RESULT_OK表示处理成功
        setResult(Activity.RESULT_OK, intent);

        finish(); // 结束当前的活动页面
    }

    class thread extends Thread {
        @Override
        public void run() {
            super.run();
            String s = wl.get("http://139.196.224.229/muqing/enroll.php?user=" + user + "&password=" + password
                    + "&cookie" + wl.Cookie);
            try {
                JSONObject jsonObject = new JSONObject(s);
                int code = jsonObject.getInt("code");
                String msg = jsonObject.getString("msg");
                main.handler.post(() -> gj.ts(enroll.this, msg));
                if (code == 200) {
                    end();
                }
            } catch (Exception e) {
                yc.start(e);
            }
        }
    }


}