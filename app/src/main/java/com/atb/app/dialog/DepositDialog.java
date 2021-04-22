package com.atb.app.dialog;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.atb.app.model.NewsFeedEntity;
import com.bumptech.glide.Glide;

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
        txv_price.setText("£" + newsFeedEntity.getDeposit());
        txv_name.setText(newsFeedEntity.getTitle());
        String text1 = "You are about to book the service and it requires a\n" ;
        String text2 = "£"+newsFeedEntity.getDeposit() + " deposit";
        SpannableString ss = new SpannableString(text1+text2);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        ss.setSpan(boldSpan, text1.length(), text1.length()+newsFeedEntity.getDeposit().length()+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txv_description.setText(ss);

        txv_pay.setText("Pay £"+ newsFeedEntity.getDeposit() + " Deposit and Book");

        txv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPurchase();
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
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    public interface OnConfirmListener {
        void onPurchase();
    }
}

