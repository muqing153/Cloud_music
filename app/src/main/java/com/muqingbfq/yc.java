package com.muqingbfq;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class yc extends AppCompatActivity {
    public static Object exception;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yc);
        TextView text = findViewById(R.id.text);
        try {
            text.setText(exception.toString());
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public static void start(Object e) {
        start(main.context, e);
    }

    public static void start(Context context, Object e) {
        yc.exception = e;
        context.startActivity(new Intent(context,yc.class));
    }
}
