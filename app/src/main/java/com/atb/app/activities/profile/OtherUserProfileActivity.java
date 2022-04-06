package com.atb.app.activities.profile;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.applozic.mobicomkit.contact.AppContactService;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.applozic.mobicommons.people.contact.Contact;
import com.atb.app.R;
import com.atb.app.activities.navigationItems.PurchasesActivity;
import com.atb.app.activities.newsfeedpost.NewsDetailActivity;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.dialog.PaymentSuccessDialog;
import com.atb.app.fragement.MainListFragment;
import com.atb.app.fragement.PostsFragment;
import com.atb.app.fragement.StoreFragment;
import com.atb.app.model.FollowerModel;
import com.atb.app.model.NewsFeedEntity;
import com.atb.app.model.UserModel;
import com.atb.app.model.VariationModel;
import com.atb.app.util.RoundedCornersTransformation;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.dropin.utils.PaymentMethodType;
import com.braintreepayments.api.models.VenmoAccountNonce;
import com.braintreepayments.cardform.view.CardForm;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fxn.pix.Pix;
import com.google.gson.Gson;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OtherUserProfileActivity extends CommonActivity implements View.OnClickListener , SmartTabLayout.TabProvider {
    ImageView imv_back,imv_profile,imv_rating,imv_profile_chat,imv_facebook,imv_instagram,imv_twitter,imv_on,imv_follow;
    FrameLayout lyt_profile;
    TextView txv_name,txv_id,txv_follower,txv_following,txv_post,txv_description,txv_follow,txv_on;
    LinearLayout lyt_follower,lyt_following,lyt_post,lyt_following_on,lyt_on,lyt_busines_description;
    SmartTabLayout viewPagerTab;
    ViewPager viewPager;
    UserModel userModel = new UserModel();
    int userType =0;
    Map<String, String> payment_params = new HashMap<>();
    int REQUEST_PAYMENT_CODE =10034;
    NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
    String facebook ="" ,instagra = "",twitter ="";
    int followModelId = -1;
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
        imv_on = findViewById(R.id.imv_on);
        imv_follow = findViewById(R.id.imv_follow);
        txv_follow = findViewById(R.id.txv_follow);
        txv_on = findViewById(R.id.txv_on);
        lyt_busines_description = findViewById(R.id.lyt_busines_description);
        imv_back.setOnClickListener(this);
        lyt_profile.setOnClickListener(this);
        imv_rating.setOnClickListener(this);
        imv_profile_chat.setOnClickListener(this);
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
            for(int i =0;i<userModel.getBusinessModel().getSocialModels().size();i++){
                if(userModel.getBusinessModel().getSocialModels().get(i).getType()==0){
                    imv_facebook.setColorFilter(getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);
                    facebook = userModel.getBusinessModel().getSocialModels().get(i).getSocial_name();
                    imv_facebook.setOnClickListener(this);

                }else if(userModel.getBusinessModel().getSocialModels().get(i).getType()==1){
                    imv_instagram.setColorFilter(getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);
                    instagra = userModel.getBusinessModel().getSocialModels().get(i).getSocial_name();
                    imv_instagram.setOnClickListener(this);

                }else {
                    imv_twitter.setColorFilter(getResources().getColor(R.color.head_color), PorterDuff.Mode.SRC_IN);
                    twitter = userModel.getBusinessModel().getSocialModels().get(i).getSocial_name();
                    imv_twitter.setOnClickListener(this);

                }
            }
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

        setFollow();

    }
    void setFollow(){
         followModelId = -1;
        for(int i =0;i<userModel.getFollowerModels().size();i++){
            FollowerModel follower = userModel.getFollowerModels().get(i);
            if(follower.getFollow_user_id() == Commons.g_user.getId() && follower.getFollower_user_id() == userModel.getId()){
                followModelId = i;
                break;
            }
        }

        if(followModelId ==-1){
            lyt_on.setVisibility(View.GONE);
            lyt_following_on.setBackground(getResources().getDrawable(R.drawable.round_button1));
            txv_follow.setText("Follow");
            txv_follow.setTextColor(getResources().getColor(R.color.txt_color));
            imv_follow.setColorFilter(getResources().getColor(R.color.txt_color), PorterDuff.Mode.SRC_IN);
        }else {
            lyt_following_on.setBackground(getResources().getDrawable(R.drawable.round_button_theme));
            txv_follow.setText("Following");
            txv_follow.setTextColor(getResources().getColor(R.color.white));
            imv_follow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
            lyt_on.setVisibility(View.VISIBLE);
            FollowerModel followerModel = userModel.getFollowerModels().get(followModelId);
            if(followerModel.getPost_notifications()==0){
                lyt_on.setBackground(getResources().getDrawable(R.drawable.round_button1));
                txv_on.setText("OFF");
                txv_on.setTextColor(getResources().getColor(R.color.txt_color));
                imv_on.setColorFilter(getResources().getColor(R.color.txt_color), PorterDuff.Mode.SRC_IN);
            }else {
                lyt_on.setBackground(getResources().getDrawable(R.drawable.round_button_theme));
                txv_on.setText("ON");
                txv_on.setTextColor(getResources().getColor(R.color.white));
                imv_on.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
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
                    icon.setImageDrawable(res.getDrawable(R.drawable.icon_store));
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
                bundle.putBoolean("editable",false);
                goTo(this,ReviewActivity.class,false,bundle);
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

            case R.id.imv_profile_chat:
                gotochat(this,userType,userModel);
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
                notificationChange();
                break;
            case R.id.lyt_following_on:
                addFollow();
                break;
        }
    }

    void notificationChange(){
        String apilink = API.LIKE_NOTIFICATIONS;
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                apilink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                int notification = 0;
                                if(userModel.getFollowerModels().get(followModelId).getPost_notifications()==0)notification = 1;
                                userModel.getFollowerModels().get(followModelId).setPost_notifications(notification);
                                if(notification ==1)
                                    showToast("The notification are now\\nActive for this account");
                                else
                                    showToast("The notification have been\\nDisabled for this user");
                                setFollow();
                            }
                        }catch (Exception e){
                            Log.d("Exception ",e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        closeProgress();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                String notification = "0";
                if(userModel.getFollowerModels().get(followModelId).getPost_notifications()==0)notification = "1";
                params.put("token", Commons.token);
                params.put("follow_user_id", String.valueOf(Commons.g_user.getId()));
                params.put("follower_user_id", String.valueOf(userModel.getId()));
                params.put("notifications",notification );

                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }
    void addFollow(){
        String apilink = API.ADD_FOLLOW;
        if(followModelId !=-1)
            apilink = API.DELETE_FOLLOWER;
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                apilink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                if(followModelId ==-1){
                                    FollowerModel followerModel = new FollowerModel();
                                    followerModel.setFollow_user_id(Commons.g_user.getId());
                                    followerModel.setFollower_user_id(userModel.getId());
                                    userModel.getFollowerModels().add(followerModel);
                                }
                                else{
                                    userModel.getFollowerModels().remove(followModelId);
                                }
                                setFollow();
                            }
                        }catch (Exception e){
                            Log.d("Exception ",e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        closeProgress();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", Commons.token);
                params.put("follow_user_id", String.valueOf(Commons.g_user.getId()));
                params.put("follower_user_id", String.valueOf(userModel.getId()));
                if(followModelId==-1){
                    params.put("follow_business_id", "0");
                    params.put("follower_business_id", "0");
                }
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    @Override
    public void processPayment(String price, String client_id, String clicnet_token, NewsFeedEntity newsFeedEntity, int deliveryOption, ArrayList<String> selected_Variation){
        this.newsFeedEntity = newsFeedEntity;
        payment_params.clear();
        payment_params.put("token",Commons.token);
        payment_params.put("customerId",Commons.g_user.getBt_customer_id());
        payment_params.put("amount",price);
        payment_params.put("toUserId", String.valueOf(newsFeedEntity.getUser_id()));
        payment_params.put("is_business",String.valueOf(newsFeedEntity.getPoster_profile_type() ));
        payment_params.put("quantity","1");
        payment_params.put("delivery_option",String.valueOf(deliveryOption));
        if(selected_Variation.size()>0){
            VariationModel variationModel = newsFeedEntity.productHasStock(selected_Variation);
            payment_params.put("variation_id",String.valueOf(variationModel.getId()));
            payment_params.put("product_id",String.valueOf(variationModel.getProduct_id()));
        }
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(clicnet_token)
                .cardholderNameStatus(CardForm.FIELD_OPTIONAL)
                .collectDeviceData(true)
                .vaultManager(true);
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_PAYMENT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if (requestCode == REQUEST_PAYMENT_CODE) {
            if (resultCode == RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                payment_params.put("paymentNonce", Objects.requireNonNull(result.getPaymentMethodNonce()).getNonce());
                if(result.getPaymentMethodType().name().equals("PAYPAL")){
                    payment_params.put("paymentMethod","Paypal");
                }else {
                    payment_params.put("paymentMethod","Card");
                }
                paymentProcessing(payment_params,0);

                String deviceData = result.getDeviceData();
                if (result.getPaymentMethodType() == PaymentMethodType.PAY_WITH_VENMO) {
                    VenmoAccountNonce venmoAccountNonce = (VenmoAccountNonce) result.getPaymentMethodNonce();
                    String venmoUsername = venmoAccountNonce.getUsername();
                }
                // use the result to update your UI and send the payment method nonce to your server
            } else if (resultCode == RESULT_CANCELED) {
                // the user canceled
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d("error:", error.toString());
            }
        }
    }

    @Override
    public void finishPayment(String transaction_id) {
//        PaymentSuccessDialog paymentSuccessDialog = new PaymentSuccessDialog();
//        paymentSuccessDialog.setOnConfirmListener(new PaymentSuccessDialog.OnConfirmListener() {
//            @Override
//            public void onPurchase() {
//                goTo(OtherUserProfileActivity.this, PurchasesActivity.class,false);
//            }
//        },newsFeedEntity);
//        paymentSuccessDialog.show(getSupportFragmentManager(), "DeleteMessage");
    }

}