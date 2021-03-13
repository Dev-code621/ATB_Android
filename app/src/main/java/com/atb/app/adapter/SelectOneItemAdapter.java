package com.atb.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atb.app.R;
import com.atb.app.model.submodel.AttributeModel;

import java.util.ArrayList;

public class SelectOneItemAdapter extends RecyclerView.Adapter<SelectOneItemAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<AttributeModel> list;
    private OnSelectListener listener;
    int select = -1;
    public SelectOneItemAdapter(Context context, ArrayList<AttributeModel> list, OnSelectListener listener) {
        this.list = list;
        this.listener = listener;
        this.context = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.select_one_item, parent, false);

        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.txv_email.setText(list.get(position).getVariant_attirbute_value());
        if(select != position) {
            holder.txv_email.setBackgroundColor(context.getResources().getColor(R.color.signup_popup_color));
            holder.txv_email.setTextColor(context.getResources().getColor(R.color.head_color));
        }else {
            holder.txv_email.setBackgroundColor(context.getResources().getColor(R.color.head_color));
            holder.txv_email.setTextColor(context.getResources().getColor(R.color.signup_popup_color));
        }
        holder.txv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = position;
                listener.onSelect(list.get(position).getVariant_attirbute_value());

                notifyDataSetChanged();

            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
       TextView txv_email;
        public ViewHolder(View itemView) {
            super(itemView);
            txv_email=itemView.findViewById(R.id.txv_email);
        }
    }

    public interface OnSelectListener{
        void onSelect(String path);
    }
}
