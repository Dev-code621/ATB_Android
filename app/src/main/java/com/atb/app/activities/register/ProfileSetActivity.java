package com.atb.app.activities.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.transition.Scene;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.atb.app.R;
import com.atb.app.activities.LoginActivity;
import com.atb.app.activities.SplashActivity;
import com.atb.app.activities.navigationItems.SetPostRangeActivity;
import com.atb.app.activities.navigationItems.booking.CreateABookingActivity;
import com.atb.app.activities.navigationItems.booking.CreateBooking2Activity;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.commons.Constants;
import com.atb.app.commons.Helper;
import com.atb.app.dialog.PickImageDialog;
import com.atb.app.dialog.SelectMediaDialog;
import com.atb.app.model.UserModel;
import com.atb.app.util.ImageUtils;
import com.atb.app.util.MediaPicker;
import com.atb.app.util.MultiPartRequest;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.service.Common;
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

import org.json.JSONObject;

import java.io.File;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProfileSetActivity extends CommonActivity implements View.OnClickListener , ImageUtils.ImageAttachmentListener,TextWatcher{
    ViewGroup sceneRoot;
    Scene aScene,anotherScene;
    SimpleDraweeView imv_profile;
    EditText edt_username,edt_firstname,edt_lastname,edt_invitecode;
    TextView txv_male,txv_female,txv_next,txv_location,edt_birthday;
    ImageView imv_selector2,imv_selector1;
    FrameLayout lyt_paste;
    private ImageUtils imageUtils;
    private File profileImage;
    String photoPath = "",birthday = "";
    int gender = 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_set);
        sceneRoot = (ViewGroup) findViewById(R.id.scene_root);
        aScene = Scene.getSceneForLayout(sceneRoot, R.layout.signup_animation1, this);
        anotherScene =
                Scene.getSceneForLayout(sceneRoot, R.layout.layout_complete_profile, this);

        activityAnimation(aScene,R.id.lyt_container);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activityAnimation(anotherScene,R.id.lyt_container);
                loadLayout();
                Keyboard();

            }
        }, 1000);
        imageUtils = new ImageUtils(this);
    }

    void loadLayout(){
        imv_profile = sceneRoot.findViewById(R.id.imv_profile);
        edt_username = sceneRoot.findViewById(R.id.edt_username);
        edt_firstname = sceneRoot.findViewById(R.id.edt_firstname);
        edt_lastname = sceneRoot.findViewById(R.id.edt_lastname);
        txv_location = sceneRoot.findViewById(R.id.txv_location);
        edt_birthday = sceneRoot.findViewById(R.id.edt_birthday);
        edt_invitecode = sceneRoot.findViewById(R.id.edt_invitecode);
        txv_male = sceneRoot.findViewById(R.id.txv_male);
        txv_female = sceneRoot.findViewById(R.id.txv_female);
        imv_selector2 = sceneRoot.findViewById(R.id.imv_selector2);
        imv_selector1 = sceneRoot.findViewById(R.id.imv_selector1);
        lyt_paste  = sceneRoot.findViewById(R.id.lyt_paste);
        txv_next  = sceneRoot.findViewById(R.id.txv_next);

        imv_profile.setOnClickListener(this);
        txv_female.setOnClickListener(this);
        txv_male.setOnClickListener(this);
        lyt_paste.setOnClickListener(this);
        txv_next.setOnClickListener(this);
        txv_location.setOnClickListener(this);
        edt_birthday.setOnClickListener(this);

        edt_username.addTextChangedListener(this);
        edt_firstname.addTextChangedListener(this);
        edt_lastname.addTextChangedListener(this);
        txv_location.addTextChangedListener(this);
        edt_birthday.addTextChangedListener(this);
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        txv_next.setEnabled(completeFieldState());
        if(completeFieldState()) {
            txv_next.setTextColor(_context.getResources().getColor(R.color.white));
        }else
            txv_next.setTextColor(_context.getResources().getColor(R.color.white_transparent));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    boolean completeFieldState(){
        if(photoPath.length()==0)return false;
        if(edt_username.getText().toString().length()==0)return false;
        if(edt_firstname.getText().toString().length()==0)return false;
        if(edt_lastname.getText().toString().length()==0)return false;
        if(txv_location.getText().toString().length()==0)return false;
        if(edt_birthday.getText().toString().length()==0)return false;
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_profile:
                uploadPicture();
                break;
            case R.id.txv_female:
                imv_selector1.setVisibility(View.GONE);
                imv_selector2.setVisibility(View.VISIBLE);
                break;
            case R.id.txv_male:
                imv_selector1.setVisibility(View.VISIBLE);
                imv_selector2.setVisibility(View.GONE);
//                txv_male.setBackground(ContextCompat.getDrawable(this, R.drawable.edit_rectangle_round));
//                txv_female.setBackground(ContextCompat.getDrawable(this, R.drawable.edit_rectangle_round1));
                break;
            case R.id.lyt_paste:

                break;
            case R.id.txv_next:
                register();
                break;
            case R.id.txv_location:
                startActivityForResult(new Intent(this, SetPostRangeActivity.class),1);
                overridePendingTransition(0, 0);
                break;
            case R.id.edt_birthday:
                SelectDay();
                break;
        }
    }

    void register(){
        showProgress();
        try {
            File file = null;
            file = new File(photoPath);
            Map<String, String> params = new HashMap<>();
            params.put("email", Commons.g_user.getEmail());
            params.put("pass", Commons.g_user.getPassword());
            params.put("user_name", edt_username.getText().toString());
            params.put("first_name", edt_firstname.getText().toString());
            params.put("last_name", edt_lastname.getText().toString());
            params.put("location", txv_location.getText().toString());
            params.put("dob", birthday);
            params.put("gender", "");
            params.put("lat", String.valueOf(Commons.g_user.getLatitude()));
            params.put("lng", String.valueOf(Commons.g_user.getLongitude()));
            params.put("range", String.valueOf(Commons.g_user.getRange()));
            params.put("token", Commons.token);


            MultiPartRequest reqMultiPart = new MultiPartRequest(API.REGISTER_API, new Response.ErrorListener() {

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
                    parseResponse(json);
                }
            }, file, "pic", params);
            reqMultiPart.setRetryPolicy(new DefaultRetryPolicy(
                    6000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(reqMultiPart, API.REGISTER_API);

        } catch (Exception e) {

            e.printStackTrace();
            closeProgress();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    void parseResponse(String json){

        try {
            JSONObject jsonObject = new JSONObject(json);
            if(jsonObject.getBoolean("result")== false)
                showAlertDialog(jsonObject.getString("msg"));
            else {
                JSONObject userObject = jsonObject.getJSONObject("extra");
                UserModel userModel = new UserModel();
                userModel.setId(userObject.getInt("id"));
                userModel.setUserName(userObject.getString("user_name"));
                userModel.setComplete(userObject.getInt("complete"));
                userModel.setFb_user_id(userObject.getString("fb_user_id"));
                userModel.setFacebook_token(userObject.getString("facebook_token"));
                userModel.setEmail(userObject.getString("user_email"));
                userModel.setImvUrl(userObject.getString("pic_url"));
                userModel.setFirstname(userObject.getString("first_name"));
                userModel.setLastname(userObject.getString("last_name"));
                userModel.setLocation(userObject.getString("country"));
                userModel.setBirhtday(userObject.getString("birthday"));
                userModel.setSex(userObject.getString("gender"));
                userModel.setDescription(userObject.getString("description"));
                userModel.setPost_search_region(userObject.getString("post_search_region"));
                userModel.setAccount_type(userObject.getInt("account_type"));
                userModel.setStatus(userObject.getInt("status"));
                userModel.setStatus_reason(userObject.getString("status_reason"));
                userModel.setOnline(userObject.getInt("online"));
                userModel.setUpdate_at(userObject.getLong("updated_at"));
                userModel.setCreated_at(userObject.getLong("created_at"));
//                userModel.setLatitude(userObject.getDouble("latitude"));
//                userModel.setLongitude(userObject.getDouble("longitude"));
                userModel.setStripe_customer_token(userObject.getString("stripe_customer_token"));
                userModel.setStripe_connect_account(userObject.getString("stripe_connect_account"));
                userModel.setPush_tokenm(userObject.getString("push_token"));
                userModel.setInvitecode(userObject.getString("invite_code"));
                userModel.setInvited_by(userObject.getString("invited_by"));
                userModel.setPost_count(userObject.getInt("post_count"));
                userModel.setFollow_count(userObject.getInt("follow_count"));
                userModel.setFollowers_count(userObject.getInt("followers_count"));
                Commons.g_user = userModel;
                goTo(this, CreateFeedActivity.class,false);
            }
        }catch (Exception e){

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
                cal.set(year,month,day);
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

    void Keyboard() {
        ScrollView lytContainer = sceneRoot.findViewById(R.id.lyt_scroll);
        lytContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_username.getWindowToken(), 0);
                return false;
            }
        });
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
                MediaPicker mediaPicker = new MediaPicker(ProfileSetActivity.this);
                PickImageDialog pickImageDialog = new PickImageDialog();
                pickImageDialog.setImagePickListener(mediaPicker.getAllShownImagesPath(ProfileSetActivity.this), new PickImageDialog.OnImagePickListener() {
                    @Override
                    public void OnImagePick(String path) {
                        Uri uri = Uri.fromFile(new File(path));

                        Intent intent = CropImage.activity(uri)
                                .setGuidelines(CropImageView.Guidelines.ON).setCropShape(CropImageView.CropShape.RECTANGLE).setAspectRatio(1, 1)
                                .getIntent(ProfileSetActivity.this);

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
                File file = new File(Helper.getUriRealPathAboveKitkat(ProfileSetActivity.this, resultUri));
                if (imageUtils.getImage(file).getWidth() > 1024) {
                    options.width = 1024;
                    options.height = 1024;
                }

                Tiny.getInstance().source(file).asFile().withOptions(options).compress(new FileCallback() {
                    @Override
                    public void callback(boolean isSuccess, String outfile, Throwable t) {
                        //return the compressed file path
                        if (isSuccess) {
                            Log.e("IMAGE", outfile);
                            profileImage = new File(outfile);

                        } else {
                            profileImage = new File(Helper.getUriRealPathAboveKitkat(ProfileSetActivity.this, resultUri));
                        }
                    }
                });
                //                  Picasso.get().load(resultUri).resize(200, 200).centerCrop().into(iv_profile);
                //                    iv_profile.setImageURI(resultUri);
                imv_profile.setPadding(0, 0, 0, 0);
                imv_profile.setScaleType(ImageView.ScaleType.CENTER_CROP);
                Glide.with(this).load(resultUri).placeholder(R.drawable.profile_pic).dontAnimate().into(imv_profile);
                photoPath = resultUri.getPath();
                txv_next.setEnabled(completeFieldState());
                if(completeFieldState()) {
                    txv_next.setTextColor(_context.getResources().getColor(R.color.white));
                }else
                    txv_next.setTextColor(_context.getResources().getColor(R.color.white_transparent));

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else if(resultCode == Commons.location_code){
            txv_location.setText(Commons.location);
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