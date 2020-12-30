package com.example.bookseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class listProductPage extends AppCompatActivity {

    private List<ReleasedBook> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product_page);
        setTitle("书籍列表");

        if(fetchCollections()){
            showCollections();
        }
    }

    private void showCollections(){
        RecyclerView releasedView = findViewById(R.id.release_books_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        releasedView.setLayoutManager(manager);
        ReleaseAdapter bookAdapter = new ReleaseAdapter(books);
        releasedView.setAdapter(bookAdapter);
    }

    private boolean fetchCollections(){
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        String token = "Bearer " + sp.getString("token", "");

        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("num", "10")
                .build();
        final Request request = new Request.Builder()
                .url(NetworkUtils.getRequestUrl("/getNewRelease"))
                .addHeader("Authorization", token)
                .post(formBody)
                .build();

        NetworkUtils.forceNetworkRequesting();
        try(Response response = client.newCall(request).execute()){
            String responseData = response.body().string();
            System.out.println("getNewRelease-responseData:" + responseData);
            if(TextUtils.isEmpty(responseData)) {
                new AlertDialog.Builder(this)
                        .setTitle("登陆凭证过期")
                        .setMessage("登录凭证过期，请重新登录")
                        .setPositiveButton("确定", (dialog, which) -> {
                            LoginUtil.toLoginActivity(this);
                        })
                        .show();
                return false;
            }
            return handleResponseData(responseData);
        }catch (IOException e){
            Toast.makeText(this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("网络异常：");
            e.printStackTrace();
        }
        return false;
    }

    private boolean handleResponseData(String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            String status = jsonObject.getString("status");
            switch (status) {
                case "200": {
                    JSONArray data = jsonObject.getJSONArray("data");
                    books = new ArrayList<>();
                    for (int i = 0; i < data.length(); ++i) {
                        JSONObject item = data.getJSONObject(i);
                        ReleasedBook book = new ReleasedBook();
                        book.setUserId(item.getString("userId"))
                                .setUsername(item.getString("username"))
                                .setUid(item.getString("book_id"))
                                .setName(item.getString("name"))
                                .setUrl(item.getString("url"))
                                .setPrice(item.getDouble("price"))
                                .setDate(item.getString("date"));
                        books.add(book);
                    }

                    return true;
                }
                default:
            }
        } catch (JSONException e) {
            Toast.makeText(this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("返回信息处理异常：");
            e.printStackTrace();
        }
        return false;
    }

    static class ReleaseAdapter extends RecyclerView.Adapter<ReleaseAdapter.ViewHolder> {

        private List<ReleasedBook> list;

        static class ViewHolder extends RecyclerView.ViewHolder {
            View bookView;
            ImageView bookImage;
            TextView bookName;
            TextView bookPrice;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                bookView = itemView;
                bookImage = itemView.findViewById(R.id.book_image);
                bookName = itemView.findViewById(R.id.book_name);
                bookPrice = itemView.findViewById(R.id.book_price);
            }
        }

        public ReleaseAdapter(List<ReleasedBook> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ReleaseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);
            ReleaseAdapter.ViewHolder holder = new ReleaseAdapter.ViewHolder(view);
            /** 点击收藏夹中的某一项，跳转到该项的详情页
             *  传入新页面的同名参数有 bookId, name, price, url, details
             */
            holder.bookView.setOnClickListener(v -> {
                int position = holder.getAdapterPosition();
                ReleasedBook book = list.get(position);
                Intent intent = new Intent(v.getContext(), ProductPage.class);
                intent.putExtra("bookId", book.getUid());
                intent.putExtra("name", book.getName());
                intent.putExtra("price", book.getPrice());
                intent.putExtra("url", book.getUrl());
                intent.putExtra("userId", book.getUserId());
                intent.putExtra("username", book.getUsername());
                v.getContext().startActivity(intent);
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ReleaseAdapter.ViewHolder holder, int position) {
            ReleasedBook book = list.get(position);
            holder.bookName.setText(book.getName());
            holder.bookPrice.setText("￥" + book.getPrice());
            // 获取书的图片并显示
            Request request = new Request.Builder()
                    .url(book.getUrl())
                    .build();
            OkHttpClient client = new OkHttpClient();
            NetworkUtils.forceNetworkRequesting();
            try (Response response = client.newCall(request).execute()) {
                InputStream inputStream = response.body().byteStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                holder.bookImage.setImageBitmap(bitmap);
                System.out.println("显示书图成功:" + bitmap + book.getUrl());
            } catch (IOException e) {
                System.out.println("显示书图失败：");
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return this.list.size();
        }
    }

    class ReleasedBook{
        private String userId;
        private String username;
        private String uid;
        private String name;
        private String url;
        private double price;
        private String date;
        public void ReleasedBook(){

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

        public ReleasedBook setUrl(String url) {
            this.url = url;
            return this;
        }

        public ReleasedBook setPrice(double price) {
            this.price = price;
            return this;
        }

        public ReleasedBook setName(String name) {
            this.name = name;
            return this;
        }

        public ReleasedBook setDate(String date) {
            this.date = date;
            return this;
        }

        public ReleasedBook setUid(String uid) {
            this.uid = uid;
            return this;
        }

        public ReleasedBook setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public ReleasedBook setUsername(String username) {
            this.username = username;
            return this;
        }
    }
}