package com.example.bookseller;

import android.Manifest;
import android.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class PutOnFragment extends Fragment {

    private static final int TAKE_PHOTO = 0;
    private static final int CHOOSE_PHOTO = 1;

    private TextView cancelView;
    private Button buttonPut;
    private EditText storyView;
    private ConstraintLayout opmoney;
    private ConstraintLayout optel;
    private ConstraintLayout optitle;
    private TextView out_sec_txt;
    private TextView out_sec_sect_txt;
    private TextView out_sec_title;
    private ImageView picture_add;
    String num = ".*[0-9].*";
    private PutOnEvent putOnEvent = new PutOnEvent();

    // 发布图片的本地路径
    private String imagePath = null;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_put_on_fragment, container, false);
        cancelView = view.findViewById(R.id.cancelView);
        buttonPut = view.findViewById(R.id.bt_output);
        storyView = view.findViewById(R.id.storyView);
        picture_add = view.findViewById(R.id.picture_add);
        opmoney = view.findViewById(R.id.outputtab_money);
        optel = view.findViewById(R.id.outputtab_tel);
        optitle = view.findViewById(R.id.outputtab_title);
        out_sec_txt = view.findViewById(R.id.output_sec_tab);
        out_sec_sect_txt = view.findViewById(R.id.output_sec_sec_tab);
        out_sec_title = view.findViewById(R.id.output_sec_title_tab);


        putOnEvent.setPrice("");
        putOnEvent.setName("");
        //输入商品标题
        optitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(getActivity());
                new AlertDialog.Builder(getActivity()).setTitle("请输入商品标题")
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                out_sec_title.setText(editText.getText());
                                putOnEvent.setName(editText.getText().toString());
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });
        //输入商品价格
        opmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(getActivity());
                new AlertDialog.Builder(getActivity()).setTitle("请输入价格")
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!editText.getText().toString().matches(num)) {
                                    Toast.makeText(getActivity(), "请输入正常价格", Toast.LENGTH_SHORT).show();
                                } else {
                                    out_sec_txt.setText(editText.getText());
                                    putOnEvent.setPrice(editText.getText().toString());
                                }
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });

        //发布按钮
        buttonPut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //待修改获取is_login
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", MODE_PRIVATE);
                boolean is_login = sharedPreferences.getBoolean("is_login", false);
                putOnEvent.setDetail(storyView.getText().toString().trim());

                if (is_login) {
                    if (putOnEvent.getDetail().equals("")) {
                        Toast.makeText(getActivity(), "请补充描述", Toast.LENGTH_SHORT).show();
                    } else if (putOnEvent.getPrice().equals("")) {
                        Toast.makeText(getActivity(), "设置金额", Toast.LENGTH_SHORT).show();
                    } else if (putOnEvent.getName().equals("")) {
                        Toast.makeText(getActivity(), "请添加标题", Toast.LENGTH_SHORT).show();
                    } else {

                        // 将选择的头像上传到服务器
                        uploadUserImage(imagePath);

                        release(putOnEvent.getName(), "", putOnEvent.getDetail(), putOnEvent.getPrice(), putOnEvent.getUrl());
                    }
                } else {
                    Toast.makeText(getActivity(), "请先登录哦", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), LoginActivity01.class);
                    startActivity(intent);
                }
            }
        });


        //取消按钮
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BaseActivity.class);
                getContext().startActivity(intent);
            }
        });

        // 添加图片
        picture_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent();
//               intent.setAction(Intent.ACTION_PICK);
//               intent.setType("image/*");
//                startActivityForResult(intent, CHOOSE_PHOTO);
                showSelectingSourceDialog();
            }
        });

        return view;

    }

    private void showSelectingSourceDialog() {
        final String[] items = {"拍摄照片", "从相册中选择", "取消"};
        androidx.appcompat.app.AlertDialog.Builder listDialog = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        listDialog.setTitle("选择头像来源");
        listDialog.setItems(items, (DialogInterface dialog, int index) -> {
            switch (index) {
                case 0: {
                    new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                            .setTitle("敬请期待...")
                            .setMessage("敬请期待...")
                            .setPositiveButton("确定", null)
                            .show();
                    break;
                }
                case 1: {
                    // 运行时申请 SD 卡权限
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
                    new androidx.appcompat.app.AlertDialog.Builder(getActivity())
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO: {
                new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                        .setTitle("完善功能中...")
                        .setMessage("完善功能中...")
                        .setPositiveButton("确定", null)
                        .show();
                break;
            }
            case CHOOSE_PHOTO: {
                if (resultCode == RESULT_OK) {
                    imagePath = null;
                    Uri uri = data.getData();
                    System.out.println("uri:" + uri);
                    if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
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
                }
                break;
            }
            default:
        }
    }

    // 获取图片路径
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
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
                picture_add.setImageBitmap(bitmap);
        } else {
            Toast.makeText(getActivity(), "无法获取相片", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 上传用户头像到服务器
     *
     * @param imagePath 头像的本地路径
     */
    private void uploadUserImage(String imagePath) {
        System.out.println("PutOnFragment-uploadUserImage()");

        SharedPreferences sp = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String token = "Bearer " + sp.getString("token", null);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .addFormDataPart("img", putOnEvent.getName() + ".jpg", RequestBody.create(MediaType.parse("image/jpeg"), new File(imagePath)))
                .build();
        Request request = new Request.Builder()
                .url(NetworkUtils.getRequestUrl("/bookImgUpload"))
                .addHeader("Authorization", token)     //登录认证得到的token
                .post(requestBody)
                .build();

        NetworkUtils.forceNetworkRequesting();
        try (Response response = client.newCall(request).execute()) {
            String responseData = response.body().string();
            System.out.println("PutOnFragment-userImgUpload-responseData:" + responseData);

            // 处理用户头像上传返回信息
            JSONObject jsonObject = new JSONObject(responseData);
            String status = jsonObject.getString("status");
            switch (status) {
                case "200": {
                    String bookImgUrl = jsonObject.getString("data");
                    putOnEvent.setUrl(bookImgUrl);
                    break;
                }
                default: {
                    Toast.makeText(getActivity(), "请检查网络是否正常", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (IOException e) {
            Toast.makeText(getActivity(), "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("网络异常：");
            e.printStackTrace();
        } catch (JSONException e) {
            Toast.makeText(getActivity(), "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("上传发布图片返回信息处理异常：");
            e.printStackTrace();
        }
    }

    private void release(String bookName, String date, String details, String price, String url) {
        Toast.makeText(getActivity(), "正在发布", Toast.LENGTH_SHORT).show();

        SharedPreferences sp = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        String token = "Bearer " + sp.getString("token", "");

        OkHttpClient client = new OkHttpClient();

        String releaseContent = String.format("{" +
                "\"bookName\":\"%s\"," +
                "\"date\":\"%s\"," +
                "\"details\":\"%s\"," +
                "\"price\":\"%s\"," +
                "\"url\":\"%s\"" +
                "}", bookName, date, details, price, url);
        System.out.println("releaseContent:" + releaseContent);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), releaseContent);

        String ip = "47.107.117.59";
        String port = "80";
        String releaseUrl = "http://" + ip + ":" + port + "/release";
        Request request = new Request.Builder()
                .url(releaseUrl)
                .post(requestBody)
                .addHeader("Authorization", token)
                .header("ContentType", "application/json")
                .build();

        NetworkUtils.forceNetworkRequesting();

        try (Response response = client.newCall(request).execute()) {
            String responseData = response.body().string();
            System.out.println("release-responseData" + responseData);
            handleResponseData(responseData);
        } catch (IOException e) {
            Toast.makeText(getActivity(), "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("网络异常：");
            e.printStackTrace();
        }
    }


    private boolean handleResponseData(String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            String status = jsonObject.getString("status");
            switch (status) {
                case "200": {
                    Toast.makeText(getActivity(), "发布成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), BaseActivity.class);
                    startActivity(intent);
                    return true;
                }
                case "500": {
                    String message = jsonObject.getString("message");
                    switch (message) {
                        case "重复邮箱": {
                            Toast.makeText(getActivity(), "邮箱已存在", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        case "重复命名": {
                            Toast.makeText(getActivity(), "用户名已存在", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
                }
                default: {  // 其他
                    Toast.makeText(getActivity(), "请检查网络是否正常", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        } catch (JSONException e) { // 返回信息处理异常
            Toast.makeText(getActivity(), "请检查网络是否正常", Toast.LENGTH_SHORT).show();
            System.out.println("发布返回信息处理异常：");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}