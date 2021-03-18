package com.atb.app.dialog;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.atb.app.R;
import com.atb.app.activities.newsfeedpost.ExistSalesPostActivity;
import com.atb.app.base.CommonActivity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AddInsuranceDialog extends DialogFragment {
    private OnConfirmListener listener;
    int type;
    long date;
    public AddInsuranceDialog(int type) {
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.add_insurance_dialog, container, false);
    }

    public ConfirmDialog setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
        return null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView txv_title = (TextView) view.findViewById(R.id.txv_title);
        TextView txv_time = (TextView) view.findViewById(R.id.txv_time);
        ImageView imv_back = (ImageView) view.findViewById(R.id.imv_back);
        EditText edt_company = (EditText) view.findViewById(R.id.edt_company);
        EditText edt_number = (EditText) view.findViewById(R.id.edt_number);
        LinearLayout lyt_add_file = (LinearLayout) view.findViewById(R.id.lyt_add_file);
        LinearLayout lyt_add_insurance = (LinearLayout)view.findViewById(R.id.lyt_add_insurance);
        TextView txv_imagename = (TextView) view.findViewById(R.id.txv_imagename);
        TextView txv_addtitle = (TextView) view.findViewById(R.id.txv_addtitle);

        if(type ==0){
            txv_title.setText("Add An Insurance");
            txv_addtitle.setText("Add An Insurance");
            edt_company.setHint("Insurance Company");
            edt_number.setHint("Insurance Number");
            txv_time.setHint("Insurance Expiry");
        }else if(type == 1){
            txv_title.setText("Add An Certification");
            txv_addtitle.setText("Add An Certification");
            edt_company.setHint("Qualified Service Name");
            edt_number.setHint("Certification Number");
            txv_time.setHint("Qualified Since");
        }
        imv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        lyt_add_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFileSelect();
            }
        });
        lyt_add_insurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onConfirm(edt_company.getText().toString(),edt_number.getText().toString(),date);
                dismiss();
            }
        });

        txv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar now = Calendar.getInstance();
                                now.set(year,monthOfYear,dayOfMonth);
                                SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
                                txv_time.setText(formatter.format(now.getTimeInMillis()));
                            }
                        },
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );
// If you're calling this from a support Fragment
                dpd.show(getActivity().getSupportFragmentManager(), "Datepickerdialog");
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
        void onConfirm(String comapny,String number,long date);
        void onFileSelect();
    }
}


