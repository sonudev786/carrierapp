package com.joaquimley.com;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.logging.Logger;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                EditText username = findViewById(R.id.username);
                EditText password = findViewById(R.id.password);

                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (user.length() > 10 || user.equals("")){
                    Toast.makeText(LoginActivity.this, "Please enter a valid mobile number.", Toast.LENGTH_LONG).show();
                }else{
                    if (pass.equals("")){
                        Toast.makeText(LoginActivity.this, "Please enter password!", Toast.LENGTH_LONG).show();
                    }else{

                        if (user.equals("9999999999") && pass.equals("Admin@123")){
                            editor.putString("username", user);
                            editor.apply();
                            Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(LoginActivity.this, UserActivity.class);
                            i.putExtra("username", user);
                            startActivity(i); // invoke the SecondActivity.
                            finish();
                        }else{
                            editor.putString("username", user);
                            editor.apply();
                            Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            i.putExtra("username", user);
                            startActivity(i); // invoke the SecondActivity.
                            finish(); // the current activity will get finished.
                        }
                    }
                }
            }
        });
    }
}