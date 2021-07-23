package com.atb.app.activities.profile.boost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Scene;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.atb.app.R;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import static android.view.View.GONE;

public class ProfilePinAnimationActivity extends CommonActivity {
    LinearLayout lyt_back,lyt_container,lyt_text;
    ImageView imv_profile,imv_init;
    FrameLayout lyt_profile_pin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pin_animation);
        lyt_back = findViewById(R.id.lyt_back);
        lyt_container = findViewById(R.id.lyt_container);
        lyt_text = findViewById(R.id.lyt_text);
        imv_profile = findViewById(R.id.imv_profile);
        imv_init = findViewById(R.id.imv_init);
        lyt_profile_pin = findViewById(R.id.lyt_profile_pin);
        Glide.with(this).load(Commons.g_user.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.icon_boost_profile).dontAnimate().apply(RequestOptions.bitmapTransform(
                new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
        lyt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(ProfilePinAnimationActivity.this);
            }
        });

        lyt_profile_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTo(ProfilePinAnimationActivity.this,ProfilePinActivity.class,true);
            }
        });
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_down);
         animation.setDuration(1000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                imv_init.setVisibility(GONE);
                lyt_text.setVisibility(View.VISIBLE);
                if (lyt_container.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) lyt_container.getLayoutParams();
                    p.setMargins(0, 0, 0, 0);
                    lyt_container.setAnimation(new Animation() {
                        @Override
                        public void start() {
                            super.start();
                        }
                    });
                    lyt_container.requestLayout();
                }
            }
        });

        imv_init.startAnimation(animation);
    }
}