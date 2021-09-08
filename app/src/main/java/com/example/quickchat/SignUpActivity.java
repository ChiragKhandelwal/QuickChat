package com.example.quickchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SignUpActivity extends AppCompatActivity {
    EditText name,mail,password,confirm;
    Button signUp;
    TextView tvSignin;
    ImageView userImage;
    FirebaseAuth firebaseAuth;
    String Name,email,Password,Confirm;
    static int PICK_IMAGE=123;
        Uri imageUri;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseStorage storage;
    FirebaseDatabase database;
    String imageaccesstoken;
    ProgressDialog progressDialog;
    String status="Hey There!!!!!!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        userImage=findViewById(R.id.ivUserImageofRow);
        name=findViewById(R.id.etNameSignUp);
        mail=findViewById(R.id.etMailsignUp);
        password=findViewById(R.id.etPasswordsignUp);
        confirm=findViewById(R.id.etConfirmPasswordsignUp);
        signUp=findViewById(R.id.btnsignUp);
        tvSignin=findViewById(R.id.tvOldUser);

        firebaseAuth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();




        tvSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
            }
        });


        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });


        signUp.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Name=name.getText().toString();
                email=mail.getText().toString();
                Password=password.getText().toString();
                Confirm=confirm.getText().toString();

                if (email.isEmpty() || Password.isEmpty() || Name.isEmpty()){
                    if(Name.isEmpty())  Toast.makeText(getApplicationContext(), "Connot Name be Empty", Toast.LENGTH_SHORT).show();
                    if(Password.isEmpty())  Toast.makeText(getApplicationContext(), "Connot Password be Empty", Toast.LENGTH_SHORT).show();
                   // if(email.isEmpty())  Toast.makeText(getApplicationContext(), "Connot email Name be Empty", Toast.LENGTH_SHORT).show();


                }


                else if (!email.matches(emailPattern))
                    Toast.makeText(getApplicationContext(), "Enter a Valid Email Address", Toast.LENGTH_SHORT).show();

                else if (Password.length() < 6)
                    Toast.makeText(getApplicationContext(), "Password is too short", Toast.LENGTH_SHORT).show();

                else if (!Password.equals(Confirm))
                    Toast.makeText(getApplicationContext(), "Make Sure Both Password Matches Correctly", Toast.LENGTH_SHORT).show();

                else {
                    firebaseAuth.createUserWithEmailAndPassword(email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference databaseReference = database.getReference().child("user").child(firebaseAuth.getUid());
                                StorageReference storageReference = storage.getReference().child("upload").child(firebaseAuth.getUid());
                                Toast.makeText(getApplicationContext(), "User Crested SuccessFully", Toast.LENGTH_SHORT).show();

                                String status="Hey There!!!!!!";
                                if (imageUri != null) {
                                    storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageaccesstoken = uri.toString();
                                                        firbasemodel firbasemodel = new firbasemodel(firebaseAuth.getUid(), Name, email, imageaccesstoken,status);
                                                        databaseReference.setValue(firbasemodel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                                                                } else
                                                                    Toast.makeText(getApplicationContext(), "Error in Creating this User", Toast.LENGTH_SHORT).show();

                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                                else {
                                    imageaccesstoken="https://firebasestorage.googleapis.com/v0/b/quickchat-c9ad7.appspot.com/o/default.png?alt=media&token=c76fe338-38bf-4322-aeb3-3ebcbc281145";
                                    firbasemodel firbasemodel = new firbasemodel(firebaseAuth.getUid(), Name, email, imageaccesstoken,status);
                                    databaseReference.setValue(firbasemodel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                                            } else
                                                Toast.makeText(getApplicationContext(), "Error in Creating this User", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }

                            } else {


                                Toast.makeText(getApplicationContext(), "Ooops Something ent wrong", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE&&data!=null){
           imageUri=data.getData();
            userImage.setImageURI(imageUri);
        }
    }
}