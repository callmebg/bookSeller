package com.example.bookseller;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyFragment extends Fragment {
    private ImageView head;
    private TextView mailbox;
    private Button collection;
    private Button publish;
    private Button buy;
    private ImageView settings;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main1);
//
//        // 由 “我的” 跳转到 “我收藏的”
//        Button collection = (Button) findViewById(R.id.collection);
//        collection.setOnClickListener(view -> {
//            Intent intent = new Intent(BaseActivity.this, LoginActivity01.class);
//            startActivity(intent);
//        });
//
//        // 由 “我的” 跳转到 “我发布的”
//        Button publish = (Button) findViewById(R.id.publish);
//        publish.setOnClickListener(view -> {
//            Intent intent = new Intent(MyFragment.this, RegisterActivity01.class);
//            startActivity(intent);
//        });
//
//        // 由 “我的” 跳转到 “我买入的”
//        Button buy = (Button) findViewById(R.id.buy);
//
//        // 由 “我的” 跳转到 “设置”
//        ImageView settings = (ImageView) findViewById(R.id.settings);
        View view = View.inflate(getActivity(), R.layout.activity_my_fragment, null);
        Button collection = (Button) view.findViewById(R.id.collection);
        collection.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), LoginActivity01.class);
                startActivity(intent);
            }
        });

        // 由 “我的” 跳转到 “我发布的”
        Button publish = (Button) view.findViewById(R.id.publish);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), RegisterActivity01.class);
                startActivity(intent);
            }
        });

        // 由 “我的” 跳转到 “我买入的”
        Button buy = (Button) view.findViewById(R.id.buy);

        // 由 “我的” 跳转到 “设置”
        ImageView settings = (ImageView) view.findViewById(R.id.settings);

        return view;
    }
}