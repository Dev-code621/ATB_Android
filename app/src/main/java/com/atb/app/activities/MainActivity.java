package com.atb.app.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.atb.app.R;
import com.atb.app.activities.newpost.SelectPostCategoryActivity;
import com.atb.app.activities.profile.ProfileBusinessNaviagationActivity;
import com.atb.app.activities.profile.ProfileUserNavigationActivity;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.commons.Constants;
import com.atb.app.dialog.ConfirmDialog;
import com.atb.app.dialog.SelectCategoryDialog;
import com.atb.app.fragement.ChatFragment;
import com.atb.app.fragement.MainListFragment;
import com.atb.app.fragement.SearchFragment;
import com.atb.app.view.NonSwipeableViewPager;

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
    MainListFragment mainListFragment;
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
        mainListFragment = new MainListFragment();
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
               // setColor(3);
                break;
            case R.id.imv_profile:
                if(Commons.g_user.getAccount_type()==1)
                    startActivityForResult(new Intent(this, ProfileBusinessNaviagationActivity.class),1);
                else
                    goTo(this, ProfileUserNavigationActivity.class,false);
                break;
            case R.id.imv_post:
                setColor(1);
                startActivityForResult(new Intent(this, SelectPostCategoryActivity.class),1);
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
            Commons.main_category = getResources().getString(R.string.my_atb);
        }else {
            txv_category.setText(Constants.category_word[posstion]);
            Commons.main_category = Constants.category_word[posstion];
        }
        mainListFragment.getList();
    }
    @SuppressLint("ResourceAsColor")
    public void setColor(int id){
        lyt_title.setVisibility(View.VISIBLE);
        lyt_title1.setVisibility(View.GONE);
        if(id==0){
            main_flag = true;
            Commons.selectUsertype = -1;
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.lyt_fragement,mainListFragment).commit();
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
            imv_chat.setColorFilter(getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);
        }else if(id==1){
            imv_post.setColorFilter(getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);

        }else {
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
        setColor(0);

    }
}