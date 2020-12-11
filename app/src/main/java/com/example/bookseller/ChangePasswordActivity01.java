package com.example.bookseller;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class ChangePasswordActivity01 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password01);
        setTitle("修改密码");

        Button changePwdButton = findViewById(R.id.changePwdButton);

        EditText oldPwdText = findViewById(R.id.oldPwd);
        EditText newPwdText = findViewById(R.id.newPwd);
        EditText confirmPwdText = findViewById(R.id.confirmPwd);
        List<EditText> list = new ArrayList<>();
        list.add(oldPwdText);
        list.add(newPwdText);
        list.add(confirmPwdText);
        TextWatcher textWatcher = new TextWatcherImpl01(changePwdButton, list);

        changePwdButton.setOnClickListener(view -> {
            String oldpwd = oldPwdText.getText().toString();
            String newPwd = newPwdText.getText().toString();
            String confirmPwd = confirmPwdText.getText().toString();
            changePassword(oldpwd, newPwd, confirmPwd);
        });
    }

    /**
     * 修改密码处理逻辑
     *
     * @param oldPwd     旧密码
     * @param newPwd     新密码
     * @param confirmPwd 确认密码
     */
    private void changePassword(String oldPwd, String newPwd, String confirmPwd) {
        if (!TextUtils.equals(newPwd, confirmPwd)) {
            Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
            String token = "Bearer " + sp.getString("token", "");
            System.out.println("token: " + token);

            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("newPassword", newPwd)
                    .add("oldPassword", oldPwd)
                    .build();
            Request request = new Request.Builder()
                    .url(NetworkUtils.getRequestUrl("/changePassword"))
                    .addHeader("Authorization", token)
                    .post(requestBody)
                    .build();

            NetworkUtils.forceNetworkRequesting();
            try (Response response = client.newCall(request).execute()) {
                String responseData = response.body().string();
                System.out.println("changePwd-responseData:" + responseData);
                handleResponseData(responseData);
            } catch (IOException e) {
                Toast.makeText(this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
                System.out.println("网络异常：");
                e.printStackTrace();
            }
        }
    }

    private void handleResponseData(String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            String status = jsonObject.getString("status");
            switch (status) {
                case "200": {
                    Toast.makeText(this, "修改密码成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, UserInfoActivity01.class);
                    startActivity(intent);
                    break;
                }
                default: {
                    Toast.makeText(this, "原密码不正确", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            Toast.makeText(this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("修改密码返回信息处理异常：");
            e.printStackTrace();
        }
    }
}