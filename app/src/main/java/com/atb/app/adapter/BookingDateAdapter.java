package com.atb.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.atb.app.R;
import com.atb.app.commons.Commons;

import java.util.ArrayList;

public class BookingDateAdapter extends RecyclerView.Adapter<BookingDateAdapter.ViewHolder> {
    private final Context context;
    private  ArrayList<Integer> list;
    private OnSlotSelectListener listener;
    int month,year;
    int select = -1;
    public BookingDateAdapter(Context context, ArrayList<Integer> timeSlot,int year,int month, OnSlotSelectListener listener) {
        this.list = timeSlot;
        this.listener = listener;
        this.context = context;
        this.month =month;
        this.year = year;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feature_booking_slot_item, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txt_day.setText(String.valueOf(position+1));
        holder.txv_date.setText(Commons.getWeekday(String.valueOf(position+1)+"-" + String.valueOf(month)+"-"+String.valueOf(year)));
        holder.txv_slot_count.setText(String.valueOf(list.get(position)));
        holder.lyt_container.setBackgroundColor(context.getResources().getColor(R.color.signup_popup_color));
        if(position==select)
            holder.lyt_container.setBackground(context.getResources().getDrawable(R.drawable.button_rectangle_round));

        holder.lyt_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.get(position)!=0) {
                    listener.onSlotSelect(list.get(position));
                    select = position;
                    notifyDataSetChanged();
                }
            }
        });
        if(list.get(position)==0){
            holder.view_count.setVisibility(View.VISIBLE);
            holder.txv_date.setTextColor(context.getResources().getColor(R.color.signup_textcolor));
            holder.txt_day.setTextColor(context.getResources().getColor(R.color.header_color1));
        }else {
            holder.view_count.setVisibility(View.GONE);
            holder.txt_day.setTextColor(context.getResources().getColor(R.color.txt_color));
            holder.txt_day.setTextColor(context.getResources().getColor(R.color.head_color));
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
       TextView txv_slot_count,txt_day,txv_date;
       CardView card_user_info_image;
       LinearLayout lyt_container;
       View view_count;
        public ViewHolder(View itemView) {
            super(itemView);
            txv_slot_count=itemView.findViewById(R.id.txv_slot_count);
            txt_day=itemView.findViewById(R.id.txt_day);
            txv_date=itemView.findViewById(R.id.txv_date);
            card_user_info_image=itemView.findViewById(R.id.card_user_info_image);
            lyt_container = itemView.findViewById(R.id.lyt_container);
            view_count = itemView.findViewById(R.id.view_count);
        }
    }

    public interface OnSlotSelectListener{
        void onSlotSelect(int posstion);
    }
}
