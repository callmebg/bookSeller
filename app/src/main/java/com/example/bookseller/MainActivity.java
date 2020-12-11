package com.example.bookseller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView headImageView;
    private TextView usernameView;
    private static Class<?> toChat;

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
            if (toChat != null) {
                Intent intent = new Intent(this, toChat);
                startActivity(intent);
            }
        });

        // 测试：暂时跳转到登录页面
        this.setChatActivity(LoginActivity01.class);
    }

    /**
     * 设置点击“聊天记录”按钮后跳转的页面
     * 如，聊天记录的 Activity 名为 ChatActivity,可以在该 Activity 中添加
     * MainActivity.setChatActivity(ChatActivity.class);
     * 语句，点击“聊天记录”按钮后将会跳转到相应页面
     */
    protected static void setChatActivity(Class<?> chatActivityClass) {
        toChat = chatActivityClass;
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