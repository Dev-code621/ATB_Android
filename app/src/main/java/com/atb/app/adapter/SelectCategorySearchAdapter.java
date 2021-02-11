package com.atb.app.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.atb.app.R;
import com.atb.app.activities.MainActivity;
import com.atb.app.commons.Constants;
import com.atb.app.fragement.SearchFragment;

import java.util.ArrayList;

public class SelectCategorySearchAdapter extends RecyclerView.Adapter<SelectCategorySearchAdapter.ViewHolder> {
     MainActivity context;
    ArrayList<Boolean> booleans = new ArrayList<>();
    SearchFragment searchFragment;
    public SelectCategorySearchAdapter(MainActivity context,  SearchFragment searchFragment,  ArrayList<Boolean>booleans) {
        this.context = context;
        this.booleans = booleans;
        this.searchFragment = searchFragment;
    }

    public SelectCategorySearchAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.searchcategory_item, parent, false);
        
        return new ViewHolder(itemView);
    }

    public void setData(ArrayList<Boolean>booleans){
        this.booleans = booleans;
        notifyDataSetChanged();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.imv_image.clearColorFilter();
        if(position ==0){
            holder.txv_text.setText(R.string.my_atb);
            holder.imv_image.setImageResource(R.drawable.icon_plus);
            holder.lyt_item.setBackgroundResource(R.drawable.login_button_rectangle_round);
            holder.txv_text.setTextColor(Color.WHITE);
        }else {
            holder.txv_text.setText(Constants.category_word[position-1]);
            holder.imv_image.setImageResource(Constants.category_selected[position-1]);
            holder.lyt_item.setBackgroundResource(R.drawable.login_button_rectangle_round);
            holder.txv_text.setTextColor(Color.WHITE);
        }

        if(booleans.get(position)){
            holder.txv_text.setTextColor(R.color.head_color);
            holder.lyt_item.setBackgroundResource(R.drawable.edit_rectangle_round);
            holder.imv_image.setColorFilter(R.color.head_color, PorterDuff.Mode.SRC_IN);
        }

        holder.lyt_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               searchFragment.selectCategory(position);

            }
        });
    }
    @Override
    public int getItemCount() {
        return booleans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txv_text;
        ImageView imv_image;
        FrameLayout lyt_item;
        public ViewHolder(View itemView) {
            super(itemView);
            txv_text=itemView.findViewById(R.id.txv_text);
            imv_image=itemView.findViewById(R.id.imv_image);
            lyt_item =itemView.findViewById(R.id.lyt_item);
        }
    }

}

