package com.example.bookseller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        // 由 “我的” 跳转到 “我收藏的”
        Button collection = (Button) findViewById(R.id.collection);
        collection.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity1.this, LoginActivity01.class);
            startActivity(intent);
        });

        // 由 “我的” 跳转到 “我发布的”
        Button publish = (Button) findViewById(R.id.publish);
        publish.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity1.this, RegisterActivity01.class);
            startActivity(intent);
        });

        // 由 “我的” 跳转到 “我买入的”
        Button buy = (Button) findViewById(R.id.buy);

        // 由 “我的” 跳转到 “设置”
        ImageView settings = (ImageView) findViewById(R.id.settings);
    }
}