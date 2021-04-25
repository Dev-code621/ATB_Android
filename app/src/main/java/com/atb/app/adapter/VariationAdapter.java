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
import com.atb.app.activities.newsfeedpost.NewSalePostActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.TransactionEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VariationAdapter extends BaseAdapter {

    private Context _context;

    HashMap<String, ArrayList<String>> _roomDatas = new HashMap<>();
    public VariationAdapter(Context context) {

        super();
        this._context = context;
    }


    public void setRoomData(HashMap<String, ArrayList<String>>data) {
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
            convertView = inflater.inflate(R.layout.variation_item, parent, false);
            holder.txv_name = (TextView) convertView.findViewById(R.id.txv_name);
            holder.txv_option = (TextView) convertView.findViewById(R.id.txv_option);
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
        }
        int index = 0;
        for ( String key : _roomDatas.keySet() ) {
            if(index==position){
                holder.txv_name.setText(key);
                final ArrayList<String> options = _roomDatas.get(key);
                String str = options.get(0);
                for(int i =1;i<options.size();i++){
                    str += "," + options.get(i);
                }
                holder.txv_option.setText(str);
                break;

            }
            index++;
        }

        return convertView;
    }


    public class CustomHolder {
        TextView txv_name, txv_option;
    }


    protected boolean isItemSelected(int position) {
        //return !selectedItems.isEmpty() && selectedItems.contains(getItem(position));
        return true;
    }

}


