package com.atb.app.dialog;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.atb.app.R;
import com.atb.app.model.BookingEntity;

public class RequestPaypalDialog extends DialogFragment {

    private ConfirmDialog.OnConfirmListener listener;
    TextView txv_booking_price,txv_deposit,txv_pending_funds;
    LinearLayout lyt_request_payment;
    BookingEntity bookingEntity = new BookingEntity();
    int type;
    public RequestPaypalDialog(BookingEntity bookingEntity,int type) {
        this.bookingEntity = bookingEntity;
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.request_paypal_dialog, container, false);
    }

    public ConfirmDialog setOnConfirmListener(ConfirmDialog.OnConfirmListener listener) {
        this.listener = listener;
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        txv_booking_price = view.findViewById(R.id.txv_booking_price);
        txv_deposit = view.findViewById(R.id.txv_deposit);
        txv_pending_funds = view.findViewById(R.id.txv_pending_funds);
        lyt_request_payment = view.findViewById(R.id.lyt_request_payment);
        TextView txv_title = view.findViewById(R.id.txv_title);
        TextView txv_description = view.findViewById(R.id.txv_description);
        TextView txv_button = view.findViewById(R.id.txv_button);
        if(type==0){

        }else {
            txv_title.setText("Pay now with your\nPaypal account");
            txv_description.setText("You can pay now the complete the bill from your phone. You can also pay by cash the day of the service");
            txv_button.setText("Pay £" + String.valueOf(Double.parseDouble(bookingEntity.getNewsFeedEntity().getPrice()) - bookingEntity.getPaid_amount()));
        }

        txv_booking_price.setText("£" + bookingEntity.getNewsFeedEntity().getPrice());
        txv_deposit.setText("£" + bookingEntity.getPaid_amount());
        txv_pending_funds.setText("£" + String.valueOf(Double.parseDouble(bookingEntity.getNewsFeedEntity().getPrice()) - bookingEntity.getPaid_amount()));

        lyt_request_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onConfirm();
                dismiss();

//                Toast.makeText(getActivity(), "Repost Successfully", Toast.LENGTH_SHORT).show();
            }
        });

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
        void onConfirm();
    }
}

