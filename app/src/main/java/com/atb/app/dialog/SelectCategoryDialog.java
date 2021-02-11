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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atb.app.R;
import com.atb.app.activities.MainActivity;
import com.atb.app.adapter.CategoryAdapter;
import com.atb.app.adapter.EmailAdapter;
import com.atb.app.adapter.SelectCategoryAdapter;

import org.w3c.dom.Text;

public class SelectCategoryDialog  extends DialogFragment {
    String str_camera = "Camera",str_album = "Album";
    MainActivity mainActivity;

    public SelectCategoryDialog(MainActivity mainActivity) {

        this.mainActivity = mainActivity;
    }

    public void SelectCategoryDialog(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.dialog_select_category, container, false);
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
        TextView txv_myatb = (TextView) view.findViewById(R.id.txv_myatb);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView_categories);
        recyclerView.setLayoutManager(new GridLayoutManager(mainActivity,2));
        SelectCategoryAdapter selectCategoryAdapter = new SelectCategoryAdapter(mainActivity);
        recyclerView.setAdapter(selectCategoryAdapter);

        txv_myatb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.selectItem(-1);
            }
        });

    }

    public void setOnActionClick(OnActionListener onActionListener) {
    }


    public interface OnActionListener {
        void OnSelect();

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
