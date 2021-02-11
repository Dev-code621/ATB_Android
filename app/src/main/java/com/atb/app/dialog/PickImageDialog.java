package com.atb.app.dialog;


import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atb.app.R;
import com.atb.app.adapter.ImageAdapter;

import java.util.ArrayList;

public class PickImageDialog extends DialogFragment {
    private OnImagePickListener listener;
    private RecyclerView recyclerView_images;
    private ArrayList<String> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return inflater.inflate(R.layout.dialog_choose_image, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.fullScreenDialog);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //RecyclerView
        recyclerView_images=(RecyclerView)view.findViewById(R.id.recyclerView_images);
        recyclerView_images.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerView_images.setAdapter(new ImageAdapter(getActivity(),list, new ImageAdapter.OnImageSelectListener() {
            @Override
            public void onImageSelect(String path) {
                dismiss();
                listener.OnImagePick(path);
            }
        }));


        ImageView iv_back=(ImageView)view.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }
    public void setImagePickListener(ArrayList<String> list, OnImagePickListener listener){
        this.listener=listener;
        this.list=list;

    }

    public interface OnImagePickListener{
        void OnImagePick(String path);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}

