package com.atb.app.activities.navigationItems;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.atb.app.util.ImageUtils;
import com.atb.app.util.MediaPicker;
import com.bumptech.glide.Glide;
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
    String photoPath = "";
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
        initLayout();

    }

    void initLayout(){
        if(Commons.g_user.getAccount_type()==1){
            businessModel = Commons.g_user.getBusinessModel();
            Glide.with(this).load(businessModel.getBusiness_logo()).placeholder(R.drawable.star2).dontAnimate().into(imv_profile);
            edt_business_name.setText(businessModel.getBusiness_name());
            edt_yourwebsite.setText(businessModel.getBusiness_website());
            edt_tell_us.setText(businessModel.getBusiness_bio());
            if(businessModel.getOpeningTimeModels().size()>0 || businessModel.getHolidayModels().size()>0)
                txv_setoperate.setVisibility(View.GONE);
            loadingQalification_Insurance();
        }
    }

    public void deleteHoliday(int posstion, int type){
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_back:
                finish(this);
                break;
            case R.id.imv_profile:
                selectProfilePicture();
                break;
            case R.id.lyt_setoperation_hour:
                goTo(this,SetOperatingActivity.class,false);
                break;
            case R.id.lyt_add_ceritfication:
                AddInsuranceDialog addInsuranceDialog = new AddInsuranceDialog(1);
                addInsuranceDialog.setOnConfirmListener(new AddInsuranceDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm(String comapny, String number, long date) {

                    }
                    @Override
                    public void onFileSelect() {
                        selectInsuranceFile();
                    }
                });
                addInsuranceDialog.show(getSupportFragmentManager(), "action picker");
                break;
            case R.id.lyt_addinsurance:
                addInsuranceDialog = new AddInsuranceDialog(0);
                addInsuranceDialog.setOnConfirmListener(new AddInsuranceDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm(String comapny, String number, long date) {

                    }
                    @Override
                    public void onFileSelect() {
                        selectInsuranceFile();
                    }
                });
                addInsuranceDialog.show(getSupportFragmentManager(), "action picker");
                break;
            case R.id.lyt_facebook:
                imv_fb_selector.setEnabled(!imv_fb_selector.isEnabled());
                break;
            case R.id.lyt_twitter:
                if(imv_twitter_selector.isEnabled())
                    lyt_twitter_content.setVisibility(View.VISIBLE);
                break;
            case R.id.lyt_instgram:
                if(imv_instagram_selector.isEnabled())
                    lyt_instagram_content.setVisibility(View.VISIBLE);

                break;
            case R.id.lyt_instagram_link:
                imv_instagram_selector.setEnabled(!imv_instagram_selector.isEnabled());
                break;
            case R.id.lyt_twitter_link:
                imv_twitter_selector.setEnabled(!imv_twitter_selector.isEnabled());
                break;
            case R.id.lyt_save:
                finish(this);
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
                Glide.with(this).load(resultUri).placeholder(R.drawable.profile_pic).dontAnimate().into(imv_profile);
                photoPath = resultUri.getPath();

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