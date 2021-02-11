package com.atb.app.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.register.CreateFeedActivity;
import com.atb.app.activities.register.Signup1Activity;
import com.atb.app.base.CommonActivity;

public class LoginActivity extends CommonActivity implements View.OnClickListener {
    TextView txv_login,txv_signup;
    LinearLayout lyt_facebook;
    EditText  edt_email,edt_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txv_login = findViewById(R.id.txv_login);
        txv_signup = findViewById(R.id.txv_signup);
        lyt_facebook = findViewById(R.id.lyt_facebook);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);

        txv_login.setOnClickListener(this);
        lyt_facebook.setOnClickListener(this);
        txv_signup.setOnClickListener(this);
        Keyboard();

    }
    void Keyboard(){
        RelativeLayout lytContainer = (RelativeLayout) findViewById(R.id.lyt_container);
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
            case R.id.txv_login:
                goTo(LoginActivity.this, MainActivity.class,true);

                break;
            case R.id.lyt_facebook:
                Log.d("aaaa","ok");
                break;
            case R.id.txv_signup:
                goTo(this, Signup1Activity.class,false);
                break;
        }
    }
}