package com.atb.app.adapter;

import android.content.Context;
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
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PollEmageAdapter extends RecyclerView.Adapter<PollEmageAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<String> list = new ArrayList<>();
    private SelectListener listener;
    int maxsize =5;
    public PollEmageAdapter(Context context,SelectListener listener) {
        this.context = context;
        this.listener = listener;
    }
    public PollEmageAdapter(Context context,SelectListener listener,int maxsize) {
        this.context = context;
        this.listener = listener;
        this.maxsize = maxsize;
    }


    public void setData(ArrayList<String>arrayList){
        list = arrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.poll_image_item, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if(position == list.size()){
            holder.lyt_image.setVisibility(View.GONE);
            holder.lyt_add.setVisibility(View.VISIBLE);
            holder.lyt_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.addImage();
                }
            });
        }else {
            holder.lyt_image.setVisibility(View.VISIBLE);
            holder.lyt_add.setVisibility(View.GONE);
            Glide.with(context).load(list.get(position)).placeholder(R.drawable.image_thumnail).dontAnimate().into(holder.imageView);
            holder.imv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClose(position);
                }
            });
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
    @Override
    public int getItemCount() {
        if(list.size()==maxsize)return  list.size();
        else
            return list.size()+1;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
       ImageView imageView,imv_close;
       FrameLayout lyt_image;
       LinearLayout lyt_add;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imv_item);
            imv_close = itemView.findViewById(R.id.imv_close);
            lyt_image = itemView.findViewById(R.id.lyt_image);
            lyt_add = itemView.findViewById(R.id.lyt_add);
        }
    }

    public interface SelectListener{
        void onClose(int posstion);
        void addImage();
    }

}
