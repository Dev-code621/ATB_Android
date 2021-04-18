package com.atb.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.navigationItems.NotificationActivity;
import com.atb.app.activities.navigationItems.booking.CreateABookingActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.BookingEntity;
import com.atb.app.model.NewsFeedEntity;
import com.atb.app.model.NotiEntity;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class BookingServiceAdapter extends BaseAdapter {

    private CreateABookingActivity _context;

    public ArrayList<NewsFeedEntity> _roomDatas = new ArrayList<>();

    public BookingServiceAdapter(CreateABookingActivity context) {

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
            convertView = inflater.inflate(R.layout.bookservice_item, parent, false);
            holder.txv_name = (TextView) convertView.findViewById(R.id.txv_name);
            holder.txv_price = (TextView) convertView.findViewById(R.id.txv_price);
            holder.imv_image = (ImageView) convertView.findViewById(R.id.imv_image);
            holder.imv_video = convertView.findViewById(R.id.imv_video);
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
        }
        final NewsFeedEntity newsFeedEntity = _roomDatas.get(position);
        holder.txv_name.setText(newsFeedEntity.getTitle());
        holder.txv_price.setText("£" + newsFeedEntity.getPrice());
        Glide.with(_context).load(newsFeedEntity.getPostImageModels().get(0).getPath()).placeholder(R.drawable.image_thumnail).dontAnimate().apply(RequestOptions.bitmapTransform(
                new RoundedCornersTransformation(_context, 10, Commons.glide_magin, "#A6BFDE", Commons.glide_boder))).into(holder.imv_image);
        if(Commons.mediaVideoType(newsFeedEntity.getPostImageModels().get(0).getPath()))
            holder.imv_video.setVisibility(View.VISIBLE);
        else
            holder.imv_video.setVisibility(View.GONE);
        return convertView;
    }


    public class CustomHolder {
        TextView txv_name, txv_price;
        ImageView imv_image,imv_video;
    }


    protected boolean isItemSelected(int position) {
        //return !selectedItems.isEmpty() && selectedItems.contains(getItem(position));
        return true;
    }
}


