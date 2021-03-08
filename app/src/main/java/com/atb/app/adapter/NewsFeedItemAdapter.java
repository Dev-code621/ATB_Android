package com.atb.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atb.app.R;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.register.CreateFeedActivity;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Constants;
import com.atb.app.model.NewsFeedEntity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class NewsFeedItemAdapter extends RecyclerView.Adapter<NewsFeedItemAdapter.ViewHolder> {
    Context context;
    ArrayList<NewsFeedEntity>newsFeedEntities = new ArrayList<>();
    private   OnSelectListener listener;

    public NewsFeedItemAdapter(Context context,  ArrayList<NewsFeedEntity>newsFeedEntities, OnSelectListener listener) {
        this.newsFeedEntities = newsFeedEntities;
        this.listener = listener;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.newsfeed_item1, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        NewsFeedEntity newsFeedEntity = newsFeedEntities.get(position);
        Glide.with(context).load(newsFeedEntity.getProfile_image()).placeholder(R.drawable.image_thumnail).dontAnimate().into(holder.imv_profile);
        holder.txv_name.setText(newsFeedEntity.getProfile_name());
        holder.txv_time.setText(newsFeedEntity.getRead_created());
        holder.txv_comment.setText(String.valueOf(newsFeedEntity.getComments()));
        holder.txv_heart.setText(String.valueOf(newsFeedEntity.getLikes()));
        if(newsFeedEntity.getPostImageModels().size()>0){
            holder.lyt_image.setVisibility(View.VISIBLE);
            Glide.with(context).load(newsFeedEntity.getPostImageModels().get(0).getPath()).placeholder(R.drawable.image_thumnail).dontAnimate().into(holder.imv_imageview);
        }else {
            holder.lyt_image.setVisibility(View.GONE);
        }
        holder.lyt_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelect( newsFeedEntities.get(position));
            }
        });
    }
    @Override
    public int getItemCount() {
        return newsFeedEntities.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txv_name,txv_time,txv_description,txv_price,txv_deposit,txv_heart,txv_comment;
        ImageView imv_profile,imv_imageview,imv_atb_approved,imv_group,imv_type;
        LinearLayout lyt_container;
        FrameLayout lyt_image;
        public ViewHolder(View itemView) {
            super(itemView);
            lyt_container=itemView.findViewById(R.id.lyt_container);
            txv_name = itemView.findViewById(R.id.txv_name);
            txv_time = itemView.findViewById(R.id.txv_time);
            txv_description = itemView.findViewById(R.id.txv_description);
            txv_price = itemView.findViewById(R.id.txv_price);
            txv_deposit = itemView.findViewById(R.id.txv_deposit);
            txv_heart = itemView.findViewById(R.id.txv_heart);
            txv_comment = itemView.findViewById(R.id.txv_comment);
            imv_profile = itemView.findViewById(R.id.imv_profile);
            imv_imageview = itemView.findViewById(R.id.imv_imageview);
            imv_atb_approved = itemView.findViewById(R.id.imv_atb_approved);
            imv_group = itemView.findViewById(R.id.imv_group);
            imv_type = itemView.findViewById(R.id.imv_type);
            lyt_image = itemView.findViewById(R.id.lyt_image);
            if(getItemCount()>1){
                ViewGroup.LayoutParams layoutParams = lyt_container.getLayoutParams();
                DisplayMetrics metrics = new DisplayMetrics();
                ((CommonActivity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
                layoutParams.width = metrics.widthPixels - 300;
                lyt_container.setLayoutParams(layoutParams);

                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) lyt_container.getLayoutParams();
                p.setMargins(0, 0, 30, 0);
                lyt_container.requestLayout();

                imv_group.setVisibility(View.VISIBLE);

            }else {
                ViewGroup.LayoutParams layoutParams = lyt_container.getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                lyt_container.setLayoutParams(layoutParams);
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) lyt_container.getLayoutParams();
                p.setMargins(0, 0, 0, 0);
                lyt_container.requestLayout();
                imv_group.setVisibility(View.GONE);
            }

        }
    }

    public interface OnSelectListener{
        void onSelect(NewsFeedEntity newsFeedEntity);
    }

}
