package com.example.bookseller.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookseller.BaseActivity;
import com.example.bookseller.FavoriteActivity01;
import com.example.bookseller.LoginActivity01;
import com.example.bookseller.Product;
import com.example.bookseller.ProductPage;
import com.example.bookseller.R;
import com.example.bookseller.SearchActivity;
import com.example.bookseller.listProductPage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private HomeFragmentAdapter adapter;
    private ListView listView;
    private Button searchButton;
    private Button showBookButton;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //searchButton = getView().findViewById(R.id.home_search);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        searchButton = view.findViewById(R.id.frag_search);
        showBookButton = view.findViewById(R.id.frag_listProduct);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        showBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), listProductPage.class);
                startActivity(intent);
            }
        });

//        RecyclerView favoriteBooksView = getView().findViewById(R.id.favorite_books_view);
//        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
//        favoriteBooksView.setLayoutManager(manager);
//        BookAdapter bookAdapter = new BookAdapter(books);
//        favoriteBooksView.setAdapter(bookAdapter);
        return view;
//        homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);
//        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
//        return root;
    }
//    static class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {
//
//        private List<FavoriteBook> list;
//
//        static class ViewHolder extends RecyclerView.ViewHolder {
//            View bookView;
//            ImageView bookImage;
//            TextView bookName;
//            TextView bookPrice;
//
//            public ViewHolder(@NonNull View itemView) {
//                super(itemView);
//                bookView = itemView;
//                bookImage = itemView.findViewById(R.id.book_image);
//                bookName = itemView.findViewById(R.id.book_name);
//                bookPrice = itemView.findViewById(R.id.book_price);
//            }
//        }
//
//        public BookAdapter(List<FavoriteBook> list) {
//            this.list = list;
//        }
//
//        @NonNull
//        @Override
//        public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);
//            ViewHolder holder = new BookAdapter.ViewHolder(view);
//            /** 点击收藏夹中的某一项，跳转到该项的详情页
//             *  传入新页面的同名参数有 bookId, name, price, url, details
//             */
//            holder.bookView.setOnClickListener(v -> {
//                int position = holder.getAdapterPosition();
//                FavoriteBook book = list.get(position);
//                Intent intent = new Intent(v.getContext(), ProductPage.class);
//                intent.putExtra("bookId", book.getBookId());
//                intent.putExtra("name", book.getName());
//                intent.putExtra("price", book.getPrice());
//                intent.putExtra("url", book.getUrl());
//                intent.putExtra("details", book.getDetails());
//                v.getContext().startActivity(intent);
//            });
//            return holder;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {
//            FavoriteBook book = list.get(position);
//            holder.bookName.setText(book.getName());
//            holder.bookPrice.setText("￥" + book.getPrice());
//            // 获取书的图片并显示
//            Request request = new Request.Builder()
//                    .url(book.getUrl())
//                    .build();
//            OkHttpClient client = new OkHttpClient();
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//            try (Response response = client.newCall(request).execute()) {
//                InputStream inputStream = response.body().byteStream();
//                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                holder.bookImage.setImageBitmap(bitmap);
//                System.out.println("收藏夹显示书图成功:" + bitmap + book.getUrl());
//            } catch (IOException e) {
//                System.out.println("收藏夹显示书图失败：");
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            return this.list.size();
//        }
//    }

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

