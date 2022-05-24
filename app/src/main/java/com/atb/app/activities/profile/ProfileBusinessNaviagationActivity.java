package com.atb.app.activities.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.net.Uri;
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
import com.atb.app.activities.LoginActivity;
import com.atb.app.activities.navigationItems.ContactAdminActivity;
import com.atb.app.activities.navigationItems.CreateAmendBioActivity;
import com.atb.app.activities.navigationItems.ItemSoldActivity;
import com.atb.app.activities.navigationItems.NotificationActivity;
import com.atb.app.activities.navigationItems.ProfileActivity;
import com.atb.app.activities.navigationItems.SavePostActivity;
import com.atb.app.activities.navigationItems.SetPostRangeActivity;
import com.atb.app.activities.navigationItems.TransactionHistoryActivity;
import com.atb.app.activities.navigationItems.booking.BookingSplashActivity;
import com.atb.app.activities.navigationItems.business.UpdateBusinessActivity;
import com.atb.app.activities.navigationItems.DraftPostActivity;
import com.atb.app.activities.newpost.SelectPostCategoryActivity;
import com.atb.app.activities.newpost.SelectProductCategoryActivity;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.dialog.ConfirmDialog;
import com.atb.app.fragement.ChatFragment;
import com.atb.app.fragement.PostsFragment;
import com.atb.app.fragement.SearchFragment;
import com.atb.app.fragement.StoreFragment;
import com.atb.app.preference.PrefConst;
import com.atb.app.preference.Preference;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ProfileBusinessNaviagationActivity extends CommonActivity implements View.OnClickListener , SmartTabLayout.TabProvider  {

    ImageView imv_back,imv_profile,imv_business,imv_rating,imv_profile_chat,imv_dot,imv_facebook,imv_instagram,imv_twitter,imv_feed,imv_post,imv_chat,imv_search;
    FrameLayout lyt_profile,lyt_navigation,lyt_content;
    TextView txv_name,txv_id,txv_follower,txv_following,txv_post,txv_description;
    LinearLayout lyt_follower,lyt_following,lyt_post,lyt_following_on,lyt_on,lyt_title;
    SmartTabLayout viewPagerTab;
    ViewPager viewPager;
    DrawerLayout drawer;
    private AppBarConfiguration mAppBarConfiguration;
    CardView card_addstore;
    boolean main_flag;
    FragmentTransaction ft;
    ChatFragment chatFragment;
    FrameLayout lyt_fragement,frame_chat;
    String facebook ="" ,instagra = "",twitter ="";
    int selectIcon =0;
    StoreFragment storeFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_naviagation);
        imv_back = findViewById(R.id.imv_back);
        imv_profile = findViewById(R.id.imv_profile);
        imv_business = findViewById(R.id.imv_business);
        imv_rating = findViewById(R.id.imv_rating);
        imv_profile_chat = findViewById(R.id.imv_profile_chat);
        imv_dot = findViewById(R.id.imv_dot);
        imv_facebook = findViewById(R.id.imv_facebook);
        imv_instagram = findViewById(R.id.imv_instagram);
        imv_twitter = findViewById(R.id.imv_twitter);
        imv_feed = findViewById(R.id.imv_feed);
        imv_post = findViewById(R.id.imv_post);
        imv_chat = findViewById(R.id.imv_chat);
        imv_search = findViewById(R.id.imv_search);
        lyt_profile = findViewById(R.id.lyt_profile);
        lyt_navigation = findViewById(R.id.lyt_navigation);
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
        card_addstore = findViewById(R.id.card_addstore);
        lyt_title = findViewById(R.id.lyt_title);
        lyt_content = findViewById(R.id.lyt_content);
        lyt_fragement = findViewById(R.id.lyt_fragement);
        frame_chat = findViewById(R.id.frame_chat);
        LinearLayout lyt_show_notis = findViewById(R.id.lyt_show_notis);
        LinearLayout lyt_upgrade_business = findViewById(R.id.lyt_upgrade_business);
        LinearLayout lyt_booking = findViewById(R.id.lyt_booking);
        LinearLayout  lyt_item_sold = findViewById(R.id.lyt_item_sold);
        LinearLayout lyt_create_bio = findViewById(R.id.lyt_create_bio);
        LinearLayout lyt_set_range = findViewById(R.id.lyt_set_range);
        LinearLayout lyt_user_setting = findViewById(R.id.lyt_user_setting);
        LinearLayout  lyt_transaction = findViewById(R.id.lyt_transaction);
        LinearLayout  lyt_contact_admin = findViewById(R.id.lyt_contact_admin);
        LinearLayout lyt_save_post = findViewById(R.id.lyt_save_post);
        LinearLayout lyt_logout = findViewById(R.id.lyt_logout);
        LinearLayout lyt_busines_upgrade = findViewById(R.id.lyt_busines_upgrade);
        LinearLayout lyt_draft_post = findViewById(R.id.lyt_draft_post);
        lyt_on.setVisibility(View.GONE);
        lyt_upgrade_business.setOnClickListener(this);
        lyt_show_notis.setOnClickListener(this);
        lyt_busines_upgrade.setOnClickListener(this);
        lyt_logout.setOnClickListener(this);
        lyt_save_post.setOnClickListener(this);
        lyt_contact_admin.setOnClickListener(this);
        lyt_transaction.setOnClickListener(this);
        lyt_user_setting.setOnClickListener(this);
        lyt_set_range.setOnClickListener(this);
        lyt_create_bio.setOnClickListener(this);
        lyt_item_sold.setOnClickListener(this);
        lyt_booking.setOnClickListener(this);
        imv_back.setOnClickListener(this);
        lyt_profile.setOnClickListener(this);
        imv_rating.setOnClickListener(this);
        imv_profile_chat.setOnClickListener(this);
        lyt_navigation.setOnClickListener(this);
        lyt_draft_post.setOnClickListener(this);
        lyt_follower.setOnClickListener(this);
        lyt_following.setOnClickListener(this);
        lyt_post.setOnClickListener(this);
        lyt_following_on.setOnClickListener(this);
        lyt_on.setOnClickListener(this);
        imv_feed.setOnClickListener(this);
        imv_post.setOnClickListener(this);
        imv_search.setOnClickListener(this);
        frame_chat.setOnClickListener(this);
        card_addstore.setOnClickListener(this);
        setupFragment();
        drawer = findViewById(R.id.drawer_layout);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();


    }
    void setupFragment(){
        FragmentPagerItems pages = new FragmentPagerItems(this);
        storeFragment = new StoreFragment();
        pages.add(FragmentPagerItem.of("Store", storeFragment.getClass()));
        pages.add(FragmentPagerItem.of("Posts", PostsFragment.class));
        viewPagerTab.setCustomTabView(this);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), pages);

        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);
        viewPagerTab.setCustomTabView(this);
        viewPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position ==0)
                    card_addstore.setVisibility(View.VISIBLE);
                else
                    card_addstore.setVisibility(View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    void initLayout(){
        Glide.with(this).load(Commons.g_user.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
        txv_name.setText(Commons.g_user.getBusinessModel().getBusiness_name());
        txv_id.setText(Commons.g_user.getBusinessModel().getBusiness_website());
        txv_follower.setText(String.valueOf(Commons.g_user.getFollowers_count()));
        txv_following.setText(String.valueOf(Commons.g_user.getFollow_count()));
        txv_post.setText(String.valueOf(Commons.g_user.getPost_count()));
        txv_description.setText(Commons.g_user.getBusinessModel().getBusiness_bio());
        for(int i =0;i<Commons.g_user.getBusinessModel().getSocialModels().size();i++){
            if(Commons.g_user.getBusinessModel().getSocialModels().get(i).getType()==0){
                imv_facebook.setColorFilter(getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);
                facebook = Commons.g_user.getBusinessModel().getSocialModels().get(i).getSocial_name();
                imv_facebook.setOnClickListener(this);
            }else if(Commons.g_user.getBusinessModel().getSocialModels().get(i).getType()==1){
                imv_instagram.setColorFilter(getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);
                instagra = Commons.g_user.getBusinessModel().getSocialModels().get(i).getSocial_name();
                imv_instagram.setOnClickListener(this);
            }else {
                imv_twitter.setColorFilter(getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);
                twitter = Commons.g_user.getBusinessModel().getSocialModels().get(i).getSocial_name();
                imv_twitter.setOnClickListener(this);
            }
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
        return tab;
    }

    @Override
    public boolean selectProfile(boolean flag){
        if(chatFragment==null) {
            if (!flag)
                goTo(this, ProfileUserNavigationActivity.class, true);
        }else {
            chatFragment.setProfile(flag);
        }
        return flag;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_back:
                finish(this);
                break;
            case R.id.imv_post:
                setColor(2);
                startActivityForResult(new Intent(this, SelectPostCategoryActivity.class),1);
                overridePendingTransition(0, 0);
                break;
            case R.id.imv_search:
                setColor(1);
                break;
            case R.id.imv_feed:
                if(main_flag)finish(this);
                else
                    setColor(0);
                break;
            case R.id.frame_chat:
                setColor(3);
                break;
            case R.id.lyt_profile:
                SelectprofileDialog(this);
                break;
            case R.id.lyt_show_notis:
                drawer.closeDrawer(GravityCompat.END);
                goTo(this, NotificationActivity.class,false);
                break;
            case R.id.lyt_upgrade_business:
                drawer.closeDrawer(GravityCompat.END);
                goTo(this, UpdateBusinessActivity.class,false);
                break;
            case R.id.lyt_booking:

                drawer.closeDrawer(GravityCompat.END);
              goTo(this, BookingSplashActivity.class,false);
                break;
            case R.id.lyt_item_sold:
                drawer.closeDrawer(GravityCompat.END);
                Bundle bundle = new Bundle();
                bundle.putBoolean("bussiness",true);
                goTo(this, ItemSoldActivity.class,false,bundle);
                break;
            case R.id.lyt_create_bio:
                drawer.closeDrawer(GravityCompat.END);
                bundle = new Bundle();
                bundle.putBoolean("bussiness",true);
                goTo(this, CreateAmendBioActivity.class,false,bundle);
                break;
            case R.id.lyt_set_range:
                drawer.closeDrawer(GravityCompat.END);
                goTo(this, SetPostRangeActivity.class,false);
                break;
            case R.id.lyt_user_setting:
                drawer.closeDrawer(GravityCompat.END);
                goTo(this, ProfileActivity.class,false);
                break;
            case R.id.lyt_transaction:
                drawer.closeDrawer(GravityCompat.END);
                goTo(this, TransactionHistoryActivity.class,false);
                break;
            case R.id.lyt_contact_admin:
                drawer.closeDrawer(GravityCompat.END);
                goTo(this, ContactAdminActivity.class,false);
                break;
            case R.id.lyt_save_post:
                drawer.closeDrawer(GravityCompat.END);
                goTo(this, SavePostActivity.class,false);
                break;
            case R.id.lyt_draft_post:
                drawer.closeDrawer(GravityCompat.END);
                goTo(this, DraftPostActivity.class,false);
                break;
            case R.id.lyt_logout:
                drawer.closeDrawer(GravityCompat.END);
                gotoLogout();
                break;
            case R.id.lyt_busines_upgrade:
                drawer.closeDrawer(GravityCompat.END);
                goTo(this, UpdateBusinessActivity.class,false);

                break;
            case R.id.imv_rating:
                Gson gson = new Gson();
                String usermodel = gson.toJson(Commons.g_user);
                bundle = new Bundle();
                bundle.putString("userModel",usermodel);
                bundle.putBoolean("editable",false);
                goTo(this,ReviewActivity.class,false,bundle);
                break;

            case R.id.imv_profile_chat:

                break;

            case R.id.lyt_navigation:
                drawer.openDrawer(Gravity.RIGHT);
                break;

            case R.id.imv_facebook:
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                String facebookUrl = getFacebookPageURL(facebook);
                facebookIntent.setData(Uri.parse(facebookUrl));
                startActivity(facebookIntent);
                break;

            case R.id.imv_instagram:
                Uri uri = Uri.parse("http://instagram.com/_u/" + instagra);
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
                likeIng.setPackage("com.instagram.android");
                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/" + instagra)));
                }
                break;

            case R.id.imv_twitter:
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("twitter://user?screen_name=["+ twitter+"]"));
                    startActivity(intent);
                } catch (Exception e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://twitter.com/#!/["+ twitter+  "]")));
                }
                break;

            case R.id.lyt_follower:
                bundle = new Bundle();
                bundle.putBoolean("isFollower", true);
                bundle.putInt("userType",1);
                gson = new Gson();
                usermodel = gson.toJson(Commons.g_user);
                bundle.putString("userModel",usermodel);
                goTo(this, FollowerAndFollowingActivity.class,false,bundle);
                break;

            case R.id.lyt_following:
                Bundle bundle1 = new Bundle();
                bundle1.putBoolean("isFollower", false);
                bundle1.putInt("userType",1);
                gson = new Gson();
                usermodel = gson.toJson(Commons.g_user);
                bundle1.putString("userModel",usermodel);
                goTo(this, FollowerAndFollowingActivity.class,false,bundle1);
                break;
            case R.id.lyt_post:

                break;
            case R.id.lyt_following_on:

                break;
            case R.id.lyt_on:

                break;
            case R.id.card_addstore:
                startActivityForResult(new Intent(this, SelectProductCategoryActivity.class),1);
                break;

        }
    }
    void gotoLogout(){
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
            @Override
            public void onConfirm() {
                pubnubTokenLogout();

            }
        },getString(R.string.logout_description));
        confirmDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
    }

    void pubnubTokenLogout(){

        Commons.mPubNub.removePushNotificationsFromChannels()
                .channels(Commons.pubnub_channels)
                .pushType(PNPushType.FCM)
                .deviceId(Commons.fcmtoken)
                .async(new PNCallback<PNPushRemoveChannelResult>() {
                    @Override
                    public void onResponse(PNPushRemoveChannelResult result, PNStatus status) {
                        // Handle status, response
                        Preference.getInstance().put(ProfileBusinessNaviagationActivity.this, PrefConst.PREFKEY_USEREMAIL, "");
                        Preference.getInstance().put(ProfileBusinessNaviagationActivity.this, PrefConst.PREFKEY_USERPWD, "");

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
    }

@SuppressLint("ResourceAsColor")
    public void setColor(int id){
        chatFragment = null;
        lyt_title.setVisibility(View.VISIBLE);
        lyt_content.setVisibility(View.VISIBLE);
        lyt_fragement.setVisibility(View.GONE);
        if(Commons.g_user.getAccount_type() ==1 ){
            Glide.with(this).load(Commons.g_user.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
        }else {
            Glide.with(this).load(Commons.g_user.getImvUrl()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
        }
        if(id==0){
            selectIcon = id;
            main_flag = true;
        }
        else if(id==1){
            selectIcon = id;
            lyt_title.setVisibility(View.GONE);
            lyt_content.setVisibility(View.GONE);
            lyt_fragement.setVisibility(View.VISIBLE);
            main_flag = false;
            SearchFragment mFragment = new SearchFragment();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.lyt_fragement,mFragment).commit();
        }
        else if(id ==3){
            selectIcon = id;
            main_flag = false;
            lyt_title.setVisibility(View.GONE);
            lyt_content.setVisibility(View.GONE);
            lyt_fragement.setVisibility(View.VISIBLE);
            chatFragment = new ChatFragment();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.lyt_fragement,chatFragment).commit();
        }

        imv_feed.setImageDrawable(getResources().getDrawable(R.drawable.icon_home));
        imv_post.setImageDrawable(getResources().getDrawable(R.drawable.icon_addpost));
        imv_search.setImageDrawable(getResources().getDrawable(R.drawable.icon_search));
        imv_chat.setImageDrawable(getResources().getDrawable(R.drawable.icon_message));
        imv_feed.clearColorFilter();
        imv_post.clearColorFilter();
        imv_search.clearColorFilter();
        imv_chat.clearColorFilter();
        if(id==2){
            imv_post.setColorFilter(getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);
        }else if(id==1){
            imv_search.setColorFilter(getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);

        }else if(id == 0) {
            imv_feed.setColorFilter(getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);
        }else if(id ==3){
            imv_chat.setColorFilter(getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            onResume();
            setupFragment();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Commons.selectUsertype = 1;
        Commons.selected_user = Commons.g_user;
        setColor(selectIcon);
        initLayout();
    }
}