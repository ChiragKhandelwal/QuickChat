package com.example.quickchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    Adapter adapter;
    FirebaseDatabase firebaseDatabase;
    ArrayList<firbasemodel> firbasemodelArrayList;
    ImageView ivLogOut,ivSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ivLogOut=findViewById(R.id.logout);
        ivSettings=findViewById(R.id.settings);
        firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference();
        firebaseAuth=FirebaseAuth.getInstance();

        firbasemodelArrayList=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerView);
        adapter=new Adapter(HomeActivity.this,firbasemodelArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);



        if(firebaseAuth.getCurrentUser()==null){
            startActivity(new Intent(HomeActivity.this,LoginActivity.class));

        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    firbasemodel model=dataSnapshot.getValue(firbasemodel.class);
                    firbasemodelArrayList.add(model);

                }
                Toast.makeText(HomeActivity.this,"Data Fetched",Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ivLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(HomeActivity.this,R.style.Dialog);
                dialog.show();
                dialog.setContentView(R.layout.dialog_layout);
                Button yes,no;
                yes=findViewById(R.id.btnYes);
                no=findViewById(R.id.btnNo);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                dialog.dismiss();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
            dialog.dismiss();
                    }
                });

            }
        });
        ivSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,settings.class));
            }
        });
    }
}