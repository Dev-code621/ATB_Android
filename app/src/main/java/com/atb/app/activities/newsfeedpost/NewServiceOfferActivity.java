package com.atb.app.activities.newsfeedpost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;
import com.atb.app.activities.navigationItems.SetPostRangeActivity;
import com.atb.app.activities.navigationItems.UpdateBusinessActivity;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.dialog.SelectInsuranceDialog;
import com.atb.app.dialog.SelectMediaDialog;
import com.atb.app.model.submodel.InsuranceModel;
import com.bumptech.glide.Glide;
import com.fxn.pix.Options;
import com.fxn.pix.Pix;
import com.zcw.togglebutton.ToggleButton;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewServiceOfferActivity extends CommonActivity implements View.OnClickListener {

    LinearLayout lyt_back,lyt_image,lyt_deposit;
    FrameLayout lyt_video,lyt_profile;
    NiceSpinner spiner_media_type,spiner_category_type;
    ImageView imv_videothumnail,imv_videoicon,imv_imageicon,imv_profile;
    EditText edt_title,edt_description,edt_price,edt_deposit;
    TextView txv_location;
    ArrayList<ImageView> imageViews = new ArrayList<>();
    TextView txv_minus,txv_plus,txt_cancelday,txv_post;
    ToggleButton toggle_quality,toggle_insurance,toggle_cash,toggle_paypal,toggle_deposit;
    int media_type = 1;
    ArrayList<String>returnValue = new ArrayList<>();
    ArrayList<String>completedValue = new ArrayList<>();
    ArrayList<InsuranceModel>insuranceModels = new ArrayList<>();
    ArrayList<InsuranceModel>qualifications = new ArrayList<>();

    TextView txv_quality,txv_quality_name,txv_quality_time,txv_insurance_time,txv_insurance_name,txv_insurance;
    LinearLayout lyt_qualitfied,lyt_qualitfied_minus,lyt_qualitfied_plus,lyt_insurance_plus,lyt_insurance_minus,lyt_insurance;
    String videovalue ="";
    int maxImagecount = 9;
     int insurance_id = -1,qualitfication_id = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_service_offer);
        txv_quality = findViewById(R.id.txv_quality);
        txv_quality_name = findViewById(R.id.txv_quality_name);
        txv_quality_time = findViewById(R.id.txv_quality_time);
        txv_insurance_time = findViewById(R.id.txv_insurance_time);
        txv_insurance_name = findViewById(R.id.txv_insurance_name);
        txv_insurance = findViewById(R.id.txv_insurance);
        lyt_qualitfied = findViewById(R.id.lyt_qualitfied);
        lyt_qualitfied_minus = findViewById(R.id.lyt_qualitfied_minus);
        lyt_qualitfied_plus = findViewById(R.id.lyt_qualitfied_plus);
        lyt_insurance_plus = findViewById(R.id.lyt_insurance_plus);
        lyt_insurance_minus = findViewById(R.id.lyt_insurance_minus);
        lyt_insurance = findViewById(R.id.lyt_insurance);
        spiner_media_type = findViewById(R.id.spiner_media_type);
        spiner_category_type = findViewById(R.id.spiner_category_type);
        lyt_back = findViewById(R.id.lyt_back);
        imv_videothumnail = findViewById(R.id.imv_videothumnail);
        imv_videoicon = findViewById(R.id.imv_videoicon);
        lyt_video = findViewById(R.id.lyt_video);
        imageViews.add(findViewById(R.id.imv_image));
        imageViews.add(findViewById(R.id.imv_image1));
        imageViews.add(findViewById(R.id.imv_image2));
        imageViews.add(findViewById(R.id.imv_image3));
        imageViews.add(findViewById(R.id.imv_image4));
        imageViews.add(findViewById(R.id.imv_image5));
        imageViews.add(findViewById(R.id.imv_image6));
        imageViews.add(findViewById(R.id.imv_image7));
        imageViews.add(findViewById(R.id.imv_image8));
        lyt_deposit = findViewById(R.id.lyt_deposit);
        lyt_image = findViewById(R.id.lyt_image);
        edt_title = findViewById(R.id.edt_title);
        edt_description = findViewById(R.id.edt_description);
        lyt_profile = findViewById(R.id.lyt_profile);
        edt_price = findViewById(R.id.edt_price);
        edt_deposit = findViewById(R.id.edt_deposit);
        txv_location = findViewById(R.id.txv_location);
        txv_minus = findViewById(R.id.txv_minus);
        txv_plus = findViewById(R.id.txv_plus);
        txt_cancelday = findViewById(R.id.txt_cancelday);
        txv_post = findViewById(R.id.txv_post);
        toggle_quality = findViewById(R.id.toggle_quality);
        toggle_insurance = findViewById(R.id.toggle_insurance);
        toggle_cash = findViewById(R.id.toggle_cash);
        toggle_paypal = findViewById(R.id.toggle_paypal);
        imv_profile = findViewById(R.id.imv_profile);
        imv_imageicon = findViewById(R.id.imv_imageicon);
        toggle_deposit = findViewById(R.id.toggle_deposit);

        imv_videothumnail.setOnClickListener(this);
        lyt_back.setOnClickListener(this);
        txv_post.setOnClickListener(this);
        txv_location. setOnClickListener(this);
        lyt_qualitfied_minus.setOnClickListener(this);
        lyt_insurance_minus.setOnClickListener(this);
        lyt_qualitfied_plus.setOnClickListener(this);
        lyt_insurance_plus.setOnClickListener(this);
        spiner_media_type.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                // This example uses String, but your type can be any
                String item = String.valueOf(parent.getItemAtPosition(position));
                media_type = position+1;
                if(position==0){
                    lyt_image.setVisibility(View.VISIBLE);
                    lyt_video.setVisibility(View.GONE);

                }else{
                    lyt_image.setVisibility(View.GONE);
                    lyt_video.setVisibility(View.VISIBLE);
                }
            }
        });

        toggle_deposit.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if(on)lyt_deposit.setVisibility(View.VISIBLE);
                else lyt_deposit.setVisibility(View.GONE);
            }
        });
        toggle_quality.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
               if(on){
                   if(qualifications.size()==0){
                       showAlertDialog("You don't have any qualification to add");
                       toggle_quality.setToggleOff();
                       return;
                   }

                   selectInsuranceDialog(1);

               }else {
                    qualitfication_id = -1;
               }
                initLayout();
            }
        });
        toggle_insurance.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if(on){
                    if(qualifications.size()==0){
                        showAlertDialog("You don't have any insurance to add");
                        toggle_insurance.setToggleOff();
                        return;
                    }
                    selectInsuranceDialog(0);
                }else {
                    insurance_id = -1;
                }
                initLayout();
            }
        });

        Keyboard();
        for(int i =0;i<imageViews.size();i++){
            int finalI = i;
            imageViews.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage(finalI);
                }
            });
        }
        loadingQalification_Insurance();
        initLayout();
    }
    void initLayout(){
        Glide.with(this).load(Commons.g_user.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.icon_image1).dontAnimate().into(imv_profile);
        if(qualitfication_id ==-1 ){
            txv_quality.setVisibility(View.VISIBLE);
            lyt_qualitfied.setVisibility(View.GONE);
        }else {
            txv_quality.setVisibility(View.GONE);
            lyt_qualitfied.setVisibility(View.VISIBLE);
            txv_quality_name.setText(qualifications.get(qualitfication_id).getCompany() + " " + qualifications.get(qualitfication_id).getReference() );
            txv_quality_time.setText("Qualified Since " + qualifications.get(qualitfication_id).getExpiry());
        }
        if(insurance_id ==-1 ){
            txv_insurance.setVisibility(View.VISIBLE);
            lyt_insurance.setVisibility(View.GONE);
        }else {
            txv_insurance.setVisibility(View.GONE);
            lyt_insurance.setVisibility(View.VISIBLE);
            txv_insurance_name.setText(qualifications.get(qualitfication_id).getCompany() + " " + qualifications.get(qualitfication_id).getReference() );
            txv_insurance_time.setText("Expires" + qualifications.get(qualitfication_id).getExpiry());
        }
    }


    void selectInsuranceDialog(int type){
        ArrayList<InsuranceModel>arrayList = insuranceModels;
        if(type == 1)arrayList = qualifications;
        SelectInsuranceDialog selectInsuranceDialog = new SelectInsuranceDialog();
        selectInsuranceDialog.setCancelable(false);
        selectInsuranceDialog.setOnActionClick(new SelectInsuranceDialog.OnActionListener() {
            @Override
            public void OnSelect(int posstion) {
                if(type ==0)
                    insurance_id = posstion;
                else
                    qualitfication_id = posstion;
                initLayout();
            }
        },arrayList);
        selectInsuranceDialog.show(getSupportFragmentManager(), "action picker");
    }
    void loadingQalification_Insurance(){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GETSERVICEFILES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            JSONArray jsonArray = jsonObject.getJSONArray("extra");
                            for(int i =0;i<jsonArray.length();i++){
                                InsuranceModel insuranceModel = new InsuranceModel();
                                insuranceModel.initModel(jsonArray.getJSONObject(i));

                                if(insuranceModel.getType()==0)
                                    insuranceModels.add(insuranceModel);
                                else
                                    qualifications.add(insuranceModel);
                            }

                        }catch (Exception e){
                            Log.d("aaaaaa",e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        closeProgress();
                        showToast(error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", Commons.token);
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lyt_back:
                finish(this);
                break;
            case R.id.txv_post:
                finish(this);
                break;
            case R.id.imv_videothumnail:
                videovalue = "";
                selectVideo();
                break;
            case R.id.txv_location:
                startActivityForResult(new Intent(this, SetPostRangeActivity.class),1);
                overridePendingTransition(0, 0);
                break;
            case R.id.lyt_insurance_plus:
               selectInsuranceDialog(0);
                break;
            case R.id.lyt_qualitfied_plus:
                selectInsuranceDialog(1);
                break;
            case R.id.lyt_insurance_minus:
                toggle_insurance.setToggleOff();
                insurance_id = -1;
                initLayout();
                break;
            case R.id.lyt_qualitfied_minus:
                toggle_quality.setToggleOff();
                qualitfication_id = -1;
                initLayout();
                break;
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


    void selectImage(int posstion){
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

                Pix.start(NewServiceOfferActivity.this, options);
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
        }else if(resultCode == Commons.location_code){
            txv_location.setText(Commons.g_user.getLocation());
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