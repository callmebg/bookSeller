package com.example.bookseller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
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
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatList extends AppCompatActivity {
    private static final String TAG = "ChatList";
    private List<ChatListItem> chatList = new ArrayList<>();
    private RecyclerView msgRecyclerView;
    private ChatListItemAdapter adapter;
    private ImageView back;
    private TextView refresh;

    private String uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();//隐藏标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);

        //我自己的id
        uuid = getmyuuid();
        if(uuid == null){
            Toast.makeText(ChatList.this, "网络错误，请稍后重试",Toast.LENGTH_LONG).show();
            finish();
        }
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
        getAllChatMessage();
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

    private void getAllChatMessage() {
        String URLNAME = "/getUserAllChatMessage";
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        String token = "Bearer " + sp.getString("token", "");

        Toast.makeText(ChatList.this, "刷新中...", Toast.LENGTH_SHORT).show();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(NetworkUtils.getRequestUrl(URLNAME))
                .addHeader("Authorization", token)
                .get()
                .build();

        NetworkUtils.forceNetworkRequesting();
        try (Response response = client.newCall(request).execute()) {
            String chatlistResponseData = response.body().string();
            Log.d("chatlistResponseData", chatlistResponseData);

            // 处理返回信息
            JSONObject jsonObject = new JSONObject(chatlistResponseData);
            boolean isSuccess = jsonObject.getBoolean("success");
            Log.d("chatlistResponseData", String.valueOf(isSuccess));
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
     * 处理由服务器返回的相关聊天数据
     *
     * @param responseData 服务器返回的数据
     */
    private void handlerResponseData(String responseData) throws JSONException {
        //给对方uuid去个重
        HashMap<String, String> tmap  = new HashMap<String, String>();

        Log.d("chatlistResponseData_handler", responseData);
        JSONArray array = new JSONArray(responseData);

        //tmap的keys就是去重的对方id
        for(int i = 0; i < array.length(); i++){
            JSONObject t = array.optJSONObject(i);

            if(!t.getString("recId").equals(uuid)){
                tmap.put(t.getString("recId"), t.getString("recName"));
            }
            if(!t.getString("senId").equals(uuid)){
                tmap.put(t.getString("senId"), t.getString("senName"));
            }
        }
        Set<String> tset = tmap.keySet();
        for(String t : tset){
            Log.d("ttt", t);
            chatList.add(new ChatListItem(t, tmap.get(t)));
        }
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
                Toast.makeText(ChatList.this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
                System.out.println("获取用户uuid处理异常：");
                e.printStackTrace();
            }

        } catch (IOException e) {
            Toast.makeText(ChatList.this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("网络异常：");
            e.printStackTrace();
        }
        return null;
    }
}



