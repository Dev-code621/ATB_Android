package com.atb.app.fragement;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.profile.SearchActivity;
import com.atb.app.base.CommonActivity;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

public class SearchFragment extends Fragment {
    Context context;
    View view;
    EditText edt_serach;
    TextView txv_atb_business,txv_atb_post;
    NiceSpinner spiner_category_type;
    int type =0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment3
        view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edt_serach = (EditText)view.findViewById(R.id.edt_serach);
        spiner_category_type = (NiceSpinner)view.findViewById(R.id.spiner_category_type);
        txv_atb_business = (TextView)view.findViewById(R.id.txv_atb_business);
        txv_atb_post = (TextView)view.findViewById(R.id.txv_atb_post);

        txv_atb_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txv_atb_business.setBackground(context.getResources().getDrawable(R.drawable.edit_rectangle_round));
                txv_atb_business.setTextColor(context.getResources().getColor(R.color.head_color));
                txv_atb_post.setBackground(context.getResources().getDrawable(R.drawable.edit_rectangle_round1));
                txv_atb_post.setTextColor(context.getResources().getColor(R.color.txt_color));
                type = 0;
            }
        });
        txv_atb_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txv_atb_post.setBackground(context.getResources().getDrawable(R.drawable.edit_rectangle_round));
                txv_atb_post.setTextColor(context.getResources().getColor(R.color.head_color));
                txv_atb_business.setBackground(context.getResources().getDrawable(R.drawable.edit_rectangle_round1));
                txv_atb_business.setTextColor(context.getResources().getColor(R.color.txt_color));
                type = 1;
            }
        });

        edt_serach.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    Bundle bundle = new Bundle();
                    bundle.putString("search", edt_serach.getText().toString());
                    bundle.putInt("type",type);
                    bundle.putString("category",spiner_category_type.getSelectedItem().toString());

                    ((CommonActivity)context).goTo(context, SearchActivity.class, false, bundle);
                    return true;

                }
                return false;
            }
        });
        Keyboard();
    }




    @Override
    public void onResume() {
        super.onResume();
        context =getActivity();
    }


    void Keyboard(){
        FrameLayout lytContainer = (FrameLayout) view.findViewById(R.id.lyt_container);
        lytContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_serach.getWindowToken(), 0);
                return false;
            }
        });


    }

}