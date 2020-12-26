package com.example.bookseller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView headImageView;
    private TextView usernameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        // 跳转到用户信息页面
        headImageView = findViewById(R.id.head);
        headImageView.setOnClickListener(view -> {
            Intent intent = new Intent(this, UserInfoActivity01.class);
            startActivity(intent);
        });
        usernameView = findViewById(R.id.username);
        usernameView.setOnClickListener(view -> {
            Intent intent = new Intent(this, UserInfoActivity01.class);
            startActivity(intent);
        });
        showSimpleInfo();

        // 由 “我的” 跳转到 “我收藏的”
        Button collection = (Button) findViewById(R.id.collection);
        collection.setOnClickListener(view -> {
            Intent intent = new Intent(this, FavoriteActivity01.class);
            startActivity(intent);
        });

        // 由 “我的” 跳转到 “聊天记录”
        Button chat = (Button) findViewById(R.id.chat);
        chat.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChatList.class);
            startActivity(intent);
        });

    }

    // 显示头像和名字
    private void showSimpleInfo() {
        System.out.println("showSimpleInfo()");
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);

        // 显示名字
        String username = sp.getString("username", null);
        if (username == null) {
            Intent intent = new Intent(this, LoginActivity01.class);
            startActivity(intent);
            return;
        }
        usernameView.setText("名字：" + username);

        // 显示头像
        String imagePath = sp.getString("imagePath", null);
        System.out.println("main-headImagePath: " + imagePath);
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            if (bitmap != null)
                headImageView.setImageBitmap(bitmap);
        }


    }

}