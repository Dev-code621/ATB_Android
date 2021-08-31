package com.atb.app.dialog;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.atb.app.R;
import com.atb.app.base.CommonActivity;
import com.atb.app.model.NewsFeedEntity;
import com.bumptech.glide.Glide;

public class TermsSuccessDialog extends DialogFragment {

    private OnConfirmListener listener;
    NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.terms_select_dialog, container, false);
    }

    public ConfirmDialog setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
        this.newsFeedEntity = newsFeedEntity;
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView txv_terms = view.findViewById(R.id.txv_terms);
        LinearLayout lyt_check = view.findViewById(R.id.lyt_check);
        CheckBox checkbox = view.findViewById(R.id.checkbox);
        LinearLayout lyt_paypal = view.findViewById(R.id.lyt_paypal);
        TextView txv_cancel = view.findViewById(R.id.txv_cancel);
        txv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
                listener.onCancel();
            }
        });

        lyt_paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkbox.isChecked()) {
                    //dismiss();
                    listener.onPurchase();
                }
                else
                    ((CommonActivity)getContext()).showAlertDialog("Please check terms and condition!");
            }
        });

        txv_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://test.myatb.co.uk/landing"));
                startActivity(browserIntent);
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
        void onCancel();
    }
}

