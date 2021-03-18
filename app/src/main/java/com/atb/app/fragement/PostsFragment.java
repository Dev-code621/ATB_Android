package com.atb.app.fragement;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;
import com.atb.app.activities.newsfeedpost.NewsDetailActivity;
import com.atb.app.activities.profile.ProfileBusinessNaviagationActivity;
import com.atb.app.adapter.StoreItemAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.NewsFeedEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.atb.app.base.BaseActivity.closeProgress;
import static com.atb.app.base.BaseActivity.showToast;

public class PostsFragment extends Fragment {
    View view;
    Context context;
    ArrayList<NewsFeedEntity> newsFeedEntities = new ArrayList<>();
    RecyclerView recyclerView;
    StoreItemAdapter storeItemAdapter ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_posts, container, false);
        return view;

    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(context,2));


    }

    void addAdapter(){
        storeItemAdapter = new StoreItemAdapter(context, 1, 1, new StoreItemAdapter.SelectListener() {
            @Override
            public void OnItemSelect(int posstion) {
                Bundle bundle = new Bundle();
                bundle.putInt("postId",newsFeedEntities.get(posstion).getId());

                bundle.putBoolean("CommentVisible",true);
                ((CommonActivity)context).goTo(context, NewsDetailActivity.class,false,bundle);
            }

            @Override
            public void OnEditSelect(int posstion) {

            }

            @Override
            public void OnPostSelect(int posstion) {
            }
        });
        recyclerView.setAdapter(storeItemAdapter);
        storeItemAdapter.setDate(newsFeedEntities);

    }


    void loadStoreItems(){

        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                API.GET_USERS_POSTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();

                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            JSONArray jsonArray = jsonObject.getJSONArray("msg");
                            newsFeedEntities.clear();
                            for(int i =0;i<jsonArray.length();i++){
                                NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
                                newsFeedEntity.initModel(jsonArray.getJSONObject(i));
                                newsFeedEntities.add(newsFeedEntity);
                            }
                            addAdapter();

                        }catch (Exception e){
                            Log.d("aaaaa",e.toString());
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
                params.put("user_id",String.valueOf(Commons.g_user.getId()));
                params.put("business", String.valueOf(Commons.selectUsertype));
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myRequest, "tag");
    }
    @Override
    public void onResume() {
        super.onResume();
        context = getActivity();
        loadStoreItems();
    }
}