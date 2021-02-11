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
        TextView txv_text;
        ImageView imv_image,imv_selector;
        LinearLayout lyt_container;
        public ViewHolder(View itemView) {
            super(itemView);
            lyt_container=itemView.findViewById(R.id.lyt_container);

            if(getItemCount()>1){
                ViewGroup.LayoutParams layoutParams = lyt_container.getLayoutParams();
                DisplayMetrics metrics = new DisplayMetrics();
                ((CommonActivity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
                layoutParams.width = metrics.widthPixels - 300;
                lyt_container.setLayoutParams(layoutParams);

                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) lyt_container.getLayoutParams();
                p.setMargins(0, 0, 30, 0);
                lyt_container.requestLayout();

            }else {
                ViewGroup.LayoutParams layoutParams = lyt_container.getLayoutParams();
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                lyt_container.setLayoutParams(layoutParams);
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) lyt_container.getLayoutParams();
                p.setMargins(0, 0, 0, 0);
                lyt_container.requestLayout();
            }
        }
    }

    public interface OnSelectListener{
        void onSelect(NewsFeedEntity newsFeedEntity);
    }

}
