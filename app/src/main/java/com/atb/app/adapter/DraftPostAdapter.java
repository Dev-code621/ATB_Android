package com.atb.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.atb.app.R;
import com.atb.app.activities.navigationItems.DraftPostActivity;
import com.atb.app.activities.navigationItems.NotificationActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.NewsFeedEntity;
import com.atb.app.model.NotiEntity;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class DraftPostAdapter extends BaseAdapter {

    private DraftPostActivity _context;

    public ArrayList<NewsFeedEntity> _roomDatas = new ArrayList<>();

    public DraftPostAdapter(DraftPostActivity context) {

        super();
        this._context = context;
    }


    public void setRoomData(ArrayList<NewsFeedEntity> data) {
        _roomDatas = data;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return _roomDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return _roomDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final CustomHolder holder;
        if (convertView == null) {
            holder = new CustomHolder();
            LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.draft_item, parent, false);
            holder.txv_title = (TextView) convertView.findViewById(R.id.txv_title);
            holder.txv_price = (TextView) convertView.findViewById(R.id.txv_price);
            holder.txv_time = (TextView) convertView.findViewById(R.id.txv_time);
            holder.imv_image = (ImageView) convertView.findViewById(R.id.imv_image);
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
        }
        final NewsFeedEntity newsFeedEntity = _roomDatas.get(position);
        holder.txv_title.setText(newsFeedEntity.getTitle());
        holder.txv_price.setText(newsFeedEntity.getPrice());
        holder.txv_time.setText(Commons.getDisplayDate4(newsFeedEntity.getCreated_at()));
        String str = "";
        if(newsFeedEntity.getPostImageModels().size() >0){
            str = newsFeedEntity.getPostImageModels().get(0).getPath();
        }
        Glide.with(_context).load(str).placeholder(R.drawable.image_thumnail).dontAnimate().apply(RequestOptions.bitmapTransform(
                new RoundedCornersTransformation(_context, 20, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(holder.imv_image);

        return convertView;
    }


    public class CustomHolder {
        TextView txv_title, txv_price, txv_time;
        ImageView imv_image;
        CardView card_unread_noti;
    }
    private String secToTime(long old_time) {
        int sec = (int) (System.currentTimeMillis()/1000l - old_time);
        int seconds = sec % 60;
        int minutes = sec / 60;
        if (seconds <= 0) {
            return "Just now";
        } else if (minutes == 0) {
            return String.format(_context.getString(R.string.second_ago), seconds);
        } else {
            if (minutes >= 60) {
                int hours = minutes / 60;
                minutes %= 60;
                if (hours >= 24) {
                    int days = hours / 24;
                    return String.format(_context.getString(R.string.days_ago), days);
                }
                return String.format(_context.getString(R.string.hours_ago), hours);
            }
            return String.format(_context.getString(R.string.min_ago), minutes);
        }
    }


    protected boolean isItemSelected(int position) {
        //return !selectedItems.isEmpty() && selectedItems.contains(getItem(position));
        return true;
    }
}


