package com.atb.app.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.atb.app.R;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.newsfeedpost.ExistSalesPostActivity;
import com.atb.app.activities.newsfeedpost.NewsDetailActivity;
import com.atb.app.fragement.MainListFragment;
import com.atb.app.model.NewsFeedEntity;

import java.util.ArrayList;

public class PostFeedAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<NewsFeedEntity> newsFeedEntities = new ArrayList<>();
   NewsFeedItemAdapter newsFeedItemAdapter;

    public PostFeedAdapter(Context context) {
        this.context = context;
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
            convertView = inflater.inflate(R.layout.postfeed_item, parent, false);
            holder.imv_selector = (ImageView ) convertView.findViewById(R.id.imv_selector);
            holder.item_image = (ImageView) convertView.findViewById(R.id.item_image);
            holder.imv_type = (ImageView ) convertView.findViewById(R.id.imv_type);
            holder.txv_price = (TextView ) convertView.findViewById(R.id.txv_price);
            holder.txv_name = (TextView ) convertView.findViewById(R.id.txv_name);
            holder.lyt_container = (LinearLayout)convertView.findViewById(R.id.lyt_container) ;
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
        }
        final NewsFeedEntity newsFeedEntity = newsFeedEntities.get(position);
        holder.imv_selector.setEnabled(newsFeedEntity.isSelect());
        return convertView;
    }



    public class CustomHolder {
       ImageView imv_selector,item_image,imv_type;
       TextView txv_price,txv_name;
       LinearLayout lyt_container;
    }
    public void  setData(ArrayList<NewsFeedEntity> arrayList){
        newsFeedEntities = arrayList;
        notifyDataSetChanged();
    }
}
