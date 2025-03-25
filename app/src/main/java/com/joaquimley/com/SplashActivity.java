package com.joaquimley.com;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_SCREEN_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView imageView = findViewById(R.id.imageView);
        Glide.with(this).load(R.drawable.logo).into(imageView);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                String savedName = sharedPreferences.getString("username", "");

                if (savedName.isEmpty()){
                    // Intent is used to switch from one activity to another.
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i); // invoke the SecondActivity.
                    finish(); // the current activity will get finished.
                }else{
                    if (savedName.equals("9999999999")){
                        // Intent is used to switch from one activity to another.
                        Intent i = new Intent(SplashActivity.this, UserActivity.class);
                        startActivity(i); // invoke the SecondActivity.
                        finish(); // the current activity will get finished.
                    }else{
                        // Intent is used to switch from one activity to another.
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i); // invoke the SecondActivity.
                        finish(); // the current activity will get finished.
                    }

                }
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}