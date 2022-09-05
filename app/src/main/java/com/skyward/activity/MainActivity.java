package com.skyward.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.skyward.pagedragframe.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bt_1).setOnClickListener(v -> {
            startActivity(new Intent(this, DragActivity.class));
        });
        findViewById(R.id.bt_2).setOnClickListener(v -> {
            startActivity(new Intent(this, DragActivity2.class));
        });
        findViewById(R.id.bt_3).setOnClickListener(v -> {
            startActivity(new Intent(this, DragActivity3.class));
        });
    }
}