package com.example.bookseller;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FavoriteActivity01 extends AppCompatActivity {

    private List<FavoriteBook> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite01);
        setTitle("收藏夹");

        if (fetchCollections()) {
            showCollections();
        }

//        test01();
    }

    /**
     * 测试收藏夹的显示：非正式数据
     */
    private void test01() {
        List<FavoriteBook> list = new ArrayList<>();
        for (int i = 1; i < 11; ++i) {
            FavoriteBook book = new FavoriteBook();
            list.add(book);
        }

        RecyclerView favoriteBooksView = findViewById(R.id.favorite_books_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        favoriteBooksView.setLayoutManager(manager);
        BookAdapter bookAdapter = new BookAdapter(list);
        favoriteBooksView.setAdapter(bookAdapter);
    }

    /**
     * 显示收藏夹内容
     */
    private void showCollections() {
        RecyclerView favoriteBooksView = findViewById(R.id.favorite_books_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        favoriteBooksView.setLayoutManager(manager);
        BookAdapter bookAdapter = new BookAdapter(books);
        favoriteBooksView.setAdapter(bookAdapter);
    }

    /**
     * 从服务器获取登录用户的收藏夹。
     * 成功获取和处理返回 true；否则返回 false
     */
    private boolean fetchCollections() {
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        String token = "Bearer " + sp.getString("token", "");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(NetworkUtils.getRequestUrl("/seeAllBookCollect"))
                .addHeader("Authorization", token)
                .get().build();

        NetworkUtils.forceNetworkRequesting();
        try (Response response = client.newCall(request).execute()) {
            String responseData = response.body().string();
            System.out.println("favorite-responseData:" + responseData);
            if (TextUtils.isEmpty(responseData)) {
                new AlertDialog.Builder(this)
                        .setTitle("登录凭证过期")
                        .setMessage("登录凭证过期，请重新登录")
                        .setPositiveButton("确定", (dialog, which) -> {
                            LoginUtil.toLoginActivity(this);
                        })
                        .show();
                return false;
            }
            return handleResponseData(responseData);
        } catch (IOException e) {
            Toast.makeText(this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("网络异常：");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 处理服务器返回信息，提取出收藏夹内容
     * 未完成...
     *
     * @param responseData 响应体
     */
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
                        FavoriteBook book = new FavoriteBook();
                        book.setBookId(item.getString("uid"))
                                .setName(item.getString("name"))
                                .setPrice(item.getDouble("price"))
                                .setUrl(item.getString("url"))
                                .setDetails(item.getString("details"));
                        books.add(book);
                    }

                    return true;
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

    static class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

        private List<FavoriteBook> list;

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

        public BookAdapter(List<FavoriteBook> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);
            ViewHolder holder = new ViewHolder(view);
            /** 点击收藏夹中的某一项，跳转到该项的详情页
             *  传入新页面的同名参数有 bookId, name, price, url, details
             */
            holder.bookView.setOnClickListener(v -> {
                int position = holder.getAdapterPosition();
                FavoriteBook book = list.get(position);
                Intent intent = new Intent(v.getContext(), ProductPage.class);
                intent.putExtra("bookId", book.getBookId());
                intent.putExtra("name", book.getName());
                intent.putExtra("price", book.getPrice());
                intent.putExtra("url", book.getUrl());
                intent.putExtra("details", book.getDetails());
                v.getContext().startActivity(intent);
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {
            FavoriteBook book = list.get(position);
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
                System.out.println("收藏夹显示书图成功:" + bitmap + book.getUrl());
            } catch (IOException e) {
                System.out.println("收藏夹显示书图失败：");
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return this.list.size();
        }
    }

    class FavoriteBook {
        private String bookId;
        private String name;
        private double price;
        private String url;
        private String details;

        public FavoriteBook() {
        }

        public String getBookId() {
            return bookId;
        }

        public FavoriteBook setBookId(String bookId) {
            this.bookId = bookId;
            return this;
        }

        public String getName() {
            return name;
        }

        public FavoriteBook setName(String name) {
            this.name = name;
            return this;
        }

        public double getPrice() {
            return price;
        }

        public FavoriteBook setPrice(double price) {
            this.price = price;
            return this;
        }

        public String getUrl() {
            return url;
        }

        public FavoriteBook setUrl(String url) {
            this.url = url;
            return this;
        }

        public String getDetails() {
            return details;
        }

        public FavoriteBook setDetails(String details) {
            this.details = details;
            return this;
        }
    }
}