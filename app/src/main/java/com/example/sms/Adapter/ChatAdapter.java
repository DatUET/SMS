package com.example.sms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sms.R;
import com.example.sms.models.Messages;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;

    Context context;
    List<Messages> messagesList;

    public ChatAdapter(Context context, List<Messages> messagesList) {
        this.context = context;
        this.messagesList = messagesList;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(viewType == MSG_TYPE_LEFT) {
            View view = inflater.inflate(R.layout.chat_left, parent, false);
            ChatViewHolder chatViewHolder = new ChatViewHolder(view);
            return chatViewHolder;
        }
        else
        {
            View view = inflater.inflate(R.layout.chat_right, parent, false);
            ChatViewHolder chatViewHolder = new ChatViewHolder(view);
            return chatViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.txt_message.setText(messagesList.get(position).getBody());
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView txt_message;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_message = itemView.findViewById(R.id.txt_message);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(messagesList.get(position).getType() == 2){
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }
}
