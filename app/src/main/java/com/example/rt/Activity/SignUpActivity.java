package com.example.rt.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.rt.Model.User;
import com.example.rt.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    EditText SName,SEmail,SUsername,SPassword;
    TextView LoginRedirectText;
    Button SButton;
    FirebaseDatabase database;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        SName = findViewById(R.id.SName);
        SEmail = findViewById(R.id.SEmail);
        SUsername = findViewById(R.id.SUsername);
        SPassword = findViewById(R.id.SPassword);
        SButton = findViewById(R.id.SButton);
        LoginRedirectText = findViewById(R.id.LoginRedirectText);

        SButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String name  = SName.getText().toString();
                String email  =SEmail.getText().toString();
                String username  = SUsername.getText().toString();
                String password  = SPassword.getText().toString();

                User user = new User(name,email,username,password);
                reference.child(name).setValue(user);
                if(!validateUsername() | !validatePassword() | !validateEmail() | !validateName()){
                    

                }else{
                     Toast.makeText(SignUpActivity.this, "You have Signup Successfully", Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                     startActivity(intent);
                }
            }
        });


        LoginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        });

    }
    public Boolean validateUsername() {
        String val = SUsername.getText().toString();
        if (val.isEmpty()) {
            SUsername.setError("Username cannot be empty");
            return false;
        } else {
            SUsername.setError(null);
            return true;
        }
    }
    public Boolean validateName() {
        String val = SName.getText().toString();
        if (val.isEmpty()) {
            SName.setError("Name cannot be empty");
            return false;
        } else {
            SName.setError(null);
            return true;
        }

    }
    public Boolean validateEmail() {
        String val = SEmail.getText().toString();
        if (val.isEmpty()) {
            SEmail.setError("Email cannot be empty");
            return false;
        } else {
            SEmail.setError(null);
            return true;
        }

    }
    public Boolean validatePassword() {
        String val = SPassword.getText().toString();
        if (val.isEmpty()) {
            SPassword.setError("Password cannot be empty");
            return false;
        } else {
            SPassword.setError(null);
            return true;
        }

    }
}