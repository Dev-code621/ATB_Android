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

public class PaidBookingConfirmDialog extends DialogFragment {

    private OnConfirmListener listener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.paid_booking_dialog, container, false);
    }

    public ConfirmDialog setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LinearLayout lyt_rate =view.findViewById(R.id.lyt_rate);
        LinearLayout lyt_back =view.findViewById(R.id.lyt_back);

        lyt_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onRate();
                dismiss();

//                Toast.makeText(getActivity(), "Repost Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        lyt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.goBack();
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
        void onRate();
        void goBack();
    }
}

