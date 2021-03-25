package com.atb.app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atb.app.R;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.newpost.SelectPostCategoryActivity;
import com.atb.app.activities.newsfeedpost.NewsDetailActivity;
import com.atb.app.activities.profile.OtherUserProfileActivity;
import com.atb.app.activities.profile.ProfileBusinessNaviagationActivity;
import com.atb.app.activities.profile.ProfileUserNavigationActivity;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.fragement.MainListFragment;
import com.atb.app.model.NewsFeedEntity;
import com.atb.app.model.UserModel;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainFeedAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<NewsFeedEntity> newsFeedEntities = new ArrayList<>();
    MainListFragment mainListFragment ;
   NewsFeedItemAdapter newsFeedItemAdapter;
    CustomHolder holder;

    public MainFeedAdapter(Context context, MainListFragment fragment) {
        this.context = context;
        mainListFragment = fragment;
    }

    @Override
    public int getCount() {
        return newsFeedEntities.size();
    }

    @Override
    public Object getItem(int i) {
        return newsFeedEntities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Commons.video_flag = -1;
        if (convertView == null) {
            holder = new CustomHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.newsfeed_item, parent, false);
            holder.recyclerView = (RecyclerView) convertView.findViewById(R.id.recyclerView_news);
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
        }
        final NewsFeedEntity newsFeedEntity = newsFeedEntities.get(position);
        ArrayList<NewsFeedEntity>arrayList = new ArrayList<>();
        arrayList.add(newsFeedEntity);
        arrayList.addAll(newsFeedEntity.getPostEntities());
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager( new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        newsFeedItemAdapter = new NewsFeedItemAdapter(context, arrayList, new NewsFeedItemAdapter.OnSelectListener() {
            @Override
            public void onSelect(NewsFeedEntity entity) {
                Bundle bundle = new Bundle();
                bundle.putInt("postId",entity.getId());
                bundle.putBoolean("CommentVisible",true);
                ((CommonActivity)context).startActivityForResult(new Intent(context, NewsDetailActivity.class).putExtra("data",bundle),1);
                //((CommonActivity)context).goTo(context, NewsDetailActivity.class,false,bundle);
            }

            @Override
            public void onSelectProfile(NewsFeedEntity newsFeedEntity) {
                if(newsFeedEntity.getUser_id() != Commons.g_user.getId()){
                    ((CommonActivity)context).getuserProfile(newsFeedEntity.getUser_id(),newsFeedEntity.getPoster_profile_type());

                }
                else {
                    if(newsFeedEntity.getPoster_profile_type() ==1)
                        ((CommonActivity)context).startActivityForResult(new Intent(context, ProfileBusinessNaviagationActivity.class),1);
                    else
                        ((CommonActivity)context).startActivityForResult(new Intent(context, ProfileUserNavigationActivity.class),1);
                }
            }
        });
        holder.recyclerView.setAdapter(newsFeedItemAdapter);
        return convertView;
    }

    public void changeItem(int position){

        final NewsFeedEntity newsFeedEntity = newsFeedEntities.get(position);
        ArrayList<NewsFeedEntity>arrayList = new ArrayList<>();
        arrayList.add(newsFeedEntity);
        arrayList.addAll(newsFeedEntity.getPostEntities());

        Commons.video_flag = 0;
        Log.d("ccccc", String.valueOf(position));
        //newsFeedItemAdapter.notifyDataSetChanged();

        holder.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int index = ((LinearLayoutManager) holder.recyclerView.getLayoutManager())
                            .findFirstVisibleItemPosition();
                    Commons.video_flag = index;
                    Log.d("ccccc", String.valueOf(index) + "    " + String.valueOf(position));
                 //   newsFeedItemAdapter.notifyItemChanged(position,arrayList);

                }
            }
        });
    }



    public class CustomHolder {
       RecyclerView recyclerView;
    }
    public void  setData(ArrayList<NewsFeedEntity> arrayList){
        newsFeedEntities = arrayList;
        notifyDataSetChanged();
    }
}
