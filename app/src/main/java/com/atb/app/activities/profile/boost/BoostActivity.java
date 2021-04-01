package com.atb.app.activities.profile.boost;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.atb.app.R;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class BoostActivity extends CommonActivity implements View.OnClickListener {
    ImageView imv_profile,icon_back;
    FrameLayout lyt_profile_pin,lyt_pin_point;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boost);
        imv_profile = findViewById(R.id.imv_profile);
        icon_back = findViewById(R.id.icon_back);
        lyt_profile_pin = findViewById(R.id.lyt_profile_pin);
        lyt_pin_point = findViewById(R.id.lyt_pin_point);

        lyt_profile_pin.setOnClickListener(this);
        lyt_pin_point.setOnClickListener(this);
        icon_back.setOnClickListener(this);
        Glide.with(this).load(Commons.g_user.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.icon_boost_profile).dontAnimate().apply(RequestOptions.bitmapTransform(
                new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_back:
                finish(this);
                break;
            case R.id.lyt_profile_pin:
                goTo(this,ProfilePinAnimationActivity.class,false);
                break;
            case R.id.lyt_pin_point:
                goTo(this,PinPointAnimationActivity.class,false);
                break;
        }
    }
}