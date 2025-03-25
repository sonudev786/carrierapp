package com.joaquimley.com;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {
    Dialog dialog;
    private UserAdapter userAdapter;
    List<UserModel> userList;
    RecyclerView recyclerView;
    Dialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getSupportActionBar().setTitle("SELECT USER");
        recyclerView = findViewById(R.id.userListRv);
        userList = new ArrayList<>();
        dialog = new Dialog(UserActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        RelativeLayout rl = findViewById(R.id.rlAddUser);
        RelativeLayout main = findViewById(R.id.mainLayout);
        main.setVisibility(View.VISIBLE);
        rl.setVisibility(View.GONE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        apiCall();


        FloatingActionButton floatingActionButton = findViewById(R.id.btnAdd);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.setVisibility(View.GONE);
                rl.setVisibility(View.VISIBLE);

                EditText etName, etUser, etPass;
                etName = findViewById(R.id.etName);
                etUser = findViewById(R.id.etUser);
                etPass = findViewById(R.id.etPass);

                Button btnAdd = findViewById(R.id.addUser);
                Button cancel = findViewById(R.id.cancel);

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = etName.getText().toString();
                        String user = etUser.getText().toString();
                        String pass = etPass.getText().toString();

                        if (name.equals("")){
                            etName.setError("Name is required.");
                        }else if(user.length() < 10){
                            etUser.setError("Enter correct mobile number");
                        }else if(pass.equals("")){
                            etPass.setError("Password is required");
                        }else{
                            ApiClient.addRequest(user, name, pass);
                            Toast.makeText(UserActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                            main.setVisibility(View.VISIBLE);
                            rl.setVisibility(View.GONE);
                            apiCall();
                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        main.setVisibility(View.VISIBLE);
                        rl.setVisibility(View.GONE);
                    }
                });

            }
        });

    }

    public void apiCall(){
        Call<List<UserModel>> call = ApiClient.getApiService().getUsers();
        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                dialog.dismiss();
                userList.clear();
                if (response.isSuccessful() && response.body() != null) {
                    for (UserModel user : response.body()) {

                        dialog.cancel();

                        userList.add(user);
                        Log.d("API_RESPONSE",
                                ", Name: " + user.getName() +
                                        ", Username: " + user.getUsername() +
                                        ", Password: " + user.getPassword());
                    }
                }
                userAdapter = new UserAdapter(UserActivity.this, userList);
                recyclerView.setAdapter(userAdapter);

                dialog.dismiss();
            }
            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable t) {
                Log.e("API_ERROR", "Error: " + t.getMessage());
            }
        });
    }
}