package com.example.bookseller;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout ly_tab_menu_channel;
    private TextView tab_menu_channel;
    private TextView tab_menu_channel_num;
    private LinearLayout ly_tab_menu_message;
    private TextView tab_menu_message;
    private TextView tab_menu_message_num;
    private LinearLayout ly_tab_menu_better;
    private TextView tab_menu_better;
    private TextView tab_menu_better_num;
    private TextView text_topbar;
    private LinearLayout sample_product;
    private ProductPage productPage;
    private Button btn_test_jump;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        ly_tab_menu_channel.performClick();
        //隐藏默认标题栏
        ActionBar actionbar = getSupportActionBar();
        if(actionbar != null){
            actionbar.hide();
        }

    }
    //声明所有控件
    private void bindViews() {
        ly_tab_menu_channel = (LinearLayout) findViewById(R.id.ly_tab_menu_channel);
        tab_menu_channel = (TextView) findViewById(R.id.tab_menu_channel);
        tab_menu_channel_num = (TextView) findViewById(R.id.tab_menu_channel_num);
        ly_tab_menu_message = (LinearLayout) findViewById(R.id.ly_tab_menu_message);
        tab_menu_message = (TextView) findViewById(R.id.tab_menu_message);
        tab_menu_message_num = (TextView) findViewById(R.id.tab_menu_message_num);
        ly_tab_menu_better = (LinearLayout) findViewById(R.id.ly_tab_menu_better);
        tab_menu_better = (TextView) findViewById(R.id.tab_menu_better);
        tab_menu_better_num = (TextView) findViewById(R.id.tab_menu_better_num);
        text_topbar = (TextView) findViewById(R.id.txt_topbar);
        sample_product = (LinearLayout) findViewById(R.id.sample_product);
        btn_test_jump = (Button) findViewById(R.id.btn_test_jump);

        ly_tab_menu_channel.setOnClickListener(this);
        ly_tab_menu_message.setOnClickListener(this);
        ly_tab_menu_better.setOnClickListener(this);
        btn_test_jump.setOnClickListener(this);

    }
    //设置点按导航栏按钮后红点消失
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ly_tab_menu_channel:
                setSelected();
                tab_menu_channel.setSelected(true);
                tab_menu_channel_num.setVisibility(View.INVISIBLE);
                text_topbar.setText("大学城二手交易平台");
                break;
            case R.id.ly_tab_menu_message:
                setSelected();
                tab_menu_message.setSelected(true);
                tab_menu_message_num.setVisibility(View.INVISIBLE);
                text_topbar.setText("发布");
                break;
            case R.id.ly_tab_menu_better:
                //if(isLogin需要一个表示登录状态的变量){
                setSelected();
                tab_menu_better.setSelected(true);
                tab_menu_better_num.setVisibility(View.INVISIBLE);
                text_topbar.setText("我的");
                /*} else{
                            AlertDialog alert;
                            AlertDialog.Buider alertBuilder = new AlertDialog.Builder(MainActivity.this;
                            alertBuilder.setTitle("提醒")；
                            alertBuilder.setIcon(R.drawable.警告图片的id）；
                            alertBuilder.setMessage("当前尚未登录，请前往登录或注册！")；
                            alertBuilder.setPositiveButton("确定", new DialogInterface.onClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which){
                                    finish();
                                    }
                            });
                            Intent intent = new Intent(MainActivity.this, LoginPage.class);
                            startActivity(intent);
                        }*/
                break;
                //仅供测试按钮
            case R.id.btn_test_jump:
                /*productPage = new ProductPage();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.ly_content, productPage, "Product");*/
                Intent intent = new Intent(MainActivity.this, ProductPage.class);
                startActivity(intent);
                break;
        }
    }

    //重置所有文本的选中状态
    private void setSelected() {
        tab_menu_channel.setSelected(false);
        tab_menu_message.setSelected(false);
        tab_menu_better.setSelected(false);
    }
}