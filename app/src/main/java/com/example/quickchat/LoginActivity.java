package com.example.quickchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText mail,pass;
    Button signIn;
    TextView tvSignUp;
    FirebaseAuth firebaseAuth;
    String Emails,Password;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mail=findViewById(R.id.etMailLogin);
        pass=findViewById(R.id.etPasswordLogin);
        signIn=findViewById(R.id.button);
        tvSignUp=findViewById(R.id.tvNewUser);
        firebaseAuth=FirebaseAuth.getInstance();

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Emails=mail.getText().toString();
                Password=pass.getText().toString();
                if(Emails.isEmpty()||Password.isEmpty()) Toast.makeText(getApplicationContext(),"Connot be Empty",Toast.LENGTH_SHORT).show();
                else if(!Emails.matches(emailPattern)) Toast.makeText(getApplicationContext(),"Enter a Valid Email Address",Toast.LENGTH_SHORT).show();
              //  else if(Password.length()<6) Toast.makeText(getApplicationContext(),"Password is too short",Toast.LENGTH_SHORT).show();
                else {
                    firebaseAuth.signInWithEmailAndPassword(Emails, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            else Toast.makeText(getApplicationContext(),"Connot be Empty",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });
    }
}