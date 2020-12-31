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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment01 extends Fragment {

    // 初始化首页时显示的书籍数
    private static final int initNum = 20;
    private RecyclerView recyclerView;
    private BookAdapter initBookAdapter;
    private List<HomeReleaseBook> books;
    private Button searchButton;
    private EditText searchEditor;

    public HomeFragment01() {
    }

    public static HomeFragment01 newInstance() {
        HomeFragment01 fragment = new HomeFragment01();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home01, container, false);
        showHomeReleases(view);

        searchEditor = view.findViewById(R.id.search_editor);
        searchButton = view.findViewById(R.id.search_button);
        searchButton.setOnClickListener(v -> fetchSearchItems(view));

        return view;
    }

    private void fetchSearchItems(View view) {
        String bookName = searchEditor.getText().toString();

        if (TextUtils.isEmpty(bookName)) {
            recyclerView = view.findViewById(R.id.home_release_books_view);
            StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(initBookAdapter);
            return;
        }

        SharedPreferences sp = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String token = "Bearer " + sp.getString("token", "");

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("bookName", bookName)
                .build();
        Request request = new Request.Builder()
                .url(NetworkUtils.getRequestUrl("/searchBookByName"))
                .addHeader("Authorization", token)
                .post(requestBody)
                .build();

        NetworkUtils.forceNetworkRequesting();
        try (Response response = client.newCall(request).execute()) {
            String responseData = response.body().string();
            System.out.println("search-responseData:" + responseData);
            handleResponseData(responseData);

            recyclerView = view.findViewById(R.id.home_release_books_view);
            StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(manager);
            BookAdapter bookAdapter = new BookAdapter(books);
            recyclerView.setAdapter(bookAdapter);
        } catch (IOException e) {
            Toast.makeText(getActivity(), "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("网络异常：");
            e.printStackTrace();
        }
    }

    private void showHomeReleases(View view) {
        fetchHomeReleases();

        recyclerView = view.findViewById(R.id.home_release_books_view);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        initBookAdapter = new BookAdapter(books);
        recyclerView.setAdapter(initBookAdapter);
    }

    private void fetchHomeReleases() {
        SharedPreferences sp = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String token = "Bearer " + sp.getString("token", "");

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("num", String.valueOf(initNum))
                .build();
        Request request = new Request.Builder()
                .url(NetworkUtils.getRequestUrl("/getNewRelease"))
                .addHeader("Authorization", token)
                .post(requestBody)
                .build();

        NetworkUtils.forceNetworkRequesting();
        try (Response response = client.newCall(request).execute()) {
            String responseData = response.body().string();
            System.out.println("homeRelease-responseData:" + responseData);
            handleResponseData(responseData);
        } catch (IOException e) {
            Toast.makeText(getActivity(), "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("网络异常：");
            e.printStackTrace();
        }
    }

    private void handleResponseData(String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            String status = jsonObject.getString("status");
            switch (status) {
                case "200": {
                    JSONArray data = jsonObject.getJSONArray("data");
                    books = new ArrayList<>();
                    //如果没有搜到
                    if(data.length() == 0){
                        Toast.makeText(getActivity(), "抱歉，查无此物！", Toast.LENGTH_LONG).show();
                    }
                    for (int i = 0; i < data.length(); ++i) {
                        JSONObject item = data.getJSONObject(i);
                        HomeReleaseBook book = new HomeReleaseBook();
                        book.setUserId(item.getString("userId"))
                                .setUsername(item.getString("username"))
                                .setName(item.getString("name"))
                                .setUrl(item.getString("url"))
                                .setPrice(item.getDouble("price"))
                                .setDate(item.getString("date"))
                                .setBookId(item.getString("book_id"));
                        books.add(book);
                    }
                }
                default:
            }
        } catch (JSONException e) {
            Toast.makeText(getActivity(), "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("查看首页图书返回信息处理异常：");
            e.printStackTrace();
        }
    }

    static class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

        private List<HomeReleaseBook> list;

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

        public BookAdapter(List<HomeReleaseBook> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);
            ViewHolder holder = new ViewHolder(view);
            /** 点击列表中的某一项，跳转到该项的详情页
             *  传入新页面的同名参数有 bookId, name, price, url, details
             */
            holder.bookView.setOnClickListener(v -> {
                int position = holder.getAdapterPosition();
                HomeReleaseBook book = list.get(position);
                Intent intent = new Intent(v.getContext(), ProductPage.class);
                intent.putExtra("userId", book.getUserId());
                intent.putExtra("username", book.getUsername());
                intent.putExtra("name", book.getName());
                intent.putExtra("url", book.getUrl());
                intent.putExtra("price", book.getPrice());
                intent.putExtra("date", book.getDate());
                intent.putExtra("bookId", book.getBookId());
                v.getContext().startActivity(intent);
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            HomeReleaseBook book = list.get(position);
            holder.bookName.setText(book.getName());
            holder.bookPrice.setText("￥" + book.getPrice());
            // 获取书的图片并显示
            Request request = new Request.Builder()
                    .url(book.getUrl().equals("null") ? "http://null" : book.getUrl())
                    .build();
            OkHttpClient client = new OkHttpClient();
            NetworkUtils.forceNetworkRequesting();
            try (Response response = client.newCall(request).execute()) {
                InputStream inputStream = response.body().byteStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (bitmap != null)
                    holder.bookImage.setImageBitmap(bitmap);
                else
                    holder.bookImage.setImageResource(R.drawable.book_pause);
                System.out.println("首页显示书图成功:" + bitmap + book.getUrl());
            } catch (IOException e) {
                System.out.println("首页显示书图失败：");
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return this.list.size();
        }
    }

    class HomeReleaseBook {
        private String userId;
        private String username;
        private String name;
        private String url;
        private double price;
        private String date;
        private String bookId;

        public HomeReleaseBook() {
        }

        public String getUserId() {
            return userId;
        }

        public HomeReleaseBook setUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public String getUsername() {
            return username;
        }

        public HomeReleaseBook setUsername(String username) {
            this.username = username;
            return this;
        }

        public String getName() {
            return name;
        }

        public HomeReleaseBook setName(String name) {
            this.name = name;
            return this;
        }

        public String getUrl() {
            return url;
        }

        public HomeReleaseBook setUrl(String url) {
            this.url = url;
            return this;
        }

        public double getPrice() {
            return price;
        }

        public HomeReleaseBook setPrice(double price) {
            this.price = price;
            return this;
        }

        public String getDate() {
            return date;
        }

        public HomeReleaseBook setDate(String date) {
            this.date = date;
            return this;
        }

        public String getBookId() {
            return bookId;
        }

        public HomeReleaseBook setBookId(String bookId) {
            this.bookId = bookId;
            return this;
        }
    }
}