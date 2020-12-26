package com.example.bookseller;

import android.Manifest;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserInfoActivity01 extends AppCompatActivity {

    private static final int TAKE_PHOTO = 0;
    private static final int CHOOSE_PHOTO = 1;

    private String username;
    private String email;
    private String id;
    private String headImgUrl = null;
    private ImageView headImgInfoView;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info01);
        setTitle("个人信息");

        logoutButton = findViewById(R.id.logoutButton);
        ButtonUtils.setButtonDisabled(logoutButton);
        logoutButton.setOnClickListener(view -> {
            logout();
        });

        if (getUserInfo()) {
            ButtonUtils.setButtonEnabled(logoutButton);
            // 显示用户信息：头像，邮箱，名字和 id
            fillUserInfo();
            // 修改密码和更换头像
            changeInfo();
        }
    }

    // 注销
    private void logout() {
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        String token = "Bearer " + sp.getString("token", "");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(NetworkUtils.getRequestUrl("/userLogout"))
                .addHeader("Authorization", token)
                .post(new FormBody.Builder().build())
                .build();

        NetworkUtils.forceNetworkRequesting();
        try (Response response = client.newCall(request).execute()) {
            String logoutResponseData = response.body().string();
            System.out.println("logoutResponseData" + logoutResponseData);

            // 处理注销返回信息
            JSONObject jsonObject = new JSONObject(logoutResponseData);
            boolean isSuccess = jsonObject.getBoolean("success");
            if (isSuccess) {
                // 清除用户的信息
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();
                ButtonUtils.setButtonDisabled(logoutButton);

                new AlertDialog.Builder(this)
                        .setTitle("注销成功")
                        .setMessage("注销成功")
                        .setPositiveButton("确定", (dialog, which) -> {
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                        })
                        .show();
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
     * 将获取到的用户信息显示到页面上
     */
    private void fillUserInfo() {

        // 显示用户名、邮箱和 id
        TextView usernameInfo = findViewById(R.id.usernameInfo);
        usernameInfo.setText(username);
        TextView emailInfo = findViewById(R.id.emailInfo);
        emailInfo.setText(email);
        TextView idInfo = findViewById(R.id.idInfo);
        idInfo.setText(id);

        // 显示头像
        headImgInfoView = findViewById(R.id.headImgInfo);
        showUserImage();
    }

    // 显示头像
    private void showUserImage() {
        Request request = new Request.Builder()
                .url(NetworkUtils.getUserImageUrl(username))
                .build();
        OkHttpClient client = new OkHttpClient();
        NetworkUtils.forceNetworkRequesting();
        try (Response response = client.newCall(request).execute()) {
            InputStream inputStream = response.body().byteStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            runOnUiThread(() -> headImgInfoView.setImageBitmap(bitmap));
            System.out.println("个人信息页面显示头像成功");
        } catch (IOException e) {
            Toast.makeText(this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("我的页面获取头像失败：");
            e.printStackTrace();
        }
    }

    /**
     * 点击修改密码按钮跳转到“修改密码”页面
     * 点击头像更换头像
     */
    private void changeInfo() {
        Button changePwdButton = findViewById(R.id.changePwdButton);
        changePwdButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, ChangePasswordActivity01.class);
            startActivity(intent);
        });

        // 更换头像
        LinearLayout headImgInfoLayout = findViewById(R.id.headImgInfoLayout);
        headImgInfoLayout.setOnClickListener(view -> showSelectingSourceDialog());
    }

    private void showSelectingSourceDialog() {
        final String[] items = {"拍摄照片", "从相册中选择", "取消"};
        AlertDialog.Builder listDialog = new AlertDialog.Builder(this);
        listDialog.setTitle("选择头像来源");
        listDialog.setItems(items, (DialogInterface dialog, int index) -> {
            switch (index) {
                case 0: {
                    new AlertDialog.Builder(this)
                            .setTitle("敬请期待...")
                            .setMessage("敬请期待...")
                            .setPositiveButton("确定", null)
                            .show();
                    break;
                }
                case 1: {
                    // 运行时申请 SD 卡权限
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    } else {
                        openAlbum();
                    }
                    break;
                }
                default:
            }
        });
        listDialog.show();
    }

    // 打开相册
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    // 权限申请结果处理
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("权限申请失败")
                            .setMessage("需要授权存储空间权限，以正常使用设置头像等功能")
                            .setPositiveButton("确定", null)
                            .show();
                }
            default:
        }
    }

    // 相片返回结果处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO: {
                new AlertDialog.Builder(this)
                        .setTitle("完善功能中...")
                        .setMessage("完善功能中...")
                        .setPositiveButton("确定", null)
                        .show();
                break;
            }
            case CHOOSE_PHOTO: {
                if (resultCode == RESULT_OK) {
                    String imagePath = null;
                    Uri uri = data.getData();
                    System.out.println("uri:" + uri);
                    if (DocumentsContract.isDocumentUri(this, uri)) {
                        String docId = DocumentsContract.getDocumentId(uri);
                        if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                            String id = docId.split(":")[1];
                            String selection = MediaStore.Images.Media._ID + "=" + id;
                            imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                        } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                            Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                            imagePath = getImagePath(contentUri, null);
                        }
                    } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                        System.out.println("uri-scheme: content");
                        imagePath = getImagePath(uri, null);
                    } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                        System.out.println("uri-scheme: file");
                        imagePath = uri.getPath();
                    }
                    displayImage(imagePath);

                    // 将选择的头像上传到服务器
                    uploadUserImage(imagePath);
                }
                break;
            }
            default:
        }
    }

    // 获取图片路径
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    // 显示图片
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            if (bitmap != null)
                headImgInfoView.setImageBitmap(bitmap);
        } else {
            Toast.makeText(UserInfoActivity01.this, "无法获取相片", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取用户信息：如果用户未登录，则返回 false；如果已登录，则获取用户信息，返回 true
     */
    private boolean getUserInfo() {
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
                        .setTitle("登录凭证过期")
                        .setMessage("登录凭证过期，请重新登录")
                        .setPositiveButton("确定", (dialog, which) -> {
                            LoginUtil.toLoginActivity(this);
                        })
                        .show();
                return false;
            }
            return handleResponseData(responseData);
        } catch (IOException e) {
            Toast.makeText(UserInfoActivity01.this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("网络异常：");
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 处理 获取用户信息 的返回信息
     *
     * @param responseData 服务器返回的响应体内容
     * @return
     */
    private boolean handleResponseData(String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            String status = jsonObject.getString("status");
            switch (status) {
                case "200": {
                    JSONObject data = new JSONObject(jsonObject.getString("data"));
                    username = data.getString("username");
                    email = data.getString("email");
                    id = data.getString("id");
                    if (!data.isNull("url"))
                        headImgUrl = data.getString("url");
                    return true;
                }
                default: {
                    return false;
                }
            }
        } catch (JSONException e) {
            Toast.makeText(UserInfoActivity01.this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("获取用户信息处理异常：");
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 上传用户头像到服务器
     *
     * @param imagePath 头像的本地路径
     */
    private void uploadUserImage(String imagePath) {
        // 保存头像本地路径
//        System.out.println("imagePath:" + imagePath);
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
//        editor.putString("imagePath", imagePath);
//        editor.apply();

        System.out.println("uploadUserImage()");
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        String username = sp.getString("username", null);
        System.out.println("username:" + username);
        String token = "Bearer " + sp.getString("token", null);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .addFormDataPart("img", username + ".jpg", RequestBody.create(MediaType.parse("image/jpeg"), new File(imagePath)))
                .build();
        Request request = new Request.Builder()
                .url(NetworkUtils.getRequestUrl("/userImgUpload"))
                .addHeader("Authorization", token)     //登录认证得到的token
                .post(requestBody)
                .build();

        NetworkUtils.forceNetworkRequesting();
        try (Response response = client.newCall(request).execute()) {
            String responseData = response.body().string();
            System.out.println("userImgUpload-responseData:" + responseData);

            // 处理用户头像上传返回信息
            JSONObject jsonObject = new JSONObject(responseData);
            String status = jsonObject.getString("status");
            switch (status) {
                case "200": {
                    String imageUrl = jsonObject.getString("data");
                    editor.putString("url", imageUrl);
                    editor.apply();
                    break;
                }
                case "403": {
                    new AlertDialog.Builder(this)
                            .setTitle("登录凭证失效")
                            .setMessage("登录凭证失效，请重新登录")
                            .setPositiveButton("确定", (dialog, which) -> {
                                Intent intent = new Intent(this, LoginActivity01.class);
                                startActivity(intent);
                            })
                            .show();
                    break;
                }
                default: {
                    Toast.makeText(this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (IOException e) {
            Toast.makeText(this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("网络异常：");
            e.printStackTrace();
        } catch (JSONException e) {
            Toast.makeText(this, "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("上传用户头像返回信息处理异常：");
            e.printStackTrace();
        }
    }

}