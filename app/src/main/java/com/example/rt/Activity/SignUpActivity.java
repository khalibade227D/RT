package com.example.rt.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends BaseActivity2{
    EditText SName,SEmail,SUsername,SPassword;
    private static final String EMAIL_PATTERN =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    TextView LoginRedirectText;
    ImageView Github,Facebook;
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

        Github = findViewById(R.id.Github);
        Facebook = findViewById(R.id.Facebook);
        Github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://github.com/khalibade227D");
            }
        });
        Facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://web.facebook.com/khalifa.abdullahi.143289");
            }
        });
        SButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                String name  = SName.getText().toString();
                String email  =SEmail.getText().toString();
                String username  = SUsername.getText().toString();
                String password  = SPassword.getText().toString();


                if(!validateUsername() | !validatePassword() | !validateEmail(email) | !validateName()){

                }else{
                     User user = new User(name,email,username,password);
                     reference.child(name).setValue(user);
                     Toast.makeText(SignUpActivity.this, "You have Signup Successfully", Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                     Clear();
                     startActivity(intent);
                }
            }
        });


        LoginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                Clear();
                startActivity(intent);

            }
        });

    }

    private void gotoUrl(String s) {
        Uri uri  = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
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
    public Boolean validateEmail(String email) {
         email = SEmail.getText().toString();
        if (email.isEmpty() || !email.matches(EMAIL_PATTERN)) {
            SEmail.setError("Something is wrong with your email!");
            return false;
        } else{
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
    public void Clear(){
        SUsername.setText("");
        SPassword.setText("");
        SEmail.setText("");
        SName.setText("");
    }
}