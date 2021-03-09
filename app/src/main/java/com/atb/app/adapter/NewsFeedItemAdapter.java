package com.atb.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.atb.app.R;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.register.CreateFeedActivity;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.commons.Constants;
import com.atb.app.commons.Helper;
import com.atb.app.model.NewsFeedEntity;
import com.atb.app.model.submodel.VotingModel;
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
        holder.txv_description.setText(newsFeedEntity.getTitle());
        holder.txv_price.setText("£"+newsFeedEntity.getPrice() );
        holder.txv_deposit.setText(newsFeedEntity.getDescription());
        holder.imv_type.setImageResource(Constants.postType[newsFeedEntity.getPost_type()]);
        holder.imv_txv_type.setImageResource(Constants.postType[newsFeedEntity.getPost_type()]);

        holder.txv_description.setTextColor(context.getResources().getColor(R.color.txt_color));
        holder.txv_price.setTextColor(context.getResources().getColor(R.color.txt_color));
        holder.txv_deposit.setTextColor(context.getResources().getColor(R.color.txt_color));
        holder.lyt_approve.setBackgroundColor(context.getResources().getColor(R.color.white));
        holder.imv_txv_type.setVisibility(View.GONE);
        if(newsFeedEntity.getPoster_profile_type()==1){
            holder.lyt_container.setBackgroundColor(context.getResources().getColor(R.color.head_color));
            holder.imv_atb_approved.setVisibility(View.VISIBLE);
            holder.txv_name.setTextColor(context.getResources().getColor(R.color.white));
            holder.txv_time.setTextColor(context.getResources().getColor(R.color.white));

            if(newsFeedEntity.getPostImageModels().size()==0){
                holder.lyt_approve.setBackgroundColor(context.getResources().getColor(R.color.head_color));
                holder.txv_description.setTextColor(context.getResources().getColor(R.color.white));
                holder.txv_price.setTextColor(context.getResources().getColor(R.color.white));
                holder.txv_deposit.setTextColor(context.getResources().getColor(R.color.white));
                holder.imv_txv_type.setVisibility(View.VISIBLE);
                holder.imv_txv_type.clearColorFilter();
                holder.imv_txv_type.setColorFilter(context.getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
            }
        }else {
            holder.lyt_container.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.imv_atb_approved.setVisibility(View.GONE);
            holder.txv_name.setTextColor(context.getResources().getColor(R.color.head_color));
            holder.txv_time.setTextColor(context.getResources().getColor(R.color.head_color));
            if(newsFeedEntity.getPostImageModels().size()==0) {
                holder.imv_txv_type.setVisibility(View.VISIBLE);
                holder.imv_txv_type.clearColorFilter();
                holder.imv_txv_type.setColorFilter(context.getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);
            }
        }
        if(newsFeedEntity.getPost_type()==2 || newsFeedEntity.getPost_type() ==3)
            holder.txv_price.setVisibility(View.VISIBLE);
        else
            holder.txv_price.setVisibility(View.GONE);
        if(newsFeedEntity.getPostImageModels().size()>0){
            holder.lyt_image.setVisibility(View.VISIBLE);
            Glide.with(context).load(newsFeedEntity.getPostImageModels().get(0).getPath()).placeholder(R.drawable.image_thumnail).dontAnimate().into(holder.imv_imageview);
        }else {
            holder.lyt_image.setVisibility(View.GONE);
        }

        if(newsFeedEntity.getPost_type() ==4){
         holder.txv_price.setVisibility(View.GONE);
         holder.txv_deposit.setVisibility(View.GONE);
         VotingListAdapter votingListAdapter = new VotingListAdapter(context);
         holder.lyt_votelist.setAdapter(votingListAdapter);
         votingListAdapter.setRoomData(newsFeedEntity.getPoll_options());
         Helper.getListViewSize(holder.lyt_votelist);
         holder.lyt_votelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 VotingModel votingModel = newsFeedEntity.getPoll_options().get(position);
                 for(int i =0;i<newsFeedEntity.getPoll_options().size();i++) {
                     if (Commons.myVoting(newsFeedEntity.getPoll_options().get(i))) {
                         ((CommonActivity) context).showAlertDialog("You've aleady voted on this poll!");
                         return;
                     }
                 }

                 /// change poll event

             }
         });
        }else {
            holder.txv_price.setVisibility(View.VISIBLE);
            holder.txv_deposit.setVisibility(View.VISIBLE);
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
        ImageView imv_profile,imv_imageview,imv_atb_approved,imv_group,imv_type,imv_txv_type;
        LinearLayout lyt_container,lyt_approve;
        FrameLayout lyt_image;
        CardView card_imv_group,card_imv_type;
        ListView lyt_votelist;
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
            card_imv_group = itemView.findViewById(R.id.card_imv_group);
            card_imv_type = itemView.findViewById(R.id.card_imv_type);
            lyt_approve = itemView.findViewById(R.id.lyt_approve);
            imv_txv_type = itemView.findViewById(R.id.imv_txv_type);
            lyt_votelist = itemView.findViewById(R.id.lyt_votelist);
            if(getItemCount()>1){
                ViewGroup.LayoutParams layoutParams = lyt_container.getLayoutParams();
                DisplayMetrics metrics = new DisplayMetrics();
                ((CommonActivity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
                layoutParams.width = metrics.widthPixels - 300;
                lyt_container.setLayoutParams(layoutParams);

                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) lyt_container.getLayoutParams();
                p.setMargins(0, 0, 30, 0);
                lyt_container.requestLayout();

                card_imv_group.setVisibility(View.VISIBLE);

            }else {
                ViewGroup.LayoutParams layoutParams = lyt_container.getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                lyt_container.setLayoutParams(layoutParams);
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) lyt_container.getLayoutParams();
                p.setMargins(0, 0, 0, 0);
                lyt_container.requestLayout();
                card_imv_group.setVisibility(View.GONE);
            }

        }
    }

    public interface OnSelectListener{
        void onSelect(NewsFeedEntity newsFeedEntity);
    }

}
