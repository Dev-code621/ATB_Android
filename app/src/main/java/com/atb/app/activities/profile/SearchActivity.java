package com.atb.app.activities.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.adapter.SearchAdapter;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.UserModel;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SearchActivity extends CommonActivity implements View.OnClickListener {
    String search,category;
    int type;
    TextView txv_atb_business,txv_atb_post,txv_search,txv_number;
    ImageView imv_back;
    ListView list_search;
    ArrayList<ImageView>imv_profile = new ArrayList<>();
    ArrayList<TextView>txv_distance = new ArrayList<>();
    ArrayList<TextView>txv_description = new ArrayList<>();
    ArrayList<TextView>txv_star = new ArrayList<>();
    ArrayList<TextView>txv_name = new ArrayList<>();
    ArrayList<LinearLayout>boost_layout = new ArrayList<>();
    SearchAdapter searchAdapter;
    ArrayList<UserModel> atb_business = new ArrayList<>();
    ArrayList<UserModel> atb_post = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        list_search = findViewById(R.id.list_search);
        txv_atb_business = findViewById(R.id.txv_atb_business);
        txv_atb_post = findViewById(R.id.txv_atb_post);
        imv_back = findViewById(R.id.imv_back);
        txv_search = findViewById(R.id.txv_search);
        txv_number = findViewById(R.id.txv_number);
        imv_profile.add(findViewById(R.id.imv_profile1));
        imv_profile.add(findViewById(R.id.imv_profile2));
        imv_profile.add(findViewById(R.id.imv_profile3));
        txv_distance.add(findViewById(R.id.txv_distance1));
        txv_distance.add(findViewById(R.id.txv_distance2));
        txv_distance.add(findViewById(R.id.txv_distance3));
        txv_description.add(findViewById(R.id.txv_description1));
        txv_description.add(findViewById(R.id.txv_description2));
        txv_description.add(findViewById(R.id.txv_description3));
        txv_star.add(findViewById(R.id.txv_star1));
        txv_star.add(findViewById(R.id.txv_star2));
        txv_star.add(findViewById(R.id.txv_star3));
        txv_name.add(findViewById(R.id.txv_name1));
        txv_name.add(findViewById(R.id.txv_name2));
        txv_name.add(findViewById(R.id.txv_name3));
        boost_layout.add(findViewById(R.id.boost_layout1));
        boost_layout.add(findViewById(R.id.boost_layout2));
        boost_layout.add(findViewById(R.id.boost_layout3));

        imv_back.setOnClickListener(this);
        txv_atb_business.setOnClickListener(this);
        txv_atb_post.setOnClickListener(this);
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                search =  bundle.getString("search");
                category =  bundle.getString("category");
                type =  bundle.getInt("type");
                txv_search.setText(search);
                setbutton();
                loadingData();
            }
        }
        searchAdapter = new SearchAdapter(this);
        list_search.setAdapter(searchAdapter);
        for(int i =0;i<10;i++){
            UserModel userModel = new UserModel();
            atb_business.add(userModel);
            atb_post.add(userModel);
        }
        searchAdapter.setRoomData(atb_business);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_back:
                finish(this);
                break;
            case R.id.txv_atb_business:
                type = 0;
                setbutton();
                searchAdapter.setRoomData(atb_business);
                break;
            case R.id.txv_atb_post:
                type =1;
                setbutton();
                searchAdapter.setRoomData(atb_post);
                break;

        }
    }
    void setbutton(){
        if(type ==1){
            txv_atb_business.setBackground(getResources().getDrawable(R.drawable.login_button_rectangle_round));
            txv_atb_business.setTextColor(getResources().getColor(R.color.white));
            txv_atb_post.setBackground(getResources().getDrawable(R.drawable.edit_rectangle_round));
            txv_atb_post.setTextColor(getResources().getColor(R.color.head_color));
        }else {
            txv_atb_post.setBackground(getResources().getDrawable(R.drawable.login_button_rectangle_round));
            txv_atb_post.setTextColor(getResources().getColor(R.color.white));
            txv_atb_business.setBackground(getResources().getDrawable(R.drawable.edit_rectangle_round));
            txv_atb_business.setTextColor(getResources().getColor(R.color.head_color));
        }
    }
    void loadingData(){

    }

}