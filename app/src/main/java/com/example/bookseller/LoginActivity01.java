package com.example.bookseller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class LoginActivity01 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login01);

        Button loginBtn = (Button) findViewById(R.id.login);
        Button registerBtn = (Button) findViewById(R.id.register);

        EditText mailboxText = (EditText) findViewById(R.id.mailbox);
        EditText passwordText = (EditText) findViewById(R.id.password);
        List<EditText> editTextList = new ArrayList<>();
        editTextList.add(mailboxText);
        editTextList.add(passwordText);
        TextWatcher textWatcher = new TextWatcherImpl01(loginBtn, editTextList);
        mailboxText.addTextChangedListener(textWatcher);
        passwordText.addTextChangedListener(textWatcher);

        loginBtn.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity01.this, RegisterActivity01.class);
            startActivity(intent);
            String mail = mailboxText.getText().toString();
            String password = passwordText.getText().toString();
            System.out.println(mail + ":" + password);
            login(mail, password);
        });

        registerBtn.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity01.this, RegisterActivity01.class);
            startActivity(intent);
        });
    }

    /**
     * 当用户填写的邮箱和密码不为空时，提交到服务器登录，
     * 否则提示用户输入邮箱或密码
     *
     * @param mail     用户邮箱
     * @param password 用户密码
     */
    private void login(String mail, String password) {
        Toast.makeText(LoginActivity01.this, "登录中...", Toast.LENGTH_LONG).show();

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("mail", mail).add("password", password).build();
        String ip = this.getResources().getString(R.string.ip);
        String port = this.getResources().getString(R.string.port);
        String loginUrl = "http://" + ip + ":" + port + "/login";
        Request request = new Request.Builder().url(loginUrl).post(requestBody).build();

        try (Response response = client.newCall(request).execute()) {
            String responseData = response.body().string();
            handlerResponseData(responseData);

            // 登录完成之后跳转到 “我的” 页面
            Intent intent = new Intent(LoginActivity01.this, MainActivity.class);
            startActivity(intent);
        } catch (IOException e) {
            Toast.makeText(LoginActivity01.this, "网络异常", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 处理由服务器返回的相关登录数据
     *
     * @param responseData 服务器返回的数据
     */
    private void handlerResponseData(String responseData) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(responseData);
            String status = jsonObject.getString("status");
            if (status.equals("20000"))
                Toast.makeText(LoginActivity01.this, "密码错误", Toast.LENGTH_LONG).show();
            else if (status.equals("10000")) {
                Toast.makeText(LoginActivity01.this, "登录成功", Toast.LENGTH_LONG).show();
                String message = jsonObject.getString("message");
                System.out.println(message);

                String uuid = jsonObject.getString("uuid");
                String profile_url = jsonObject.getString("profile_url");
                String nick = jsonObject.getString("nick");
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putString("uuid", uuid);
                editor.putString("profile_url", profile_url);
                editor.putString("nick", nick);
                editor.putBoolean("is_login", true);
                editor.apply();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}