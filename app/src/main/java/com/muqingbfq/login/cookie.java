package com.muqingbfq.login;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.muqingbfq.R;
import com.muqingbfq.main;
import com.muqingbfq.mq.FragmentActivity;
import com.muqingbfq.mq.gj;
import com.muqingbfq.mq.wl;

import org.json.JSONObject;

import java.util.Objects;

public class cookie extends FragmentActivity {

    View view_a, view_b;
    TextView textView;
    ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cookie);
        view_a = findViewById(R.id.view1);
        view_b = findViewById(R.id.view2);
        view_a.setVisibility(View.GONE);

        //view2 里面的控件
        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        new erweima();
    }

    class erweima extends Thread {
        int code = 800;
        String unikey, qrimg, hq;
        private long time = 0;
        public erweima() {
            textView.setText("请使用网易云音乐扫码");
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
                                code = 0;
                                cookie.this.finish();
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
            main.handler.post(() -> imageView.setImageBitmap(user_logs.stringToBitmap(qrimg)));
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
