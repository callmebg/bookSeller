package com.example.bookseller;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
            FavoriteBook book = new FavoriteBook("" + i + 1, "aaaaaaaaaaaa", 2.2 * i);
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
                        String bookId = item.getString("bookId");
                        book.setBookId(bookId);
                        book.setTitle("bookId: " + bookId);
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
            TextView bookTitle;
            TextView bookPrice;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                bookView = itemView;
                bookImage = itemView.findViewById(R.id.book_image);
                bookTitle = itemView.findViewById(R.id.book_title);
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
            // 点击收藏夹中的某一项，跳转到该项的详情页
            holder.bookView.setOnClickListener(v -> {
                int position = holder.getAdapterPosition();
                FavoriteBook book = list.get(position);
                Toast.makeText(v.getContext(), "bookId " + book.getBookId(), Toast.LENGTH_SHORT).show();
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {
            FavoriteBook book = list.get(position);
            holder.bookTitle.setText(book.getTitle());
            holder.bookPrice.setText(String.valueOf(book.getPrice()));
        }

        @Override
        public int getItemCount() {
            return this.list.size();
        }
    }

    class FavoriteBook {
        private String bookId;
        private String title;
        private double price;

        public FavoriteBook() {
        }

        public FavoriteBook(String bookId, String title, double price) {
            this.bookId = bookId;
            this.title = title;
            this.price = price;
        }

        public String getBookId() {
            return bookId;
        }

        public void setBookId(String bookId) {
            this.bookId = bookId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }
}