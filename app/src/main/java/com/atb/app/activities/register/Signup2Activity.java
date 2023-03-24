package com.atb.app.activities.register;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.profile.OtherUserProfileActivity;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.UserModel;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Signup2Activity extends CommonActivity implements View.OnClickListener {
    ImageView imv_back,imv_selector1,imv_selector2;
    EditText edt_password,edt_confirm_password;
    TextView txv_next,txv_terms;
    RadioButton radio_terms;
    LinearLayout lyt_terms;
    boolean flag1 = true,flag2 = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        imv_back = findViewById(R.id.imv_back);
        imv_selector1 = findViewById(R.id.imv_selector1);
        imv_selector2 = findViewById(R.id.imv_selector2);
        edt_password = findViewById(R.id.edt_password);
        edt_confirm_password = findViewById(R.id.edt_confirm_password);
        txv_next = findViewById(R.id.txv_next);
        lyt_terms = findViewById(R.id.lyt_terms);
        txv_terms = findViewById(R.id.txv_terms);
        radio_terms = findViewById(R.id.radio_terms);

        radio_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!radio_terms.isSelected()) {
                    radio_terms.setChecked(true);
                    radio_terms.setSelected(true);
                } else {
                    radio_terms.setChecked(false);
                    radio_terms.setSelected(false);
                }
                checkButton();
            }

        });
        imv_back.setOnClickListener(this);
        imv_selector1.setOnClickListener(this);
        imv_selector2.setOnClickListener(this);
        txv_next.setOnClickListener(this);



        edt_confirm_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkButton();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Keyboard();
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://app.termly.io/document/privacy-policy/a5b8733a-4988-42d7-8771-e23e311ab486") );
                startActivity(browserIntent);
            }
        };
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://app.termly.io/document/terms-of-use-for-online-marketplace/cbadd502-052f-40a2-8eae-30b1bb3ae9b1") );
                startActivity(browserIntent);

            }
        };

        SpannableString spannableString = new SpannableString(getResources().getString(R.string.terms_condition));

        spannableString.setSpan(clickableSpan1, 15,30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(clickableSpan2, 38,spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txv_terms.setText(spannableString);
        txv_terms.setMovementMethod(LinkMovementMethod.getInstance());
    }

    void checkButton(){
        if(edt_password.getText().toString().equals(edt_confirm_password.getText().toString()) && edt_password.getText().toString().length()>0 && radio_terms.isChecked()){
            txv_next.setEnabled(true);
            txv_next.setTextColor(_context.getResources().getColor(R.color.white));

        }else {
            txv_next.setEnabled(false);
            txv_next.setTextColor(_context.getResources().getColor(R.color.line_white));

        }
    }
    void Keyboard() {
        LinearLayout lytContainer = (LinearLayout) findViewById(R.id.lyt_container);
        lytContainer.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_password.getWindowToken(), 0);
                return false;
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_back:
                finish(this);
                break;
            case R.id.imv_selector1:
                if(flag1){
                    edt_password.setInputType(InputType.TYPE_CLASS_TEXT);
                    imv_selector1.setImageDrawable(getResources().getDrawable(R.drawable.icon_password_visible));
                }else {
                    edt_password.setInputType(0x00000081);
                    imv_selector1.setImageDrawable(getResources().getDrawable(R.drawable.icon_password_invisible));
                }
                flag1 = !flag1;
                break;
            case R.id.imv_selector2:
                if(flag2){
                    edt_confirm_password.setInputType(InputType.TYPE_CLASS_TEXT);
                    imv_selector2.setImageDrawable(getResources().getDrawable(R.drawable.icon_password_visible));
                }else {
                    edt_confirm_password.setInputType(0x00000081);
                    imv_selector2.setImageDrawable(getResources().getDrawable(R.drawable.icon_password_invisible));
                }
                flag2 = !flag2;
                break;
            case R.id.txv_next:
                Commons.g_user.setPassword(edt_password.getText().toString());
                //finishAffinity();
                //goTo(Signup2Activity.this, ProfileSetActivity.class,true);
                gotoSignUp();
                break;

        }
    }

    void gotoSignUp(){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.STAGE_ONE_REGISTER_API,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            Commons.token = jsonObject.getString("msg");
                            if(jsonObject.getBoolean("result")){
                                finishAffinity();
                                goTo(Signup2Activity.this, ProfileSetActivity.class,true);
                            }else {
                                showAlertDialog(Commons.token);
                            }



                        }catch (Exception e){

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        closeProgress();
                        //showToast(error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", Commons.g_user.getEmail());
                params.put("pwd", Commons.g_user.getPassword());
                params.put("fbToken",Commons.fbtoken);
                params.put("fcmtoken","");
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");

    }
}