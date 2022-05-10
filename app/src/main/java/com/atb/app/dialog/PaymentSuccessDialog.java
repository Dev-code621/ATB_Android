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
import com.atb.app.base.CommonActivity;
import com.atb.app.model.NewsFeedEntity;
import com.atb.app.model.VariationModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PaymentSuccessDialog extends DialogFragment {

    private OnConfirmListener listener;
    NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
    int deliveryOption;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.payment_success_dialog, container, false);
    }

    public ConfirmDialog setOnConfirmListener(OnConfirmListener listener, NewsFeedEntity newsFeedEntity,int deliveryOption) {
        this.listener = listener;
        this.newsFeedEntity = newsFeedEntity;
        this.deliveryOption = deliveryOption;
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView imv_image = view.findViewById(R.id.imv_image);

        LinearLayout lyt_mypurchase = view.findViewById(R.id.lyt_mypurchase);
        LinearLayout lyt_keep_buying = view.findViewById(R.id.lyt_keep_buying);
        LinearLayout lyt_contact_seller = view.findViewById(R.id.lyt_contact_seller);
        TextView txv_title = view.findViewById(R.id.txv_title);
        if(deliveryOption == 3){
            txv_title.setText("Thank you for your purchase, please contact the Seller to arrange collection of your item");
        }else if(deliveryOption ==5){
            txv_title.setText("Thank you for your purchase, please contact the Seller to arrange delivery of your item");

        }
        lyt_mypurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPurchase();
                dismiss();
            }
        });

        lyt_keep_buying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onKeepBy();
                dismiss();

            }
        });

        lyt_contact_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CommonActivity)getContext()).gotochat(getContext(),newsFeedEntity.getPoster_profile_type(),newsFeedEntity.getUserModel());

            }
        });
        Glide.with(getContext()).load(newsFeedEntity.getPostImageModels().get(0).getPath()).placeholder(R.drawable.profile_pic).into(imv_image);
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
        void onPurchase();
        void onKeepBy();
    }
}

