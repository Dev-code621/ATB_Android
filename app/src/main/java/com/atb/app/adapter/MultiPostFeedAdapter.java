package com.atb.app.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.commons.Commons;
import com.atb.app.commons.Constants;
import com.atb.app.model.NewsFeedEntity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MultiPostFeedAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<NewsFeedEntity> newsFeedEntities = new ArrayList<>();
   NewsFeedItemAdapter newsFeedItemAdapter;

    public MultiPostFeedAdapter(Context context) {
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
            holder.imv_icon = (ImageView) convertView.findViewById(R.id.imv_icon);
            holder.imv_type = (ImageView ) convertView.findViewById(R.id.imv_type);
            holder.txv_price = (TextView ) convertView.findViewById(R.id.txv_price);
            holder.txv_name = (TextView ) convertView.findViewById(R.id.txv_name);
            holder.lyt_container = (LinearLayout)convertView.findViewById(R.id.lyt_container) ;
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
        }
        holder.imv_selector.setVisibility(View.GONE);
        final NewsFeedEntity newsFeedEntity = newsFeedEntities.get(position);
        holder.imv_selector.setEnabled(newsFeedEntity.isSelect());
        holder.txv_name.setText(newsFeedEntity.getTitle());
        holder.txv_price.setText("£" + newsFeedEntity.getPrice());
        if(newsFeedEntity.getPost_type()==3)
            holder.txv_price.setText("Starting at £" + newsFeedEntity.getPrice());
        holder.imv_type.setImageResource(Constants.postType[newsFeedEntity.getPost_type()]);
        holder.imv_type.setColorFilter(context.getResources().getColor(R.color.txt_color), PorterDuff.Mode.SRC_IN);
        holder.imv_icon.setVisibility(View.GONE);
        if(newsFeedEntity.getMedia_type()==1){
            Glide.with(context).load(newsFeedEntity.getCompletedValue().get(0)).placeholder(R.drawable.image_thumnail).dontAnimate().into(holder.item_image);
        }else {
            Glide.with(context).load(newsFeedEntity.getVideovalue()).placeholder(R.drawable.image_thumnail).dontAnimate().into(holder.item_image);
            holder.imv_icon.setVisibility(View.VISIBLE);
        }

        return convertView;
    }



    public class CustomHolder {
       ImageView imv_selector,item_image,imv_type,imv_icon;
       TextView txv_price,txv_name;
       LinearLayout lyt_container;
    }
    public void  setData(ArrayList<NewsFeedEntity> arrayList){
        newsFeedEntities = arrayList;
        notifyDataSetChanged();
    }
}
