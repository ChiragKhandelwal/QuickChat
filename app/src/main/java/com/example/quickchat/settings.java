package com.example.quickchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.EventListener;

public class settings extends AppCompatActivity {
    ImageView ivUser;
    EditText etName,etStatus;
    Button save;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri imageUri;//uri of selected image
    int PICK_IMAGE=123;
    String mail;
    StorageReference storageReference;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        progressBar=findViewById(R.id.progressBar);


        ivUser=findViewById(R.id.evSettings);
        etName=findViewById(R.id.etNameSettings);
        etStatus=findViewById(R.id.etStatusSettings);
        save=findViewById(R.id.btnSettings);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        DatabaseReference reference=database.getReference().child("user").child(auth.getUid());
        storageReference=storage.getReference().child("upload").child(auth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mail=snapshot.child("email").getValue().toString();
                String name=snapshot.child("name").getValue().toString();
                String status=snapshot.child("status").getValue().toString();
                String image=snapshot.child("image").getValue().toString();

                etName.setText(name);
                etStatus.setText(status);
                Picasso.get().load(image).into(ivUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String name=etName.getText().toString();
                String status=etStatus.getText().toString();
                if(imageUri!=null){
                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageAccessToken=uri.toString();
                                    firbasemodel model=new firbasemodel(auth.getUid(),name,mail,imageAccessToken,status);

                                    reference.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    progressBar.setVisibility(View.INVISIBLE);Toast.makeText(settings.this,"Profile Updated",Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(settings.this,HomeActivity.class));
                                                }
                                                else {Toast.makeText(settings.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.INVISIBLE);}
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
                else {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageAccessToken=uri.toString();
                            firbasemodel model=new firbasemodel(auth.getUid(),name,mail,imageAccessToken,status);

                            reference.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        progressBar.setVisibility(View.INVISIBLE);Toast.makeText(settings.this,"Profile Updated",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(settings.this,HomeActivity.class));}
                                    else {Toast.makeText(settings.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);}
                                }
                            });
                        }
                    });
                }
            }
        });
        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE&&data!=null){
            imageUri=data.getData();
            ivUser.setImageURI(imageUri);
        }

    }
}