package com.joaquimley.com;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.SmsViewHolder> {

    private List<SmsModel> smsList;

    public SmsAdapter(List<SmsModel> smsList) {
        this.smsList = smsList;
    }

    @NonNull
    @Override
    public SmsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sms_item, parent, false);
        return new SmsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SmsViewHolder holder, int position) {
        SmsModel sms = smsList.get(position);
        holder.senderTextView.setText(sms.getSender());
        holder.messageTextView.setText(sms.getMessage());
    }

    @Override
    public int getItemCount() {
        return smsList.size();
    }

    public static class SmsViewHolder extends RecyclerView.ViewHolder {
        TextView senderTextView, messageTextView;

        public SmsViewHolder(@NonNull View itemView) {
            super(itemView);
            senderTextView = itemView.findViewById(R.id.senderTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
        }
    }

    public void addMessage(SmsModel sms) {
        smsList.add(0, sms);  // Add new messages to the top
        notifyItemInserted(0);
    }
}
