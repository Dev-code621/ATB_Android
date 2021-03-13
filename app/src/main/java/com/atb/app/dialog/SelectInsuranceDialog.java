package com.atb.app.dialog;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.atb.app.R;
import com.atb.app.adapter.SelectInsuranceAdapter;
import com.atb.app.model.submodel.InsuranceModel;

import java.util.ArrayList;


public class SelectInsuranceDialog extends DialogFragment {
    private OnActionListener listener;
    ArrayList<InsuranceModel>insuranceModels = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.dialog_insurance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ListView listView = (ListView) view.findViewById(R.id.listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();
                listener.OnSelect(position);
            }
        });
        SelectInsuranceAdapter selectInsuranceAdapter = new SelectInsuranceAdapter(getContext());
        listView.setAdapter(selectInsuranceAdapter);
        selectInsuranceAdapter.setRoomData(insuranceModels);
    }

    public void setOnActionClick(OnActionListener listener) {

        this.listener = listener;
    }
    public void setOnActionClick(OnActionListener listener, ArrayList<InsuranceModel>insuranceModels) {
        this.insuranceModels = insuranceModels;
        this.listener = listener;
    }

    public interface OnActionListener {
        void OnSelect( int posstion);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}
