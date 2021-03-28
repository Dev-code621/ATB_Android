package com.atb.app.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atb.app.R;
import com.atb.app.activities.navigationItems.BoostActivity;
import com.atb.app.activities.navigationItems.NotificationActivity;
import com.atb.app.activities.newpost.SelectPostCategoryActivity;
import com.atb.app.activities.profile.OtherUserProfileActivity;
import com.atb.app.activities.profile.ProfileBusinessNaviagationActivity;
import com.atb.app.activities.profile.ProfileUserNavigationActivity;
import com.atb.app.adapter.BoostItemAdapter;
import com.atb.app.adapter.EmailAdapter;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.commons.Constants;
import com.atb.app.dialog.ConfirmDialog;
import com.atb.app.dialog.SelectCategoryDialog;
import com.atb.app.fragement.ChatFragment;
import com.atb.app.fragement.MainListFragment;
import com.atb.app.fragement.SearchFragment;
import com.atb.app.model.BoostModel;
import com.atb.app.model.UserModel;
import com.atb.app.util.RoundedCornersTransformation;
import com.atb.app.view.NonSwipeableViewPager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener;
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;

import java.util.ArrayList;


public class MainActivity extends CommonActivity implements View.OnClickListener {
    ImageView imv_search ,imv_profile,imv_feed,imv_post,imv_chat;
    FragmentTransaction ft;
    boolean main_flag = false;
    SelectCategoryDialog selectCategoryDialog;
    TextView txv_category;
    EditText edt_serach;
    LinearLayout lyt_title,lyt_profile;
    MainListFragment mainListFragment;
    int selectIcon= 0 ;
    TextView txv_username;
    FrameLayout frame_noti,frame_chat;
    CardView card_unread_noti,card_unread_chat;
    RecyclerView recycler_view_boost;
    ArrayList<BoostModel>boostModels = new ArrayList<>();
    BoostItemAdapter boostAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imv_search = findViewById(R.id.imv_search);
        imv_profile = findViewById(R.id.imv_profile);
        imv_feed = findViewById(R.id.imv_feed);
        imv_post = findViewById(R.id.imv_post);
        imv_chat = findViewById(R.id.imv_chat);
        txv_category = findViewById(R.id.txv_category);
        lyt_title = findViewById(R.id.lyt_title);
        edt_serach = findViewById(R.id.edt_serach);
        txv_username = findViewById(R.id.txv_username);
        lyt_profile = findViewById(R.id.lyt_profile);
        frame_noti = findViewById(R.id.frame_noti);
        frame_chat = findViewById(R.id.frame_chat);
        card_unread_noti = findViewById(R.id.card_unread_noti);
        card_unread_chat = findViewById(R.id.card_unread_chat);
        recycler_view_boost = findViewById(R.id.recycler_view_boost);

        imv_search.setOnClickListener(this);
        lyt_profile.setOnClickListener(this);
        imv_feed.setOnClickListener(this);
        imv_post.setOnClickListener(this);
        mainListFragment = new MainListFragment();
        frame_chat.setOnClickListener(this);
        frame_noti.setOnClickListener(this);
        Commons.mVideoPlayerManager = new SingleVideoPlayerManager(new PlayerItemChangeListener() {
            @Override
            public void onPlayerItemChanged(MetaData metaData) {

            }
        });

        recycler_view_boost.setLayoutManager( new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        boostAdapter = new BoostItemAdapter(this,  new BoostItemAdapter.OnSelectListener() {
            @Override
            public void onSelectItem(int posstion) {
                if(posstion ==0)
                    goTo(MainActivity.this, BoostActivity.class,false);
                else
                    getuserProfile(49,1);
            }

        });
       // boostAdapter.setHasStableIds(true);
        recycler_view_boost.setItemAnimator(null);
        recycler_view_boost.setAdapter(boostAdapter);
        for(int i=0;i<10;i++){
            BoostModel boostModel = new BoostModel();
            boostModel.setName("test name" + String.valueOf(i));
            boostModel.setImv_pic("https://atb-test-files.s3.eu-west-2.amazonaws.com/690825476604ba863511047.68183948profileimg.jpg");
            boostModels.add(boostModel);
        }
        boostAdapter.setRoomData(boostModels);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_search:
                setColor(1);
                break;
            case R.id.lyt_profile:
                if(Commons.g_user.getAccount_type()==1)
                    startActivityForResult(new Intent(this, ProfileBusinessNaviagationActivity.class),1);
                else
                    goTo(this, ProfileUserNavigationActivity.class,false);
                break;
            case R.id.imv_post:
                setColor(2);
                startActivityForResult(new Intent(this, SelectPostCategoryActivity.class),1);
                overridePendingTransition(0, 0);
                break;
            case R.id.imv_feed:
                if(main_flag)selectCategory();
                setColor(0);
                selectIcon = 0;
                break;
            case R.id.frame_chat:

                break;
            case R.id.frame_noti:
                goTo(this, NotificationActivity.class,false);
                break;
        }
    }


    void selectCategory(){

        selectCategoryDialog = new SelectCategoryDialog(this);
        selectCategoryDialog.show(getSupportFragmentManager(), "action picker");
    }
    public void selectItem(int posstion){
        selectCategoryDialog.dismiss();
        if(posstion<0){
            txv_category.setText(getResources().getString(R.string.my_atb));
            Commons.main_category = getResources().getString(R.string.my_atb);
        }else {
            txv_category.setText(Constants.category_word[posstion]);
            Commons.main_category = Constants.category_word[posstion];
        }
        mainListFragment.getList();
    }
    @SuppressLint("ResourceAsColor")
    public void setColor(int id){
        if(Commons.g_user.getAccount_type() ==1 ){
            Glide.with(this).load(Commons.g_user.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
        }else {
            Glide.with(this).load(Commons.g_user.getImvUrl()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
        }
        if(id==0){
            main_flag = true;
            Commons.selectUsertype = -1;
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.lyt_fragement,mainListFragment).commit();
            txv_category.setText(Commons.main_category);
        }
        else if(id==1){
            main_flag = false;
            SearchFragment mFragment = new SearchFragment();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.lyt_fragement,mFragment).commit();
        }
        else if(id ==3){

        }
        else if( id ==4){
            MainListFragment mFragment = new MainListFragment();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.lyt_fragement,mFragment).commit();
        }

        imv_feed.setImageDrawable(getResources().getDrawable(R.drawable.icon_home));
        imv_post.setImageDrawable(getResources().getDrawable(R.drawable.icon_addpost));
        imv_search.setImageDrawable(getResources().getDrawable(R.drawable.icon_search));
        imv_feed.clearColorFilter();
        imv_post.clearColorFilter();
        imv_search.clearColorFilter();
        if(id==2){
            imv_post.setColorFilter(getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);
        }else if(id==1){
            imv_search.setColorFilter(getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);

        }else if(id == 0) {
            imv_feed.setColorFilter(getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);
        }
    }



    @Override
    public void onBackPressed() {
        gotoExit();
    }

    void gotoExit(){
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
            @Override
            public void onConfirm() {
                onExit();
            }
        },getString(R.string.finish_app));
        confirmDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            onResume();
            mainListFragment.getList();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Commons.selected_user = Commons.g_user;
        setColor(selectIcon);

    }
    @Override
    public void UserProfile(UserModel userModel,int usertype){
        Gson gson = new Gson();
        String usermodel = gson.toJson(userModel);
        Bundle bundle = new Bundle();
        bundle.putString("userModel",usermodel);
        bundle.putInt("userType",usertype);
        goTo(this, OtherUserProfileActivity.class,false,bundle);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("aaaa","aaaa");
        Commons.mVideoPlayerManager.stopAnyPlayback();
    }
}