package com.example.bookseller.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookseller.BaseActivity;
import com.example.bookseller.Product;
import com.example.bookseller.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private HomeFragmentAdapter adapter;
    private ListView listView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        listView= (ListView) getView().findViewById(R.id.list_test);
//        List<Product> productList=new ArrayList<>();
//        for(int i=0;i<10;i++){
//            Product pro=new Product();
//            pro.setProductName("10"+i);
//            pro.setProductDetails("name"+i);
//            pro.setProductUri(R.mipmap.ic_launcher);
//            productList.add(pro);
//        }
//        adapter=new HomeFragmentAdapter(productList, getActivity());
//        listView.setAdapter(adapter);
        View view = inflater.inflate(R.layout.)
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



}