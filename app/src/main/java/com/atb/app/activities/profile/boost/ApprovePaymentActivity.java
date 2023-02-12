package com.atb.app.activities.profile.boost;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.atb.app.R;
import com.atb.app.base.CommonActivity;

public class ApprovePaymentActivity extends CommonActivity {
    WebView wb;
    String web_link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_payment);
        wb=(WebView)findViewById(R.id.webView1);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                web_link= bundle.getString("web_link");
            }
        }

        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setLoadWithOverviewMode(true);
        wb.getSettings().setUseWideViewPort(true);
        wb.getSettings().setBuiltInZoomControls(true);
        wb.getSettings().setPluginState(WebSettings.PluginState.ON);
        wb.setWebViewClient(new HelloWebViewClient());
        wb.loadUrl(web_link);
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.contains("onboard/return")){
                setResult(RESULT_OK);
                finish(ApprovePaymentActivity.this);
            }
            return false;
        }
    }
}