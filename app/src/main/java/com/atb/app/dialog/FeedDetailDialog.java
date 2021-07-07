package com.atb.app.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.atb.app.R;
import com.atb.app.model.NewsFeedEntity;


public class FeedDetailDialog extends DialogFragment {

    private OnConfirmListener listener;
    boolean type ;
    int follower;
    NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.dialog_feed_detail, container, false);
    }

    public FeedDetailDialog setOnConfirmListener(OnConfirmListener listener, boolean type,int follower) {
        this.listener = listener;
        this.type = type;
        this.follower = follower;
        return null;
    }
    public FeedDetailDialog setOnConfirmListener(OnConfirmListener listener, boolean type, int follower, NewsFeedEntity newsFeedEntity) {
        this.listener = listener;
        this.type = type;
        this.follower = follower;
        this.newsFeedEntity = newsFeedEntity;
        return null;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LinearLayout lyt_report = view.findViewById(R.id.lyt_report);
        LinearLayout lyt_block = view.findViewById(R.id.lyt_block);
        LinearLayout lyt_follow = view.findViewById(R.id.lyt_follow);
        LinearLayout lyt_share = view.findViewById(R.id.lyt_share);
        ImageView imv_share = view.findViewById(R.id.imv_share);
        ImageView imv_follow = view.findViewById(R.id.imv_follow);
        ImageView imv_block = view.findViewById(R.id.imv_block);
        ImageView imv_report = view.findViewById(R.id.imv_report);
        TextView txv_report = view.findViewById(R.id.txv_report);
        TextView txv_follow = view.findViewById(R.id.txv_follow);
        TextView txv_share = view.findViewById(R.id.txv_share);
        TextView txv_block = view.findViewById(R.id.txv_block);
        TextView txv_canel = view.findViewById(R.id.txv_canel);
        if(!type){
            if(newsFeedEntity.getPost_type()!=2){
                lyt_report.setVisibility(View.GONE);
            }else {
                if(newsFeedEntity.getIs_sold() ==0){
                    txv_report.setText("Sold out");
                }else {
                    txv_report.setText("Re-list");
                }
                imv_report.setImageDrawable(getResources().getDrawable(R.drawable.icon_sales));
                imv_report.setColorFilter(getResources().getColor(R.color.discard_color), PorterDuff.Mode.SRC_IN);
            }
            txv_block.setText("Delete");
            imv_block.setImageDrawable(getResources().getDrawable(R.drawable.file_trash));
            imv_block.setColorFilter(getResources().getColor(R.color.discard_color), PorterDuff.Mode.SRC_IN);
            txv_block.setTextColor(getResources().getColor(R.color.discard_color));
            txv_follow.setText("Edit");
            imv_follow.setImageDrawable(getResources().getDrawable(R.drawable.icon_edit));
        }else {
            if(follower==1)
                txv_follow.setText("Unfollow");
            else
                txv_follow.setText("Follow");
        }

        lyt_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onReportPost();
                dismiss();
            }
        });

        lyt_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onBlockUser();
                dismiss();
            }
        });
        lyt_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFollowUser();
                dismiss();
            }
        });

        lyt_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onShare();
                dismiss();
            }
        });
        txv_canel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }




    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    public interface OnConfirmListener {
        void onReportPost();
        void onBlockUser();
        void onFollowUser();
        void onShare();
    }
}

