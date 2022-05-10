package com.atb.app.activities.navigationItems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.atb.app.R;
import com.atb.app.activities.LoginActivity;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.newpost.SelectPostCategoryActivity;
import com.atb.app.activities.register.ProfileSetActivity;
import com.atb.app.activities.register.Signup1Activity;
import com.atb.app.activities.register.forgotPassword.ForgotPasswordActivity;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.commons.Helper;
import com.atb.app.dialog.PickImageDialog;
import com.atb.app.dialog.SelectMediaDialog;
import com.atb.app.fragement.PostsFragment;
import com.atb.app.fragement.StoreFragment;
import com.atb.app.util.ImageUtils;
import com.atb.app.util.MediaPicker;
import com.atb.app.util.MultiPartRequest;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentStatePagerItemAdapter;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileCallback;

import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends CommonActivity  implements View.OnClickListener , ImageUtils.ImageAttachmentListener{
    LinearLayout lyt_back;
    ImageView imv_profile,imv_selector2,imv_selector1;
    FrameLayout lyt_addpicture;
    EditText edt_firstname,edt_lastname,edt_email;
    TextView txv_update,txv_male,txv_female,txv_location,edt_birthday,txv_resetPassword;
    String photoPath = "",birthday = "";
    int gender = 0 ;
    private ImageUtils imageUtils;
    String locationText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        lyt_back = findViewById(R.id.lyt_back);
        imv_profile = findViewById(R.id.imv_profile);
        imv_selector2 = findViewById(R.id.imv_selector2);
        imv_selector1 = findViewById(R.id.imv_selector1);
        lyt_addpicture = findViewById(R.id.lyt_addpicture);
        edt_firstname = findViewById(R.id.edt_firstname);
        edt_lastname = findViewById(R.id.edt_lastname);
        edt_email = findViewById(R.id.edt_email);
        txv_location = findViewById(R.id.txv_location);
        txv_male = findViewById(R.id.txv_male);
        txv_female = findViewById(R.id.txv_female);
        edt_birthday = findViewById(R.id.edt_birthday);
        txv_update = findViewById(R.id.txv_update);
        txv_resetPassword = findViewById(R.id.txv_resetPassword);
        lyt_back.setOnClickListener(this);
        txv_female.setOnClickListener(this);
        txv_male.setOnClickListener(this);
        txv_update.setOnClickListener(this);
        txv_location.setOnClickListener(this);
        edt_birthday.setOnClickListener(this);
        imv_profile.setOnClickListener(this);
        txv_resetPassword.setOnClickListener(this);
        Keyboard();
        imageUtils = new ImageUtils(this);
        initLayout();

    }

    void initLayout(){
        Glide.with(_context).load(Commons.g_user.getImvUrl()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                new RoundedCornersTransformation(_context, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
        edt_firstname.setText(Commons.g_user.getFirstname());
        edt_lastname.setText(Commons.g_user.getLastname());
        edt_email.setText(Commons.g_user.getEmail());
        txv_location.setText(Commons.getPostalCode(Commons.g_user.getLocation()));
        locationText = Commons.g_user.getLocation();
        if(Commons.g_user.getSex().equals("0")){
            imv_selector1.setVisibility(View.GONE);
            imv_selector2.setVisibility(View.VISIBLE);
            gender = 0;
        }else {
            imv_selector1.setVisibility(View.VISIBLE);
            imv_selector2.setVisibility(View.GONE);
            gender = 1;
        }
        birthday = Commons.g_user.getBirhtday();
        edt_birthday.setText(getDisplayDate(Commons.g_user.getBirhtday()));

    }
    String getDisplayDate(String date){
        String dayName = "";
        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date myDate = inFormat.parse(date);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
            dayName=simpleDateFormat.format(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  dayName;
    }
    void Keyboard(){
        LinearLayout lytContainer = (LinearLayout) findViewById(R.id.lyt_container);
        lytContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_email.getWindowToken(), 0);
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
            case R.id.txv_female:
                gender = 0;
                imv_selector1.setVisibility(View.GONE);
                imv_selector2.setVisibility(View.VISIBLE);
                break;
            case R.id.txv_male:
                gender = 1;
                imv_selector1.setVisibility(View.VISIBLE);
                imv_selector2.setVisibility(View.GONE);
                break;
            case R.id.txv_update:
                updateProfile();
                break;
            case R.id.txv_location:
                startActivityForResult(new Intent(this, SetPostRangeActivity.class),1);
                overridePendingTransition(0, 0);
                break;
            case R.id.edt_birthday:
                SelectDay();
                break;
            case R.id.imv_profile:
                uploadPicture();
                break;
            case R.id.txv_resetPassword:
                goTo(this, ForgotPasswordActivity.class,false);
                break;

        }
    }

    void updateProfile(){
        showProgress();
        try {
            File file = null;
            if(photoPath.length()>0)
            file = new File(photoPath);
            Map<String, String> params = new HashMap<>();
            params.put("token", Commons.token);
            params.put("user_email", edt_email.getText().toString());
            params.put("first_name", edt_firstname.getText().toString());
            params.put("last_name", edt_lastname.getText().toString());
            params.put("country", locationText);
            params.put("birthday", birthday);
            params.put("gender", String.valueOf(gender));


            MultiPartRequest reqMultiPart = new MultiPartRequest(API.UPDATE_PROFILE_API, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    closeProgress();
// Toast.makeText(Signup2Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
// showAlertDialog("Photo Upload is failed. Please try again.");
                }
            }, new Response.Listener<String>() {
                @Override
                public void onResponse(String json) {
                    closeProgress();
                    try{
                        JSONObject jsonObject = new JSONObject(json);
                        if(jsonObject.getBoolean("result")){
                            JSONObject profile = jsonObject.getJSONObject("msg");
                            Commons.g_user.setImvUrl(profile.getString("pic_url"));
                            Commons.g_user.setFirstname(edt_firstname.getText().toString());
                            Commons.g_user.setLastname(edt_lastname.getText().toString());
                            Commons.g_user.setEmail(edt_email.getText().toString());
                            Commons.g_user.setLocation(locationText);
                            Commons.g_user.setSex(String.valueOf(gender));
                            Commons.g_user.setBirhtday(birthday);
                            finish(ProfileActivity.this);
                        }

                    }catch (Exception e){

                    }

                }
            }, file, "pic", params);
            reqMultiPart.setRetryPolicy(new DefaultRetryPolicy(
                    6000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(reqMultiPart, API.UPDATE_PROFILE_API);

        } catch (Exception e) {

            e.printStackTrace();
            closeProgress();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    void SelectDay(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener mDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker , int year , int month , int day) {
                month = month + 1;
                Log.d( "onDateSet" , month + "/" + day + "/" + year );
                edt_birthday.setText(String.valueOf(day)+ " " + Commons.Months[month-1]+ " " + year);
                cal.set(year,month-1,day);
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                birthday = format.format(cal.getTime());

            }
        };
        DatePickerDialog dialog =  new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateListener,
                year,month,day
        );
        dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
        dialog.show();


    }
    void uploadPicture(){
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

    private void chooseAction() {

        SelectMediaDialog selectMediaDialog = new SelectMediaDialog();
        selectMediaDialog.setOnActionClick(new SelectMediaDialog.OnActionListener() {
            @Override
            public void OnCamera() {
                imageUtils.camera_call();
            }

            @Override
            public void OnAlbum() {
                MediaPicker mediaPicker = new MediaPicker(ProfileActivity.this);
                PickImageDialog pickImageDialog = new PickImageDialog();
                pickImageDialog.setImagePickListener(mediaPicker.getAllShownImagesPath(ProfileActivity.this), new PickImageDialog.OnImagePickListener() {
                    @Override
                    public void OnImagePick(String path) {
                        Uri uri = Uri.fromFile(new File(path));

                        Intent intent = CropImage.activity(uri)
                                .setGuidelines(CropImageView.Guidelines.ON).setCropShape(CropImageView.CropShape.RECTANGLE).setAspectRatio(1, 1)
                                .getIntent(ProfileActivity.this);

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
                File file = new File(Helper.getUriRealPathAboveKitkat(ProfileActivity.this, resultUri));
                if (imageUtils.getImage(file).getWidth() > 1024) {
                    options.width = 1024;
                    options.height = 1024;
                }

                //                  Picasso.get().load(resultUri).resize(200, 200).centerCrop().into(iv_profile);
                //                    iv_profile.setImageURI(resultUri);
                imv_profile.setPadding(0, 0, 0, 0);
                imv_profile.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(this).load(resultUri).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                        new RoundedCornersTransformation(_context, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
                photoPath = resultUri.getPath();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else if(resultCode == Commons.location_code){

            txv_location.setText(Commons.getPostalCode(Commons.location));
            locationText = Commons.location;

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