package com.atb.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.navigationItems.TransactionHistoryActivity;
import com.atb.app.activities.profile.SearchActivity;
import com.atb.app.model.TransactionEntity;
import com.atb.app.model.UserModel;

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
            holder.txv_star = (TextView) convertView.findViewById(R.id.txv_star);
            holder.imv_profile = (ImageView) convertView.findViewById(R.id.imv_profile);
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
        }
        final UserModel userModel = _roomDatas.get(position);
//        holder.txv_title.setText(noti_item.getTitle());
//        holder.txv_content.setText(noti_item.getContent());
//        holder.txv_time.setText(noti_item.getTime());
        return convertView;
    }


    public class CustomHolder {
        TextView txv_name, txv_distance, txv_star;
        ImageView imv_profile;
    }


    protected boolean isItemSelected(int position) {
        //return !selectedItems.isEmpty() && selectedItems.contains(getItem(position));
        return true;
    }
}


