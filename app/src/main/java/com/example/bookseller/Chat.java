package com.example.bookseller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
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
    private String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();//隐藏标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        //接收对象的id和名字
        Intent intent = getIntent();
        chaterUid = intent.getStringExtra("chaterUid");
        chaterName = intent.getStringExtra("chaterName");

        //我自己的id
        uuid = getmyuuid();
        if (uuid == null) {
            Toast.makeText(Chat.this, "网络错误，请稍后重试", Toast.LENGTH_LONG).show();
            finish();
        }
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
        msgRecyclerView.scrollToPosition(msgList.size() - 1);//将RecyclerView定位在最后一行
    }

    private void initmessage() {
        msgList.clear();
        //访问接口获取数据
        getChatMessage();
    }

    private String getMegTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
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

/*                    //本地信息及时显示上去，还是刷新靠谱
                    Msg msg = new Msg(content, contentTime, Msg.TYPE_SEND);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size() - 1);//当有新消息是刷新RecyclerView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1);//将RecyclerView定位在最后一行
                    inputText.setText("");//清空输入框的内容
                    inputText.requestFocus();//输入光标回到输入框中*/

                    //发到服务器
                    sendMessage(content);
                    inputText.setText("");//清空输入框的内容
                    inputText.requestFocus();//输入光标回到输入框中
                    //刷新
                    initData();
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

    private  void sendMessage(String content){
        String URLNAME = "/insertChatMessage";
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        String token = "Bearer " + sp.getString("token", "");

        String sendContent = String.format("{\"chatList\":[{" +
                "\"date\":\"%s\"," +
                "\"details\":\"%s\"," +
                "\"recId\":\"%s\"," +
                "\"senId\":\"%s\"" +
                "}]}",getMegTime(),content,chaterUid,uuid);
        Log.d("chatSendResponseData", sendContent);
        Toast.makeText(Chat.this, "发送中...", Toast.LENGTH_SHORT).show();

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), sendContent);
        /*
        json格式数据要用上面的传
        RequestBody requestBody = new FormBody.Builder()
                .add("chatDto", sendContent)
                .build();
         */
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(NetworkUtils.getRequestUrl(URLNAME))
                .addHeader("Authorization", token)
                .post(requestBody)
                .build();

        NetworkUtils.forceNetworkRequesting();
        try (Response response = client.newCall(request).execute()) {
            String chatResponseData = response.body().string();
            Log.d("chatSendResponseData", chatResponseData);

            // 不处理返回信息
        } catch (IOException e) {
            Toast.makeText(this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("网络异常：");
            e.printStackTrace();
        }
    }

    private void getChatMessage() {
        String URLNAME = "/getChatMessage";
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        String token = "Bearer " + sp.getString("token", "");

        Toast.makeText(Chat.this, "刷新中...", Toast.LENGTH_SHORT).show();
        RequestBody requestBody = new FormBody.Builder()
                .add("anotherUserId", chaterUid)
                .build();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(NetworkUtils.getRequestUrl(URLNAME))
                .addHeader("Authorization", token)
                .post(requestBody)
                .build();

        NetworkUtils.forceNetworkRequesting();
        try (Response response = client.newCall(request).execute()) {
            String chatResponseData = response.body().string();
            Log.d("chatResponseData", chatResponseData);

            // 处理返回信息
            JSONObject jsonObject = new JSONObject(chatResponseData);
            boolean isSuccess = jsonObject.getBoolean("success");
            Log.d("chatResponseData", String.valueOf(isSuccess));
            if (isSuccess) {
                // 处理信息
                handlerResponseData(jsonObject.getString("data"));
            }
        } catch (IOException e) {
            Toast.makeText(this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("网络异常：");
            e.printStackTrace();
        } catch (JSONException e) {
            Toast.makeText(this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("注销返回信息处理异常：");
            e.printStackTrace();
        }

    }

    /**
     * 处理由服务器返回的相关登录数据
     *
     * @param responseData 服务器返回的数据
     */
    private void handlerResponseData(String responseData) throws JSONException {
        Log.d("chatResponseData_handler", responseData);
        JSONArray array = new JSONArray(responseData);

        //tmap的keys就是去重的对方id
        for (int i = 0; i < array.length(); i++) {
            JSONObject t = array.optJSONObject(i);
            if (t.getString("senId").equals(uuid)) {
                msgList.add(new Msg(t.getString("details"),t.getString("date"), Msg.TYPE_SEND));
            } else {
                msgList.add(new Msg(t.getString("details"),t.getString("date"), Msg.TYPE_RECEIVE));
            }
        }
    }

    /**
     * 获取用户uuid
     */
    private String getmyuuid() {
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        String token = "Bearer " + sp.getString("token", "");

        OkHttpClient client = new OkHttpClient();
        String userInfoUrl = NetworkUtils.getRequestUrl("/getUserDetails");
        Request request = new Request.Builder()
                .url(userInfoUrl)
                .addHeader("Authorization", token)
                .get().build();

        NetworkUtils.forceNetworkRequesting();
        try (Response response = client.newCall(request).execute()) {
            String responseData = response.body().string();
            System.out.println("userInfo-responseData:" + responseData);
            if (TextUtils.isEmpty(responseData)) {
                new AlertDialog.Builder(this)
                        .setTitle("登录凭证失效")
                        .setMessage("登录凭证失效，请重新登录")
                        .setPositiveButton("确定", (dialog, which) -> {
                            Intent intent = new Intent(this, LoginActivity01.class);
                            startActivity(intent);
                        })
                        .show();
                return null;
            }
            String ans;
            try {
                JSONObject jsonObject = new JSONObject(responseData);
                String status = jsonObject.getString("status");
                switch (status) {
                    case "200": {
                        JSONObject data = new JSONObject(jsonObject.getString("data"));
                        ans = data.getString("id");
                        return ans;
                    }
                    default: {
                        return null;
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(Chat.this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
                System.out.println("获取用户uuid处理异常：");
                e.printStackTrace();
            }

        } catch (IOException e) {
            Toast.makeText(Chat.this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("网络异常：");
            e.printStackTrace();
        }
        return null;
    }
}

