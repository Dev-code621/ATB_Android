package com.atb.app.activities.navigationItems.business;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.profile.ProfileBusinessNaviagationActivity;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.facebook.common.Common;

public class BuildBusinessStoreActivity extends CommonActivity {
    TextView txv_businessstore,txv_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_business_store);
        txv_businessstore = findViewById(R.id.txv_businessstore);
        txv_name = findViewById(R.id.txv_name);
        txv_name.setText(Commons.g_user.getBusinessModel().getBusiness_name() +"! " + "Thats all we need\nfor now");
        txv_businessstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTo(BuildBusinessStoreActivity.this, ProfileBusinessNaviagationActivity.class, true);

            }
        });
    }
}