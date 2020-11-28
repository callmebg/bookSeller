package com.example.bookseller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.bookseller.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationItemView;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        if(Build.VERSION.SDK_INT >= 21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        bottomNavigationItemView = findViewById(R.id.nav_view);
        viewPager = findViewById(R.id.view_pager);
        //设置缓存Pages个数
        viewPager.setOffscreenPageLimit(6);
        List<Fragment> fragments = new ArrayList<>();
        /*还没写fragment
        fragments.add(new HomeFragment());
        fragments.add(new AddFragment());
        fragments.add(new MyFragment());
        */
        //绑定Adapter
        FragmentAdapter fragmentAdapter = new FragmentAdapter(fragments, getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
        //相应按钮操作切换界面
        bottomNavigationItemView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
                int menuID = menuItem.getItemId();

                switch (menuID){
                    case R.id.navigation_home:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.navigation_sport:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.navigation_user:
                        viewPager.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });
        //设置页面切换监听器
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){

            }

            @Override
            public void onPageSelected(int position){
                bottomNavigationItemView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state){

            }
        });
    }


}