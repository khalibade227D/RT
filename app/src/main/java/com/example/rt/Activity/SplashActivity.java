package com.example.rt.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.rt.R;

public class SplashActivity extends BaseActivity2{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.logoImage);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
        logo.startAnimation(animation);

        // Move to introActivity after 2 seconds
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, introActivity.class);
            startActivity(intent);
            finish();
        }, 3000);
    }
}
