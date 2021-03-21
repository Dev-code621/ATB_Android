package com.atb.app.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atb.app.R;
import com.atb.app.adapter.ImageAdapter;
import com.atb.app.commons.Commons;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class SelectProfileDialog extends DialogFragment {
    private OnSelectListener listener;
    private RecyclerView recyclerView_images;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.dialog_profile_select, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //RecyclerView
        RelativeLayout lyt_business=(RelativeLayout)view.findViewById(R.id.lyt_business);
        RelativeLayout lyt_user=(RelativeLayout)view.findViewById(R.id.lyt_user);

        ImageView imv_business_profile=(ImageView)view.findViewById(R.id.imv_business_profile);
        TextView txv_businessname = (TextView)view.findViewById(R.id.txv_businessname);
        ImageView imv_profile=(ImageView)view.findViewById(R.id.imv_profile);
        TextView txv_name = (TextView)view.findViewById(R.id.txv_name);
        ImageView imv_check2=(ImageView)view.findViewById(R.id.imv_check2);
        ImageView imv_check1=(ImageView)view.findViewById(R.id.imv_check1);
        Glide.with(this).load(Commons.g_user.getImvUrl()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                new RoundedCornersTransformation(getContext(), Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
        txv_name.setText(Commons.g_user.getUserName());
        Glide.with(this).load(Commons.g_user.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                new RoundedCornersTransformation(getContext(), Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_business_profile);
        txv_businessname.setText(Commons.g_user.getBusinessModel().getBusiness_name());
        lyt_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnSelectProfile(true);
                imv_check1.setVisibility(View.VISIBLE);
                dismiss();
            }
        });

        lyt_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnSelectProfile(false);
                imv_check2.setVisibility(View.VISIBLE);
                dismiss();
            }
        });

    }
    public SelectProfileDialog OnSelectListener( OnSelectListener listener){
        this.listener=listener;
        return null;

    }

    public interface OnSelectListener{
        void OnSelectProfile(boolean flag );
    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}

