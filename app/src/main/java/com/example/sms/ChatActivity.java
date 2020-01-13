package com.example.sms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sms.Adapter.ChatAdapter;
import com.example.sms.models.Messages;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    String address, displayName;
    Toolbar toolbar;
    TextView txt_name;
    RecyclerView recycler_chats;
    EditText txt_chat;
    ImageButton btn_send;

    ChatAdapter chatAdapter;
    List<Messages> messagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        address = intent.getStringExtra("address");
        displayName = intent.getStringExtra("displayName");


        toolbar = findViewById(R.id.toolbars);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        txt_name = findViewById(R.id.txt_name);
        if(displayName.equals("none")){
            txt_name.setText(address);
        }
        else {
            txt_name.setText(displayName);
        }
        txt_chat = findViewById(R.id.txt_chat);
        recycler_chats = findViewById(R.id.recycler_chats);
        btn_send = findViewById(R.id.btn_send);

        messagesList = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        recycler_chats.setLayoutManager(layoutManager);

        getMessage();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(txt_chat.getText().toString().trim())){
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(address, null, txt_chat.getText().toString().trim(), null, null);
                    Messages newMsg = new Messages(2, address, txt_chat.getText().toString().trim(), String.valueOf(System.currentTimeMillis()));
                    messagesList.add(newMsg);
                    chatAdapter.notifyDataSetChanged();
                    txt_chat.setText("");
                }
            }
        });
    }

    private void getMessage(){
        messagesList.clear();
        Cursor cur = getContentResolver().query(Uri.parse("content://sms"), null, "address LIKE '%" + address + "%'", null, "date asc");
        int indexBody = cur.getColumnIndex("body");
        int indexAddress = cur.getColumnIndex("address");
        int indexType = cur.getColumnIndex("type");
        int indexDate = cur.getColumnIndex("date");

        if (indexBody < 0 || !cur.moveToFirst() || indexType < 0) return;
        do {
            int type = cur.getInt(indexType);
            String body = cur.getString(indexBody);
            String address = cur.getString(indexAddress);
            String date = cur.getString(indexDate);

            Messages messages = new Messages(type, address, body, date);

            messagesList.add(messages);

        } while (cur.moveToNext());
        chatAdapter = new ChatAdapter(this, messagesList);
        recycler_chats.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
    }
}
