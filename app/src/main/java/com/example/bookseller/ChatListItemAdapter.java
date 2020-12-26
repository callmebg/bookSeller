package com.example.bookseller;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatListItemAdapter extends RecyclerView.Adapter<ChatListItemAdapter.ViewHold>{
    private List<ChatListItem> chatList;

    // 对外的接口
    private OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    static class ViewHold extends RecyclerView.ViewHolder{
        LinearLayout chatListLayout;
        TextView chatListName;
        public ViewHold(@NonNull View itemView) {
            super(itemView);
            chatListLayout=(LinearLayout)itemView.findViewById(R.id.chatList_layout);
            chatListName=(TextView)itemView.findViewById(R.id.chatlist_name);
        }
    }
    public ChatListItemAdapter(List<ChatListItem> chatList){
        this.chatList=chatList;
    }
    @NonNull
    @Override
    public ViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.chatlist_item,parent,false);
        ViewHold holder = new ViewHold(view);
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int t = holder.getAdapterPosition();
                Log.i("现在点击的是第几项", String.valueOf(t));
                //通过接口名调用方法
                mOnItemClickListener.onItemClick(view, t);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHold holder, int position) {
        ChatListItem item=chatList.get(position);//position是当前子项在集合中的位置，通过position参数得到当前项的Msg实例

        holder.chatListLayout.setVisibility(View.VISIBLE);
        holder.chatListName.setText(item.getName());
    }


    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
