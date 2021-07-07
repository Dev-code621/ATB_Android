package com.atb.app.activities.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;
import com.atb.app.activities.navigationItems.SavePostActivity;
import com.atb.app.adapter.MainFeedAdapter;
import com.atb.app.adapter.SearchAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.NewsFeedEntity;
import com.atb.app.model.UserModel;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    ArrayList<UserModel> pin_business = new ArrayList<>();
    ArrayList<NewsFeedEntity> newsFeedEntities = new ArrayList<>();
    MainFeedAdapter mainFeedAdapter;
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
        mainFeedAdapter = new MainFeedAdapter(this);
        load_search();

    }
    void load_search(){
          showProgress();
          String api_link = API.SEARCH_BUSINESS;
        if(type==1)
            api_link = API.GET_SELECTED_FEED_API;
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                api_link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                         closeProgress();
                         Log.d("aaaaa",json);
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            if(type ==0) {
                                atb_business.clear();
                                pin_business.clear();
                                JSONArray pins = jsonObject.getJSONObject("extra").getJSONArray("pins");
                                JSONArray search_result = jsonObject.getJSONObject("extra").getJSONArray("search_result");
                                txv_number.setText(String.valueOf(pins.length()+search_result.length()) + " results");
                                for (int i = 0; i < pins.length(); i++) {
                                    UserModel userModel = new UserModel();
                                    userModel.initModel(pins.getJSONObject(i));
                                    pin_business.add(userModel);
                                }
                                for (int i = 0; i < search_result.length(); i++) {
                                    UserModel userModel = new UserModel();
                                    userModel.initModel(search_result.getJSONObject(i));
                                    atb_business.add(userModel);
                                }
                                list_search.setAdapter(searchAdapter);
                                searchAdapter.setRoomData(atb_business);

                                for(int i =0;i<3;i++){
                                    if(pin_business.size()<=i){
                                        boost_layout.get(i).setVisibility(View.GONE);

                                    }else {
                                        boost_layout.get(i).setVisibility(View.VISIBLE);
                                        int finalI = i;
                                        boost_layout.get(i).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                getuserProfile(pin_business.get(finalI).getId(),1);
                                            }
                                        });
                                        Glide.with(SearchActivity.this).load(pin_business.get(i).getBusinessModel().getBusiness_logo()).placeholder(R.drawable.icon_image1).dontAnimate().apply(RequestOptions.bitmapTransform(
                                                new RoundedCornersTransformation(SearchActivity.this, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile.get(i));
                                        txv_name.get(i).setText(pin_business.get(i).getBusinessModel().getBusiness_name());
                                        txv_description.get(i).setText(pin_business.get(i).getBusinessModel().getBusiness_bio());
                                        txv_distance.get(i).setText(String.valueOf(pin_business.get(i).getDistance())+"KM");
                                        txv_star.get(i).setText(String.valueOf(new DecimalFormat("##.#").format(pin_business.get(i).getBusinessModel().getRating()))+" / 5.0 (" +String.valueOf(pin_business.get(i).getBusinessModel().getReviews())+")");
                                    }

                                }
                            }else {
                                for(int i =0;i<3;i++)
                                    boost_layout.get(i).setVisibility(View.GONE);
                                newsFeedEntities.clear();
                                JSONArray jsonArray;
                                if(Commons.selectUsertype==-1)
                                    jsonArray = jsonObject.getJSONArray("extra");
                                else
                                    jsonArray = jsonObject.getJSONArray("msg");
                                txv_number.setText(String.valueOf(jsonArray.length()) + " results");

                                for(int i =0;i<jsonArray.length();i++){
                                    NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
                                    JSONObject newsObject = jsonArray.getJSONObject(i);
                                    newsFeedEntity.initModel(newsObject);
                                    newsFeedEntities.add(newsFeedEntity);
                                }
                                list_search.setAdapter(mainFeedAdapter);
                                mainFeedAdapter.setData(newsFeedEntities);

                            }
                        }catch (Exception e){

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        closeProgress();
                        showToast(error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", Commons.token);
                if(type ==0) {
                    params.put("category", category);
                    params.put("tags", search);
                }else {
                    params.put("category_title", category);
                    params.put("search_key", search);
                }
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
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
                load_search();
                break;
            case R.id.txv_atb_post:
                type =1;
                setbutton();
                load_search();
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
    @Override
    public void UserProfile(UserModel userModel,int usertype){
        if(userModel.getId()==Commons.g_user.getId()){
            if(Commons.g_user.getAccount_type()==1)
                startActivityForResult(new Intent(this, ProfileBusinessNaviagationActivity.class),1);
            else
                goTo(this, ProfileUserNavigationActivity.class,false);
        }else {
            Gson gson = new Gson();
            String usermodel = gson.toJson(userModel);
            Bundle bundle = new Bundle();
            bundle.putString("userModel", usermodel);
            bundle.putInt("userType", usertype);
            goTo(this, OtherUserProfileActivity.class, false, bundle);
        }
    }

}