package com.example.quickchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    ImageButton ibSend;
    EditText etMessage;
    RecyclerView rViewOfChat;
    String name,image,uid;//all these things are of receiver's
    String senderID;
    ImageView profileImage;
    TextView tvName;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    String senderRoom,receiverRoom;
    public static String senderImage;
    public static String receiverImage;
    ArrayList<Message> messageArrayList;
    ChatAdapter chatAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ibSend=findViewById(R.id.ibSend);
        etMessage=findViewById(R.id.etMessage);
        rViewOfChat=findViewById(R.id.rViewOfchat);
     LinearLayoutManager layoutManager=new LinearLayoutManager(this);
     layoutManager.setStackFromEnd(true);
        chatAdapter=new ChatAdapter(ChatActivity.this,messageArrayList);
        rViewOfChat.setLayoutManager(layoutManager);
        rViewOfChat.setAdapter(chatAdapter);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        senderID=firebaseAuth.getUid();

        name=getIntent().getStringExtra("name");
        image=getIntent().getStringExtra("image");
        uid=getIntent().getStringExtra("uid");
        messageArrayList=new ArrayList<>();

        senderRoom=senderID+uid;//sender+receiver
        receiverRoom=uid+senderID;//reciever +sender
        profileImage=findViewById(R.id.ivUserImageofChat);

        Picasso.get().load(image).into(profileImage);
        tvName=findViewById(R.id.recieverName);
        tvName.setText(name);

        DatabaseReference ref=firebaseDatabase.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatReference=firebaseDatabase.getReference().child("chats").child(senderRoom).child("messages");



        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageArrayList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Message msg=dataSnapshot.getValue(Message.class);
                    messageArrayList.add(msg);
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                senderImage=snapshot.child("image").getValue().toString();
                receiverImage=image;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ibSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=etMessage.getText().toString();
                if(message.isEmpty()) Toast.makeText(getApplicationContext(),"Please Enter Something",Toast.LENGTH_SHORT).show();
                else{
                    etMessage.setText("");
                    Date date=new Date();
                    Message message1=new Message(message,senderID,date.getTime());
                    firebaseDatabase.getReference().child("chats").child(senderRoom).child("messages").push().setValue(message1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            firebaseDatabase.getReference().child("chats").child(receiverRoom).child("messages").push().setValue(message1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }
                    });
                }
            }
        });
    }
}