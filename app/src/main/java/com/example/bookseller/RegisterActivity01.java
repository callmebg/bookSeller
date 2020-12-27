package com.example.bookseller;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity01 extends AppCompatActivity {

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register01);
        this.setTitle("注册");

        Button registerBtn = (Button) findViewById(R.id.register);

        EditText regNicknameTextView = (EditText) findViewById(R.id.regNickname);
        EditText regMailboxTextView = (EditText) findViewById(R.id.regMailbox);
        EditText regPasswordTextView = (EditText) findViewById(R.id.regPassword);
        EditText regConfirmTextView = (EditText) findViewById(R.id.regConfirm);
        List<EditText> editTextList = new ArrayList<>();
        editTextList.add(regNicknameTextView);
        editTextList.add(regMailboxTextView);
        editTextList.add(regPasswordTextView);
        editTextList.add(regConfirmTextView);
        TextWatcher textWatcher = new TextWatcherImpl01(registerBtn, editTextList);
//        regNicknameTextView.addTextChangedListener(textWatcher);
//        regMailboxTextView.addTextChangedListener(textWatcher);
//        regPasswordTextView.addTextChangedListener(textWatcher);
//        regConfirmTextView.addTextChangedListener(textWatcher);

        registerBtn.setOnClickListener(view -> {
            String regNickname = regNicknameTextView.getText().toString();
            String regMailbox = regMailboxTextView.getText().toString();
            String regPassword = regPasswordTextView.getText().toString();
            String regConfirm = regConfirmTextView.getText().toString();
            System.out.println(regNickname + "," + regMailbox + "," + regPassword + "," + regConfirm);
            register(regNickname, regMailbox, regPassword, regConfirm);
        });
    }

    private void register(String regNickname, String regMailbox, String regPassword, String regConfirm) {
        if (!TextUtils.equals(regPassword, regConfirm)) {
            Toast.makeText(RegisterActivity01.this, "两次输入的密码不同", Toast.LENGTH_SHORT).show();
        } else {
            toast = Toast.makeText(RegisterActivity01.this, "注册中...", Toast.LENGTH_SHORT);
            toast.show();

            OkHttpClient client = new OkHttpClient();
            // 请求体：邮箱，用户名和密码
            RequestBody requestBody = new FormBody.Builder()
                    .add("email", regMailbox)
                    .add("password", regPassword)
                    .add("username", regNickname)
                    .build();

            // 请求地址
            String ip = this.getResources().getString(R.string.ip);
            String port = this.getResources().getString(R.string.port);
            String registerUrl = "http://" + ip + ":" + port + "/register";
            Request request = new Request.Builder().url(registerUrl).post(requestBody).build();

            NetworkUtils.forceNetworkRequesting();
            // 进行网络请求并处理返回信息
            try (Response response = client.newCall(request).execute()) {
                String responseData = response.body().string();
                System.out.println("register-responseData:" + responseData);
                handleResponseData(responseData);
            } catch (IOException e) {
                Toast.makeText(RegisterActivity01.this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
                System.out.println("网络异常：");
                e.printStackTrace();
            }
        }
    }

    private boolean handleResponseData(String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            String status = jsonObject.getString("status");
            switch (status) {
                case "200": {   // 注册成功：显示提示信息并跳转到 “我的" 界面
                    if (toast != null) {
                        toast.cancel();
                        Toast.makeText(RegisterActivity01.this, "注册成功", Toast.LENGTH_SHORT).show();
                    }
//                    Intent intent = new Intent(RegisterActivity01.this, LoginActivity01.class);
//                    startActivity(intent);
                    finish();
                    return true;
                }
                case "500": {   // 注册失败
                    String message = jsonObject.getString("message");
                    switch (message) {
                        case "重复邮箱": {  // 邮箱已存在
                            if (toast != null) {
                                toast.cancel();
                                Toast.makeText(RegisterActivity01.this, "邮箱已存在", Toast.LENGTH_SHORT).show();
                            }
                            return false;
                        }
                        case "重复命名": {   // 用户名已存在
                            if (toast != null) {
                                toast.cancel();
                                Toast.makeText(RegisterActivity01.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                            }
                            return false;
                        }
                    }
                }
                default: {  // 其他
                    Toast.makeText(RegisterActivity01.this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        } catch (JSONException e) { // 返回信息处理异常
            Toast.makeText(RegisterActivity01.this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("注册返回信息处理异常：");
            e.printStackTrace();
        }
        return false;
    }
}