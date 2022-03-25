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
import com.atb.app.commons.Commons;
import com.atb.app.model.CommentModel;


public class CommentActionDialog extends DialogFragment {

    private OnConfirmListener listener;
    CommentModel commentModel = new CommentModel();
    String txt_title;
    String okay_text = "Ok";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.dialog_comment_action, container, false);
    }

    public CommentActionDialog setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
        return null;
    }
    public CommentActionDialog setOnConfirmListener(OnConfirmListener listener, String title, CommentModel commentModel) {

        this.listener = listener;
        this.txt_title = title;
        this.commentModel = commentModel;
        return null;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        TextView txv_reply = view.findViewById(R.id.txv_reply);
        TextView txv_report = view.findViewById(R.id.txv_report);
        TextView txv_copy = view.findViewById(R.id.txv_copy);
        TextView txv_hide = view.findViewById(R.id.txv_hide);
        TextView txv_delete = view.findViewById(R.id.txv_delete);

        TextView tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText(txt_title);
        if(commentModel.getCommenter_user_id() != Commons.g_user.getId()){
            txv_delete.setVisibility(View.GONE);
        }
        txv_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onReply();
                dismiss();
            }
        });
        txv_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onReport();
                dismiss();
            }
        });
        txv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCopy();
                dismiss();
            }
        });
        txv_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onHide();
                dismiss();
            }
        });
        txv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDelete();
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
        void onReply();
        void onReport();
        void onCopy();
        void onHide();
        void onDelete();
    }
}

