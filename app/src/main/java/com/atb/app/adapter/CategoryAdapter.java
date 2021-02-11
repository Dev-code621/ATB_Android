package com.atb.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atb.app.R;
import com.atb.app.activities.register.CreateFeedActivity;
import com.atb.app.commons.Constants;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
     CreateFeedActivity context;
    ArrayList<Boolean> categoriModels = new ArrayList<>();
    public CategoryAdapter(CreateFeedActivity context) {
        this.context = context;
    }


    public void setData( ArrayList<Boolean>categories){
        this.categoriModels.clear();
        this.categoriModels.addAll(categories);
        notifyDataSetChanged();
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
        if(categoriModels.get(position)){
            holder.imv_image.setImageResource(Constants.category_selected[position]);
            holder.imv_selector.setVisibility(View.VISIBLE);
            holder.lyt_item.setBackgroundResource(R.drawable.button_rectangle_round);
            holder.txv_text.setTextColor(Color.WHITE);

        }else {
            holder.imv_image.setImageResource(Constants.category_unselected[position]);
            holder.imv_selector.setVisibility(View.GONE);
            holder.lyt_item.setBackgroundResource(R.drawable.edit_rectangle_round);
            holder.txv_text.setTextColor(Color.parseColor("#707070"));
        }

        holder.lyt_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.selectItem(position);


            }
        });
    }
    @Override
    public int getItemCount() {
        return categoriModels.size();
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

