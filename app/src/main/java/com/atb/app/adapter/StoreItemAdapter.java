package com.atb.app.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atb.app.R;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.register.CreateFeedActivity;
import com.atb.app.commons.Constants;

import java.util.ArrayList;

public class SelectCategoryAdapter extends RecyclerView.Adapter<SelectCategoryAdapter.ViewHolder> {
    MainActivity context;
    public SelectCategoryAdapter(MainActivity context) {
        this.context = context;
    }

    public SelectCategoryAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);
        
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txv_text.setText(Constants.category_word[position]);
        holder.imv_image.setImageResource(Constants.category_selected[position]);
        holder.imv_selector.setVisibility(View.GONE);
        holder.lyt_item.setBackgroundResource(R.drawable.button_rectangle_round);
        holder.txv_text.setTextColor(Color.WHITE);
        holder.lyt_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.selectItem(position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return Constants.category_selected.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txv_text;
        ImageView imv_image,imv_selector;
        FrameLayout lyt_item;
        public ViewHolder(View itemView) {
            super(itemView);
            txv_text=itemView.findViewById(R.id.txv_text);
            imv_image=itemView.findViewById(R.id.imv_image);
            imv_selector=itemView.findViewById(R.id.imv_selector);
            lyt_item =itemView.findViewById(R.id.lyt_item);
        }
    }

}

