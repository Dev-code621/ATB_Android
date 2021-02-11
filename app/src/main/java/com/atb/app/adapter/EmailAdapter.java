package com.atb.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.atb.app.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.net.URLConnection;
import java.util.ArrayList;

public class EmailAdapter  extends RecyclerView.Adapter<EmailAdapter.ViewHolder> {
    private final Context context;
    private String[] list;
    private OnEmailSelectListener listener;
    public EmailAdapter(Context context, String[] list, OnEmailSelectListener listener) {
        this.list = list;
        this.listener = listener;
        this.context = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.email_item, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txv_email.setText(list[position]);
        holder.txv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onEmailSelect(list[position]);
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.length;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
       TextView txv_email;
        public ViewHolder(View itemView) {
            super(itemView);
            txv_email=itemView.findViewById(R.id.txv_email);
        }
    }

    public interface OnEmailSelectListener{
        void onEmailSelect(String path);
    }
}
