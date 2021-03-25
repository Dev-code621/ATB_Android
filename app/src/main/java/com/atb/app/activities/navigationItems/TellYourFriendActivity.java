package com.atb.app.activities.navigationItems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.atb.app.R;
import com.atb.app.activities.newpost.SelectPostCategoryActivity;
import com.atb.app.activities.profile.ProfileBusinessNaviagationActivity;
import com.atb.app.activities.profile.ProfileUserNavigationActivity;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;

public class TellYourFriendActivity extends CommonActivity implements View.OnClickListener {
    LinearLayout lyt_back;
    CardView card_share ,card_instagram,card_youtube,card_twitter,card_facebook;
    LinearLayout lyt_showmore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tell_your_friend);
        lyt_back = findViewById(R.id.lyt_back);
        card_share = findViewById(R.id.card_share);
        card_instagram = findViewById(R.id.card_instagram);
        card_youtube = findViewById(R.id.card_youtube);
        card_twitter = findViewById(R.id.card_twitter);
        card_facebook = findViewById(R.id.card_facebook);
        lyt_showmore = findViewById(R.id.lyt_showmore);

        lyt_back.setOnClickListener(this);
        card_share.setOnClickListener(this);
        card_instagram.setOnClickListener(this);
        card_youtube.setOnClickListener(this);
        card_twitter.setOnClickListener(this);
        card_facebook.setOnClickListener(this);
        lyt_showmore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lyt_back:
                finish(this);
                break;
            case R.id.card_share:

                break;
            case R.id.card_instagram:

                break;
            case R.id.card_youtube:

                break;
            case R.id.card_twitter:

                break;
            case R.id.card_facebook:

                break;
            case R.id.lyt_showmore:

                break;
        }
    }
}