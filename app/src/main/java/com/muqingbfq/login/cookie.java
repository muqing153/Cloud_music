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
//        new erweima();
    }


}
