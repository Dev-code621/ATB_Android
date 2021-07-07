package com.atb.app.activities.navigationItems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.atb.app.R;
import com.atb.app.activities.newpost.SelectPostCategoryActivity;
import com.atb.app.activities.profile.ProfileBusinessNaviagationActivity;
import com.atb.app.activities.profile.ProfileUserNavigationActivity;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

public class TellYourFriendActivity extends CommonActivity implements View.OnClickListener {
    LinearLayout lyt_back;
    CardView card_share ,card_instagram,card_youtube,card_twitter,card_facebook;
    LinearLayout lyt_showmore;
    TextView txv_code;
    String share_link = "";
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
        txv_code = findViewById(R.id.txv_code);
        lyt_back.setOnClickListener(this);
        card_share.setOnClickListener(this);
        card_instagram.setOnClickListener(this);
        card_youtube.setOnClickListener(this);
        card_twitter.setOnClickListener(this);
        card_facebook.setOnClickListener(this);
        lyt_showmore.setOnClickListener(this);
        txv_code.setText(Commons.g_user.getInvitecode());
        share_link = "https://test.myatb.co.uk/invite?code=" + Commons.g_user.getInvitecode();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lyt_back:
                finish(this);
                break;
            case R.id.card_share:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(share_link, share_link);
                clipboard.setPrimaryClip(clip);
                break;
            case R.id.card_youtube:

                break;
            case R.id.card_instagram:
                SharingToSocialMedia("com.instagram.android");
                break;
            case R.id.card_twitter:
                SharingToSocialMedia("com.twitter.android");
                break;
            case R.id.card_facebook:
                //SharingToSocialMedia("com.facebook.katana");
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(share_link))
                        .build();
                ShareDialog shareDialog = new ShareDialog(this);
                shareDialog.show(content);

                break;
            case R.id.lyt_showmore:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/html");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(share_link));
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
                break;
        }

    }

    public void SharingToSocialMedia(String application) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        //intent.putExtra(Intent.EXTRA_STREAM, Html.fromHtml(share_link));
        intent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(share_link));
        boolean installed = checkAppInstall(application);
        if (installed) {
            intent.setPackage(application);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Please install application first", Toast.LENGTH_LONG).show();
        }

    }


    private boolean checkAppInstall(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }
}