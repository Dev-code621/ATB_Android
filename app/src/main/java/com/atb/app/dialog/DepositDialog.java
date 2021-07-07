package com.atb.app.dialog;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
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
import com.atb.app.activities.navigationItems.booking.BookFromPostActivity;
import com.atb.app.base.BaseActivity;
import com.atb.app.base.CommonActivity;
import com.atb.app.model.NewsFeedEntity;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;

public class DepositDialog extends DialogFragment {

    private OnConfirmListener listener;
    NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.deposit_dialog, container, false);
    }

    public ConfirmDialog setOnConfirmListener(OnConfirmListener listener, NewsFeedEntity newsFeedEntity) {
        this.listener = listener;
        this.newsFeedEntity = newsFeedEntity;
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView txv_price = view.findViewById(R.id.txv_price);
        TextView txv_name = view.findViewById(R.id.txv_name);
        TextView txv_description = view.findViewById(R.id.txv_description);
        TextView txv_pay = view.findViewById(R.id.txv_pay);
        LinearLayout lyt_select = view.findViewById(R.id.lyt_select);
        ImageView imv_selector = view.findViewById(R.id.imv_selector);
        txv_price.setText("£" + String.format("%.2f",Float.parseFloat(newsFeedEntity.getDeposit())));

        txv_name.setText(newsFeedEntity.getTitle());
        String text1 = "Please Tick box to confirm that you agree with " ;
        String text2 = newsFeedEntity.getUserModel().getBusinessModel().getBusiness_name();
        String text3 = " cancellation policy of ";
        String text4 = newsFeedEntity.getCancellations() +" days";
        String text5 = " and deposit payment of ";
        String text6= "£" + String.format("%.2f",Float.parseFloat(newsFeedEntity.getDeposit()));
        String  ss = text1 + "<b>" + text2 + "</b> " + text3 + "<b>" + text4 + "</b> "+ text5 + "<b>" + text6 + "</b> ";
        txv_description.setText(Html.fromHtml(ss));

        txv_pay.setText("Pay £"+ String.format("%.2f",Float.parseFloat(newsFeedEntity.getDeposit())) + " Deposit and Book");
        imv_selector.setEnabled(false);
        txv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imv_selector.isEnabled()) {
                    listener.onPurchase();
                    dismiss();
                }else {
                    ((BookFromPostActivity)(getContext())).showAlertDialog("You need to agree to the business Deposit & Cancellation policy.");
                }
            }
        });

        lyt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imv_selector.setEnabled(!imv_selector.isEnabled());
            }
        });
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
    }
}

