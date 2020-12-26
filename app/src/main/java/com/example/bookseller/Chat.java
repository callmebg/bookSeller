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

public class Chat extends AppCompatActivity {
    private List<Msg> msgList = new ArrayList<>();
    private Button send;
    private EditText inputText;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    private ImageView back;
    private TextView chater;

    private String chaterUid, chaterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();//隐藏标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        //接收对象的id和名字
        Intent intent = getIntent();
        chaterUid = intent.getStringExtra("chaterUid");
        chaterName = intent.getStringExtra("chaterName");

        initView();//初始化UI控件
        initData();//初始化数据
        initListener();//初始化事件监听器
    }

    private void initView() {
        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        msgRecyclerView = (RecyclerView) findViewById(R.id.msg_recycler_view);

        chater = (TextView) findViewById(R.id.chat_name);
        chater.setText(chaterName);

        back = (ImageView) findViewById(R.id.chat_back);
        chater = (TextView) findViewById(R.id.chat_name);
    }

    private void initData() {
        initmessage();//初始化信息
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
    }

    private void initmessage() {
        msgList.clear();
        //访问接口获取数据

        //getChatMessage();

        Msg msg1 = new Msg("hello guy", "2020年12月25日21:14:13", Msg.TYPE_RECEIVE);
        msgList.add(msg1);
        Msg msg2 = new Msg("hello,who is that?", "2020年12月25日21:24:13", Msg.TYPE_SEND);
        msgList.add(msg2);
        Msg msg3 = new Msg("This is Tom.Nice to meet you.", "2020年12月25日21:34:13", Msg.TYPE_RECEIVE);
        msgList.add(msg3);



    }

    private String getMegTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String gmt = dateFormat.format(new Date());
        return gmt;
    }

    private void initListener() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                String contentTime = getMegTime();
                if (!"".equals(content)) {
                    //如果字符串不为空，则创建Msg对象

                    //本地信息及时显示上去
                    Msg msg = new Msg(content, contentTime, Msg.TYPE_SEND);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size() - 1);//当有新消息是刷新RecyclerView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);//将RecyclerView定位在最后一行
                    inputText.setText("");//清空输入框的内容
                    inputText.requestFocus();//输入光标回到输入框中

                    //发到服务器

                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        chater.setOnClickListener(new View.OnClickListener() {
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

        Toast toast = Toast.makeText(Chat.this, "刷新中...", Toast.LENGTH_SHORT);
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

}

