package com.atb.app.activities.navigationItems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.LoginActivity;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.newpost.SelectPostCategoryActivity;
import com.atb.app.activities.register.Signup1Activity;
import com.atb.app.base.CommonActivity;
import com.atb.app.fragement.PostsFragment;
import com.atb.app.fragement.StoreFragment;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentStatePagerItemAdapter;

public class ProfileActivity extends CommonActivity  implements View.OnClickListener {
    LinearLayout lyt_back;
    ImageView imv_profile,imv_selector2,imv_selector1;
    FrameLayout lyt_addpicture;
    EditText edt_firstname,edt_lastname,edt_email,edt_location,edt_birthday;
    TextView txv_update,txv_male,txv_female;
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
        edt_location = findViewById(R.id.edt_location);
        txv_male = findViewById(R.id.txv_male);
        txv_female = findViewById(R.id.txv_female);
        edt_birthday = findViewById(R.id.edt_birthday);
        txv_update = findViewById(R.id.txv_update);

        lyt_back.setOnClickListener(this);
        txv_female.setOnClickListener(this);
        txv_male.setOnClickListener(this);
        txv_update.setOnClickListener(this);
        edt_location.setOnClickListener(this);
        Keyboard();

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
                imv_selector1.setVisibility(View.GONE);
                imv_selector2.setVisibility(View.VISIBLE);
                break;
            case R.id.txv_male:
                imv_selector1.setVisibility(View.VISIBLE);
                imv_selector2.setVisibility(View.GONE);
                break;
            case R.id.txv_update:
                finish(this);
                break;
        }
    }
}