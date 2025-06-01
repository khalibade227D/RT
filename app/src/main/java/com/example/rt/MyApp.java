package com.example.rt;

import android.app.Application;
import com.google.firebase.FirebaseApp;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // This initializes Firebase properly
        FirebaseApp.initializeApp(this);
    }
}
