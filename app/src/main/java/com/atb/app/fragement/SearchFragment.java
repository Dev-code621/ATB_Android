package com.atb.app.fragement;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.atb.app.R;
import com.atb.app.activities.MainActivity;
import com.atb.app.adapter.SelectCategoryAdapter;
import com.atb.app.adapter.SelectCategorySearchAdapter;
import com.atb.app.model.NewsFeedEntity;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    MainActivity mainActivity;
    View view;
    EditText edt_serach;
    RecyclerView recyclerView_categories;
    ImageView imv_selector;
    LinearLayout lyt_search;
    ArrayList<Boolean>booleans = new ArrayList<>();
    SelectCategorySearchAdapter selectCategoryAdapter;
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
        recyclerView_categories = (RecyclerView) view.findViewById(R.id.recyclerView_categories);
        recyclerView_categories.setLayoutManager(new GridLayoutManager(mainActivity,2));
        for(int i =0;i<11;i++) booleans.add(false);
        selectCategoryAdapter = new SelectCategorySearchAdapter(mainActivity,this,booleans);
        selectCategoryAdapter.setHasStableIds(true);
        recyclerView_categories.setAdapter(selectCategoryAdapter);




        imv_selector = (ImageView) view.findViewById(R.id.imv_selector);
        lyt_search = (LinearLayout)view.findViewById(R.id.lyt_search);

        lyt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!imv_selector.isEnabled()){
                    recyclerView_categories.setVisibility(View.GONE);
                }
                else {
                    recyclerView_categories.setVisibility(View.VISIBLE);
                }
                imv_selector.setEnabled(!imv_selector.isEnabled());
            }
        });

        edt_serach.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                   mainActivity.setColor(4);
                    return true;
                }
                return false;
            }
        });


        Keyboard();
    }

    public void selectCategory(int posstion){
        booleans.clear();
        for(int i =0;i<11;i++) booleans.add(false);
        booleans.set(posstion,true);
        selectCategoryAdapter.setData(booleans);

    }



    @Override
    public void onResume() {
        super.onResume();
        mainActivity =(MainActivity)getActivity();
    }


    void Keyboard(){
        FrameLayout lytContainer = (FrameLayout) view.findViewById(R.id.lyt_container);
        lytContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_serach.getWindowToken(), 0);
                return false;
            }
        });


    }

}