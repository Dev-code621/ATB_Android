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


public class GenralConfirmDialog extends DialogFragment {

    private OnConfirmListener listener;
    TextView tv_delete;
    TextView tv_cancel;
    TextView tv_title;
    TextView tv_content;
    String txt_title,content, txv1,txv2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.general_dialog_confirm, container, false);
    }

    public GenralConfirmDialog setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
        return null;
    }
    public GenralConfirmDialog setOnConfirmListener(OnConfirmListener listener, String title,String content, String txv1,String txv2) {
        this.listener = listener;
        this.txt_title = title;
        this.content = content;
        this.txv1 = txv1;
        this.txv2 = txv2;
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        tv_delete = view.findViewById(R.id.tv_delete);
        tv_cancel = view.findViewById(R.id.tv_cancel);
        tv_title = view.findViewById(R.id.tv_title);
        tv_content = view.findViewById(R.id.tv_content);
        tv_title.setText(txt_title);
        tv_content.setText(content);
        tv_delete.setText(txv1);
        tv_cancel.setText(txv2);
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.onConfirm();
                dismiss();

//                Toast.makeText(getActivity(), "Repost Successfully", Toast.LENGTH_SHORT).show();
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
    }
}

