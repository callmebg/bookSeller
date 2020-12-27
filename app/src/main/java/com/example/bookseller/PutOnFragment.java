package com.example.bookseller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.ContentUris;
import android.content.SharedPreferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class PutOnFragment extends Fragment {

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
    private static final int CHOOSE_PHOTO = 1;

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
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
        optitle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
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
        opmoney.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                final EditText editText = new EditText(getActivity());
                new AlertDialog.Builder(getActivity()).setTitle("请输入价格")
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!editText.getText().toString().matches(num)){
                                    Toast.makeText(getActivity(), "请输入正常价格", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    out_sec_sect_txt.setText(editText.getText());
                                    putOnEvent.setPrice(editText.getText().toString());
                                }
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });
        //发布按钮
        buttonPut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //待修改获取is_login
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", MODE_PRIVATE);
                Boolean is_login = sharedPreferences.getBoolean("is_login", false);
                putOnEvent.setDetail(storyView.getText().toString());

                if(is_login){
                    if(putOnEvent.getDetail().equals("")){
                        Toast.makeText(getActivity(), "请补充描述", Toast.LENGTH_SHORT).show();
                    }
                    else if(putOnEvent.getPrice().equals("")){
                        Toast.makeText(getActivity(), "设置金额", Toast.LENGTH_SHORT).show();
                    }
                    else if(putOnEvent.getName().equals("")){
                        Toast.makeText(getActivity(), "请添加标题", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //调用函数上传到数据库
                        OkHttpClient client = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("bookName", putOnEvent.getName())
                                .add("date","")
                                .add("details", putOnEvent.getDetail())
                                .add("price", putOnEvent.getPrice())
                                .add("url", "")
                                .build();
                        String ip = getResources().getString(R.string.ip);
                        String port = getResources().getString(R.string.port);
                        String putOnUrl = "http://" + ip + ":" + port + "/release";
                        Request request = new Request.Builder().url(putOnUrl).post(requestBody).build();
//
//                        try(Response response =  client.newCall(request).execute()){
//                            String responseData = response.body().string();
//                            boolean releaseSuccess = handlerResponseData(responseData);
//
//                            if(releaseSuccess){
//                                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
//
//                            }
//                        }catch(IOException e){
//                            Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_LONG).show();
//                        }

                        Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), BaseActivity.class);
                        getContext().startActivity(intent);
                    }
                }
                else{
                    Toast.makeText(getActivity(), "请先登录哦", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(),LoginActivity01.class);
                    startActivity(intent);
                }
            }
        });


        //取消按钮
        cancelView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity(), BaseActivity.class);
                getContext().startActivity(intent);
            }
        });

        picture_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, CHOOSE_PHOTO);
            }
        });

        return view;

    }

    private void release(String bookName, String date, String details, String url, String price){
        Toast.makeText(getActivity(), "正在发布", Toast.LENGTH_SHORT).show();

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("bookName", bookName)
                .add("date", date)
                .add("details", details)
                .add("price", price)
                .add("url", url)
                .build();

        String ip = "47.107.117.59";
        String port = "80";
        String releaseUrl = "http://" + ip + ":" + port + "/release";
        Request request = new Request.Builder().url(releaseUrl).post(requestBody).build();

        NetworkUtils.forceNetworkRequesting();

        try(Response response = client.newCall(request).execute()){
            String responseData = response.body().string();
            System.out.println("release-responseData" + responseData);
            handleResponseData(responseData);
        }catch (IOException e){
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
            System.out.println("注册返回信息处理异常：");
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

}