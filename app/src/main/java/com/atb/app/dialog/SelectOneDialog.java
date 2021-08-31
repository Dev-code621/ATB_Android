package com.atb.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atb.app.R;
import com.atb.app.activities.MainActivity;
import com.atb.app.adapter.SelectCategoryAdapter;
import com.atb.app.adapter.SelectOneAdapter;

import java.util.ArrayList;

public class SelectOneDialog extends DialogFragment {
    ArrayList<String>arrayList = new ArrayList<>();
    OnActionListener listener;
    String title ;
    public SelectOneDialog(Context context) {
    }

    public void SelectCategoryDialog(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.select_one_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
       RecyclerView recyclerview = view.findViewById(R.id.recyclerview);
        LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(linearLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        SelectOneAdapter selectJobAdapter = new SelectOneAdapter(arrayList, getActivity(), new SelectOneAdapter.OnSelectListener() {
            @Override
            public void onSelect(int selectPosstion) {
                dismiss();
                listener.OnSelect(selectPosstion);
            }
        });
        recyclerview.setAdapter(selectJobAdapter);
        TextView txt_title = view.findViewById(R.id.txt_title);
        txt_title.setText(title);
    }

    public void setOnActionClick(OnActionListener onActionListener , ArrayList<String> arrayList ,String title) {
        this.arrayList = arrayList;
        listener = onActionListener;
        this.title = title;
    }


    public interface OnActionListener {
        void OnSelect(int posstion);

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

}
