package com.atb.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.atb.app.R;
import com.atb.app.commons.Commons;
import com.atb.app.commons.Constants;
import com.atb.app.model.BookingEntity;
import com.atb.app.model.BoostModel;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class BoostItemAdapter extends RecyclerView.Adapter<BoostItemAdapter.ViewHolder> {
    private final Context context;
    private HashMap<String,  ArrayList<BoostModel>> roomData = new HashMap<>();
    private OnSelectListener listener;
    public BoostItemAdapter(Context context,  OnSelectListener listener) {
        this.listener = listener;
        this.context = context;
    }
    public void setRoomData(  HashMap<String,  ArrayList<BoostModel>> boostModels) {
        roomData.clear();
        roomData.putAll(boostModels);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.boost_item, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        int key_number = position/6;
        int index = position %6;
        BoostModel boostModel = roomData.get(Constants.category_word[key_number]).get(index);
        if(!boostModel.isEmptyModel() ){
            holder.imv_profile.setVisibility(View.GONE);
            holder.imv_add.setVisibility(View.VISIBLE);
            holder.txv_add.setVisibility(View.VISIBLE);
            holder.txv_name.setVisibility(View.GONE);
            holder.txv_name.setText("Boost your\nBusiness");
        }else {
            holder.imv_profile.setVisibility(View.VISIBLE);
            holder.imv_add.setVisibility(View.GONE);
            holder.txv_add.setVisibility(View.GONE);
            holder.txv_name.setVisibility(View.VISIBLE);

            Glide.with(context).load(boostModel.getUserModel().getBusinessModel().getBusiness_logo()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(context, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(holder.imv_profile);
            holder.txv_name.setText(boostModel.getUserModel().getBusinessModel().getBusiness_name());
        }
        holder.imv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelectItem(boostModel);
            }
        });

        holder.imv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSelectItem(boostModel);
            }
        });

    }
    @Override
    public int getItemCount() {
        if(roomData.size()==0)return 0;
        if(Commons.main_category.equals(context.getResources().getString(R.string.my_atb)))
            return 60;
        else
            return 6;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
       CardView card_profile;
       ImageView imv_add,imv_profile;
       TextView txv_name,txv_add;
       LinearLayout lyt_container;
        public ViewHolder(View itemView) {
            super(itemView);
            card_profile=itemView.findViewById(R.id.card_profile);
            imv_profile = itemView.findViewById(R.id.imv_profile);
            imv_add = itemView.findViewById(R.id.imv_add);
            txv_name = itemView.findViewById(R.id.txv_name);
            lyt_container = itemView.findViewById(R.id.lyt_container);
            txv_add = itemView.findViewById(R.id.txv_add);
        }
    }

    public interface OnSelectListener{
        void onSelectItem(BoostModel boostModel);
    }
}
