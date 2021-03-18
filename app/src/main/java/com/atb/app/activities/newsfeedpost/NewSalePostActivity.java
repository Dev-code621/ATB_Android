package com.atb.app.activities.newsfeedpost;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.transition.Scene;

import com.atb.app.R;
import com.atb.app.activities.register.Signup2Activity;
import com.atb.app.base.CommonActivity;
import com.atb.app.dialog.SelectProfileDialog;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.ArrayList;

public class NewSalePostActivity extends CommonActivity implements View.OnClickListener {
    LinearLayout lyt_back,lyt_header;
    FrameLayout lyt_profile;
    TextView txv_title,txv_singlepost,txv_multpost;
    ImageView imv_profile;
    ViewGroup sceneRoot;
    Scene aScene,anotherScene;
    int multitype = 0;
    ArrayList<ImageView> imageViews = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sale_post);
        lyt_back = findViewById(R.id.lyt_back);
        txv_title = findViewById(R.id.txv_title);
        txv_singlepost = findViewById(R.id.txv_singlepost);
        txv_multpost = findViewById(R.id.txv_multpost);
        imv_profile = findViewById(R.id.imv_profile);
        lyt_profile = findViewById(R.id.lyt_profile);
        lyt_header = findViewById(R.id.lyt_header);
        sceneRoot = (ViewGroup)findViewById(R.id.scene_root);
        aScene = Scene.getSceneForLayout(sceneRoot, R.layout.layout_salespost, this);
        anotherScene =
                Scene.getSceneForLayout(sceneRoot, R.layout.layout_multisales, this);


        lyt_back.setOnClickListener(this);
        txv_singlepost.setOnClickListener(this);
        txv_multpost.setOnClickListener(this);
        txv_multpost.setBackground(getResources().getDrawable(R.drawable.edit_rectangle_round1));
        lyt_profile.setOnClickListener(this);

        activityAnimation(aScene,R.id.lyt_container);
        loadLayout();
    }
    void loadLayout(){
        RelativeLayout lyt_addtitle = sceneRoot.findViewById(R.id.lyt_addtitle);
        TextView txv_add = sceneRoot.findViewById(R.id.txv_add);
        ImageView icon_back = sceneRoot.findViewById(R.id.icon_back);
        TextView txv_discard = sceneRoot.findViewById(R.id.txv_discard);
        NiceSpinner spiner_media_type = sceneRoot.findViewById(R.id.spiner_media_type);
        ImageView imv_videothumnail = sceneRoot.findViewById(R.id.imv_videothumnail);
        ImageView imv_videoicon = sceneRoot.findViewById(R.id.imv_videoicon);
        FrameLayout lyt_video = sceneRoot.findViewById(R.id.lyt_video);
        ImageView imv_imageicon = sceneRoot.findViewById(R.id.imv_imageicon);
        imageViews.add(sceneRoot.findViewById(R.id.imv_image));
        imageViews.add(sceneRoot.findViewById(R.id.imv_image1));
        imageViews.add(sceneRoot.findViewById(R.id.imv_image2));
        imageViews.add(sceneRoot.findViewById(R.id.imv_image3));
        LinearLayout lyt_image = sceneRoot.findViewById(R.id.lyt_image);


        spiner_media_type.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                // This example uses String, but your type can be any
                String item = String.valueOf(parent.getItemAtPosition(position));
                 if(position==0){
                    lyt_image.setVisibility(View.VISIBLE);
                    lyt_video.setVisibility(View.GONE);

                }else{
                    lyt_image.setVisibility(View.GONE);
                    lyt_video.setVisibility(View.VISIBLE);
                }
            }
        });

        if(multitype>0) lyt_addtitle.setVisibility(View.VISIBLE);

        txv_add.setOnClickListener(this);
        icon_back.setOnClickListener(this);
        txv_discard.setOnClickListener(this);
    }
    void loadlayout1(){
        lyt_header.setVisibility(View.VISIBLE);
        ListView list_multipost = sceneRoot.findViewById(R.id.list_multipost);
        LinearLayout lyt_addproduct = sceneRoot.findViewById(R.id.lyt_addproduct);
        LinearLayout lyt_publish = sceneRoot.findViewById(R.id.lyt_publish);

        lyt_addproduct.setOnClickListener(this);
        lyt_publish.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lyt_back:
                finish(this);
                break;
            case R.id.txv_singlepost:
                txv_singlepost.setBackground(getResources().getDrawable(R.drawable.button_rectangle_round));
                txv_multpost.setBackground(getResources().getDrawable(R.drawable.edit_rectangle_round1));
                txv_singlepost.setTextColor(getResources().getColor(R.color.white));
                txv_multpost.setTextColor(getResources().getColor(R.color.txt_color));
                txv_title.setText(getResources().getString(R.string.create_oneproduct));
                activityAnimation(aScene,R.id.lyt_container);
                multitype = 0;
                loadLayout();
                break;
            case R.id.txv_multpost:
                txv_singlepost.setBackground(getResources().getDrawable(R.drawable.edit_rectangle_round1));
                txv_multpost.setBackground(getResources().getDrawable(R.drawable.button_rectangle_round));
                txv_singlepost.setTextColor(getResources().getColor(R.color.txt_color));
                txv_multpost.setTextColor(getResources().getColor(R.color.white));
                txv_title.setText(getResources().getString(R.string.create_multiproduct));
                activityAnimation(anotherScene,R.id.lyt_container);
                multitype = 1;
                loadlayout1();
                break;
            case R.id.lyt_profile:
                SelectprofileDialog(this);
                break;
            case R.id.lyt_addproduct:
                activityAnimation(aScene,R.id.lyt_container);
                lyt_header.setVisibility(View.GONE);
                multitype = 2;
                loadLayout();
                break;
            case R.id.lyt_publish:
                activityAnimation(anotherScene,R.id.lyt_container);
                loadlayout1();
                break;
            case R.id.txv_add:
                activityAnimation(anotherScene,R.id.lyt_container);
                loadlayout1();
                break;
            case R.id.icon_back:
                activityAnimation(anotherScene,R.id.lyt_container);
                loadlayout1();
                break;
            case R.id.txv_discard:
                activityAnimation(anotherScene,R.id.lyt_container);
                loadlayout1();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(multitype ==2){
            activityAnimation(anotherScene,R.id.lyt_container);
            loadlayout1();
        }
        else
            finish(this);
    }
}