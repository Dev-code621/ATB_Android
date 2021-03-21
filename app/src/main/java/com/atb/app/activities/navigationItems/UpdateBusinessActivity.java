package com.atb.app.activities.navigationItems;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;
import com.atb.app.activities.LoginActivity;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.register.ProfileSetActivity;
import com.atb.app.activities.register.Signup1Activity;
import com.atb.app.adapter.InsuranceAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.commons.Helper;
import com.atb.app.dialog.AddInsuranceDialog;
import com.atb.app.dialog.PickImageDialog;
import com.atb.app.dialog.SelectMediaDialog;
import com.atb.app.model.BusinessModel;
import com.atb.app.model.submodel.InsuranceModel;
import com.atb.app.model.submodel.SocialModel;
import com.atb.app.util.ImageUtils;
import com.atb.app.util.MediaPicker;
import com.atb.app.util.MultiPartRequest;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateBusinessActivity extends CommonActivity implements View.OnClickListener , ImageUtils.ImageAttachmentListener{
    ImageView imv_back,imv_profile,imv_fb_selector,imv_instagram_selector,imv_twitter_selector;
    EditText edt_business_name,edt_yourwebsite,edt_tell_us,edt_instagram_name,edt_twitter_name,edt_tag;
    RelativeLayout lyt_setoperation_hour;
    TextView txv_setoperate;
    LinearLayout lyt_add_ceritfication,lyt_addinsurance,lyt_facebook,lyt_instgram,lyt_instagram_link,lyt_twitter,
            lyt_twitter_link,lyt_instagram_content,lyt_twitter_content,lyt_save;
    BusinessModel businessModel = new BusinessModel();
    ListView list_insurance,list_certification;
    InsuranceAdapter insuranceAdapter;
    InsuranceAdapter certiAdapter;
    ArrayList<InsuranceModel> insuranceModels = new ArrayList<>();
    ArrayList<InsuranceModel>qualifications = new ArrayList<>();
    private ImageUtils imageUtils;
    String photoPath = "",insuranceFile_path = "";
    boolean insurance_camera = false;
    AddInsuranceDialog addInsuranceDialog;
    boolean twitter_connect = false,instagram_connect = false, facebook_connect =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_business);
        imv_back = findViewById(R.id.imv_back);
        imv_profile = findViewById(R.id.imv_profile);
        imv_fb_selector = findViewById(R.id.imv_fb_selector);
        imv_instagram_selector = findViewById(R.id.imv_instagram_selector);
        imv_twitter_selector = findViewById(R.id.imv_twitter_selector);
        edt_business_name = findViewById(R.id.edt_business_name);
        edt_yourwebsite = findViewById(R.id.edt_yourwebsite);
        edt_tell_us = findViewById(R.id.edt_tell_us);
        edt_instagram_name = findViewById(R.id.edt_instagram_name);
        edt_twitter_name = findViewById(R.id.edt_twitter_name);
        edt_tag = findViewById(R.id.edt_tag);
        lyt_setoperation_hour = findViewById(R.id.lyt_setoperation_hour);
        txv_setoperate = findViewById(R.id.txv_setoperate);
        lyt_save = findViewById(R.id.lyt_save);
        lyt_add_ceritfication = findViewById(R.id.lyt_add_ceritfication);
        lyt_addinsurance = findViewById(R.id.lyt_addinsurance);
        lyt_facebook = findViewById(R.id.lyt_facebook);
        lyt_instgram = findViewById(R.id.lyt_instgram);
        lyt_instagram_link = findViewById(R.id.lyt_instagram_link);
        lyt_twitter = findViewById(R.id.lyt_twitter);
        lyt_twitter_link = findViewById(R.id.lyt_twitter_link);
        lyt_instagram_content = findViewById(R.id.lyt_instagram_content);
        lyt_twitter_content = findViewById(R.id.lyt_twitter_content);
        list_certification = findViewById(R.id.list_certification);
        list_insurance = findViewById(R.id.list_insurance);

        imv_back.setOnClickListener(this);
        imv_profile.setOnClickListener(this);
        lyt_setoperation_hour.setOnClickListener(this);
        lyt_add_ceritfication.setOnClickListener(this);
        lyt_addinsurance.setOnClickListener(this);
        lyt_facebook.setOnClickListener(this);
        lyt_twitter.setOnClickListener(this);
        lyt_instgram.setOnClickListener(this);
        lyt_instagram_link.setOnClickListener(this);
        lyt_twitter_link.setOnClickListener(this);
        lyt_save.setOnClickListener(this);
        insuranceAdapter = new InsuranceAdapter(this,0);
        list_insurance.setAdapter(insuranceAdapter);
        certiAdapter = new InsuranceAdapter(this,1);
        list_certification.setAdapter(certiAdapter);
        imageUtils = new ImageUtils(this);
        list_certification.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                insurance_camera = true;
                insuranceFile_path = "";
                addInsuranceDialog = new AddInsuranceDialog(1,qualifications.get(position));
                addInsuranceDialog.setOnConfirmListener(new AddInsuranceDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm(String company, String number, String date) {
                        addInsurance(company,number, date,1,position);
                    }
                    @Override
                    public void onFileSelect() {
                        selectInsuranceFile();
                    }
                });
                addInsuranceDialog.show(getSupportFragmentManager(), "action picker");

            }
        });

        list_insurance.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                insurance_camera = true;
                insuranceFile_path = "";
                addInsuranceDialog = new AddInsuranceDialog(0,insuranceModels.get(position));
                addInsuranceDialog.setOnConfirmListener(new AddInsuranceDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm(String company, String number, String date) {
                        addInsurance(company,number, date,0,position);
                    }
                    @Override
                    public void onFileSelect() {
                        selectInsuranceFile();
                    }
                });
                addInsuranceDialog.show(getSupportFragmentManager(), "action picker");
            }
        });
        initLayout();

    }

    void initLayout(){
        if(Commons.g_user.getAccount_type()==1){
            businessModel = Commons.g_user.getBusinessModel();
            Glide.with(this).load(businessModel.getBusiness_logo()).placeholder(R.drawable.star2).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(this, 500, Commons.glide_magin, "#FFFFFF", Commons.glide_boder))).into(imv_profile);
            edt_business_name.setText(businessModel.getBusiness_name());
            edt_yourwebsite.setText(businessModel.getBusiness_website());
            edt_tell_us.setText(businessModel.getBusiness_bio());
            if(businessModel.getOpeningTimeModels().size()>0 || businessModel.getHolidayModels().size()>0)
                txv_setoperate.setVisibility(View.GONE);
            loadingQalification_Insurance();
           initSocialPart();
        }
    }
    void initSocialPart(){
        lyt_twitter_content.setVisibility(View.GONE);
        lyt_instagram_content.setVisibility(View.GONE);
        edt_instagram_name.setEnabled(true);
        edt_twitter_name.setEnabled(true);
        lyt_twitter_link.setVisibility(View.VISIBLE);
        lyt_instagram_link.setVisibility(View.VISIBLE);
        edt_twitter_name.setText("");
        edt_instagram_name.setText("");
        for(int i =0;i<businessModel.getSocialModels().size();i++){
            SocialModel socialModel = businessModel.getSocialModels().get(i);
            if(socialModel.getType() == 0){
                imv_fb_selector.setEnabled(false);
                facebook_connect = true;
            }else if(socialModel.getType() ==1){
                imv_instagram_selector.setEnabled(false);
                edt_instagram_name.setText(socialModel.getSocial_name());
                lyt_instagram_content.setVisibility(View.VISIBLE);
                lyt_instagram_link.setVisibility(View.GONE);
                edt_instagram_name.setEnabled(false);
                instagram_connect = true;

            }else {
                imv_twitter_selector.setEnabled(false);
                edt_twitter_name.setText(socialModel.getSocial_name());
                lyt_twitter_content.setVisibility(View.VISIBLE);
                lyt_twitter_link.setVisibility(View.GONE);
                edt_twitter_name.setEnabled(false);
                twitter_connect = true;
            }
        }
    }

    public void deleteInsurance(int posstion, int type){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.DELETE_SERVICE_FILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                if(type==0){
                                    insuranceModels.remove(posstion);
                                    insuranceAdapter.setRoomData(insuranceModels);
                                    Helper.getListViewSize(list_insurance);
                                }else {
                                    qualifications.remove(posstion);
                                    certiAdapter.setRoomData(qualifications);
                                    Helper.getListViewSize(list_certification);
                                }
                            }
                        }catch (Exception e){

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
                if(type ==0)
                    params.put("id", String.valueOf(insuranceModels.get(posstion).getId()));
                else
                    params.put("id", String.valueOf(qualifications.get(posstion).getId()));
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
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
                            insuranceAdapter.setRoomData(insuranceModels);
                            Helper.getListViewSize(list_insurance);
                            certiAdapter.setRoomData(qualifications);
                            Helper.getListViewSize(list_certification);

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

    void addInsurance(String comapny, String number, String date,int type, int posstion){
        showProgress();
        try {
            String api_link = API.ADD_SERVICE_FILE;
            File file = null;
            if(insuranceFile_path.length()>0)
                file = new File(insuranceFile_path);
            Map<String, String> params = new HashMap<>();
            params.put("token", Commons.token);
            params.put("type", String.valueOf(type));
            params.put("company", comapny);
            params.put("reference",number);
            params.put("expiry", date);
            if(posstion >=0){
                api_link = API.UPDATE_SERVICE_FILE;
                if(type == 0){
                     params.put("id", String.valueOf(insuranceModels.get(posstion).getId()));

                }else
                    params.put("id", String.valueOf(qualifications.get(posstion).getId()));
            }
            MultiPartRequest reqMultiPart = new MultiPartRequest(api_link, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    closeProgress();
                }
            }, new Response.Listener<String>() {
                @Override
                public void onResponse(String json) {
                    closeProgress();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if(jsonObject.getBoolean("result")){
                            if(posstion <0) {
                                InsuranceModel insuranceModel = new InsuranceModel();
                                insuranceModel.setCompany(comapny);
                                insuranceModel.setReference(number);

                                insuranceModel.setExpiry(Commons.getDisplayDate2(date));
                                insuranceModel.setId(jsonObject.getInt("extra"));
                                insuranceModel.setFile(insuranceFile_path);
                                insuranceModel.setType(type);
                                insuranceModel.setUser_id(Commons.g_user.getId());
                                if (type == 0)
                                    insuranceModels.add(insuranceModel);
                                else
                                    qualifications.add(insuranceModel);
                                insuranceAdapter.setRoomData(insuranceModels);
                                Helper.getListViewSize(list_insurance);
                                certiAdapter.setRoomData(qualifications);
                                Helper.getListViewSize(list_certification);
                            }else {
                                if(type == 0){
                                    insuranceModels.get(posstion).setReference(number);
                                    insuranceModels.get(posstion).setExpiry(Commons.getDisplayDate2(date));
                                    insuranceModels.get(posstion).setCompany(comapny);
                                    if(insuranceFile_path.length()>0)
                                        insuranceModels.get(posstion).setFile(insuranceFile_path);
                                    insuranceAdapter.setRoomData(insuranceModels);
                                    Helper.getListViewSize(list_insurance);
                                }else {
                                    qualifications.get(posstion).setReference(number);
                                    qualifications.get(posstion).setExpiry(Commons.getDisplayDate2(date));
                                    qualifications.get(posstion).setCompany(comapny);
                                    if(insuranceFile_path.length()>0)
                                        qualifications.get(posstion).setFile(insuranceFile_path);
                                    certiAdapter.setRoomData(qualifications);
                                    Helper.getListViewSize(list_certification);
                                }
                            }
                        }
                    }catch (Exception e){

                    }
                }
            }, file, "service_files", params);
            reqMultiPart.setRetryPolicy(new DefaultRetryPolicy(
                    6000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(reqMultiPart, api_link);

        } catch (Exception e) {

            e.printStackTrace();
            closeProgress();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    void showInsuranceDialog(int type){
        insurance_camera = true;
        insuranceFile_path = "";
        addInsuranceDialog = new AddInsuranceDialog(type);
        addInsuranceDialog.setOnConfirmListener(new AddInsuranceDialog.OnConfirmListener() {
            @Override
            public void onConfirm(String company, String number, String date) {
                addInsurance(company,number, date,type,-1);
            }
            @Override
            public void onFileSelect() {
                selectInsuranceFile();
            }
        });
        addInsuranceDialog.show(getSupportFragmentManager(), "action picker");
    }

    void addSocial(int type){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.ADD_SOCIAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        Log.d("aaa",json);
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                SocialModel socialModel = new SocialModel();
                                socialModel.setType(type);
                                socialModel.setId(jsonObject.getInt("extra"));
                                socialModel.setUser_id(Commons.g_user.getId());
                                if(type ==0) {
                                    imv_fb_selector.setEnabled(!imv_fb_selector.isEnabled());
                                }
                                else if(type ==1) {
                                    imv_instagram_selector.setEnabled(!imv_instagram_selector.isEnabled());
                                    socialModel.setSocial_name(edt_instagram_name.getText().toString());
                                }else {
                                    imv_twitter_selector.setEnabled(!imv_twitter_selector.isEnabled());
                                    socialModel.setSocial_name(edt_twitter_name.getText().toString());
                                }
                                businessModel.getSocialModels().add(socialModel);
                                initSocialPart();
                            }
                        }catch (Exception e){

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        closeProgress();
                        Log.d("aaaa",error.toString());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", Commons.token);
                params.put("type", String.valueOf(type));
                if(type ==2) {
                    params.put("social_name", edt_twitter_name.getText().toString());
                }else if(type ==1 )
                    params.put("social_name", edt_instagram_name.getText().toString());
                else{

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

    void deleteSocial(int type){
        showProgress();

        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.REMOVE_SOCIAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                for(int i =0;i<businessModel.getSocialModels().size();i++){
                                    if(businessModel.getSocialModels().get(i).getType() == type){
                                        businessModel.getSocialModels().remove(i);
                                        break;
                                    }
                                }
                                if(type ==0) {
                                    imv_fb_selector.setEnabled(!imv_fb_selector.isEnabled());
                                }
                                else if(type ==1) {
                                    imv_instagram_selector.setEnabled(!imv_instagram_selector.isEnabled());
                                }else {
                                    imv_twitter_selector.setEnabled(!imv_twitter_selector.isEnabled());
                                }
                                initSocialPart();
                            }
                        }catch (Exception e){
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        closeProgress();
                        Log.d("aaaa",error.toString());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", Commons.token);
                params.put("type",String.valueOf(type));
                Log.d("aaaa",params.toString());
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }

    void  gotoSaveBusiness(){
        if(photoPath.length() ==0 && businessModel.getBusiness_logo().equals("null")){
            showAlertDialog("Please add your business logo.");
            return;
        }
        if(edt_business_name.getText().toString().length()==0){
            showAlertDialog("Please input business name.");
            return;
        }
        if(edt_yourwebsite.getText().toString().length()==0){
            showAlertDialog("Please input business website url.");
            return;
        }
        if(edt_tell_us.getText().toString().length() ==0){
            showAlertDialog("Please add your business infomation.");
            return;
        }
        String api_link = API.CREATE_BUSINESS_API;
        if(Commons.g_user.getAccount_type()==1)
            api_link = API.UPDATE_BUSINESS_API;

        showProgress();
        try {
            File file = null;
            if(photoPath.length()>0)
                file = new File(photoPath);
            Map<String, String> params = new HashMap<>();
            params.put("token", Commons.token);
            params.put("business_name",edt_business_name.getText().toString());
            params.put("business_website",edt_yourwebsite.getText().toString());
            params.put("business_profile_name",edt_business_name.getText().toString());
            params.put("business_bio",edt_tell_us.getText().toString());
            if(Commons.g_user.getAccount_type() == 1)
                params.put("id",String.valueOf(businessModel.getId()));
            MultiPartRequest reqMultiPart = new MultiPartRequest(api_link, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    closeProgress();
                }
            }, new Response.Listener<String>() {
                @Override
                public void onResponse(String json) {
                    closeProgress();
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if(jsonObject.getBoolean("result")){

                            businessModel.initModel(jsonObject.getJSONObject("extra"));
                            Commons.g_user.setBusinessModel(businessModel);

                            if(Commons.g_user.getAccount_type() == 1)
                                finish(UpdateBusinessActivity.this);
                            else {

                            }
                        }
                    }catch (Exception e){

                    }
                }
            }, file, "avatar", params);
            reqMultiPart.setRetryPolicy(new DefaultRetryPolicy(
                    6000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(reqMultiPart, api_link);

        } catch (Exception e) {

            e.printStackTrace();
            closeProgress();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_back:
                finish(this);
                break;
            case R.id.imv_profile:
                insurance_camera = false;
                selectProfilePicture();
                break;
            case R.id.lyt_setoperation_hour:

                goTo(this,SetOperatingActivity.class,false);
                break;
            case R.id.lyt_add_ceritfication:
                showInsuranceDialog(1);
                break;
            case R.id.lyt_addinsurance:
                showInsuranceDialog(0);
                break;
            case R.id.lyt_facebook:
                imv_fb_selector.setEnabled(!imv_fb_selector.isEnabled());
                //addSocial(0);
                break;
            case R.id.lyt_twitter:
                if(!imv_twitter_selector.isEnabled()){
                    deleteSocial(2);
                }else {
                    if(twitter_connect)
                     lyt_twitter_content.setVisibility(View.GONE);
                    else
                        lyt_twitter_content.setVisibility(View.VISIBLE);
                    twitter_connect = !twitter_connect;
                }
                break;
            case R.id.lyt_instgram:
                if(!imv_instagram_selector.isEnabled()){
                    deleteSocial(1);
                }else {
                    if(instagram_connect)
                        lyt_instagram_content.setVisibility(View.GONE);
                    else
                        lyt_instagram_content.setVisibility(View.VISIBLE);
                    instagram_connect = !instagram_connect;
                }
                break;
            case R.id.lyt_instagram_link:
                addSocial(1);
                break;
            case R.id.lyt_twitter_link:
                addSocial(2);
                break;
            case R.id.lyt_save:
                gotoSaveBusiness();
                break;
        }
    }

    void selectInsuranceFile(){
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(allPermissionsListener)
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                    }
                })
                .check();
    }
    void selectProfilePicture(){
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(allPermissionsListener_profile)
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                    }
                })
                .check();
    }

    private MultiplePermissionsListener allPermissionsListener = new MultiplePermissionsListener() {
        @Override
        public void onPermissionsChecked(MultiplePermissionsReport report) {
            if (report.areAllPermissionsGranted()) chooseFileSelect();
            else if (report.isAnyPermissionPermanentlyDenied()) {
                Snackbar snackbar = Snackbar
                        .make(imv_profile, "Storage permission is needed to choose pictures of your profile", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar snackbar1 = Snackbar.make(imv_profile, "Message is restored!", Snackbar.LENGTH_SHORT);
                                snackbar1.show();
                            }
                        });

                snackbar.show();
            }
        }

        @Override
        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
            token.continuePermissionRequest();
        }
    };

    private MultiplePermissionsListener allPermissionsListener_profile = new MultiplePermissionsListener() {
        @Override
        public void onPermissionsChecked(MultiplePermissionsReport report) {
            if (report.areAllPermissionsGranted()) chooseAction();
            else if (report.isAnyPermissionPermanentlyDenied()) {
                Snackbar snackbar = Snackbar
                        .make(imv_profile, "Storage permission is needed to choose pictures of your profile", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar snackbar1 = Snackbar.make(imv_profile, "Message is restored!", Snackbar.LENGTH_SHORT);
                                snackbar1.show();
                            }
                        });

                snackbar.show();
            }
        }

        @Override
        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
            token.continuePermissionRequest();
        }
    };

    void chooseFileSelect(){
        SelectMediaDialog selectMediaDialog = new SelectMediaDialog();
        selectMediaDialog.setOnActionClick(new SelectMediaDialog.OnActionListener() {
            @Override
            public void OnCamera() {
                MediaPicker mediaPicker = new MediaPicker(UpdateBusinessActivity.this);
                PickImageDialog pickImageDialog = new PickImageDialog();
                pickImageDialog.setImagePickListener(mediaPicker.getAllShownPDFPath(UpdateBusinessActivity.this), new PickImageDialog.OnImagePickListener() {
                    @Override
                    public void OnImagePick(String path) {
//                        Uri uri = Uri.fromFile(new File(path));
//
//                        Intent intent = CropImage.activity(uri)
//                                .setGuidelines(CropImageView.Guidelines.ON).setCropShape(CropImageView.CropShape.RECTANGLE).setAspectRatio(1, 1)
//                                .getIntent(UpdateBusinessActivity.this);
//
//                        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
                    }
                });
                pickImageDialog.show(getSupportFragmentManager(), "pick image");
                mediaPicker.chooseImage();
            }

            @Override
            public void OnAlbum() {
                imageUtils.camera_call();
            }
        },"Select File","Explore Files","Take a Picture");
        selectMediaDialog.show(getSupportFragmentManager(), "action picker");
    }

    private void chooseAction() {

        SelectMediaDialog selectMediaDialog = new SelectMediaDialog();
        selectMediaDialog.setOnActionClick(new SelectMediaDialog.OnActionListener() {
            @Override
            public void OnCamera() {
                imageUtils.camera_call();
            }

            @Override
            public void OnAlbum() {
                MediaPicker mediaPicker = new MediaPicker(UpdateBusinessActivity.this);
                PickImageDialog pickImageDialog = new PickImageDialog();
                pickImageDialog.setImagePickListener(mediaPicker.getAllShownImagesPath(UpdateBusinessActivity.this), new PickImageDialog.OnImagePickListener() {
                    @Override
                    public void OnImagePick(String path) {
                        Uri uri = Uri.fromFile(new File(path));

                        Intent intent = CropImage.activity(uri)
                                .setGuidelines(CropImageView.Guidelines.ON).setCropShape(CropImageView.CropShape.RECTANGLE).setAspectRatio(1, 1)
                                .getIntent(UpdateBusinessActivity.this);

                        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
                    }
                });
                pickImageDialog.show(getSupportFragmentManager(), "pick image");
                mediaPicker.chooseImage();
            }
        });
        selectMediaDialog.show(getSupportFragmentManager(), "action picker");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageUtils.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                final Uri resultUri = result.getUri();
                Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
                options.size = 500;
                File file = new File(Helper.getUriRealPathAboveKitkat(UpdateBusinessActivity.this, resultUri));
                if (imageUtils.getImage(file).getWidth() > 1024) {
                    options.width = 1024;
                    options.height = 1024;
                }
                imv_profile.setPadding(0, 0, 0, 0);
                imv_profile.setScaleType(ImageView.ScaleType.CENTER_CROP);
                if(!insurance_camera) {
                    Glide.with(this).load(resultUri).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                            new RoundedCornersTransformation(this, 500, Commons.glide_magin, "#FFFFFF", Commons.glide_boder))).into(imv_profile);
                    photoPath = resultUri.getPath();
                }else {
                    insuranceFile_path = resultUri.getPath();
                    String filename=insuranceFile_path.substring(insuranceFile_path.lastIndexOf("/")+1);
                    addInsuranceDialog.setFileName();

                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        Intent intent = CropImage.activity(uri)
                .setGuidelines(CropImageView.Guidelines.ON).setCropShape(CropImageView.CropShape.RECTANGLE).setInitialCropWindowPaddingRatio(0).setAspectRatio(1, 1)
                .getIntent(this);

        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);

    }
}