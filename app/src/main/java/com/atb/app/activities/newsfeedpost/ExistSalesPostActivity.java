package com.atb.app.activities.newsfeedpost;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.LoginActivity;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.register.Signup1Activity;
import com.atb.app.adapter.PostFeedAdapter;
import com.atb.app.base.CommonActivity;
import com.atb.app.dialog.SelectProfileDialog;
import com.atb.app.model.NewsFeedEntity;

import java.util.ArrayList;

public class ExistSalesPostActivity extends CommonActivity implements View.OnClickListener {
    int type = 0;
    LinearLayout lyt_back,lyt_selectall;
    TextView txv_title,txv_product_description,txv_post;
    FrameLayout lyt_profile;
    ListView list_content;
    ImageView imv_calender,imv_selector;
    PostFeedAdapter postFeedAdapter;

    ArrayList<NewsFeedEntity>newsFeedEntities = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exist_sales_post);


        lyt_back = findViewById(R.id.lyt_back);
        lyt_selectall = findViewById(R.id.lyt_selectall);
        txv_title = findViewById(R.id.txv_title);
        txv_product_description = findViewById(R.id.txv_product_description);
        txv_post = findViewById(R.id.txv_post);
        lyt_profile = findViewById(R.id.lyt_profile);
        list_content = findViewById(R.id.list_content);
        imv_calender = findViewById(R.id.imv_calender);
        imv_selector = findViewById(R.id.imv_selector);
        imv_selector.setEnabled(false);


        lyt_back.setOnClickListener(this);
        lyt_selectall.setOnClickListener(this);
        lyt_profile.setOnClickListener(this);
        txv_post.setOnClickListener(this);
        imv_calender.setOnClickListener(this);
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                type= bundle.getInt("type");
                if(type == 2){
                    txv_title.setText(getResources().getString(R.string.post_a_service));
                    txv_product_description.setText(getResources().getString(R.string.all_service));
                    imv_calender.setVisibility(View.VISIBLE);
                }
            }
        }
        postFeedAdapter = new PostFeedAdapter(this);
        list_content.setAdapter(postFeedAdapter);

        for(int i =0;i<10;i++){
            NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
            newsFeedEntity.setId(i);
            newsFeedEntities.add(newsFeedEntity);
        }
        postFeedAdapter.setData(newsFeedEntities);
        list_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                newsFeedEntities.get(position).setSelect(!newsFeedEntities.get(position).isSelect());
                postFeedAdapter.setData(newsFeedEntities);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lyt_back:
                finish(this);
                break;
            case R.id.lyt_selectall:
                imv_selector.setEnabled(!imv_selector.isEnabled());
                for(int i =0;i<newsFeedEntities.size();i++){
                    newsFeedEntities.get(i).setSelect(imv_selector.isEnabled());
                    postFeedAdapter.setData(newsFeedEntities);
                }
                break;
            case R.id.lyt_profile:
                SelectprofileDialog(this);


                break;
            case R.id.txv_post:
                finish(this);
                break;
            case R.id.imv_calender:

                break;
        }
    }
}