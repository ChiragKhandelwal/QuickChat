package com.example.quickchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Message> messageArrayList;
    int ITEM_SEND=1;
    int ITEM_RECEIVE =2;

    public ChatAdapter(Context context, ArrayList<Message> messageArrayList) {
        this.context = context;
        this.messageArrayList = messageArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_SEND){
            View view= LayoutInflater.from(context).inflate(R.layout.senderlayout,parent,false);
            return new SenderViewHolder(view);
        }
        else{
            View view= LayoutInflater.from(context).inflate(R.layout.receiverlayout,parent,false);
            return new ReceiverViewHolder(view);
        }
       // return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message=messageArrayList.get(position);
        if(holder.getClass()==SenderViewHolder.class){
            SenderViewHolder viewHolder=(SenderViewHolder)holder;
            viewHolder.tvMessage.setText(message.getMessage());
            Picasso.get().load(ChatActivity.senderImage).into(viewHolder.ivUserImage);

        }
        else{
            ReceiverViewHolder viewHolder=(ReceiverViewHolder) holder;
            viewHolder.tvMessage.setText(message.getMessage());
            Picasso.get().load(ChatActivity.receiverImage).into(viewHolder.ivUserImage);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Message msg=messageArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(msg.getSenderID())) return ITEM_SEND;
        else return ITEM_RECEIVE;
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }
    class SenderViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUserImage;
        TextView tvMessage;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserImage=itemView.findViewById(R.id.ivSender);
            tvMessage=itemView.findViewById(R.id.tvSender);
        }
    }
    class ReceiverViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUserImage;
        TextView tvMessage;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            ivUserImage=itemView.findViewById(R.id.ivReceiver);
            tvMessage=itemView.findViewById(R.id.tvReceiver);
        }
    }
}
