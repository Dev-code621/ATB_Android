package com.atb.app.activities.newsfeedpost;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.atb.app.R;
import com.atb.app.activities.navigationItems.booking.CreateABookingActivity;
import com.atb.app.activities.navigationItems.booking.CreateBooking2Activity;
import com.atb.app.activities.navigationItems.business.BusinessProfilePaymentActivity;
import com.atb.app.activities.navigationItems.business.UpdateBusinessActivity;
import com.atb.app.activities.navigationItems.business.UpgradeBusinessSplashActivity;
import com.atb.app.activities.profile.ProfileBusinessNaviagationActivity;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.commons.Constants;
import com.atb.app.dialog.ConfirmDialog;
import com.atb.app.dialog.SelectMediaDialog;
import com.atb.app.model.NewsFeedEntity;
import com.atb.app.model.UserModel;
import com.atb.app.util.CustomMultipartRequest;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.google.android.gms.common.internal.service.Common;
import com.google.gson.Gson;


import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewAdviceActivity extends CommonActivity implements View.OnClickListener {
    NiceSpinner spiner_media_type,spiner_category_type;
    LinearLayout lyt_back;
    ImageView imv_profile;
    FrameLayout lyt_image_video,lyt_video,lyt_profile;
    ImageView imv_videothumnail,imv_videoicon,imv_imageicon;
    CardView card_business;
    ArrayList<ImageView>imageViews = new ArrayList<>();
    LinearLayout lyt_image;
    EditText edt_title,edt_description;
    TextView txv_post,txv_advice;
    ArrayList<String>returnValue = new ArrayList<>();
    ArrayList<String>completedValue = new ArrayList<>();
    String videovalue ="";
    File uploadThumbImage;
    boolean business_user = false;
    int maxImagecount = 3;
    int media_type =0;
    boolean editable =false;
    NewsFeedEntity newsFeedEntity = new NewsFeedEntity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_advice);
        spiner_media_type = findViewById(R.id.spiner_media_type);
        spiner_category_type = findViewById(R.id.spiner_category_type);
        lyt_back = findViewById(R.id.lyt_back);
        imv_profile = findViewById(R.id.imv_profile);
        lyt_image_video = findViewById(R.id.lyt_image_video);
        imv_videothumnail = findViewById(R.id.imv_videothumnail);
        imv_videoicon = findViewById(R.id.imv_videoicon);
        lyt_video = findViewById(R.id.lyt_video);
        imv_imageicon = findViewById(R.id.imv_imageicon);
        imageViews.add(findViewById(R.id.imv_image));
        imageViews.add(findViewById(R.id.imv_image1));
        imageViews.add(findViewById(R.id.imv_image2));
        imageViews.add(findViewById(R.id.imv_image3));
        imageViews.add(findViewById(R.id.imv_image4));
        imageViews.add(findViewById(R.id.imv_image5));
        imageViews.add(findViewById(R.id.imv_image6));
        imageViews.add(findViewById(R.id.imv_image7));
        imageViews.add(findViewById(R.id.imv_image8));
        txv_advice = findViewById(R.id.txv_advice);
        lyt_image = findViewById(R.id.lyt_image);
        edt_title = findViewById(R.id.edt_title);
        edt_description = findViewById(R.id.edt_description);
        txv_post = findViewById(R.id.txv_post);
        card_business = findViewById(R.id.card_business);
        lyt_profile = findViewById(R.id.lyt_profile);

        lyt_back.setOnClickListener(this);
        txv_post.setOnClickListener(this);
        imv_videothumnail.setOnClickListener(this);
        lyt_profile.setOnClickListener(this);
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                editable= bundle.getBoolean("edit");
                String string = bundle.getString("newsFeedEntity");
                Gson gson = new Gson();
                newsFeedEntity = gson.fromJson(string, NewsFeedEntity.class);
                loadEdit();
            }
        }
        spiner_media_type.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                // This example uses String, but your type can be any
                String item = String.valueOf(parent.getItemAtPosition(position));
                media_type = position;
                initLayout();
            }

        });
        for(int i =0;i<imageViews.size();i++){
            int finalI = i;
            imageViews.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage(finalI);
                }
            });
        }



        String str = "Post in ";
        if(editable) str = "Update in ";
            txv_post.setText(str +  spiner_category_type.getSelectedItem().toString());
        String finalStr = str;
        spiner_category_type.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {

                String text = finalStr +  spiner_category_type.getSelectedItem().toString();
                SpannableString ss = new SpannableString(text);
                StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                ss.setSpan(boldSpan, 0, finalStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                txv_post.setText(ss);
            }
        });
        initLayout();
        Keyboard();
    }

    void loadEdit(){
        txv_advice.setText("Edit Advice");
        media_type = newsFeedEntity.getMedia_type();
        spiner_media_type.setSelectedIndex(media_type);
        if (newsFeedEntity.getPoster_profile_type() == 1) business_user = true;

        edt_title.setText(newsFeedEntity.getTitle());
        edt_description.setText(newsFeedEntity.getDescription());
        String[] array = getResources().getStringArray(R.array.category_type);
        for (int i = 0; i < array.length; i++){
            if (array[i].equals(newsFeedEntity.getCategory_title())) {
                spiner_category_type.setSelectedIndex(i);
                break;
            }
        }

        for (int i = 0; i < newsFeedEntity.getPostImageModels().size(); i++) {
            if (Commons.mediaVideoType(newsFeedEntity.getPostImageModels().get(i).getPath())) {
                videovalue = newsFeedEntity.getPostImageModels().get(i).getPath();
            } else {
                completedValue.add(newsFeedEntity.getPostImageModels().get(i).getPath());
            }
        }
        if (media_type == 1)
            reloadImages();
        else if (media_type == 2)
            reloadVideo();

    }

    void initLayout(){
        for (int i = 0; i < imageViews.size(); i++) {
            imageViews.get(i).setVisibility(View.VISIBLE);
            if (!business_user && i > 3) imageViews.get(i).setVisibility(View.GONE);
        }
        if (business_user)
            Glide.with(this).load(Commons.g_user.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.icon_image1).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
        else
            Glide.with(this).load(Commons.g_user.getImvUrl()).placeholder(R.drawable.icon_image1).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
        if (Commons.g_user.getAccount_type() == 0) card_business.setVisibility(View.GONE);

        if(media_type==0){
            lyt_image_video.setVisibility(View.GONE);
        }else if(media_type==1){
            lyt_image_video.setVisibility(View.VISIBLE);
            lyt_image.setVisibility(View.VISIBLE);
            lyt_video.setVisibility(View.GONE);

        }else {
            lyt_image_video.setVisibility(View.VISIBLE);
            lyt_image.setVisibility(View.GONE);
            lyt_video.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean selectProfile(boolean flag){
        if(Commons.g_user.getBusinessModel().getPaid()==0){
            ConfirmDialog confirmDialog = new ConfirmDialog();
            confirmDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListener() {
                @Override
                public void onConfirm() {
                    Bundle bundle = new Bundle();
                    bundle.putInt("subScriptionType",2);
                    startActivityForResult(new Intent(NewAdviceActivity.this, BusinessProfilePaymentActivity.class).putExtra("data",bundle),1);
                    overridePendingTransition(0, 0);
                }
            },getString(R.string.subscription_alert));
            confirmDialog.show(this.getSupportFragmentManager(), "DeleteMessage");
        }else {
            business_user = flag;
            if (flag) maxImagecount = 9;
            else maxImagecount = 3;
            initLayout();
        }

        return flag;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lyt_back:
                finish(this);
                break;
            case R.id.lyt_profile:
                if(Commons.g_user.getAccount_type()==1)
                    SelectprofileDialog(this);
                break;
            case R.id.txv_post:
                postAdvice();
                break;
            case R.id.imv_videothumnail:
                videovalue = "";
                selectVideo();
                break;
        }
    }

    void postAdvice(){
        if(edt_title.getText().toString().length()==0){
            showAlertDialog(getResources().getString(R.string.input_title));
            return;
        }else  if(edt_description.getText().toString().length()==0){
            showAlertDialog(getResources().getString(R.string.input_description));
            return;
        }
        if(media_type==1 && completedValue.size()==0){
            showAlertDialog(getResources().getString(R.string.input_photos));
            return;
        }
        if(media_type ==2 && videovalue.equals("")){
            showAlertDialog(getResources().getString(R.string.input_videos));
            return;
        }

        showProgress();
        try {
            Map<String, String> params = new HashMap<>();
            String API_LINK = API.CREATE_POST_API;
            if(editable) {
                API_LINK = API.UPDATE_POST_API;
                params.put("id", String.valueOf(newsFeedEntity.getId()));
            }
            params.put("token", Commons.token);
            params.put("type", "1");
            params.put("media_type", String.valueOf(media_type));
            if(business_user)
                params.put("profile_type", "1");
            else
                params.put("profile_type", "0");
            params.put("title", edt_title.getText().toString());
            params.put("description",edt_description.getText().toString());
            params.put("brand", "");
            params.put("price", "0.00");
            params.put("category_title", spiner_category_type.getSelectedItem().toString());
            params.put("item_title", "");
            params.put("payment_options", "0");
            params.put("location_id", "0");
            params.put("delivery_option", "0");
            params.put("delivery_cost", "0");
            //File part
            ArrayList<File> post = new ArrayList<>();
            String post_image_uris = "";
            if (media_type == 1) {
                for (int i = 0; i < completedValue.size(); i++) {
                    if(URLUtil.isNetworkUrl(completedValue.get(i)))
                        post_image_uris += completedValue.get(i) + ",";
                    else {
                        post_image_uris  +="data" +",";
                        File file = new File(completedValue.get(i));
                        post.add(file);
                    }
                }
                post_image_uris = post_image_uris.substring(0,post_image_uris.length()-1);
            } else if (media_type == 2) {
                if(URLUtil.isNetworkUrl(videovalue)){
                    post_image_uris = videovalue;
                }else {
                    post_image_uris = "data";
                    File file = new File(videovalue);
                    post.add(file);
                }
            }
            if(editable){
                params.put("post_img_uris", post_image_uris);
            }

            String imageTitle = "post_imgs";
            Map<String, String> mHeaderPart= new HashMap<>();
            mHeaderPart.put("Content-type", "multipart/form-data; boundary=<calculated when request is sent>");
            mHeaderPart.put("Accept", "application/json");
            Log.d("aaa",params.toString());
            CustomMultipartRequest mCustomRequest = new CustomMultipartRequest(Request.Method.POST, this, API_LINK, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    closeProgress();
                    try {
                        if(jsonObject.getBoolean("result")) {
                            setResult(RESULT_OK);
                            finish(NewAdviceActivity.this);
                        }else {
                            showAlertDialog(jsonObject.getString("msg"));
                        }

                    }catch (Exception e){

                    }
                }
            }, new Response.ErrorListener() {
                @SuppressLint("WrongConstant")
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    showToast("File upload failed");
                    closeProgress();
                }

            }, post, params, mHeaderPart,imageTitle);
            mCustomRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(mCustomRequest, API_LINK);

        } catch (Exception e) {

            e.printStackTrace();
            closeProgress();
            showAlertDialog("Photo Upload is failed . Please try again.");
        }
    }

    void selectImage(int posstion){
        if(posstion==maxImagecount){
            SelectMediaDialog selectMediaActionDialog = new SelectMediaDialog();
            selectMediaActionDialog.setOnActionClick(new SelectMediaDialog.OnActionListener() {
                @Override
                public void OnCamera() {
                    if (Commons.g_user.getAccount_type() == 1) {
                        business_user = true;
                        maxImagecount = 9;
                        initLayout();
                    }else {
                        goTo(NewAdviceActivity.this, UpdateBusinessActivity.class,false);
                    }
                }

                @Override
                public void OnAlbum() {

                }
            },getResources().getString(R.string.upload3image),getResources().getString(R.string.yes),getResources().getString(R.string.no));
            selectMediaActionDialog.show(getSupportFragmentManager(), "action picker");
        }else {
            SelectMediaDialog selectMediaActionDialog = new SelectMediaDialog();
            selectMediaActionDialog.setOnActionClick(new SelectMediaDialog.OnActionListener() {
                @Override
                public void OnCamera() {
                    if(completedValue.size()==maxImagecount)return;
                    Options options = Options.init()
                            .setRequestCode(100)                                           //Request code for activity results
                            .setCount(maxImagecount-completedValue.size())                                                   //Number of images to restict selection count
                            .setFrontfacing(false)                                         //Front Facing camera on start
                            .setPreSelectedUrls(returnValue)                               //Pre selected Image Urls
                            .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                            .setMode(Options.Mode.Picture)                                     //Option to select only pictures or videos or both
                            .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                            .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                            .setPath("/pix/images");                                       //Custom Path For media Storage

                    Pix.start(NewAdviceActivity.this, options);
                }

                @Override
                public void OnAlbum() {
                    if(completedValue.size()>posstion)
                        completedValue.remove(posstion);
                    reloadImages();

                }
            },getResources().getString(R.string.what_wouldlike),getResources().getString(R.string.add_media),getResources().getString(R.string.remove_media));
            selectMediaActionDialog.show(getSupportFragmentManager(), "action picker");
        }
    }
    void selectVideo(){

        Options options = Options.init()
                .setRequestCode(200)                                           //Request code for activity results
                .setCount(1)                                                   //Number of images to restict selection count
                .setFrontfacing(false)                                         //Front Facing camera on start
                .setPreSelectedUrls(returnValue)                               //Pre selected Image Urls
                .setSpanCount(4)                                               //Span count for gallery min 1 & max 5
                .setMode(Options.Mode.Video)                                     //Option to select only pictures or videos or both
                .setVideoDurationLimitinSeconds(30)                            //Duration for video recording
                .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)     //Orientaion
                .setPath("/pix/images");                                       //Custom Path For media Storage

        Pix.start(this, options);
    }


    void Keyboard(){
        ScrollView lytContainer = (ScrollView) findViewById(R.id.lyt_container);
        lytContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_title.getWindowToken(), 0);
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            if(completedValue.size()>maxImagecount)return;

            completedValue.addAll(returnValue);
            reloadImages();
        }else  if(resultCode == Activity.RESULT_OK && requestCode == 200) {
            ArrayList<String> returnValue = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
            videovalue = returnValue.get(0);
            reloadVideo();
        }else if(resultCode == Commons.subscription_code){
            business_user = true;
            maxImagecount = 9;
            initLayout();
        }
    }

    void reloadImages(){
        imageViews.get(0).setImageResource(0);
        for(int i =1;i<imageViews.size();i++)imageViews.get(i).setImageResource(R.drawable.icon_image1);
        if(completedValue.size()>0)imv_imageicon.setVisibility(View.GONE);
        else imv_imageicon.setVisibility(View.VISIBLE);
        for(int i =0;i<completedValue.size();i++){
            Glide.with(this).load(completedValue.get(i)).placeholder(R.drawable.icon_image1).dontAnimate().into(imageViews.get(i));

        }
    }
    void reloadVideo(){
        //uploadThumbImage = Helper.getThumbnailPathForLocalFile(this, videovalue);

        Glide.with(this).load(videovalue).placeholder(R.drawable.image_thumnail).dontAnimate().into(imv_videothumnail);

        imv_videoicon.setImageDrawable(getResources().getDrawable(R.drawable.icon_player));

    }


}