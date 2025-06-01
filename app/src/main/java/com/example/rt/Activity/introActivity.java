package com.example.rt.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.rt.databinding.ActivityIntroBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class introActivity extends BaseActivity2 {
private ActivityIntroBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
//        FirebaseDatabase database = FirebaseDatabase.getInstance();

        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.StartBtn.setOnClickListener(v -> startActivity(new Intent(introActivity.this,MainActivity1.class)));

    }
}