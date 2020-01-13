package com.example.sms;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class NewMsgActivity extends AppCompatActivity {

    EditText txt_phone, txt_chat;
    ImageButton btn_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_msg);

        txt_phone = findViewById(R.id.txt_phone);
        txt_chat = findViewById(R.id.txt_chat);
        btn_send = findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = txt_phone.getText().toString();
                String chat = txt_chat.getText().toString();
                if(!TextUtils.isEmpty(phone.trim()) && TextUtils.isEmpty(chat.trim())){
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phone, null, chat, null, null);
                    txt_chat.setText("");
                    Toast.makeText(NewMsgActivity.this, "The message has been sent", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
