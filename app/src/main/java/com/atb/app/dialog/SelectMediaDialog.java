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


public class SelectMediaDialog extends DialogFragment {
    private OnActionListener listener;
    String str_camera = "Camera",str_album = "Album",title = "Complete Action Using";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.dialog_select_media, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        TextView tv_titme = view.findViewById(R.id.tv_title);
        TextView tv_camera = view.findViewById(R.id.tv_camera);
        TextView tv_album = view.findViewById(R.id.tv_album);
        tv_camera.setText(str_camera);
        tv_album.setText(str_album);
        tv_titme.setText(title);

        tv_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                listener.OnAlbum();
            }
        });
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                listener.OnCamera();
            }
        });
    }

    public void setOnActionClick(OnActionListener listener) {

        this.listener = listener;
    }
    public void setOnActionClick(OnActionListener listener,String title, String str1, String str2) {
        this.title = title;
        str_camera = str1;
        str_album = str2;
        this.listener = listener;
    }

    public interface OnActionListener {
        void OnCamera();

        void OnAlbum();
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

}
