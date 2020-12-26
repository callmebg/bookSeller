package com.example.bookseller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private String username;
    private ImageView headImageView;
    private TextView usernameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        // 跳转到用户信息页面
        headImageView = findViewById(R.id.head);
        headImageView.setOnClickListener(view -> {
            toOtherActivity(UserInfoActivity01.class);
        });
        usernameView = findViewById(R.id.username);
        usernameView.setOnClickListener(view -> {
            toOtherActivity(UserInfoActivity01.class);
        });
        showSimpleInfo();

        // 由 “我的” 跳转到 “我收藏的”
        Button collection = (Button) findViewById(R.id.collection);
        collection.setOnClickListener(view -> {
            toOtherActivity(FavoriteActivity01.class);
        });

        // 由 “我的” 跳转到 “聊天记录”
        Button chat = (Button) findViewById(R.id.chat);
        chat.setOnClickListener(view -> {
            toOtherActivity(ChatList.class);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        showSimpleInfo();
    }

    // 已登录则显示相应头像和名字，未登录则显示默认内容
    private void showSimpleInfo() {
        System.out.println("showSimpleInfo()");
        if (LoginUtil.isLogin(this)) {
            // 显示名字
            SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
            username = sp.getString("username", null);
            usernameView.setText("名字：" + username);

            // 显示头像
            showUserImage();
        }
    }

    // 显示头像
    private void showUserImage() {
        Request request = new Request.Builder()
                .url(NetworkUtils.getUserImageUrl(username))
                .build();
        OkHttpClient client = new OkHttpClient();
        NetworkUtils.forceNetworkRequesting();
        try (Response response = client.newCall(request).execute()) {
            InputStream inputStream = response.body().byteStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            runOnUiThread(() -> headImageView.setImageBitmap(bitmap));

            System.out.println("我的页面显示头像成功");
        } catch (IOException e) {
            Toast.makeText(this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("我的页面获取头像失败：");
            e.printStackTrace();
        }
    }

    // 如果已登录则跳转到指定页面，未登录则跳转到 “登录” 页面
    private void toOtherActivity(Class<?> cls) {
        if (LoginUtil.isLogin(this)) {
            Intent intent = new Intent(this, cls);
            startActivityForResult(intent, 1);
        } else {
            LoginUtil.toLoginActivity(this);
        }
    }

}