package com.atb.app.activities.navigationItems.business;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.newpost.SelectPostCategoryActivity;
import com.atb.app.activities.newsfeedpost.NewSalePostActivity;
import com.atb.app.activities.newsfeedpost.NewServiceOfferActivity;
import com.atb.app.activities.profile.ProfileBusinessNaviagationActivity;
import com.atb.app.activities.profile.ProfileUserNavigationActivity;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;

public class BusinessProductPostActivity extends CommonActivity implements View.OnClickListener {
    LinearLayout lyt_serviceoffer,lyt_product;
    TextView txv_later;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_product_post);

        lyt_serviceoffer = findViewById(R.id.lyt_serviceoffer);
        lyt_product = findViewById(R.id.lyt_product);
        txv_later = findViewById(R.id.txv_later);

        lyt_serviceoffer.setOnClickListener(this);
        lyt_product.setOnClickListener(this);
        txv_later.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txv_later:
                finish(this);
                break;
            case R.id.lyt_serviceoffer:
                Bundle bundle = new Bundle();
                bundle.putInt("isPosting", 0);
                startActivityForResult(new Intent(this, NewServiceOfferActivity.class).putExtra("data",bundle), 1);
                break;
            case R.id.lyt_product:
                bundle = new Bundle();
                bundle.putInt("isPosting", 0);
                startActivityForResult(new Intent(this, NewSalePostActivity.class).putExtra("data",bundle), 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK){
            setResult(RESULT_OK);
            finish(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish(Commons.profileUserNavigationActivity);
        startActivityForResult(new Intent(this, ProfileBusinessNaviagationActivity.class),1);

    }
}