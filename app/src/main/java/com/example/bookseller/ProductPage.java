package com.example.bookseller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProductPage extends BaseActivity {
    private ImageView imageView;
    private TextView shoppingfirst;
    private TextView shoppingsec;
    private TextView shoppingmoney;
    private Button shopping_btn;
    private ImageView shopping_back;
    private Button chat_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        imageView = findViewById(R.id.shopping_image);
        shoppingfirst = findViewById(R.id.shopping_firsttext);
        shoppingsec = findViewById(R.id.shopping_sectextview);
        shoppingmoney = findViewById(R.id.shopping_money);
        shopping_btn = findViewById(R.id.shopping_btn);
        shopping_back = findViewById(R.id.shopping_back);
        chat_btn = findViewById(R.id.chat_btn);
        //接受母Intent信息
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String firstText = intent.getStringExtra("name");
        String secText = intent.getStringExtra("username");
        String money = "¥" + intent.getDoubleExtra("price", 999.99);
        String bookId = intent.getStringExtra("bookId");
        String chaterUid = intent.getStringExtra("userId");
        String chaterName = intent.getStringExtra("username");
        shoppingfirst.setText("书名：" + firstText);
        shoppingsec.setText("发布者：" + secText);
        shoppingmoney.setText("价格：" + money);
        showImage(url);

        shopping_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductPage.this.finish();
            }
        });

        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "");
                SharedPreferences editor = getSharedPreferences("data", MODE_PRIVATE);
                boolean is_login = editor.getBoolean("is_login", false);//登陆是true
                if (!is_login) {
                    Toast.makeText(getApplicationContext(), "亲，请先登录哦", Toast.LENGTH_SHORT).show();
                    Log.i("123", String.valueOf(is_login));
                    Intent intent = new Intent(ProductPage.this, LoginActivity01.class);
                    startActivity(intent);
                } else {
                    //聊天跳转
                    Intent intent = new Intent(ProductPage.this, Chat.class);
                    intent.putExtra("chaterUid", chaterUid);
                    intent.putExtra("chaterName", chaterName);
                    startActivity(intent);
                }
            }
        });
        //收藏键的实现
        shopping_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "");
                SharedPreferences editor = getSharedPreferences("data", MODE_PRIVATE);
                boolean is_login = editor.getBoolean("is_login", false);//登陆是true
                if (!is_login) {
                    Toast.makeText(getApplicationContext(), "亲，请先登录哦", Toast.LENGTH_SHORT).show();
                    Log.i("123", String.valueOf(is_login));
                    Intent intent = new Intent(ProductPage.this, LoginActivity01.class);
                    startActivity(intent);
                } else {
                    //上传数据库
                    OkHttpClient client = new OkHttpClient();
                    String token = "Bearer " + sharedPreferences.getString("token", "");
                    FormBody formBody = new FormBody.Builder()
                            .add("bookId", bookId)
                            .build();
                    final Request request = new Request.Builder()
                            .url(NetworkUtils.getRequestUrl("/bookCollect"))
                            .addHeader("Authorization", token)
                            .post(formBody)
                            .build();

                    NetworkUtils.forceNetworkRequesting();
                    // 进行网络请求并处理返回信息
                    try (Response response = client.newCall(request).execute()) {
                        String responseData = response.body().string();
                        System.out.println("collect-responseData:" + responseData);
                        handleResponseData(responseData);
                    } catch (IOException e) {
                        Toast.makeText(ProductPage.this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
                        System.out.println("网络异常：");
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void showImage(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient client = new OkHttpClient();
        NetworkUtils.forceNetworkRequesting();
        try (Response response = client.newCall(request).execute()) {
            InputStream inputStream = response.body().byteStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);
            System.out.println("详情页面显示书图成功:" + bitmap + url);
        } catch (IOException e) {
            System.out.println("详情页面显示书图失败：");
            e.printStackTrace();
        }
    }

    private boolean handleResponseData(String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            String status = jsonObject.getString("status");
            switch (status) {
                case "200": {   // 收藏成功
                    Toast.makeText(ProductPage.this, "收藏成功", Toast.LENGTH_SHORT).show();
                    return true;
                }
                default: {  // 其他
                    Toast.makeText(ProductPage.this, "收藏失败，请检查网络是否正常", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        } catch (JSONException e) { // 返回信息处理异常
            Toast.makeText(ProductPage.this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("注册返回信息处理异常：");
            e.printStackTrace();
        }
        return false;
    }
}