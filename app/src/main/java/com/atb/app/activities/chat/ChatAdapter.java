package com.atb.app.activities.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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
import com.atb.app.activities.register.CreateFeedActivity;
import com.atb.app.commons.Commons;
import com.atb.app.commons.Constants;
import com.atb.app.util.RoundedCornersTransformation;
import com.atb.app.view.zoom.ImageZoomButton;
import com.atb.app.view.zoom.ZoomAnimation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pubnub.api.models.consumer.history.PNHistoryItemResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.ConsoleHandler;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    ChatActivity context;
    List<MessageModel> messageModels = new ArrayList<>();
    public ChatAdapter(ChatActivity context) {
        this.context = context;
    }


    public void setData(  List<MessageModel> messageModels){
        this.messageModels.clear();
        this.messageModels.addAll(messageModels);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        MessageModel message = messageModels.get(position);

        if(message.getSenderId().contains("user_"+Commons.g_user.getId()) || message.getSenderId().contains("business_" + Commons.g_user.getBusinessModel().getId())){
            holder.card_friendphoto.setVisibility(View.GONE);
            holder.lyt_friend.setVisibility(View.GONE);
            holder.card_myphoto.setVisibility(View.VISIBLE);
            holder.lyt_mymessage.setVisibility(View.VISIBLE);
            Glide.with(context).load(message.getSenderImage()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(context, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(holder.imv_myPhoto);
            holder.txv_message2.setText(message.getMessage());
            holder.txv_message2.setVisibility(View.VISIBLE);
            holder.imv_imgmsg2.setVisibility(View.GONE);
            holder.txv_time2.setText(getFormattedDateAndTime(context,
                    message.getCreateAt()/10000));

            if(message.getMessageType().equals("Image")){
                holder.txv_message2.setVisibility(View.GONE);
                holder.imv_imgmsg2.setVisibility(View.VISIBLE);
                Glide.with(context).load(message.getMessage()).placeholder(R.drawable.image_thumnail).dontAnimate().into(holder.imv_imgmsg2);
            }
            holder.imv_imgmsg2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.showPicture(v, message.getMessage());
                }
            });

        }else {
            holder.card_friendphoto.setVisibility(View.VISIBLE);
            holder.lyt_friend.setVisibility(View.VISIBLE);
            holder.card_myphoto.setVisibility(View.GONE);
            holder.lyt_mymessage.setVisibility(View.GONE);
            Glide.with(context).load(message.getSenderImage()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(context, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(holder.imv_friendPhoto);
            holder.txv_message1.setText(message.getMessage());
            holder.txv_friendName.setText(message.getSenderName());
            holder.txv_message1.setVisibility(View.VISIBLE);
            holder.imv_imgmsg1.setVisibility(View.GONE);
            holder.txv_time1.setText(getFormattedDateAndTime(context,
                    message.getCreateAt()/10000));
            holder.lyt_friend.setBackground(context.getResources().getDrawable(R.drawable.bg_balloon_g));
            if(message.getMessageType().equals("Image")){
                holder.txv_message1.setVisibility(View.GONE);
                holder.imv_imgmsg1.setVisibility(View.VISIBLE);
                Glide.with(context).load(message.getMessage()).placeholder(R.drawable.image_thumnail).dontAnimate().into(holder.imv_imgmsg1);
                holder.lyt_friend.setBackground(null);
            }
            holder.imv_imgmsg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.showPicture(v, message.getMessage());
                }
            });
        }



    }
    @Override
    public int getItemCount() {
        return messageModels.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txv_friendName,txv_message1,txv_time1,txv_message2,txv_time2;
        LinearLayout lyt_friend,lyt_mymessage;
        ImageView imv_friendPhoto,imv_myPhoto,message_state;
        ImageZoomButton imv_imgmsg1,imv_imgmsg2;
        CardView card_friendphoto,card_myphoto;

        public ViewHolder(View itemView) {
            super(itemView);
            txv_friendName=itemView.findViewById(R.id.txv_friendName);
            txv_message1=itemView.findViewById(R.id.txv_message1);
            txv_time1=itemView.findViewById(R.id.txv_time1);
            txv_message2 =itemView.findViewById(R.id.txv_message2);
            txv_time2=itemView.findViewById(R.id.txv_time2);
            lyt_friend=itemView.findViewById(R.id.lyt_friend);
            lyt_mymessage=itemView.findViewById(R.id.lyt_mymessage);
            imv_friendPhoto =itemView.findViewById(R.id.imv_friendPhoto);

            imv_imgmsg1=itemView.findViewById(R.id.imv_imgmsg1);
            imv_myPhoto=itemView.findViewById(R.id.imv_myPhoto);
            imv_imgmsg2=itemView.findViewById(R.id.imv_imgmsg2);
            message_state =itemView.findViewById(R.id.message_state);
            card_friendphoto=itemView.findViewById(R.id.card_friendphoto);
            card_myphoto=itemView.findViewById(R.id.card_myphoto);

        }
    }

    public static String getFormattedDateAndTime(Context context, Long timestamp) {

        Date date = new Date(timestamp);
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd MMM hh:mm aa");
        return fullDateFormat.format(date);
    }

}

