package com.atb.app.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import com.atb.app.R;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.VariationModel;
import com.atb.app.model.submodel.InsuranceModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class AddVariationDialog extends DialogFragment {
    private OnConfirmListener listener;

    ArrayList<String>arrayList = new ArrayList<>();
    ArrayList<EditText>edt_option = new ArrayList<>();
    ArrayList<LinearLayout>lyt_option = new ArrayList<>();
    ArrayList<CardView>remove_option = new ArrayList<>();
    int optionNumber = 3;
    Context context;
    HashMap<String, ArrayList<String>>hashMap = new HashMap<>();
    int posstion =0;
    public AddVariationDialog( HashMap<String, ArrayList<String>> hashMap,int posstion) {
        this.hashMap.clear();
        this.hashMap = hashMap;
        this.posstion = posstion;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.add_variation_dialog, container, false);
    }

    public ConfirmDialog setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView imv_back = (ImageView) view.findViewById(R.id.imv_back);
        EditText edt_name = (EditText) view.findViewById(R.id.edt_name);
        CardView remove_name = (CardView) view.findViewById(R.id.remove_name);
        edt_option.add((EditText) view.findViewById(R.id.edt_option1));
        edt_option.add((EditText) view.findViewById(R.id.edt_option2));
        edt_option.add((EditText) view.findViewById(R.id.edt_option3));
        edt_option.add((EditText) view.findViewById(R.id.edt_option4));
        edt_option.add((EditText) view.findViewById(R.id.edt_option5));
        edt_option.add((EditText) view.findViewById(R.id.edt_option6));
        edt_option.add((EditText) view.findViewById(R.id.edt_option7));
        edt_option.add((EditText) view.findViewById(R.id.edt_option8));

        lyt_option.add((LinearLayout) view.findViewById(R.id.lyt_option1));
        lyt_option.add((LinearLayout) view.findViewById(R.id.lyt_option2));
        lyt_option.add((LinearLayout) view.findViewById(R.id.lyt_option3));
        lyt_option.add((LinearLayout) view.findViewById(R.id.lyt_option4));
        lyt_option.add((LinearLayout) view.findViewById(R.id.lyt_option5));
        lyt_option.add((LinearLayout) view.findViewById(R.id.lyt_option6));
        lyt_option.add((LinearLayout) view.findViewById(R.id.lyt_option7));
        lyt_option.add((LinearLayout) view.findViewById(R.id.lyt_option8));
        remove_option.add((CardView) view.findViewById(R.id.remove_option1));
        remove_option.add((CardView) view.findViewById(R.id.remove_option2));
        remove_option.add((CardView) view.findViewById(R.id.remove_option3));
        remove_option.add((CardView) view.findViewById(R.id.remove_option4));
        remove_option.add((CardView) view.findViewById(R.id.remove_option5));
        remove_option.add((CardView) view.findViewById(R.id.remove_option6));
        remove_option.add((CardView) view.findViewById(R.id.remove_option7));
        remove_option.add((CardView) view.findViewById(R.id.remove_option8));

        LinearLayout lyt_add_option = (LinearLayout)view.findViewById(R.id.lyt_add_option);
        TextView txv_addvariation = (TextView)view.findViewById(R.id.txv_addvariation);

        refreshOption();
        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        txv_addvariation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_name.getText().toString().length()==0){
                    ((CommonActivity)getContext()).showAlertDialog("Please enter the valid variation name.");
                    return;
                }
                if(hashMap.get(edt_name.getText().toString())!=null){
                    int index = 0;
                    for ( String key : hashMap.keySet() ) {
                        if( index != posstion && key.equals(edt_name.getText().toString())){
                            ((CommonActivity)getContext()).showAlertDialog("The Name is exist in variation, Please input another name.");
                            return;
                        }
                        index++;
                    }
                }
                boolean flag = false;
                for(int i =0;i<arrayList.size();i++){
                    if(arrayList.get(i).length()>0){
                        flag = true;
                    }
                    for(int j=i+1;j<8;j++){
                        if(arrayList.get(i).equals(arrayList.get(j)) && arrayList.get(i).length()>0){
                            ((CommonActivity)getContext()).showAlertDialog("Options should be unique.");
                            return;
                        }
                    }
                }
                if(!flag){
                    ((CommonActivity)getContext()).showAlertDialog("Please add variation options.");
                    return;
                }

                listener.onConfirm(edt_name.getText().toString(),arrayList);
                dismiss();
            }
        });



        remove_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        for(int i =0;i<8;i++){
            int finalI = i;
            remove_option.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeOption(finalI);
                }
            });

            edt_option.get(i).addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    arrayList.set(finalI,edt_option.get(finalI).getText().toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
        lyt_add_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(optionNumber==8)
                {
                    ((CommonActivity)getContext()).showAlertDialog("You can add up to 8 options");
                    return;
                }
                optionNumber++;
                refreshOption();
            }
        });

        if(posstion!=-1){
            int index = 0;
            for ( String key : hashMap.keySet() ) {
                if (index == posstion) {
                    edt_name.setText(key);
                    for(int i =0;i<hashMap.get(key).size();i++){
                        arrayList.set(i,hashMap.get(key).get(i));
                        edt_option.get(i).setText(hashMap.get(key).get(i));
                    }
                    optionNumber = hashMap.get(key).size();
                    refreshOption();
                }
                index++;
            }
        }


    }

    void removeOption(int possion){
        if(optionNumber==1)return;
        arrayList.remove(possion);
        arrayList.add("");
        for(int i =0;i<8;i++)edt_option.get(i).setText(arrayList.get(i));
        optionNumber--;
        refreshOption();

    }
    void refreshOption(){
        for(int i =0;i<8;i++){
            if(arrayList.size()<=i)
                arrayList.add(edt_option.get(i).getText().toString());
            if(i<optionNumber) {
                lyt_option.get(i).setVisibility(View.VISIBLE);
            }
            else
                lyt_option.get(i).setVisibility(View.GONE);
        }
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
        void onConfirm(String name,ArrayList<String>strings);
    }
}


