package com.example.bookseller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
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

import static android.content.Context.MODE_PRIVATE;

public class LoginActivity01 extends AppCompatActivity {

    private String username;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login01);
        this.setTitle("登录");

        Button loginBtn = (Button) findViewById(R.id.login);
        Button registerBtn = (Button) findViewById(R.id.register);

        EditText usernameText = (EditText) findViewById(R.id.username);
        EditText passwordText = (EditText) findViewById(R.id.password);
        List<EditText> editTextList = new ArrayList<>();
        editTextList.add(usernameText);
        editTextList.add(passwordText);
        TextWatcher textWatcher = new TextWatcherImpl01(loginBtn, editTextList);

        loginBtn.setOnClickListener(view -> {
            username = usernameText.getText().toString();
            String password = passwordText.getText().toString();
            System.out.println(username + ":" + password);
            login(username, password);
        });

        registerBtn.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity01.this, RegisterActivity01.class);
            startActivity(intent);
        });
    }

    /**
     * 当用户填写的名字和密码不为空时，提交到服务器登录，
     * 否则提示用户输入邮箱或密码
     *
     * @param username 用户名字
     * @param password 用户密码
     */
    private void login(String username, String password) {
        toast = Toast.makeText(LoginActivity01.this, "登录中...", Toast.LENGTH_SHORT);
        toast.show();

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        String ip = this.getResources().getString(R.string.ip);
        String port = this.getResources().getString(R.string.port);
        String loginUrl = "http://" + ip + ":" + port + "/userLogin";
        Request request = new Request.Builder()
                .url(loginUrl)
                .post(requestBody)
                .build();

        NetworkUtils.forceNetworkRequesting();
        try (Response response = client.newCall(request).execute()) {
            String responseData = response.body().string();
            System.out.println("login-responseData:" + responseData);
            handlerResponseData(responseData);
        } catch (IOException e) {
            Toast.makeText(LoginActivity01.this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("网络异常：");
            e.printStackTrace();
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
                    Intent intent = new Intent(this, BaseActivity.class);
                    startActivity(intent);
                    // 销毁当前活动
                    finish();
                    break;
                }
                case "500": {    // 用户名或密码不正确
                    if (toast != null) {
                        toast.cancel();
                        Toast.makeText(LoginActivity01.this, "名字或密码不正确", Toast.LENGTH_SHORT).show();
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
        }
    }
}

class NetworkUtils {

    public static final String REQUEST_URL_PREFIX = "http://47.107.117.59:80";
    public static final String USER_IMAGE_DOWNLOAD_URL_PREFIX = REQUEST_URL_PREFIX + "/static/";


    /**
     * 为指定后缀添加前缀以形成完整的请求 url
     *
     * @param suffix 后缀，如 /test,test02
     * @return 相应的请求 url，如 http://47.107.117.59:80/test, http://47.107.117.59:80/test02
     */
    public static String getRequestUrl(String suffix) {
        if (!suffix.startsWith("/"))
            suffix = "/" + suffix;
        return REQUEST_URL_PREFIX + suffix;
    }

    /**
     * 返回获取用户头像的请求路径
     *
     * @param username 用户名字
     * @return
     */
    public static String getUserImageUrl(String username) {
        return USER_IMAGE_DOWNLOAD_URL_PREFIX + username + ".jpg";
    }

    // 强制在主线程中进行网络请求
    public static void forceNetworkRequesting() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

}

class LoginUtil {

    /**
     * 用户已登录返回 true；
     * 未登录则清楚存储在本地的用户信息，并返回 false
     *
     * @param context 需要判断是否登录的活动
     * @return
     */
    public static boolean isLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences("data", MODE_PRIVATE);
        String username = sp.getString("username", null);
        String token = sp.getString("token", null);
        boolean isLogin = sp.getBoolean("is_login", false);
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(token) && isLogin == true) {
            return true;
        }

        clearUserInfo(context);
        return false;
    }

    /**
     * 清除存储在本地的用户信息
     *
     * @param context 需要判断是否登录的活动或需要跳转到登录页面的活动
     */
    public static void clearUserInfo(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

    /**
     * 跳转到登录页面，跳转之前
     *
     * @param context 需要跳转到登录页面的活动
     */
    public static void toLoginActivity(Context context) {
        clearUserInfo(context);
        Intent intent = new Intent(context, LoginActivity01.class);
        context.startActivity(intent);
    }
}