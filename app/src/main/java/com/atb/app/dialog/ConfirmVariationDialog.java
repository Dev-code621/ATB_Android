package com.atb.app.dialog;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.atb.app.R;

public class ConfirmVariationDialog extends DialogFragment {

    private ConfirmDialog.OnConfirmListener listener;
    TextView txt_email;
    TextView txv_create;
    int type =0;
    public ConfirmVariationDialog(int type) {
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.variation_dialog_confirm, container, false);
    }

    public ConfirmDialog setOnConfirmListener(ConfirmDialog.OnConfirmListener listener) {
        this.listener = listener;
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ImageView imv_close = view.findViewById(R.id.imv_close);
        ImageView imv_image = view.findViewById(R.id.imv_image);
        TextView txv_title = view.findViewById(R.id.txv_title);
        TextView txv_description = view.findViewById(R.id.txv_description);
        TextView txv_deposit = view.findViewById(R.id.txv_deposit);
        if(type == 1){
            imv_image.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.icon_deposit));
            txv_title.setText("Why a deposit?");
            txv_description.setText("The purpose of the deposit is to proect both you and the business. The deposit secures your booking with the business, in the event of the business cancelling the booking your deposit will be refunded. If you cancel the booking, the businesses has the right to keep the deposit paid or alternatively rebook you for another date. Please note that the deposit paid will be deducted from the cost of the service once completed (e.g. if the services costs £20 and you have paid a £5 deposit, then you will only need to pay £15 to the business");
        }else if(type ==2){
            txv_deposit.setVisibility(View.VISIBLE);
            imv_image.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.icon_cancel));

            txv_title.setText("Cancellations Within");
            txv_description.setText("Cancellations within is the period of time a purchaser of the service has to cancel the service booked for a full refund, if you choose to cancel after this time then the business is entitled to keep the deposit. After the cancellation period has lapsed, the business may refund you your deposit at their discretion");
        }else  if(type ==3){
            txv_deposit.setVisibility(View.VISIBLE);
            imv_image.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.hama));

            txv_title.setText("Confirm Your Bid");
            txv_description.setText("Once you confirm the bid this cannot be reversed. If your bid is successful and once the auction has completed, we will take the payment for the bid amount.");
            txv_deposit.setText("I Understand, Place Bid");
        }

        txv_deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onConfirm();
                dismiss();
            }
        });
        imv_close.setOnClickListener(new View.OnClickListener() {
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
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    public interface OnConfirmListener {
        void onConfirm();
    }
}

