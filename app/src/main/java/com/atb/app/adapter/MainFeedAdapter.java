package com.atb.app.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atb.app.R;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.newsfeedpost.NewsDetailActivity;
import com.atb.app.fragement.MainListFragment;
import com.atb.app.model.NewsFeedEntity;

import java.util.ArrayList;

public class MainFeedAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<NewsFeedEntity> newsFeedEntities = new ArrayList<>();
    MainListFragment mainListFragment ;
   NewsFeedItemAdapter newsFeedItemAdapter;

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

        final CustomHolder holder;
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
        holder.recyclerView.setLayoutManager( new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(new NewsFeedItemAdapter(context, arrayList, new NewsFeedItemAdapter.OnSelectListener() {
            @Override
            public void onSelect(NewsFeedEntity entity) {
                Bundle bundle = new Bundle();
                bundle.putInt("postId",entity.getId());
                ((MainActivity)context).goTo(context, NewsDetailActivity.class,false,bundle);
            }
        }));





        return convertView;
    }



    public class CustomHolder {
       RecyclerView recyclerView;
    }
    public void  setData(ArrayList<NewsFeedEntity> arrayList){
        newsFeedEntities = arrayList;
        notifyDataSetChanged();
    }
}
