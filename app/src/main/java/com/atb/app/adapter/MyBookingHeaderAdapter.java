package com.atb.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.navigationItems.BookingActivity;
import com.atb.app.activities.navigationItems.booking.BookingViewActivity;
import com.atb.app.activities.navigationItems.booking.CreateABookingActivity;
import com.atb.app.activities.navigationItems.booking.MyBookingViewActivity;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.BookingEntity;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MyBookingHeaderAdapter extends SectioningAdapter {

    static final String TAG = MyBookingHeaderAdapter.class.getSimpleName();
    static final boolean USE_DEBUG_APPEARANCE = false;
    Context _context ;


    private class Section {
        int index;
        int copyCount;
        String header;
        String footer;
        ArrayList<String> items = new ArrayList<>();

        public Section duplicate() {
            Section c = new Section();
            c.index = this.index;
            c.copyCount = this.copyCount + 1;
            c.header = c.index + " copy " + c.copyCount;
            c.footer = this.footer;
            for (String i : this.items) {
                c.items.add(i + " copy " + c.copyCount);
            }

            return c;
        }

        public void duplicateItem(int item) {
            String itemCopy = items.get(item) + " copy";
            items.add(item + 1, itemCopy);
        }

    }

    public class ItemViewHolder extends SectioningAdapter.ItemViewHolder{
        ImageView imv_image,imv_profile,imv_icon;
        TextView txv_name,txv_itemnumber,txv_price,txv_time,txv_date;
        LinearLayout lyt_item;
        public ItemViewHolder(View itemView) {
            super(itemView);
            imv_image =  itemView.findViewById(R.id.imv_image);
            imv_profile =  itemView.findViewById(R.id.imv_profile);
            txv_name =  itemView.findViewById(R.id.txv_name);
            txv_itemnumber =  itemView.findViewById(R.id.txv_itemnumber);
            txv_price =  itemView.findViewById(R.id.txv_price);
            txv_time =  itemView.findViewById(R.id.txv_time);
            imv_icon = itemView.findViewById(R.id.imv_icon);
            txv_date = itemView.findViewById(R.id.txv_date);
            lyt_item = itemView.findViewById(R.id.lyt_item);
        }
    }

    public class HeaderViewHolder extends SectioningAdapter.HeaderViewHolder {
        TextView textView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }

    public class FooterViewHolder extends SectioningAdapter.FooterViewHolder {
        TextView textView;
        TextView adapterPositionTextView;

        public FooterViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            adapterPositionTextView = (TextView) itemView.findViewById(R.id.adapterPositionTextView);

            if (!MyBookingHeaderAdapter.this.showAdapterPositions) {
                adapterPositionTextView.setVisibility(View.GONE);
            }
        }
    }


    ArrayList<Section> sections = new ArrayList<>();
    boolean showModificationControls;
    boolean showCollapsingSectionControls;
    boolean showAdapterPositions;
    boolean hasFooters;
    ArrayList<BookingEntity> _roomDatas = new ArrayList<>();
    ArrayList<ArrayList<BookingEntity>> _bookings = new ArrayList<>();
    String[] header_text = {"Coming up","Next week", "Past Bookings"};
    int type = 0;
    public MyBookingHeaderAdapter(Context context, boolean hasFooters, boolean showModificationControls, boolean showCollapsingSectionControls, boolean showAdapterPositions, ArrayList<BookingEntity> data,int type) {
        this._context = context;
        this.showModificationControls = showModificationControls;
        this.showCollapsingSectionControls = showCollapsingSectionControls;
        this.showAdapterPositions = showAdapterPositions;
        this.hasFooters = hasFooters;
        this.type = type;
        _roomDatas = data;
        Collections.sort(_roomDatas, new Comparator<BookingEntity>() {
            @Override
            public int compare(BookingEntity o1, BookingEntity o2) {
               return String.valueOf(o1.getBooking_datetime()).compareTo(String.valueOf(o2.getBooking_datetime()));
            }
        });

        for(int i=0;i<3;i++){
            ArrayList<BookingEntity> arrayList = new ArrayList<>();
            _bookings.add(arrayList);
        }
        for(int i =0;i<_roomDatas.size();i++){
            final BookingEntity bookingEntity = _roomDatas.get(i);
            int t1 = (int)(System.currentTimeMillis()/1000);
            Calendar c=Calendar.getInstance();
            c.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
            c.set(Calendar.HOUR_OF_DAY,23);
            c.set(Calendar.MINUTE,59);
            c.set(Calendar.SECOND,59);
            c.add(Calendar.DATE,7);
            int t2 = (int)(c.getTimeInMillis()/1000);
            if(bookingEntity.getBooking_datetime()<t1){
                _bookings.get(2).add(bookingEntity);
            }else if(bookingEntity.getBooking_datetime()<=t2){
                _bookings.get(0).add(bookingEntity);
            }else {
                _bookings.get(1).add(bookingEntity);
            }
        }
        for(int i =0;i<2;i++){
            appendSection(i,header_text[i],_bookings.get(i).size());
        }

    }

    public void viewPastBooking(){
        sections.clear();
        for(int i =0;i<3;i++){
            appendSection(i,header_text[i],_bookings.get(i).size());
        }
        notifyAllSectionsDataSetChanged();
    }


    void appendSection(int index, String str,int itemCount) {
        Section section = new Section();
        section.index = index;
        section.copyCount = 0;
        section.header = str;
        for (int j = 0; j < itemCount; j++) {
            section.items.add(index + "/" + j);
        }
        sections.add(section);
    }

    void onToggleSectionCollapse(int sectionIndex) {
        Log.d(TAG, "onToggleSectionCollapse() called with: " + "sectionIndex = [" + sectionIndex + "]");
        setSectionIsCollapsed(sectionIndex, !isSectionCollapsed(sectionIndex));
    }

    void onDeleteSection(int sectionIndex) {
        Log.d(TAG, "onDeleteSection() called with: " + "sectionIndex = [" + sectionIndex + "]");
        sections.remove(sectionIndex);
        notifySectionRemoved(sectionIndex);
    }

    void onCloneSection(int sectionIndex) {
        Log.d(TAG, "onCloneSection() called with: " + "sectionIndex = [" + sectionIndex + "]");

        Section s = sections.get(sectionIndex);
        Section d = s.duplicate();
        sections.add(sectionIndex + 1, d);
        notifySectionInserted(sectionIndex + 1);
    }

    void onDeleteItem(int sectionIndex, int itemIndex) {
        Log.d(TAG, "onDeleteItem() called with: " + "sectionIndex = [" + sectionIndex + "], itemIndex = [" + itemIndex + "]");
        Section s = sections.get(sectionIndex);
        s.items.remove(itemIndex);
        notifySectionItemRemoved(sectionIndex, itemIndex);
    }

    void onCloneItem(int sectionIndex, int itemIndex) {
        Log.d(TAG, "onCloneItem() called with: " + "sectionIndex = [" + sectionIndex + "], itemIndex = [" + itemIndex + "]");
        Section s = sections.get(sectionIndex);
        s.duplicateItem(itemIndex);
        notifySectionItemInserted(sectionIndex, itemIndex + 1);
    }

    @Override
    public int getNumberOfSections() {
        return sections.size();
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        return sections.get(sectionIndex).items.size();
    }

    @Override
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return !TextUtils.isEmpty(sections.get(sectionIndex).header);
    }

    @Override
    public boolean doesSectionHaveFooter(int sectionIndex) {
        return !TextUtils.isEmpty(sections.get(sectionIndex).footer);
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_booking_simple_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_item_simple_header, parent, false);
        return new HeaderViewHolder(v);
    }

    @Override
    public FooterViewHolder onCreateFooterViewHolder(ViewGroup parent, int footerType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_item_simple_footer, parent, false);
        return new FooterViewHolder(v);
    }

    String hearder_name ="";
    int _sec =-1,_index =-1,past_sec = -1;
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindItemViewHolder(SectioningAdapter.ItemViewHolder viewHolder, int sectionIndex, int itemIndex, int itemType) {
        Section s = sections.get(sectionIndex);
        ItemViewHolder holder = (ItemViewHolder) viewHolder;
//        ivh.textView.setText(s.items.get(itemIndex));
//        ivh.adapterPostionTextView.setText(Integer.toString(getAdapterPositionForSectionItem(sectionIndex, itemIndex)));
        final BookingEntity bookingEntity= _bookings.get(sectionIndex).get(itemIndex);
        Glide.with(_context).load(bookingEntity.getNewsFeedEntity().getPostImageModels().get(0).getPath()).placeholder(R.drawable.image_thumnail).dontAnimate().apply(RequestOptions.bitmapTransform(
                new RoundedCornersTransformation(_context, 20, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(holder.imv_image);
        holder.txv_name.setText(bookingEntity.getNewsFeedEntity().getTitle());
        if(type == 1)
            holder.txv_price.setText(bookingEntity.getUserModel().getUserName());
        else
            holder.txv_price.setText(bookingEntity.getBusinessModel().getBusiness_name());
        holder.txv_time.setText(Commons.gettimeFromMilionSecond(bookingEntity.getBooking_datetime()));
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(bookingEntity.getBooking_datetime()*1000l);
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("EEEE dd MMMM");
        String formattedDate = format.format(date);
        holder.txv_date.setText(formattedDate);
        holder.lyt_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Gson gson = new Gson();
                String bookingModel = gson.toJson(bookingEntity);
                bundle = new Bundle();
                bundle.putString("bookModel",bookingModel);
                if (type == 0) {

                    ((CommonActivity)(_context)).startActivityForResult(new Intent(_context, MyBookingViewActivity.class).putExtra("data",bundle),1);

                }else{
                    ((CommonActivity)(_context)).startActivityForResult(new Intent(_context, BookingViewActivity.class).putExtra("data",bundle),1);

                }
                ((CommonActivity)(_context)).overridePendingTransition(0, 0);
            }
        });
        if(_sec!=sectionIndex){
            past_sec = _sec;
            _sec = sectionIndex;
            _index = -1;
        }
        if(itemIndex>_index) {
            if(past_sec<=_sec) {
                if (hearder_name.equals(formattedDate))
                    holder.txv_date.setVisibility(View.GONE);
                else
                    holder.txv_date.setVisibility(View.VISIBLE);
            }else {
                if(itemIndex==0 )
                    holder.txv_date.setVisibility(View.VISIBLE);
                else {
                    Calendar c=Calendar.getInstance();
                    c.setTimeInMillis(_bookings.get(sectionIndex).get(itemIndex-1).getBooking_datetime()*1000l);
                    Date d = c.getTime();
                    String str = format.format(d);
                    if(str.equals(formattedDate)){
                        holder.txv_date.setVisibility(View.GONE);
                    }else
                        holder.txv_date.setVisibility(View.VISIBLE);
                }
            }
        }else {
            if(itemIndex==0 )
                holder.txv_date.setVisibility(View.VISIBLE);
            else {
                Calendar c=Calendar.getInstance();
                c.setTimeInMillis(_bookings.get(sectionIndex).get(itemIndex-1).getBooking_datetime()*1000l);
                Date d = c.getTime();
                String str = format.format(d);
                if(str.equals(formattedDate)){
                    holder.txv_date.setVisibility(View.GONE);
                }else
                    holder.txv_date.setVisibility(View.VISIBLE);
            }
        }
        hearder_name = formattedDate;
        _index = itemIndex;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder, int sectionIndex, int headerType) {
        Section s = sections.get(sectionIndex);
        HeaderViewHolder hvh = (HeaderViewHolder) viewHolder;

        if (USE_DEBUG_APPEARANCE) {
            hvh.textView.setText(pad(sectionIndex * 2) + s.header);
            viewHolder.itemView.setBackgroundColor(0x55FF9999);
        } else {
            hvh.textView.setText(s.header);
        }

    }

    @Override
    public void onBindGhostHeaderViewHolder(GhostHeaderViewHolder viewHolder, int sectionIndex) {
        if (USE_DEBUG_APPEARANCE) {
            viewHolder.itemView.setBackgroundColor(0xFF9999FF);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindFooterViewHolder(SectioningAdapter.FooterViewHolder viewHolder, int sectionIndex, int footerType) {
        Section s = sections.get(sectionIndex);
        FooterViewHolder fvh = (FooterViewHolder) viewHolder;
        fvh.textView.setText(s.footer);
        fvh.adapterPositionTextView.setText(Integer.toString(getAdapterPositionForSectionFooter(sectionIndex)));
    }

    private String pad(int spaces) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < spaces; i++) {
            b.append(' ');
        }
        return b.toString();
    }

}
