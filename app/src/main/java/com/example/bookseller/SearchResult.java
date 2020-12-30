package com.example.bookseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchResult extends AppCompatActivity {

    private List <SearchActivity.SearchBook> books;
    private SearchActivity.SearchBook book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        setTitle("搜索结果");
        Intent intent = getIntent();
        book.setDetails(intent.getStringExtra("details"));
        book.setUid(intent.getStringExtra("uid"));
        book.setName(intent.getStringExtra("name"));
        book.setUrl(intent.getStringExtra("url"));
        book.setPrice(intent.getDoubleExtra("price", 0.0));
        //book.setUsername(intent.getStringExtra("username"));
        //book.setUserId(intent.getStringExtra("userId"));
        books.add(book);
        showSearchResult();
    }

    private void showSearchResult(){
        RecyclerView searchView = findViewById(R.id.search_books_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        searchView.setLayoutManager(manager);
        SearchAdapter searchAdapter = new SearchAdapter(books);
        searchView.setAdapter(searchAdapter);
    }

    static class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

        private List<SearchActivity.SearchBook> list;

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

        public SearchAdapter(List<SearchActivity.SearchBook> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);
            SearchAdapter.ViewHolder holder = new SearchAdapter.ViewHolder(view);
            /** 点击收藏夹中的某一项，跳转到该项的详情页
             *  传入新页面的同名参数有 bookId, name, price, url, details
             */
            holder.bookView.setOnClickListener(v -> {
                int position = holder.getAdapterPosition();
                SearchActivity.SearchBook book = list.get(position);
                Intent intent = new Intent(v.getContext(), ProductPage.class);
                intent.putExtra("uid",book.getUid());
                intent.putExtra("name", book.getName());
                intent.putExtra("url", book.getUrl());
                intent.putExtra("details", book.getDetails());
                intent.putExtra("price", book.getPrice());
                //intent.putExtra("username", book.getUsername());
                //intent.putExtra("userId", book.getUserId());
                v.getContext().startActivity(intent);
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
            SearchActivity.SearchBook book = list.get(position);
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
}