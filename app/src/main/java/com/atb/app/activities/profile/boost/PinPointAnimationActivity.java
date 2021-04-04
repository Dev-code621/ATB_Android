package com.atb.app.activities.profile.boost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Scene;

import android.os.Bundle;
import android.os.Handler;
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

public class PinPointAnimationActivity extends CommonActivity {
    Scene anotherScene;
    ViewGroup sceneRoot;
    LinearLayout lyt_back ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_point_animation);
        lyt_back = findViewById(R.id.lyt_back);
        lyt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(PinPointAnimationActivity.this);
            }
        });
        ImageView imv_profile = findViewById(R.id.imv_profile);
        Glide.with(this).load(Commons.g_user.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.icon_boost_profile).dontAnimate().apply(RequestOptions.bitmapTransform(
                new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);

        sceneRoot = (ViewGroup) findViewById(R.id.scene_root);
        anotherScene =
                Scene.getSceneForLayout(sceneRoot, R.layout.activity_pin_point2, this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                activityAnimation(anotherScene,R.id.lyt_container);
                loadlayout();
            }
        }, 1000);
    }

    void loadlayout(){
        LinearLayout lyt_init = findViewById(R.id.lyt_init);
        LinearLayout lyt_text = findViewById(R.id.lyt_text);
        FrameLayout lyt_pin_point = findViewById(R.id.lyt_pin_point);
        ImageView imv_profile = findViewById(R.id.imv_profile);
        Glide.with(this).load(Commons.g_user.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.icon_boost_profile).dontAnimate().apply(RequestOptions.bitmapTransform(
                new RoundedCornersTransformation(this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
        lyt_pin_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTo(PinPointAnimationActivity.this,PinPointActivity.class,false);
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
                lyt_init.setVisibility(GONE);
                lyt_text.setVisibility(View.VISIBLE);
            }
        });

        lyt_init.startAnimation(animation);
    }
}