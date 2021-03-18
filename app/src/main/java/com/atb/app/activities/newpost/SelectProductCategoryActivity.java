package com.atb.app.activities.newpost;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.atb.app.R;
import com.atb.app.activities.newsfeedpost.ExistSalesPostActivity;
import com.atb.app.activities.newsfeedpost.NewAdviceActivity;
import com.atb.app.activities.newsfeedpost.NewPollVotingActivity;
import com.atb.app.activities.newsfeedpost.NewSalePostActivity;
import com.atb.app.activities.newsfeedpost.NewServiceOfferActivity;
import com.atb.app.base.CommonActivity;

public class SelectProductCategoryActivity extends CommonActivity implements View.OnClickListener {
    ImageView imv_close;
    LinearLayout lyt_sale_post,lyt_serviceoffer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_product_category);
        imv_close = findViewById(R.id.imv_close);
        lyt_sale_post = findViewById(R.id.lyt_sale_post);
        lyt_serviceoffer = findViewById(R.id.lyt_serviceoffer);

        imv_close.setOnClickListener(this);
        lyt_sale_post.setOnClickListener(this);
        lyt_serviceoffer.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_close:
                finish(this);
                break;
            case R.id.lyt_sale_post:
                Bundle bundle = new Bundle();
                bundle.putInt("isPosting", 0);
                startActivityForResult(new Intent(this, NewSalePostActivity.class).putExtra("data",bundle), 1);
                break;
            case R.id.lyt_serviceoffer:
                bundle = new Bundle();
                bundle.putInt("isPosting", 0);
                startActivityForResult(new Intent(this, NewServiceOfferActivity.class).putExtra("data",bundle), 1);
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
}