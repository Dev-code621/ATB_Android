package com.atb.app.activities.navigationItems.booking;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;
import com.atb.app.activities.LoginActivity;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.register.Signup1Activity;
import com.atb.app.adapter.BookingServiceAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.dialog.CancelBookingConfirmDialog;
import com.atb.app.dialog.RequestRatingDialog;
import com.atb.app.model.BookingEntity;
import com.atb.app.model.NewsFeedEntity;
import com.google.gson.Gson;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateABookingActivity extends CommonActivity implements View.OnClickListener {
    ImageView imv_back;
    TextView txv_date,txv_time;
    MaterialSearchView search_view;
    CardView card_search;
    ListView list_booking;
    BookingServiceAdapter bookingServiceAdapter;
    ArrayList<NewsFeedEntity>newsFeedEntities = new ArrayList<>();
    ArrayList<NewsFeedEntity>search_newsFeedEntities = new ArrayList<>();
    BookingEntity bookingEntity = new BookingEntity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_a_booking);
        search_view = findViewById(R.id.search_view);
        card_search = findViewById(R.id.card_search);
        imv_back = findViewById(R.id.imv_back);
        txv_date = findViewById(R.id.txv_date);
        list_booking = findViewById(R.id.list_booking);
        txv_time = findViewById(R.id.txv_time);

        card_search.setOnClickListener(this);
        imv_back.setOnClickListener(this);
        bookingServiceAdapter = new BookingServiceAdapter(this);
        list_booking.setAdapter(bookingServiceAdapter);
        list_booking.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                Gson gson = new Gson();
                String serviceModel = gson.toJson(search_newsFeedEntities.get(position));
                bundle.putString("serviceModel",serviceModel);
                bundle.putInt("time",bookingEntity.getBooking_datetime());

                startActivityForResult(new Intent(CreateABookingActivity.this, CreateBooking2Activity.class).putExtra("data",bundle),1);
                overridePendingTransition(0, 0);
            }
        });
        search_view.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search_newsFeedEntities.clear();
                for(int i =0;i<newsFeedEntities.size();i++){
                    if(newsFeedEntities.get(i).getTitle().toLowerCase().contains(newText.toLowerCase())){
                        search_newsFeedEntities.add(newsFeedEntities.get(i));
                    }
                }
                bookingServiceAdapter.setRoomData(search_newsFeedEntities);
                return false;
            }
        });

        search_view.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
                card_search.setVisibility(View.VISIBLE);
            }
        });

        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                String books= bundle.getString("bookModel");
                Gson gson = new Gson();
                bookingEntity = gson.fromJson(books, BookingEntity.class);
            }
        }

        loadLayout();
    }

    void loadLayout(){
        txv_date.setText(getDisplayDate(bookingEntity.getBooking_datetime()));
        txv_time.setText(Commons.gettimeFromMilionSecond(bookingEntity.getBooking_datetime()));
        loadServices();
    }

    void loadServices(){
        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GET_USER_SERVICES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            JSONArray jsonArray = jsonObject.getJSONArray("extra");
                            search_newsFeedEntities.clear();
                            newsFeedEntities.clear();
                            for(int i =0;i<jsonArray.length();i++){
                                NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
                                newsFeedEntity.initModel(jsonArray.getJSONObject(i));
                                newsFeedEntities.add(newsFeedEntity);
                            }
                            search_newsFeedEntities.addAll(newsFeedEntities);
                            bookingServiceAdapter.setRoomData(search_newsFeedEntities);
                        }catch (Exception e){

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        closeProgress();
                        //showToast(error.getMessage());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", Commons.token);
                params.put("user_id", String.valueOf(Commons.g_user.getId()));
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
            case R.id.card_search:
                search_view.showSearch();
                card_search.setVisibility(View.GONE);
                break;
            case R.id.imv_back:
                finish(this);
                break;
        }
    }
    static String getDisplayDate(long milionsecond){
        String date = "";
        Date d = new Date(milionsecond*1000l);
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE dd MMM");
        date = formatter.format(d);
        return date;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Activity.RESULT_OK){
            setResult(RESULT_OK);
            finish(this);
        }
    }
}