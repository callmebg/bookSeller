package com.example.bookseller;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

public class ProductPage extends BaseActivity {
    private ImageView imageView;
    private TextView shoppingfirst;
    private TextView shoppingsec;
    private TextView shoppingmoney;
    private Button shopping_btn;
    private ImageView shopping_back;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        imageView = findViewById(R.id.shopping_image);
        shoppingfirst = findViewById(R.id.shopping_firsttext);
        shoppingsec = findViewById(R.id.shopping_sectextview);
        shoppingmoney = findViewById(R.id.shopping_money);
        shopping_btn = findViewById(R.id.shopping_btn);
        shopping_back = findViewById(R.id.shopping_back);
        //接受母Intent信息
        Intent intent = getIntent();
        Integer image = intent.getIntExtra("image", 0);
        String firsttext = intent.getStringExtra("firstshop");
        String sectext = intent.getStringExtra("secshop");
        String money = "¥" + intent.getDoubleExtra("price", 0.00);
        imageView.setImageResource(image);
        shoppingfirst.setText(firsttext);
        shoppingsec.setText(sectext);
        shoppingmoney.setText(money);

        shopping_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                ProductPage.this.finish();
            }
        });

        shopping_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "");
                if(!username.equals("")){
                    //上传数据库
                    Toast.makeText(ProductPage.this, "已加入购物车", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(ProductPage.this, "请先登录哦", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}