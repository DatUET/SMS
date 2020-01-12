package com.example.sms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sms.Adapter.UserAdapter;
import com.example.sms.models.Messages;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String[] permission = {Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_PHONE_STATE};
    private final int REQUEST_CODE_SEND_SMS = 100;

    public static String myNumberPhone;

    RecyclerView recyler_users;
    UserAdapter userAdapter;

    List<Messages> messagesList;
    List<String> nameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messagesList = new ArrayList<>();
        nameList = new ArrayList<>();
        recyler_users = findViewById(R.id.recyler_users);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyler_users.setLayoutManager(layoutManager);

        readSMS();
    }

    private void readSMS(){
        if( (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(this, permission, REQUEST_CODE_SEND_SMS);
        }
        else {
            TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
           myNumberPhone = tMgr.getLine1Number();
        }
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
            if(!nameList.contains(messages.getAddress())){
                nameList.add(messages.getAddress());
                messagesList.add(messages);
            }
        } while (smsInboxCursor.moveToNext());

        userAdapter = new UserAdapter(this, messagesList);
        recyler_users.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();
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
}
