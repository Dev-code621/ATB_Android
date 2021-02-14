package com.atb.app.activities.newpost;

import androidx.transition.Scene;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.newsfeedpost.ExistSalesPostActivity;
import com.atb.app.activities.newsfeedpost.NewAdviceActivity;
import com.atb.app.activities.newsfeedpost.NewPollVotingActivity;
import com.atb.app.activities.newsfeedpost.NewSalePostActivity;
import com.atb.app.activities.newsfeedpost.NewServiceOfferActivity;
import com.atb.app.base.CommonActivity;

public class SelectPostCategoryActivity extends CommonActivity implements View.OnClickListener {
    ViewGroup sceneRoot;
    Scene aScene,anotherScene;
    ImageView imv_close;
    LinearLayout lyt_advice,lyt_sale_post,lyt_serviceoffer,lyt_pool;
    int  type = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_post_category);
        sceneRoot = (ViewGroup) findViewById(R.id.scene_root);
        aScene = Scene.getSceneForLayout(sceneRoot, R.layout.post_selectitem_layout, this);
        anotherScene = Scene.getSceneForLayout(sceneRoot, R.layout.activity_select_post_type, this);
        activityAnimation(aScene,R.id.lyt_container);
        loadLayout();

    }

    void loadLayout(){

        imv_close = sceneRoot.findViewById(R.id.imv_close);
        lyt_advice = sceneRoot.findViewById(R.id.lyt_advice);
        lyt_sale_post = sceneRoot.findViewById(R.id.lyt_sale_post);
        lyt_serviceoffer = sceneRoot.findViewById(R.id.lyt_serviceoffer);
        lyt_pool = sceneRoot.findViewById(R.id.lyt_pool);

        imv_close.setOnClickListener(this);
        lyt_advice.setOnClickListener(this);
        lyt_sale_post.setOnClickListener(this);
        lyt_serviceoffer.setOnClickListener(this);
        lyt_pool.setOnClickListener(this);
    }
    void loadnewLayout(){
        TextView txv_cancel = sceneRoot.findViewById(R.id.txv_cancel);
        TextView txv_repost = sceneRoot.findViewById(R.id.txv_repost);
        TextView txv_new = sceneRoot.findViewById(R.id.txv_new);
        RelativeLayout lyt_newpost = sceneRoot.findViewById(R.id.lyt_newpost);
        RelativeLayout lyt_repost = sceneRoot.findViewById(R.id.lyt_repost);
        lyt_newpost.setOnClickListener(this);
        txv_cancel.setOnClickListener(this);
        lyt_repost.setOnClickListener(this);

        if(type ==1){
            txv_new.setText(getResources().getString(R.string.newproduct));
            txv_repost.setText(getResources().getString(R.string.exist_product));
        }else {
            txv_new.setText(getResources().getString(R.string.new_service));
            txv_repost.setText(getResources().getString(R.string.exist_service));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_close:
                finish(this);
                break;
            case R.id.lyt_advice:
                goTo(this, NewAdviceActivity.class,false);
                break;
            case R.id.lyt_sale_post:
                activityAnimation(anotherScene,R.id.lyt_container);
                type = 1;
                loadnewLayout();
                break;
            case R.id.lyt_serviceoffer:
                activityAnimation(anotherScene,R.id.lyt_container);
                type = 2;
                loadnewLayout();
                break;
            case R.id.lyt_pool:
                goTo(this, NewPollVotingActivity.class,false);
                break;
            case R.id.txv_cancel:
                activityAnimation(aScene,R.id.lyt_container);
                type = 0;
                loadLayout();
                break;
            case R.id.lyt_newpost:
                if(type ==1 )
                      goTo(this, NewSalePostActivity.class,false);
                else
                    goTo(this, NewServiceOfferActivity.class,false);
                break;
            case R.id.lyt_repost:
                Bundle bundle = new Bundle();
                bundle.putInt("type",type);
                goTo(this, ExistSalesPostActivity.class,false,bundle);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(type>0){
            activityAnimation(aScene,R.id.lyt_container);
            type = 0;
            loadLayout();
        }else finish(this);
    }
}