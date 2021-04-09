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
import com.atb.app.activities.newsfeedpost.NewsDetailActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.NewsFeedEntity;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class PaymentBookingDialog extends DialogFragment {

    private OnConfirmListener listener;
    NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
    int type =-1;
    int payment_type = -1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.payment_booking_dialog, container, false);
    }

    public ConfirmDialog setOnConfirmListener(OnConfirmListener listener, NewsFeedEntity newsFeedEntity,int type) {
        this.listener = listener;
        this.newsFeedEntity = newsFeedEntity;
        this.type = type;
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView imv_image = view.findViewById(R.id.imv_image);
        ImageView imv_type = view.findViewById(R.id.imv_type);
        ImageView imv_back = view.findViewById(R.id.imv_back);
        TextView txv_name = view.findViewById(R.id.txv_name);
        TextView txv_price = view.findViewById(R.id.txv_price);
        TextView txv_type = view.findViewById(R.id.txv_type);
        TextView txv_delivery_cost = view.findViewById(R.id.txv_delivery_cost);

        LinearLayout lyt_paypal = view.findViewById(R.id.lyt_paypal);
        ImageView imv_paypal = view.findViewById(R.id.imv_paypal);
        TextView txv_paypal = view.findViewById(R.id.txv_paypal);

        LinearLayout lyt_cash = view.findViewById(R.id.lyt_cash);
        ImageView imv_cash = view.findViewById(R.id.imv_cash);
        TextView txv_cash1 = view.findViewById(R.id.txv_cash1);
        TextView txv_cash2 = view.findViewById(R.id.txv_cash2);
        ImageView imv_cash2 = view.findViewById(R.id.imv_cash2);
        TextView txv_pay = view.findViewById(R.id.txv_pay);

        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        lyt_paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyt_paypal.setBackground(getContext().getResources().getDrawable(R.drawable.button_rectangle_round));
                imv_paypal.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
                txv_paypal.setTextColor(getResources().getColor(R.color.white));

                lyt_cash.setBackground(getContext().getResources().getDrawable(R.drawable.rectangle_border_round));
                imv_cash.setColorFilter(getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);
                imv_cash2.setColorFilter(getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);
                txv_cash1.setTextColor(getResources().getColor(R.color.txt_color));
                txv_cash2.setTextColor(getResources().getColor(R.color.head_color));
                txv_pay.setText("Pay £" +newsFeedEntity.getPrice());
                payment_type = 0;

            }
        });
        lyt_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyt_cash.setBackground(getContext().getResources().getDrawable(R.drawable.button_rectangle_round));
                imv_cash.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
                imv_cash2.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
                txv_cash1.setTextColor(getResources().getColor(R.color.white));
                txv_cash2.setTextColor(getResources().getColor(R.color.white));

                lyt_paypal.setBackground(getContext().getResources().getDrawable(R.drawable.rectangle_border_round));
                imv_paypal.setColorFilter(getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);
                txv_paypal.setTextColor(getResources().getColor(R.color.txt_color));
                txv_pay.setText("Be in touch with the seller");
                payment_type = 1;

            }
        });
        txv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(payment_type>=0) {
                    listener.onConfirm(payment_type);
                    dismiss();
                }
            }
        });

        Glide.with(getContext()).load(newsFeedEntity.getPostImageModels().get(0).getPath()).placeholder(R.drawable.profile_pic).into(imv_image);
        txv_price.setText("£"+ newsFeedEntity.getPrice());
        if(type==1){
            txv_type.setText("Free postage");
            imv_type.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ico_postage));

        }else if(type==3){
            txv_type.setText("I'll Collect");
            imv_type.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ico_collect));
        }else if(type ==5){
            txv_type.setText("Deliver");
            imv_type.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ico_postage));
            txv_delivery_cost.setText("+£" + newsFeedEntity.getDelivery_cost());
        }

    }




    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    public interface OnConfirmListener {
        void onConfirm(int payment);
    }
}

