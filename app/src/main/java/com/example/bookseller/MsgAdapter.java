package com.example.bookseller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHold>{
    private List<Msg> mMsgList;

    static class ViewHold extends RecyclerView.ViewHolder{
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg, leftMsgTime;
        TextView rightMsg, rightMsgTime;
        //获取item里面的各个控件
        public ViewHold(@NonNull View itemView) {
            super(itemView);
            leftLayout=(LinearLayout)itemView.findViewById(R.id.left_layout);
            rightLayout=(LinearLayout)itemView.findViewById(R.id.right_layout);
            leftMsg=(TextView)itemView.findViewById(R.id.left_msg);
            rightMsg=(TextView)itemView.findViewById(R.id.right_msg);
            leftMsgTime=(TextView)itemView.findViewById(R.id.left_msg_time);
            rightMsgTime=(TextView)itemView.findViewById(R.id.right_msg_time);
        }
    }
    public MsgAdapter(List<Msg> msgList){
        mMsgList=msgList;
    }
    @NonNull
    @Override
    public MsgAdapter.ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //获取item的布局
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item,parent,false);
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MsgAdapter.ViewHold holder, int position) {
        Msg msg=mMsgList.get(position);//position是当前子项在集合中的位置，通过position参数得到当前项的Msg实例
        if(msg.getType()==Msg.TYPE_RECEIVE){
            //如果是收到的信息，则显示左边的布局信息，将右边的信息隐藏
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
            holder.leftMsgTime.setText(msg.getSendTime());
        }else{
            //若是发出的信息，则显示右边的布局信息，隐藏左边的布局信息
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(msg.getContent());
            holder.rightMsgTime.setText(msg.getSendTime());
        }

    }

    @Override
    public int getItemCount() {
        return mMsgList.size();
    }
}