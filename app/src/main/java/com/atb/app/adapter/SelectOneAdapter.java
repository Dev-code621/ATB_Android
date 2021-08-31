package com.atb.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.atb.app.R;

import java.util.ArrayList;
import java.util.List;

public class SelectOneAdapter extends RecyclerView.Adapter<SelectOneAdapter.DataObjectHolder> {


    Context context;
   ArrayList<String> helpSupports = new ArrayList<>();
    OnSelectListener listener;
    int selectedPosition = 0;

    public SelectOneAdapter(  ArrayList<String>  helpSupportList, Context context, OnSelectListener listener) {

        this.context = context;
        this.helpSupports.clear();
        this.helpSupports.addAll(helpSupportList);
        this.listener = listener;

    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {
        TextView txt_username;
        LinearLayout lyt_item;

        public DataObjectHolder(View itemView) {
            super(itemView);
            txt_username = (TextView) itemView.findViewById(R.id.txt_username);
            lyt_item = itemView.findViewById(R.id.lyt_item);

        }
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.select_one, parent, false);
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        holder.txt_username.setText(helpSupports.get(position));
        holder.lyt_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelect(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return helpSupports.size();
    }

    public interface OnSelectListener {
        void onSelect(int selectPosstion);
    }
}

