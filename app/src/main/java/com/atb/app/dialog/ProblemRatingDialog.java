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

import java.util.ArrayList;


public class ProblemRatingDialog extends DialogFragment {

    private OnConfirmListener listener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.dialog_rating_problem, container, false);
    }

    public ProblemRatingDialog setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
        return null;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ArrayList<TextView>textViews = new ArrayList<>();
        textViews.add(view.findViewById(R.id.txv_problem1));
        textViews.add(view.findViewById(R.id.txv_problem2));
        textViews.add(view.findViewById(R.id.txv_problem3));
        textViews.add(view.findViewById(R.id.txv_problem4));

        for(int i =0;i<4;i++){
            int finalI = i;
            textViews.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onConfirm(textViews.get(finalI).getText().toString());
                    dismiss();
                }
            });
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
        void onConfirm(String problem);
    }
}

