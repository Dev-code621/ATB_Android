package com.atb.app.fragement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.atb.app.R;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.newsfeedpost.NewsDetailActivity;
import com.atb.app.adapter.MainFeedAdapter;
import com.atb.app.api.API;
import com.atb.app.application.AppController;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.NewsFeedEntity;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.atb.app.base.BaseActivity.closeProgress;
import static com.atb.app.base.BaseActivity.showProgress;
import static com.atb.app.base.BaseActivity.showToast;

public class MainListFragment extends Fragment  implements SwipyRefreshLayout.OnRefreshListener{
    View view;
    ListView list_main;
    Context context;
    ArrayList<NewsFeedEntity>newsFeedEntities = new ArrayList<>();
    MainFeedAdapter mainFeedAdapter ;
    private SwipyRefreshLayout ui_refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list_main = (ListView)view.findViewById(R.id.list_main);
        ui_refreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.refresh);
        getList();
        ui_refreshLayout.setDirection(SwipyRefreshLayoutDirection.BOTH);
        ui_refreshLayout.setOnRefreshListener(this);
    }
    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        Log.d("aaaaa","aaaa");
        if(direction == SwipyRefreshLayoutDirection.TOP){
            getList();
            ui_refreshLayout.setRefreshing(false);

        }
    }

    public void uiRefresh(){
        ui_refreshLayout.setDirection(SwipyRefreshLayoutDirection.TOP);
    }


    @Override
    public void onResume() {
        super.onResume();
        context =getActivity();
    }


    public void getList(){
        String apilink = API.GET_USERS_POSTS;
        if(Commons.selectUsertype==-1) {
            apilink = API.GET_ALL_FEED_API;
            if (!Commons.main_category.equals("MY ATB")) {
                apilink = API.GET_SELECTED_FEED_API;
            }
        }

        showProgress();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                apilink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        closeProgress();
                        parseResponse(json);
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
                if(Commons.selectUsertype==-1) {
                    if (!Commons.main_category.equals("MY ATB"))
                        params.put("category_title", Commons.main_category);
                }else{
                    params.put("user_id", String.valueOf(Commons.selected_user.getId()));
                    params.put("business", "0");
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

    void parseResponse(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray;
            if(Commons.selectUsertype==-1)
                jsonArray = jsonObject.getJSONArray("extra");
            else
                jsonArray = jsonObject.getJSONArray("msg");
            newsFeedEntities.clear();
            for(int i =0;i<jsonArray.length();i++){
                NewsFeedEntity newsFeedEntity = new NewsFeedEntity();
                JSONObject newsObject = jsonArray.getJSONObject(i);
                newsFeedEntity.initModel(newsObject);
                newsFeedEntities.add(newsFeedEntity);
            }
            mainFeedAdapter = new MainFeedAdapter(context);
            list_main.setAdapter(mainFeedAdapter);
            mainFeedAdapter.setData(newsFeedEntities);

            list_main.setOnScrollListener(new AbsListView.OnScrollListener() {
                private int currentScrollState;

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    this.currentScrollState = scrollState;
                    this.isScrollCompleted();
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {

                }
                private void isScrollCompleted() {

                    if (this.currentScrollState == SCROLL_STATE_IDLE) {

                        int index = list_main.getFirstVisiblePosition();
                        Log.d("bbbbbbbbbbbbbbb", String.valueOf(index));
                        mainFeedAdapter.changeItem(index);

                    }

                }
            });

            if(Commons.feed_id!=-1){
                Bundle bundle = new Bundle();
                bundle.putInt("postId",Commons.feed_id);
                bundle.putBoolean("CommentVisible",true);
                startActivityForResult(new Intent(context, NewsDetailActivity.class).putExtra("data",bundle),1);
            }


        }catch (Exception e){

        }

    }
}