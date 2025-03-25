package com.joaquimley.com;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MesageDetailsActivity extends AppCompatActivity {
    Dialog dialog;
    private MessageDetailsAdapter smsAdapter;
    private final List<Massage> smsList = new ArrayList<>();

    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        getSupportActionBar().setTitle("Inbox");


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        smsAdapter = new MessageDetailsAdapter(smsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(smsAdapter);

        username = getIntent().getStringExtra("username");
        new FetchUserTask().execute(username);

        dialog = new Dialog(MesageDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new FetchUserTask().execute(username);
            }
        }, 60000);

    }

    private class FetchUserTask extends AsyncTask<String, Void, List<Massage>> {
        @Override
        protected List<Massage> doInBackground(String... params) {
            return ApiClient.fetchMessageData(params[0]);
        }

        @Override
        protected void onPostExecute(List<Massage> users) {
            dialog.dismiss();
            smsList.clear();
            smsList.addAll(users);
            smsAdapter.notifyDataSetChanged();
        }
    }

}