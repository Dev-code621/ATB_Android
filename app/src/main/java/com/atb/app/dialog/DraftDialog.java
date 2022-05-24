package com.atb.app.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.atb.app.R;


public class DraftDialog extends DialogFragment {

    private OnConfirmListener listener;
    TextView tv_save;
    TextView txv_discard;
    TextView tv_cancel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.draft_dialog_confirm, container, false);
    }

    public DraftDialog setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
        return null;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        tv_save = view.findViewById(R.id.tv_save);
        tv_cancel = view.findViewById(R.id.tv_cancel);
        txv_discard = view.findViewById(R.id.txv_discard);

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onConfirm();
                dismiss();

//                Toast.makeText(getActivity(), "Repost Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        txv_discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.ondisCard();
                dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    public interface OnConfirmListener {
        void onConfirm();
        void ondisCard();
    }
}

