package com.example.bookseller.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookseller.BaseActivity;
import com.example.bookseller.Product;
import com.example.bookseller.R;

import java.util.List;

public class HomeFragmentAdapter extends BaseAdapter {

    private List<Product> productList;
    private LayoutInflater inflater;
    public HomeFragmentAdapter() {}

    public HomeFragmentAdapter(List<Product> productList,Context context) {
        this.productList = productList;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return productList==null?0:productList.size();
    }

    @Override
    public Product getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //加载布局为一个视图
        View view=inflater.inflate(R.layout.simpleitem,null);
        Product product=getItem(position);
        //在view视图中查找id为image_photo的控件
        ImageView image_photo= (ImageView) view.findViewById(R.id.itemImage);
        TextView product_name= (TextView) view.findViewById(R.id.itemTitle);
        TextView product_details= (TextView) view.findViewById(R.id.itemText);
        image_photo.setImageResource(product.getProductUri());
        product_name.setText(product.getProductName());
        product_details.setText(product.getProductDetails());
        return view;
    }
}
