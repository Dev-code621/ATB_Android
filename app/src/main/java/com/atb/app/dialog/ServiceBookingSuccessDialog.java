package com.atb.app.dialog;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.atb.app.R;
import com.atb.app.model.NewsFeedEntity;
import com.bumptech.glide.Glide;

public class ServiceBookingSuccessDialog extends DialogFragment {

    private OnConfirmListener listener;
    NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.service_book_success_dialog, container, false);
    }

    public ConfirmDialog setOnConfirmListener(OnConfirmListener listener, NewsFeedEntity newsFeedEntity) {
        this.listener = listener;
        this.newsFeedEntity = newsFeedEntity;
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        LinearLayout lyt_mypurchase = view.findViewById(R.id.lyt_mypurchase);
        LinearLayout lyt_keep_buying = view.findViewById(R.id.lyt_keep_buying);


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
                listener.returnMyATB();
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
        void returnMyATB();
    }
}

