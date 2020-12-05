package com.example.bookseller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "");
                putOnEvent.setDetail(storyView.getText().toString());

                if(!username.equals("")){
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

                        Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), BaseActivity.class);
                        getContext().startActivity(intent);
                    }
                }
                else{
                    Toast.makeText(getActivity(), "请先登录哦", Toast.LENGTH_SHORT).show();
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
                startActivity(intent);
            }
        });

        return view;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

}