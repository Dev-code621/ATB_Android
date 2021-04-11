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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.atb.app.R;
import com.atb.app.base.CommonActivity;
import com.atb.app.model.NewsFeedEntity;
import com.atb.app.model.VariationModel;
import com.atb.app.model.submodel.AttributeModel;
import com.bumptech.glide.Glide;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.List;

public class ProductVariationSelectDialog extends DialogFragment {

    private OnConfirmListener listener;
    NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
    ArrayList<String> selected_Variation = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.variationselectdialog, container, false);
    }

    public ConfirmDialog setOnConfirmListener(OnConfirmListener listener, NewsFeedEntity newsFeedEntity, ArrayList<String> selected_Variation ) {
        this.listener = listener;
        this.newsFeedEntity = newsFeedEntity;
        this.selected_Variation = selected_Variation;
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView imv_image = view.findViewById(R.id.imv_image);
        TextView txv_name = view.findViewById(R.id.txv_name);
        TextView txv_description = view.findViewById(R.id.txv_description);
        TextView txv_post_item = view.findViewById(R.id.txv_post_item);
        ArrayList<LinearLayout>linearLayouts = new ArrayList<>();
        linearLayouts.add(view.findViewById(R.id.lyt_variation1));
        linearLayouts.add(view.findViewById(R.id.lyt_variation2));
        linearLayouts.add(view.findViewById(R.id.lyt_variation3));
        ArrayList<TextView>textViews = new ArrayList<>();
        textViews.add(view.findViewById(R.id.txv_variation1));
        textViews.add(view.findViewById(R.id.txv_variation2));
        textViews.add(view.findViewById(R.id.txv_variation3));
        ArrayList<NiceSpinner>niceSpinners = new ArrayList<>();
        niceSpinners.add(view.findViewById(R.id.spiner_variation1));
        niceSpinners.add(view.findViewById(R.id.spiner_variation2));
        niceSpinners.add(view.findViewById(R.id.spiner_variation3));
        TextView txv_buy = view.findViewById(R.id.txv_buy);
        Glide.with(getContext()).load(newsFeedEntity.getPostImageModels().get(0).getPath()).placeholder(R.drawable.profile_pic).into(imv_image);
        txv_name.setText(newsFeedEntity.getBrand());
        txv_description.setText(newsFeedEntity.getDescription());
        txv_description.setText(newsFeedEntity.getPost_item());
        for(int i =0;i<newsFeedEntity.getAttribute_map().size();i++){
            linearLayouts.get(i).setVisibility(View.VISIBLE);
            textViews.get(i).setText(newsFeedEntity.getAttribute_titles().get(i));
            ArrayList<AttributeModel> list = newsFeedEntity.getAttribute_map().get(newsFeedEntity.getAttribute_titles().get(i));
            List<String> stringList = new ArrayList<>();
            for(int j=0;j<list.size();j++){
                stringList.add(list.get(j).getVariant_attirbute_value());
            }
            niceSpinners.get(i).attachDataSource(stringList);
        }
        txv_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_Variation.clear();

                for(int i =0;i<newsFeedEntity.getAttribute_map().size();i++){
                    selected_Variation.add(niceSpinners.get(i).getSelectedItem().toString());
                }
                VariationModel variationModel = newsFeedEntity.productHasStock(selected_Variation);
                if (variationModel.getStock_level() == 0) {
                    ((CommonActivity)getContext()).showAlertDialog("The product is out of stock!");
                    return;
                }
                listener.onPurchase(selected_Variation);
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
        void onPurchase(  ArrayList<String> selected_Variation);
    }
}

