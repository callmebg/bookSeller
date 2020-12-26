package com.example.bookseller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatList extends AppCompatActivity {
    private List<ChatListItem> chatList = new ArrayList<>();
    private RecyclerView msgRecyclerView;
    private ChatListItemAdapter adapter;
    private ImageView back;
    private TextView refresh;

    private String chaterUid, chaterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();//隐藏标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);

/*
        //接收对象的id和名字
        Intent intent = getIntent();
        chaterUid = intent.getStringExtra("chaterUid");
        chaterName = intent.getStringExtra("chaterName");
*/

        initView();//初始化UI控件
        initData();//初始化数据
        initListener();//初始化事件监听器
    }

    private void initView() {
        msgRecyclerView = (RecyclerView) findViewById(R.id.chatlist_recycler_view);

        refresh = (TextView) findViewById(R.id.chatlist_refresh);
        back = (ImageView) findViewById(R.id.chatlist_back);
    }

    private void initData() {
        initmessage();//初始化信息
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new ChatListItemAdapter(chatList);

        // 通过适配器调用内部类的内部方法，获取position。这就相当于内部点击事件把item条目位置给了接口  外部调用接口获取item
        adapter.setOnItemClickListener(new ChatListItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ChatList.this, Chat.class);
                intent.putExtra("chaterUid",chatList.get(position).getUuid().toString());
                intent.putExtra("chaterName",chatList.get(position).getName().toString());
                startActivity(intent);
            }
        });

        msgRecyclerView.setAdapter(adapter);
    }

    private void initmessage() {
        chatList.clear();
        //访问接口获取数据

        //getChatMessage();

        ChatListItem msg1 = new ChatListItem("110", "张思");
        chatList.add(msg1);
        ChatListItem msg2 = new ChatListItem("120", "张si");
        chatList.add(msg2);
    }


    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //刷新
                initData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getChatMessage() {
        String ip = this.getResources().getString(R.string.ip);
        String port = this.getResources().getString(R.string.port);
        String URL = "http://" + ip + ":" + port + "/getChatMessage";
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        String token = sp.getString("token", null);

        Toast toast = Toast.makeText(ChatList.this, "刷新中...", Toast.LENGTH_SHORT);
        toast.show();

        MultipartBody.Builder builder=new MultipartBody.Builder();
        builder.addFormDataPart("releaseId",chaterUid);    //对方uid
        RequestBody requestBody=builder.build();
        Request request = new Request.Builder()
                .url(URL)
                .addHeader("Authorization",token)     //登录认证得到的token
                .post(requestBody)
                .build();
        OkHttpClient okHttpClient=new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("cfcf",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody=response.body().string();
                Log.d("cfcf",responseBody);
            }
        });

    }

    /**
     * 处理由服务器返回的相关登录数据
     *
     * @param responseData 服务器返回的数据
     */
    private void handlerResponseData(String responseData) {
        /*
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(responseData);
            String status = jsonObject.getString("status");
            switch (status) {
                case "200": {
                    String token = jsonObject.getString("data");
                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                    editor.putString("token", token);
                    editor.putString("username", username);
                    editor.putBoolean("is_login", true);
                    editor.apply();

                    if (toast != null) {
                        toast.cancel();
                        Toast.makeText(LoginActivity01.this, "登录成功", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(LoginActivity01.this, MainActivity.class);
                    startActivity(intent);
                    break;
                }
                case "500": {    // 用户名或密码不正确
                    if (toast != null) {
                        toast.cancel();
                        Toast.makeText(LoginActivity01.this, "名字和密码不正确", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default: {
                    Toast.makeText(LoginActivity01.this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            Toast.makeText(LoginActivity01.this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("注册返回信息处理异常：");
            e.printStackTrace();
        }*/
    }
    /**
     * 定义RecyclerView选项单击事件的回调接口
     */
    public interface OnItemClickListener{//也可以不在这个activity或者是fragment中来声明接口，可以在项目中单独创建一个interface，就改成static就OK
        //参数（父组件，当前单击的View,单击的View的位置，数据）
        void onItemClick(RecyclerView parent,View view, int position, ChatListItem data);
        // void onItemLongClick(View view);类似，我这里没用就不写了
        //
        //这个data是List中放的数据类型，因为我这里是private List<Map> mapList;这样一个
        //然后我的每个item是这样的：
        //        HashMap map =new HashMap();
        //        map.put("img",R.drawable.delete);
        //        map.put("text","x1");
        //所以我的是map类型的，那如果是item中只有text的话比如List<String>，那么data就改成String类型
    }
}



