package com.atb.app.activities.navigationItems;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.LoginActivity;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.register.Signup1Activity;
import com.atb.app.base.CommonActivity;

public class UpdateBusinessActivity extends CommonActivity implements View.OnClickListener {
    ImageView imv_back,imv_profile,imv_fb_selector,imv_instagram_selector,imv_twitter_selector;
    EditText edt_business_name,edt_yourwebsite,edt_tell_us,edt_instagram_name,edt_twitter_name,edt_tag;
    RelativeLayout lyt_setoperation_hour;
    TextView txv_setoperate;
    LinearLayout lyt_add_ceritfication,lyt_addinsurance,lyt_facebook,lyt_instgram,lyt_instagram_link,lyt_twitter,
            lyt_twitter_link,lyt_instagram_content,lyt_twitter_content,lyt_save;

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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_back:
                finish(this);
                break;
            case R.id.imv_profile:

                break;
            case R.id.lyt_setoperation_hour:

                break;
            case R.id.lyt_add_ceritfication:

                break;
            case R.id.lyt_addinsurance:

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
}