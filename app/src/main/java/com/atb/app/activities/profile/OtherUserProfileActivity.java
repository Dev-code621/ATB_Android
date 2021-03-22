package com.atb.app.activities.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.navigationItems.BookingActivity;
import com.atb.app.activities.navigationItems.ContactAdminActivity;
import com.atb.app.activities.navigationItems.CreateAmendBioActivity;
import com.atb.app.activities.navigationItems.ItemSoldActivity;
import com.atb.app.activities.navigationItems.NotificationActivity;
import com.atb.app.activities.navigationItems.ProfileActivity;
import com.atb.app.activities.navigationItems.SavePostActivity;
import com.atb.app.activities.navigationItems.SetPostRangeActivity;
import com.atb.app.activities.navigationItems.TransactionHistoryActivity;
import com.atb.app.activities.navigationItems.UpdateBusinessActivity;
import com.atb.app.activities.newpost.SelectPostCategoryActivity;
import com.atb.app.activities.newpost.SelectProductCategoryActivity;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.fragement.MainListFragment;
import com.atb.app.fragement.PostsFragment;
import com.atb.app.fragement.StoreFragment;
import com.atb.app.model.UserModel;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

public class OtherUserProfileActivity extends CommonActivity implements View.OnClickListener , SmartTabLayout.TabProvider {
    ImageView imv_back,imv_profile,imv_rating,imv_profile_chat,imv_facebook,imv_instagram,imv_twitter;
    FrameLayout lyt_profile;
    TextView txv_name,txv_id,txv_follower,txv_following,txv_post,txv_description;
    LinearLayout lyt_follower,lyt_following,lyt_post,lyt_following_on,lyt_on,lyt_busines_description;
    SmartTabLayout viewPagerTab;
    ViewPager viewPager;
    UserModel userModel = new UserModel();
    int userType =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);
        imv_back = findViewById(R.id.imv_back);
        imv_profile = findViewById(R.id.imv_profile);
        imv_rating = findViewById(R.id.imv_rating);
        imv_profile_chat = findViewById(R.id.imv_profile_chat);
        imv_facebook = findViewById(R.id.imv_facebook);
        imv_instagram = findViewById(R.id.imv_instagram);
        imv_twitter = findViewById(R.id.imv_twitter);
        lyt_profile = findViewById(R.id.lyt_profile);
        txv_name = findViewById(R.id.txv_name);
        txv_id = findViewById(R.id.txv_id);
        txv_follower = findViewById(R.id.txv_follower);
        txv_following = findViewById(R.id.txv_following);
        txv_post = findViewById(R.id.txv_post);
        txv_description = findViewById(R.id.txv_description);
        lyt_follower = findViewById(R.id.lyt_follower);
        lyt_following = findViewById(R.id.lyt_following);
        lyt_post = findViewById(R.id.lyt_post);
        lyt_following_on = findViewById(R.id.lyt_following_on);
        lyt_on = findViewById(R.id.lyt_on);
        viewPagerTab = findViewById(R.id.viewpagertab);
        viewPager = findViewById(R.id.viewpager);
        lyt_busines_description = findViewById(R.id.lyt_busines_description);
        imv_back.setOnClickListener(this);
        lyt_profile.setOnClickListener(this);
        imv_rating.setOnClickListener(this);
        imv_profile_chat.setOnClickListener(this);
        imv_facebook.setOnClickListener(this);
        imv_instagram.setOnClickListener(this);
        imv_twitter.setOnClickListener(this);
        lyt_follower.setOnClickListener(this);
        lyt_following.setOnClickListener(this);
        lyt_post.setOnClickListener(this);
        lyt_following_on.setOnClickListener(this);
        lyt_on.setOnClickListener(this);
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                String user= bundle.getString("userModel");
                Gson gson = new Gson();
                userModel = gson.fromJson(user, UserModel.class);
                userType = bundle.getInt("userType");
            }
        }
        Commons.selected_user = userModel;
        Commons.selectUsertype = userType;
        initLayout();
    }

    void initLayout(){
        if(userType == 1) {
            Glide.with(this).load(userModel.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
            txv_name.setText(userModel.getBusinessModel().getBusiness_name());
            txv_id.setText(userModel.getBusinessModel().getBusiness_website());
            txv_follower.setText(String.valueOf(userModel.getFollowers_count()));
            txv_following.setText(String.valueOf(userModel.getFollow_count()));
            txv_post.setText(String.valueOf(userModel.getPost_count()));
            txv_description.setText(userModel.getBusinessModel().getBusiness_bio());


            FragmentPagerItems pages = new FragmentPagerItems(this);
            pages.add(FragmentPagerItem.of("Store", StoreFragment.class));
            pages.add(FragmentPagerItem.of("Posts", PostsFragment.class));
            viewPagerTab.setCustomTabView(this);
            FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                    getSupportFragmentManager(), pages);

            viewPager.setAdapter(adapter);
            viewPagerTab.setViewPager(viewPager);
            viewPagerTab.setCustomTabView(this);
        }else {
            Glide.with(this).load(userModel.getImvUrl()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
            txv_name.setText(userModel.getFirstname() + " " + userModel.getLastname());
            txv_id.setText("@"+userModel.getUserName());
            txv_follower.setText(String.valueOf(userModel.getFollowers_count()));
            txv_following.setText(String.valueOf(userModel.getFollow_count()));
            txv_post.setText(String.valueOf(userModel.getPost_count()));
            lyt_busines_description.setVisibility(View.GONE);
            imv_rating.setVisibility(View.GONE);

            FragmentPagerItems pages = new FragmentPagerItems(this);
            pages.add(FragmentPagerItem.of("Posts", PostsFragment.class));
            pages.add(FragmentPagerItem.of("Main", MainListFragment.class));

            viewPagerTab.setCustomTabView(this);
            FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                    getSupportFragmentManager(), pages);

            viewPager.setAdapter(adapter);
            viewPagerTab.setViewPager(viewPager);
            viewPagerTab.setCustomTabView(this);
        }
    }

    @Override
    public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        Resources res = container.getContext().getResources();
        View tab = inflater.inflate(R.layout.custom_tab_icon_and_text, container, false);
        ImageView icon = (ImageView) tab.findViewById(R.id.custom_tab_icon);
        TextView textView = (TextView) tab.findViewById(R.id.custom_tab_text);
        textView.setText(adapter.getPageTitle(position));
        if(userType == 1) {
            switch (position) {
                case 0:
                    icon.setImageDrawable(res.getDrawable(R.drawable.icon_sales));
                    break;
                case 1:
                    icon.setImageDrawable(res.getDrawable(R.drawable.icon_gride));
                    break;
                default:
                    throw new IllegalStateException("Invalid position: " + position);
            }
        }else {
            textView.setVisibility(View.GONE);
            switch (position) {
                case 0:
                    icon.setImageDrawable(res.getDrawable(R.drawable.icon_gride));
                    break;
                case 1:
                    icon.setImageDrawable(res.getDrawable(R.drawable.icon_sales));
                    break;
                default:
                    throw new IllegalStateException("Invalid position: " + position);
            }
        }
        return tab;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_back:
                finish(this);
                break;
            case R.id.imv_rating:
                Gson gson = new Gson();
                String usermodel = gson.toJson(userModel);
                Bundle bundle = new Bundle();
                bundle.putString("userModel",usermodel);
                goTo(this,ReviewActivity.class,false,bundle);
                break;
            case R.id.imv_facebook:

                break;

            case R.id.imv_instagram:

                break;

            case R.id.imv_twitter:

                break;

            case R.id.lyt_follower:
                bundle = new Bundle();
                bundle.putBoolean("isFollower", true);
                bundle.putInt("userType",userType);
                gson = new Gson();
                usermodel = gson.toJson(userModel);
                bundle.putString("userModel",usermodel);
                goTo(this, FollowerAndFollowingActivity.class,true,bundle);
                break;

            case R.id.lyt_following:
                Bundle bundle1 = new Bundle();
                bundle1.putBoolean("isFollower", false);
                bundle1.putInt("userType",userType);
                gson = new Gson();
                usermodel = gson.toJson(userModel);
                bundle1.putString("userModel",usermodel);
                goTo(this, FollowerAndFollowingActivity.class,true,bundle1);
                break;
            case R.id.lyt_post:

                break;
            case R.id.lyt_on:

                break;


        }
    }
}