package com.example.bookseller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class UserFragment01 extends Fragment {

    private String username;
    private ImageView headImageView;
    private TextView usernameView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user01, container, false);

        // 跳转到用户信息页面
        headImageView = v.findViewById(R.id.head);
        headImageView.setOnClickListener(view -> {
            toOtherActivity(UserInfoActivity01.class);
        });
        usernameView = v.findViewById(R.id.username);
        usernameView.setOnClickListener(view -> {
            toOtherActivity(UserInfoActivity01.class);
        });

        // 由 “我的” 跳转到 “我收藏的”
        Button collection = v.findViewById(R.id.collection);
        collection.setOnClickListener(view -> {
            toOtherActivity(FavoriteActivity01.class);
        });

        // 由 “我的” 跳转到 “聊天记录”
        Button chat = v.findViewById(R.id.chat);
        chat.setOnClickListener(view -> {
            toOtherActivity(ChatList.class);
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        showSimpleInfo();
    }

    // 已登录则显示相应头像和名字，未登录则显示默认内容
    private void showSimpleInfo() {
        System.out.println("showSimpleInfo()");
        if (LoginUtil.isLogin(getContext())) {
            // 显示名字
            SharedPreferences sp = getContext().getSharedPreferences("data", MODE_PRIVATE);
            username = sp.getString("username", null);
            usernameView.setText("名字：" + username);

            // 显示头像
            showUserImage();
        } else {
            usernameView.setText("名字：");
            headImageView.setImageResource(R.drawable.head);
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
            if (bitmap != null)
                headImageView.setImageBitmap(bitmap);
            else
                headImageView.setImageResource(R.drawable.head);

            System.out.println("我的页面显示头像成功---bitmap:" + bitmap);
        } catch (IOException e) {
            Toast.makeText(getContext(), "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("我的页面获取头像失败：");
            e.printStackTrace();
        }
    }

    // 如果已登录则跳转到指定页面，未登录则跳转到 “登录” 页面
    private void toOtherActivity(Class<?> cls) {
        if (LoginUtil.isLogin(getContext())) {
            Intent intent = new Intent(getContext(), cls);
            startActivityForResult(intent, 1);
        } else {
            LoginUtil.toLoginActivity(getContext());
        }
    }
}