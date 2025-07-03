package com.example.rt.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.rt.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends BaseActivity2{

    EditText LUsername,LPassword;
    TextView SignupRedirectText;
    Button LButton;
//    CheckBox RememberMe;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        RememberMe = findViewById(R.id.RememberMe);
        LUsername = findViewById(R.id.LUsername);
        LPassword = findViewById(R.id.LPassword);
        SignupRedirectText = findViewById(R.id.SignupRedirectText);
        LButton = findViewById(R.id.LButton);


        LButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateUsername() | !validatePassword() ){

                }else{
                    CheckUser();
                }
            }
        });
    SignupRedirectText.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        }
    });



        }


    public Boolean validateUsername() {
        String val = LUsername.getText().toString();
        if (val.isEmpty()) {
            LUsername.setError("Username cannot be empty");
            return false;
        } else {
            LUsername.setError(null);
            return true;
        }
    }
        public Boolean validatePassword(){
            String val = LPassword.getText().toString();
            if(val.isEmpty()){
                LUsername.setError("password cannot be empty");
                return false;
            }else{
                LUsername.setError(null);
                return true;
            }

    }
    public void CheckUser(){
        String userUsername = LUsername.getText().toString();
        String userPassword = LPassword.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query CheckUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        CheckUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    LUsername.setError(null);
                    String passwordFromdb = snapshot.child(userUsername).child("password").getValue(String.class);
                    if(!Objects.equals(passwordFromdb,userPassword)){
                        LUsername.setError(null);
                        Intent intent = new Intent(LoginActivity.this, MainActivity1.class);
                        intent.putExtra("USERNAME", userUsername);
                        Clear();
                        startActivity(intent);

                    }
                    else{
                        LUsername.setError("Invalid Credential");
                        LUsername.requestFocus();
                        Clear();

                    }
                }else{
                    LUsername.setError("User does not exist");
                    LUsername.requestFocus();
                    Clear();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void Clear(){
        LUsername.setText("");
        LPassword.setText("");
    }

}