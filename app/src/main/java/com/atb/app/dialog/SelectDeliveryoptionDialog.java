package com.atb.app.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.atb.app.R;
import com.atb.app.activities.navigationItems.other.LocationMapActivity;
import com.atb.app.base.CommonActivity;
import com.atb.app.model.NewsFeedEntity;

import static android.view.View.GONE;


public class SelectDeliveryoptionDialog extends DialogFragment {

    private OnConfirmListener listener;
    TextView txv_cost,txv_type,txv_description;
    ImageView imv_select;
    LinearLayout lyt_select,lyt_option1;
    NewsFeedEntity newsFeedEntity =new NewsFeedEntity();
    boolean postage = false,collect = false,deliver =false;
    int selected_type =-1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.select_delivery_option, container, false);
    }


    public SelectDeliveryoptionDialog setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
        return null;
    }
    public SelectDeliveryoptionDialog setOnConfirmListener(OnConfirmListener listener, NewsFeedEntity newsFeedEntity) {
        this.listener = listener;
        this.newsFeedEntity = newsFeedEntity;
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView imv_back =(ImageView)view.findViewById(R.id.imv_back);
        lyt_option1 =(LinearLayout) view.findViewById(R.id.lyt_option1);
        ImageView imv_postage =(ImageView)view.findViewById(R.id.imv_postage);
        TextView txv_postage =(TextView) view.findViewById(R.id.txv_postage);
        ImageView imv_collect =(ImageView)view.findViewById(R.id.imv_collect);
        TextView txv_collect =(TextView) view.findViewById(R.id.txv_collect);
        ImageView imv_deliver =(ImageView)view.findViewById(R.id.imv_deliver);
        TextView txv_deliver =(TextView) view.findViewById(R.id.txv_deliver);
        lyt_select =(LinearLayout) view.findViewById(R.id.lyt_select);
        imv_select =(ImageView)view.findViewById(R.id.imv_select);
        txv_cost =(TextView) view.findViewById(R.id.txv_cost);
        txv_type =(TextView) view.findViewById(R.id.txv_type);
        txv_description =(TextView) view.findViewById(R.id.txv_description);
        TextView txv_buy_sale =(TextView) view.findViewById(R.id.txv_buy_sale);
        LinearLayout lyt_location = (LinearLayout) view.findViewById(R.id.lyt_location);
        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        imv_postage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem(0);
            }
        });
        imv_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem(1);
            }
        });
        imv_deliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem(2);
            }
        });

        lyt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem(3);
            }
        });
        lyt_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putDouble("lat",newsFeedEntity.getLat());
                bundle.putDouble("lang" ,newsFeedEntity.getLang());
                ((CommonActivity)getContext()).goTo(getContext(), LocationMapActivity.class,false,bundle);

            }
        });
        txv_buy_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected_type>0) {
                    listener.onConfirm(selected_type);
                    dismiss();
                }
            }
        });
        int delivery_cost = newsFeedEntity.getDelivery_option();
        if(delivery_cost-5>=0){
            delivery_cost -=5;
            deliver = true;
        }
        if(delivery_cost-3>0){
            collect = true;
        }if(delivery_cost -1>=0){
            postage = true;
        }

        txv_deliver.setText("+£" + newsFeedEntity.getDelivery_cost());


    }
    void selectItem(int type){
        selected_type = -1;
        if(type==0){
            if(!postage){
                ((CommonActivity)getContext()).showAlertDialog("The option was disabled by the seller!");
                return;
            }
            imv_select.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ico_postage));
            txv_type.setText("Free postage");
            txv_description.setText(getContext().getResources().getString(R.string.str_postage) +" " +  newsFeedEntity.getPost_location());
            selected_type = 1;
        }else if(type ==1){
            if(!collect){
                ((CommonActivity)getContext()).showAlertDialog("The option was disabled by the seller!");
                return;
            }
            imv_select.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ico_collect));
            txv_type.setText("I'll Collect");
            txv_description.setText(getContext().getResources().getString(R.string.str_collect));
            selected_type = 3;
        }else if(type ==2){
            if(!deliver){
                ((CommonActivity)getContext()).showAlertDialog("The option was disabled by the seller!");
                return;
            }
            imv_select.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ico_deliver));
            txv_type.setText("Deliver");
            txv_description.setText(getContext().getResources().getString(R.string.str_postage) +" " +  newsFeedEntity.getPost_location());
            selected_type = 5;
        }
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_next);
        if(type ==3){
            animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_up);
        }
        animation.setDuration(1000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                if(type ==3){
                    lyt_option1.setVisibility(View.VISIBLE);
                    lyt_select.setVisibility(GONE);
                }else {
                    lyt_option1.setVisibility(GONE);
                    lyt_select.setVisibility(View.VISIBLE);
                }
            }
        });
        if(type==3){
            lyt_select.startAnimation(animation);
        }else {
            lyt_option1.startAnimation(animation);
        }

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
        void onConfirm(int type);
    }
}

