package com.joaquimley.com;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageDetailsAdapter extends RecyclerView.Adapter<MessageDetailsAdapter.MessageViewHolder> {

    private List<Massage> smsList;

    public MessageDetailsAdapter(List<Massage> smsList) {
        this.smsList = smsList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sms_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

        Massage sms = smsList.get(position);
        holder.senderTextView.setText(sms.getTitle());
        holder.messageTextView.setText(sms.getDescription());
        String date = sms.getDate();
        holder.dateTextView.setText(date);
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView senderTextView, messageTextView, dateTextView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }
}
