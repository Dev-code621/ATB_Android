package com.atb.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.atb.app.R;
import com.atb.app.activities.MainActivity;
import com.atb.app.commons.Commons;
import com.atb.app.commons.Constants;
import com.atb.app.model.NewsFeedEntity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class StoreItemAdapter extends RecyclerView.Adapter<StoreItemAdapter.ViewHolder> {
    Context context;
    int accountType;
    ArrayList<NewsFeedEntity> newsFeedEntities = new ArrayList<>();
    SelectListener listener;
    int groupPostType;
    public StoreItemAdapter(Context context, int accountType,int groupPostType, SelectListener listener) {
        this.context = context;
        this.accountType = accountType;
        this.groupPostType = groupPostType;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_item, parent, false);
        
        return new ViewHolder(itemView);
    }

    public void setDate(ArrayList<NewsFeedEntity> date){
        newsFeedEntities.clear();
        newsFeedEntities.addAll(date);
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        NewsFeedEntity newsFeedEntity = newsFeedEntities.get(position);
        holder.imv_type.setImageResource(Constants.postType[newsFeedEntity.getPost_type()]);
        holder.txv_description.setText(newsFeedEntity.getTitle());
        holder.txv_schedule.setText(Commons.getDatefromMilionSecond(newsFeedEntity.getScheduled()));
        if(accountType ==1 && groupPostType==0)holder.card_imv_group.setVisibility(View.GONE);
        if(accountType ==1 && groupPostType ==1) holder.lyt_detail.setVisibility(View.GONE);
        if(newsFeedEntity.getIs_multi() == 1)
            holder.card_imv_group.setVisibility(View.VISIBLE);
        else
            holder.card_imv_group.setVisibility(View.GONE);
        if(newsFeedEntity.getScheduled()==0)
            holder.lyt_schedule.setVisibility(View.GONE);
        else
            holder.lyt_schedule.setVisibility(View.VISIBLE);
        if(newsFeedEntity.getPostImageModels().size()>0){
            Glide.with(context).load(newsFeedEntity.getPostImageModels().get(0).getPath()).placeholder(R.drawable.image_thumnail).dontAnimate().into(holder.imv_imageview);
            if(Commons.mediaVideoType(newsFeedEntity.getPostImageModels().get(0).getPath()))
                holder.imv_videoplay.setVisibility(View.VISIBLE);
            else
                holder.imv_videoplay.setVisibility(View.GONE);

        }

        holder.lyt_editpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnEditSelect(position);
            }
        });
        holder.lyt_make_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnPostSelect(position);
            }
        });
        holder.lyt_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemSelect(position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return newsFeedEntities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imv_imageview,imv_videoplay,imv_type;
        CardView card_imv_group,card_imv_type;
        LinearLayout lyt_detail,lyt_editpost,lyt_item,lyt_make_post,lyt_schedule;
        TextView txv_description,txv_make_post,txv_schedule;
        public ViewHolder(View itemView) {
            super(itemView);
            imv_imageview=itemView.findViewById(R.id.imv_imageview);
            imv_videoplay=itemView.findViewById(R.id.imv_videoplay);
            card_imv_group=itemView.findViewById(R.id.card_imv_group);
            card_imv_type =itemView.findViewById(R.id.card_imv_type);
            lyt_detail =itemView.findViewById(R.id.lyt_detail);
            lyt_editpost =itemView.findViewById(R.id.lyt_editpost);
            txv_description =itemView.findViewById(R.id.txv_description);
            txv_make_post =itemView.findViewById(R.id.txv_make_post);
            lyt_make_post = itemView.findViewById(R.id.lyt_make_post);
            lyt_item =itemView.findViewById(R.id.lyt_item);
            imv_type = itemView.findViewById(R.id.imv_type);
            lyt_schedule = itemView.findViewById(R.id.lyt_schedule);
            txv_schedule = itemView.findViewById(R.id.txv_schedule);
        }
    }

    public interface SelectListener{
        void OnItemSelect(int posstion);
        void OnEditSelect(int posstion);
        void OnPostSelect(int posstion);
    }

}

