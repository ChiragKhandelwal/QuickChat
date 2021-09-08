package com.example.quickchat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    Context context;
    ArrayList<firbasemodel> arrayList;
    public Adapter(HomeActivity homeActivity, ArrayList<firbasemodel> firbasemodelArrayList) {
        context=homeActivity;
        arrayList=firbasemodelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        firbasemodel model=arrayList.get(position);
        holder.tvName.setText(model.getName());
        holder.tvStatus.setText(model.getStatus());
        Picasso.get().load(model.image).into(holder.userImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ChatActivity.class);
                intent.putExtra("name",model.getName());
                intent.putExtra("image",model.getImage());
                intent.putExtra("uid",model.getUid());
                context.startActivity(intent);
            }
        });



    }





    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView tvName,tvStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage=itemView.findViewById(R.id.ivUserImageofRow);
            tvName=itemView.findViewById(R.id.tvNameofRow);
            tvStatus=itemView.findViewById(R.id.tvStatusofRow);
        }
    }
}
