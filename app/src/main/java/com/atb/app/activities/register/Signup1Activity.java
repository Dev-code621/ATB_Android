package com.atb.app.activities.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.Scene;
import androidx.transition.Slide;
import androidx.transition.TransitionInflater;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;
import com.atb.app.activities.profile.FollowerAndFollowingActivity;
import com.atb.app.adapter.EmailAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.UserModel;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Signup1Activity extends CommonActivity implements View.OnClickListener {
    Scene aScene,anotherScene;

    LinearLayout lyt_facebook,lyt_top;
    TextView txv_email,txv_signin,txv_next;
    ImageView imv_back,imv_back1,imv_selector1,imv_selector2;
    EditText edt_email,edit_confirm_email;
    RecyclerView recyclerView_images,recyclerView_images1;
    ViewGroup sceneRoot;
    CallbackManager callbackManager;
    private LoginManager loginManager;
    LoginButton loginButton;
    String email = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);
        sceneRoot = (ViewGroup) findViewById(R.id.scene_root);
        aScene = Scene.getSceneForLayout(sceneRoot, R.layout.signup_email_layout, this);
        anotherScene =
                Scene.getSceneForLayout(sceneRoot, R.layout.signup_email_layout1, this);

      activityAnimation(aScene,R.id.lyt_container);
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();
        loadLayout1();

    }

    void loadLayout1(){
        lyt_facebook = sceneRoot.findViewById(R.id.lyt_facebook);
        txv_email = sceneRoot.findViewById(R.id.txv_email);
        txv_signin = sceneRoot.findViewById(R.id.txv_signin);
        imv_back = findViewById(R.id.imv_back);
        lyt_top = findViewById(R.id.lyt_top);
        imv_back.setOnClickListener(this);

        lyt_facebook.setOnClickListener(this);
        txv_email.setOnClickListener(this);
        txv_signin.setOnClickListener(this);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        List< String > permissionNeeds = Arrays.asList( "email");
        loginButton.setPermissions(permissionNeeds);
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {@Override
                public void onSuccess(LoginResult loginResult) {

                    System.out.println("onSuccess");

                    Commons.fbtoken = "fb_"+ loginResult.getAccessToken()
                            .getUserId();
                    Log.i("accessToken",    Commons.fbtoken);

                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {@Override
                            public void onCompleted(JSONObject object,
                                                    GraphResponse response) {


                                try {
                                    String id = object.getString("id");
                                    try {
                                        URL profile_pic = new URL(
                                                "http://graph.facebook.com/" + id + "/picture?type=large");
                                        Log.i("profile_pic",
                                                profile_pic + "");

                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }
                                    email = object.getString("email");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                loginManager.getInstance().logOut();
                                activityAnimation(anotherScene,R.id.lyt_container);
                                lyt_top.setVisibility(View.GONE);
                                loadLayout2();
                            }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields",
                            "id,name,email,gender, birthday");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                    @Override
                    public void onCancel() {
                        System.out.println("onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("onError");
                        Log.v("LoginActivity", exception.getCause().toString());
                    }
                });
    }
    void  loadLayout2(){
        txv_next = sceneRoot.findViewById(R.id.txv_next);
        txv_next.setEnabled(false);
        imv_back1 = sceneRoot.findViewById(R.id.imv_back1);
        imv_selector1 = sceneRoot.findViewById(R.id.imv_selector1);
        imv_selector2 = sceneRoot.findViewById(R.id.imv_selector2);
        edt_email = sceneRoot.findViewById(R.id.edt_email);
        edit_confirm_email = sceneRoot.findViewById(R.id.edit_confirm_email);
        recyclerView_images = sceneRoot.findViewById(R.id.recyclerView_images);
        recyclerView_images1 = sceneRoot.findViewById(R.id.recyclerView_images1);
        imv_back.setOnClickListener(this);
        imv_back1.setOnClickListener(this);
        imv_selector1.setOnClickListener(this);
        imv_selector2.setOnClickListener(this);
        txv_next.setOnClickListener(this);

        String[] list = getResources().getStringArray(R.array.email_list);

        Keyboard();

        recyclerView_images.setLayoutManager( new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView_images.setAdapter(new EmailAdapter(this, list, new EmailAdapter.OnEmailSelectListener() {
            @Override
            public void onEmailSelect(String path) {
                edt_email.setText(edt_email.getText().toString() + path);
            }
        }));
          recyclerView_images1.setLayoutManager( new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView_images1.setAdapter(new EmailAdapter(this, list, new EmailAdapter.OnEmailSelectListener() {
            @Override
            public void onEmailSelect(String path) {
                edit_confirm_email.setText(edit_confirm_email.getText().toString() + path);
            }
        }));


        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edt_email.getText().toString().equals(edit_confirm_email.getText().toString()) && edt_email.getText().toString().length()>0){
                    imv_selector1.setEnabled(false);
                    imv_selector2.setEnabled(false);
                    txv_next.setEnabled(true);
                    txv_next.setTextColor(_context.getResources().getColor(R.color.white));
                }else {
                    imv_selector1.setEnabled(true);
                    imv_selector2.setEnabled(true);
                    txv_next.setEnabled(false);
                    txv_next.setTextColor(_context.getResources().getColor(R.color.line_white));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edit_confirm_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edt_email.getText().toString().equals(edit_confirm_email.getText().toString()) && edt_email.getText().toString().length()>0){
                    imv_selector1.setEnabled(false);
                    imv_selector2.setEnabled(false);
                    txv_next.setEnabled(true);
                    txv_next.setTextColor(_context.getResources().getColor(R.color.white));
                }else {
                    imv_selector1.setEnabled(true);
                    imv_selector2.setEnabled(true);
                    txv_next.setEnabled(false);
                    txv_next.setTextColor(_context.getResources().getColor(R.color.line_white));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edt_email.setText(email);
        edit_confirm_email.setText(email);
    }

    void Keyboard() {
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
            case R.id.lyt_facebook:
                loginButton.performClick();
                break;
            case R.id.txv_email:
                activityAnimation(anotherScene,R.id.lyt_container);
                lyt_top.setVisibility(View.GONE);
                loadLayout2();
                break;
            case R.id.txv_signin:
                finish(this);
                break;
            case R.id.imv_back:
                finish(this);
                break;
            case R.id.imv_back1:
                activityAnimation(aScene,R.id.lyt_container);
                lyt_top.setVisibility(View.VISIBLE);
                loadLayout1();
                break;
            case R.id.imv_selector1:
                edt_email.setText("");
                break;
            case R.id.imv_selector2:
                edit_confirm_email.setText("");
                break;
            case R.id.txv_next:
                UserModel userModel = new UserModel();
                userModel.setEmail(edt_email.getText().toString());
                Commons.g_user = userModel;
                checkEmailValidation();

                break;

        }
    }

    void checkEmailValidation(){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.EMAIL_VALID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();

                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(jsonObject.getBoolean("result")){
                                finishAffinity();
                                goTo(Signup1Activity.this, Signup2Activity.class,false);
                            }else {
                                showAlertDialog(jsonObject.getString("msg"));
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
                params.put("email", Commons.g_user.getEmail());
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}