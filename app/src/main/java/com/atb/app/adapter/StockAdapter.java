package com.atb.app.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.navigationItems.TransactionHistoryActivity;
import com.atb.app.activities.newsfeedpost.NewSalePostActivity;
import com.atb.app.model.TransactionEntity;
import com.atb.app.model.VariationModel;

import java.util.ArrayList;
import java.util.HashMap;

public class StockAdapter extends BaseAdapter {

    private NewSalePostActivity _context;
    HashMap<String, VariationModel> _roomDatas = new HashMap<>();
    public ArrayList<Boolean>booleans = new ArrayList<>();
    public ArrayList<String>prices = new ArrayList<>();
    public ArrayList<String>stock_levels = new ArrayList<>();
    public StockAdapter(NewSalePostActivity context) {

        super();
        this._context = context;
    }


    public void setRoomData(  HashMap<String, VariationModel>stockMap) {
        this._roomDatas = stockMap;
        booleans.clear();
        stock_levels.clear();
        prices.clear();
        for(int i =0;i<stockMap.size();i++){
            booleans.add(true);
            prices.add("0.00");
            stock_levels.add("0");
        }
        notifyDataSetChanged();
    }

    public void setSelect(boolean flag){
        for(int i =0;i<_roomDatas.size();i++)booleans.set(i,flag);
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
            convertView = inflater.inflate(R.layout.stock_item, parent, false);
            holder.txv_title = (TextView) convertView.findViewById(R.id.txv_title);
            holder.txv_minus = (TextView) convertView.findViewById(R.id.txv_minus);
            holder.txv_count = (TextView) convertView.findViewById(R.id.txv_count);
            holder.txv_plus = (TextView) convertView.findViewById(R.id.txv_plus);
            holder.imv_stocker_check = (ImageView) convertView.findViewById(R.id.imv_stocker_check);
            holder.edt_price = (EditText) convertView.findViewById(R.id.edt_price);
            holder.lyt_container = (LinearLayout)convertView.findViewById(R.id.lyt_container);
            convertView.setTag(holder);
        } else {
            holder = (CustomHolder) convertView.getTag();
        }
        int index = 0;
        String choice_key = "";
        for ( String key : _roomDatas.keySet() ) {
            if(index==position){
                choice_key = key;
                final VariationModel variationModel = _roomDatas.get(key);
                String string ="";
                for(int i =0;i<variationModel.getAttributeModels().size();i++){
                    String space = "<br/>";
                    if(i==variationModel.getAttributeModels().size()-1)
                        space ="";
                    string = string +  "<font color='black'>" + "<b>"  +variationModel.getAttributeModels().get(i).getAttribute_title() + "</b>" + "</font>" + "&nbsp;"
                            + variationModel.getAttributeModels().get(i).getVariant_attirbute_value()+ space;

                }
                holder.txv_title.setText(Html.fromHtml(string));
                if(!variationModel.getPrice().equals("0.00"))
                     holder.edt_price.setText(variationModel.getPrice());
                holder.txv_count.setText(String.valueOf(variationModel.getStock_level()));
                break;

            }
            index++;
        }
        holder.edt_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                prices.set(position,holder.edt_price.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        String finalChoice_key = choice_key;
        holder.txv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _roomDatas.get(finalChoice_key).setStock_level( _roomDatas.get(finalChoice_key).getStock_level()+1);
                holder.txv_count.setText(String.valueOf(_roomDatas.get(finalChoice_key).getStock_level()));
                stock_levels.set(position,holder.txv_count.getText().toString());
            }
        });
        holder.txv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_roomDatas.get(finalChoice_key).getStock_level()==0)return;
                _roomDatas.get(finalChoice_key).setStock_level( _roomDatas.get(finalChoice_key).getStock_level()-1);
                holder.txv_count.setText(String.valueOf(_roomDatas.get(finalChoice_key).getStock_level()));
                stock_levels.set(position,holder.txv_count.getText().toString());
            }
        });

        holder.imv_stocker_check.setEnabled(booleans.get(position));
        holder.lyt_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imv_stocker_check.setEnabled(!holder.imv_stocker_check.isEnabled());
                booleans.set(position,holder.imv_stocker_check.isEnabled());
                notifyDataSetChanged();
            }
        });
        return convertView;
    }


    public class CustomHolder {
        TextView txv_title, txv_minus, txv_count,txv_plus;
        EditText edt_price;
        ImageView imv_stocker_check;
        LinearLayout lyt_container;
    }


    protected boolean isItemSelected(int position) {
        //return !selectedItems.isEmpty() && selectedItems.contains(getItem(position));
        return true;
    }
}


