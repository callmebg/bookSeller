package com.example.bookseller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {

    private Button button;
    private EditText editText;
    private List<SearchBook> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        button = findViewById(R.id.search_btn);
        editText = findViewById(R.id.search_text);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String bookName = editText.getText().toString();
                SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
                String token = "Bearer " + sp.getString("token", "");


                OkHttpClient client = new OkHttpClient();
                // 请求体：邮箱，用户名和密码
                FormBody formBody = new FormBody.Builder()
                        .add("bookName", bookName)
                        .build();

                final Request request = new Request.Builder()
                        .url(NetworkUtils.getRequestUrl("/searchBookByName"))
                        .addHeader("Authorization", token)
                        .post(formBody)
                        .build();

                NetworkUtils.forceNetworkRequesting();
                try(Response response = client.newCall(request).execute()){
                    String responseData = response.body().string();
                    System.out.println("Search-responseData:" + responseData);
                    if(TextUtils.isEmpty(responseData)) {
                        new AlertDialog.Builder(SearchActivity.this)
                                .setTitle("登陆凭证过期")
                                .setMessage("登录凭证过期，请重新登录")
                                .setPositiveButton("确定", (dialog, which) -> {
                                    LoginUtil.toLoginActivity(SearchActivity.this);
                                })
                                .show();
                    }
                    handleResponseData(responseData);
                }catch (IOException e){
                    Toast.makeText(SearchActivity.this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
                    System.out.println("网络异常：");
                    e.printStackTrace();
                }
            }

        });
    }

    private boolean handleResponseData(String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            String status = jsonObject.getString("status");
            switch (status) {
                case "200": {
                    JSONArray data = jsonObject.getJSONArray("data");
                    books = new ArrayList<>();
                    if (data.length() != 0) {
                        JSONObject item = data.getJSONObject(0);
                        SearchBook book = new SearchBook();
                        book//.setUserId(item.getString("userId"))
                                .setUid(item.getString("uid"))
                                .setName(item.getString("name"))
                                .setUrl(item.getString("url"))
                                .setDetails(item.getString("details"))
                                //.setUsername(item.getString("username"))
                                .setPrice(item.getDouble("price"));
                        books.add(book);
                        Intent intent = new Intent(SearchActivity.this, SearchResult.class);
                        intent.putExtra("uid",book.getUid());
                        intent.putExtra("name", book.getName());
                        intent.putExtra("url", book.getUrl());
                        intent.putExtra("details", book.getDetails());
                        intent.putExtra("price", book.getPrice());
                        startActivity(intent);
                        return true;
                    }
                    else{
                        Toast.makeText(SearchActivity.this, "无搜索结果！", Toast.LENGTH_SHORT).show();
                    }
                }
                default:
            }
        } catch (JSONException e) {
            Toast.makeText(this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("查看收藏夹返回信息处理异常：");
            e.printStackTrace();
        }
        return false;
    }
    class SearchBook implements Serializable {
        private String userId;
        private String username;
        private String uid;
        private String name;
        private String url;
        private double price;
        public String details;
        public String date;
        public void SearchBook(){

        }

        public String getUrl() {
            return url;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public String getDate() {
            return date;
        }

        public String getUid() {
            return uid;
        }

        public String getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }

        public SearchBook setUrl(String url) {
            this.url = url;
            return this;
        }

        public SearchBook setPrice(double price) {
            this.price = price;
            return this;
        }

        public SearchBook setName(String name) {
            this.name = name;
            return this;
        }

        public SearchBook setDate(String date) {
            this.date = date;
            return this;
        }

        public SearchBook setUid(String uid) {
            this.uid = uid;
            return this;
        }

        public SearchBook setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public SearchBook setUsername(String username) {
            this.username = username;
            return this;
        }

        public SearchBook setDetails(String details) {
            this.details = details;
            return this;
        }

        public String getDetails() {
            return details;
        }
    }
}