package com.example.bookseller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity01 extends AppCompatActivity {

    private int RESULT_LOAD_IMG = 1;
    private Uri regHeadImageUri = null;
    private Bitmap regHeadImageBitmap = null;
    private String regHeadImagePath = null;
    private Button registerBtn;
    private ImageView regHeadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register01);

        registerBtn = (Button) findViewById(R.id.register);
        regHeadView = (ImageView) findViewById(R.id.regHeadImg);

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
        regNicknameTextView.addTextChangedListener(textWatcher);
        regMailboxTextView.addTextChangedListener(textWatcher);
        regPasswordTextView.addTextChangedListener(textWatcher);
        regConfirmTextView.addTextChangedListener(textWatcher);

        registerBtn.setOnClickListener(view -> {
            if (regHeadImageUri == null) {
                Toast.makeText(this, "请选择头像", Toast.LENGTH_LONG).show();
                ButtonUtils.setButtonDisabled(registerBtn);
                return;
            }
            String regNickname = regNicknameTextView.getText().toString();
            String regMailbox = regMailboxTextView.getText().toString();
            String regPassword = regPasswordTextView.getText().toString();
            String regConfirm = regConfirmTextView.getText().toString();
            System.out.println(regHeadImageUri + "," + regNickname + "," + regMailbox + "," + regPassword + "," + regConfirm);
            register(regNickname, regMailbox, regPassword, regConfirm);
        });

        regHeadView.setOnClickListener(view -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
                regHeadImageUri = data.getData();
                System.out.println("regHeadImageUri:" + regHeadImageUri.toString());
//                regHeadView.setImageURI(regHeadImageUri);


//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//                // 获取游标
//                Cursor cursor = getContentResolver().query(regHeadImageUri, null, null, null, null);
//                cursor.moveToFirst();
//
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                regHeadImagePath = cursor.getString(columnIndex);
//                System.out.println("regHeadImagePath:" + regHeadImagePath);
//                cursor.close();
//
//                regHeadImageBitmap = BitmapFactory.decodeFile(regHeadImagePath);
//                regHeadView.setImageBitmap(regHeadImageBitmap);
//                System.out.println("regHeadImageBitmap:" + regHeadImageBitmap);

                try (InputStream is = getContentResolver().openInputStream(regHeadImageUri)) {
                    regHeadImageBitmap = BitmapFactory.decodeStream(is, null, new BitmapFactory.Options());
                }

                regHeadView.setImageBitmap(regHeadImageBitmap);
                System.out.println("regHeadImageBitmap:" + regHeadImageBitmap);

                ButtonUtils.setButtonEnabled(registerBtn);
            } else if (regHeadImageUri == null) {
                Toast.makeText(this, "请选择头像", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "系统错误", Toast.LENGTH_LONG).show();
        }

    }

    private void register(String regNickname, String regMailbox, String regPassword, String regConfirm) {
        if (TextUtils.isEmpty(regNickname) || TextUtils.isEmpty(regMailbox) || TextUtils.isEmpty(regPassword) || TextUtils.isEmpty(regConfirm)) {
            Toast.makeText(RegisterActivity01.this, "请填写全部信息", Toast.LENGTH_LONG).show();
//            ButtonUtils.setButtonDisabled(registerBtn);
        } else if (!TextUtils.equals(regPassword, regConfirm)) {
            Toast.makeText(RegisterActivity01.this, "两次输入的密码不同", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(RegisterActivity01.this, "注册中...", Toast.LENGTH_LONG).show();

            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("profile_url", regHeadImageUri.toString())
                    .add("nick", regNickname)
                    .add("mail", regMailbox)
                    .add("password", regPassword)
                    .build();

            String ip = this.getResources().getString(R.string.ip);
            String port = this.getResources().getString(R.string.port);
            String registerUrl = "http://" + ip + ":" + port + "/register";
            Request request = new Request.Builder().url(registerUrl).post(requestBody).build();

            try (Response response = client.newCall(request).execute()) {
                String responseData = response.body().string();
                boolean regSuccess = handlerResponseData(responseData);

                if (regSuccess) {
                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                    editor.putString("profile_url", regHeadImageUri.toString());
                    editor.putString("nick", regNickname);
                    editor.apply();
                }

                // 注册完成之后跳转到 "我的” 页面
                Intent intent = new Intent(RegisterActivity01.this, MainActivity.class);
                startActivity(intent);
            } catch (IOException e) {
                Toast.makeText(RegisterActivity01.this, "网络异常", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean handlerResponseData(String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            String message = jsonObject.getString("message");
            System.out.println(message);
            String status = jsonObject.getString("status");
            switch (status) {
                case "20000": {
                    Toast.makeText(RegisterActivity01.this, "邮箱已存在", Toast.LENGTH_LONG).show();
                    return false;
                }
                case "20001": {
                    Toast.makeText(RegisterActivity01.this, "名字已存在", Toast.LENGTH_LONG).show();
                    return false;
                }
                case "10000": {
                    String uuid = jsonObject.getString("uuid");
                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                    editor.putString("uuid", uuid);
                    editor.apply();
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}