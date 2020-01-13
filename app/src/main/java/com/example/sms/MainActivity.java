package com.example.sms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sms.Adapter.UserAdapter;
import com.example.sms.models.Messages;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String[] permission = {Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_CONTACTS};
    private final int REQUEST_CODE_SEND_SMS = 100;


    RecyclerView recyler_users;
    UserAdapter userAdapter;

    List<Messages> messagesList;
    List<String> nameList;

    SwipeRefreshLayout refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messagesList = new ArrayList<>();
        nameList = new ArrayList<>();
        recyler_users = findViewById(R.id.recyler_users);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyler_users.setLayoutManager(layoutManager);

        refresh = findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readSMS();
            }
        });

        readSMS();
    }

    private void readSMS(){
        if( (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this, permission, REQUEST_CODE_SEND_SMS);
        }
        else {
            messagesList.clear();
            nameList.clear();
            ContentResolver contentResolver = getContentResolver();
            Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms"), null, null, null, null);
            int indexBody = smsInboxCursor.getColumnIndex("body");
            int indexAddress = smsInboxCursor.getColumnIndex("address");
            int indexType = smsInboxCursor.getColumnIndex("type");
            int indexDate = smsInboxCursor.getColumnIndex("date");

            if (indexBody < 0 || !smsInboxCursor.moveToFirst() || indexType < 0) return;
            do {
                int type = smsInboxCursor.getInt(indexType);
                String body = smsInboxCursor.getString(indexBody);
                String address = smsInboxCursor.getString(indexAddress);
                String date = smsInboxCursor.getString(indexDate);

                Messages messages = new Messages(type, address, body, date);
                if (!nameList.contains(messages.getAddress())) {
                    nameList.add(messages.getAddress());
                    messagesList.add(messages);
                }
            } while (smsInboxCursor.moveToNext());

            userAdapter = new UserAdapter(this, messagesList);
            recyler_users.setAdapter(userAdapter);
            userAdapter.notifyDataSetChanged();
        }

        refresh.setRefreshing(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_SEND_SMS){
            readSMS();
        }
        else {
            Toast.makeText(MainActivity.this, "Permission Denied!!!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.it_add){
            Intent intent = new Intent(MainActivity.this, NewMsgActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        readSMS();
    }
}
