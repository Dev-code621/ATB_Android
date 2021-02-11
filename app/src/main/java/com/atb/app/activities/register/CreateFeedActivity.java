package com.atb.app.activities.register;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.MainActivity;
import com.atb.app.adapter.CategoryAdapter;
import com.atb.app.base.CommonActivity;

import java.util.ArrayList;

public class CreateFeedActivity extends CommonActivity {

    TextView txv_next;
    ImageView imv_selector;
    LinearLayout lyt_selectall;
    RecyclerView recycleview_categoires;
    ArrayList<Boolean>categories  = new ArrayList<>();
    CategoryAdapter categoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_feed);

        txv_next = findViewById(R.id.txv_next);
        imv_selector = findViewById(R.id.imv_selector);
        lyt_selectall = findViewById(R.id.lyt_selectall);
        recycleview_categoires = findViewById(R.id.recyclerView_categories);
        for(int i=0;i<10;i++)categories.add(false);
        categoryAdapter = new CategoryAdapter(this);
        categoryAdapter.setHasStableIds(true);
        recycleview_categoires.setLayoutManager(new GridLayoutManager(this,2));
        recycleview_categoires.setAdapter(categoryAdapter);
        categoryAdapter.setData(categories);
        lyt_selectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<10;i++)categories.set(i,imv_selector.isEnabled());
                imv_selector.setEnabled(!imv_selector.isEnabled());
                categoryAdapter.setData(categories);
            }
        });
        txv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTo(CreateFeedActivity.this, MainActivity.class,true);
            }
        });


    }

    public void selectItem(int posstion){
        categories.set(posstion,!categories.get(posstion));
        Log.d("aaaa",String.valueOf(categories.toString()));
        //recycleview_categoires.setAdapter(categoryAdapter);
        categoryAdapter.setData(categories);

    }
}