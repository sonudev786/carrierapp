package com.joaquimley.com;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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

    private static final int PERMISSION_REQUEST_CODE = 100;
    private SmsAdapter smsAdapter;
    private List<SmsModel> smsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        // Set up SMS receiver
        SmsBroadcastReceiver.setSmsListener(this);
    }

    private void loadOldMessages() {
        Uri uri = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uri, null, null, null, "date DESC");

        if (cursor != null) {
            int indexBody = cursor.getColumnIndex("body");
            int indexAddress = cursor.getColumnIndex("address");

            while (cursor.moveToNext()) {
                String sender = cursor.getString(indexAddress);
                String message = cursor.getString(indexBody);
                smsList.add(new SmsModel(sender, message));
            }
            cursor.close();
            smsAdapter.notifyDataSetChanged();
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
}