package com.atb.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.navigationItems.TransactionHistoryActivity;
import com.atb.app.activities.profile.SearchActivity;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.TransactionEntity;
import com.atb.app.model.UserModel;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SearchAdapter extends BaseAdapter {

    private SearchActivity _context;

    public ArrayList<UserModel> _roomDatas = new ArrayList<>();

    public SearchAdapter(SearchActivity context) {

        super();
        this._context = context;
    }


    public void setRoomData(ArrayList<UserModel> data) {
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
            convertView = inflater.inflate(R.layout.serach_item, parent, false);
            holder.txv_name = (TextView) convertView.findViewById(R.id.txv_name);
            holder.txv_distance = (TextView) convertView.findViewById(R.id.txv_distance);
            holder.txv_description = (TextView) convertView.findViewById(R.id.txv_description);
            holder.txv_star = (TextView) convertView.findViewById(R.id.txv_star);
            holder.imv_profile = (ImageView) convertView.findViewById(R.id.imv_profile);
            holder.lyt_item = (LinearLayout) convertView.findViewById(R.id.lyt_item);
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
        }
        final UserModel userModel = _roomDatas.get(position);
        Glide.with(_context).load(userModel.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.icon_image1).dontAnimate().apply(RequestOptions.bitmapTransform(
                new RoundedCornersTransformation(_context, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(holder.imv_profile);
        holder.txv_name.setText(userModel.getBusinessModel().getBusiness_name());
        holder.txv_description.setText(userModel.getBusinessModel().getBusiness_bio());
        holder.txv_distance.setText(String.valueOf(userModel.getDistance())+"KM");
        holder.txv_star.setText(String.valueOf(new DecimalFormat("##.#").format(userModel.getBusinessModel().getRating()))+" / 5.0 (" +String.valueOf(userModel.getBusinessModel().getReviews())+")");
        holder.lyt_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CommonActivity)_context).getuserProfile(userModel.getId(),1);
            }
        });
        return convertView;
    }


    public class CustomHolder {
        TextView txv_name, txv_distance, txv_star,txv_description;
        ImageView imv_profile;
        LinearLayout lyt_item;
    }


    protected boolean isItemSelected(int position) {
        //return !selectedItems.isEmpty() && selectedItems.contains(getItem(position));
        return true;
    }
}


