package com.atb.app.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentTransaction;

import com.atb.app.R;
import com.atb.app.activities.newpost.SelectPostCategoryActivity;
import com.atb.app.activities.register.ProfileSetActivity;
import com.atb.app.activities.register.Signup1Activity;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Constants;
import com.atb.app.dialog.ConfirmDialog;
import com.atb.app.dialog.PickImageDialog;
import com.atb.app.dialog.SelectCategoryDialog;
import com.atb.app.dialog.SelectMediaDialog;
import com.atb.app.fragement.ChatFragment;
import com.atb.app.fragement.MainListFragment;
import com.atb.app.fragement.SearchFragment;
import com.atb.app.util.MediaPicker;
import com.atb.app.view.NonSwipeableViewPager;


import java.io.File;

public class MainActivity extends CommonActivity implements View.OnClickListener {
    ImageView imv_search ,imv_profile,imv_feed,imv_post,imv_chat;
    FragmentTransaction ft;
    private NonSwipeableViewPager viewPager;
    boolean main_flag = false;
    SelectCategoryDialog selectCategoryDialog;
    TextView txv_category;
    EditText edt_serach;
    LinearLayout lyt_title,lyt_title1;
    ImageView imv_selector;
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
        lyt_title1 = findViewById(R.id.lyt_title1);
        edt_serach = findViewById(R.id.edt_serach);
        imv_selector = findViewById(R.id.imv_selector);

        imv_search.setOnClickListener(this);
        imv_profile.setOnClickListener(this);
        imv_feed.setOnClickListener(this);
        imv_post.setOnClickListener(this);
        imv_chat.setOnClickListener(this);
        imv_selector.setOnClickListener(this);
        Keyboard();
        setColor(0);
    }

    void Keyboard(){
        LinearLayout lytContainer = (LinearLayout) findViewById(R.id.lyt_container);
        lytContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_serach.getWindowToken(), 0);
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_search:
                setColor(3);
                break;
            case R.id.imv_profile:

                break;
            case R.id.imv_post:
                setColor(1);
                goTo(this, SelectPostCategoryActivity.class,false);
                break;
            case R.id.imv_feed:
                if(main_flag)selectCategory();
                setColor(0);
                break;
            case R.id.imv_chat:
                setColor(2);
                break;
            case R.id.imv_selector:
                setColor(3);
                //imv_selector.setEnabled(!imv_selector.isEnabled());
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
        }else {
            txv_category.setText(Constants.category_word[posstion]);
        }
    }
    @SuppressLint("ResourceAsColor")
    public void setColor(int id){
        lyt_title.setVisibility(View.VISIBLE);
        lyt_title1.setVisibility(View.GONE);
        if(id==0){
            main_flag = true;
            MainListFragment mFragment = new MainListFragment();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.lyt_fragement,mFragment).commit();
        }
        else if(id==2){
            main_flag = false;
            ChatFragment chatFragment = new ChatFragment();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.lyt_fragement,chatFragment).commit();
        }
        else if(id ==3){
            main_flag = false;
            SearchFragment mFragment = new SearchFragment();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.lyt_fragement,mFragment).commit();
            lyt_title.setVisibility(View.GONE);
            lyt_title1.setVisibility(View.GONE);
        }
        else if( id ==4){
            lyt_title.setVisibility(View.GONE);
            lyt_title1.setVisibility(View.VISIBLE);
            MainListFragment mFragment = new MainListFragment();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.lyt_fragement,mFragment).commit();
        }

        imv_feed.setImageDrawable(getResources().getDrawable(R.drawable.icon_feed));
        imv_post.setImageDrawable(getResources().getDrawable(R.drawable.icon_addpost));
        imv_chat.setImageDrawable(getResources().getDrawable(R.drawable.icon_message));
        imv_feed.clearColorFilter();
        imv_post.clearColorFilter();
        imv_chat.clearColorFilter();
        if(id==2){
            imv_chat.setColorFilter(R.color.head_color, PorterDuff.Mode.SRC_IN);
        }else if(id==1){
            imv_post.setColorFilter(R.color.head_color, PorterDuff.Mode.SRC_IN);

        }else {
            imv_feed.setColorFilter(R.color.head_color, PorterDuff.Mode.SRC_IN);
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
}