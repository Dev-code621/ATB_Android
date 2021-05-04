package com.atb.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.atb.app.R;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.BoostModel;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;
import org.zakariya.stickyheaders.SectioningAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;

public class ProfilePinHeaderAdapter extends SectioningAdapter {

    static final String TAG = ProfilePinHeaderAdapter.class.getSimpleName();
    static final boolean USE_DEBUG_APPEARANCE = false;
    int type;
    Context context;
    int county = 0,region =0;
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
        ImageView imv_profile;
        TextView txv_bidnumber,txv_price,txv_number,txv_bid;
        EditText txv_bidprice;
        CardView card_bidnumber;

        public ItemViewHolder(View itemView) {
            super(itemView);
            card_bidnumber = itemView.findViewById(R.id.card_bidnumber);
            txv_bidnumber =  itemView.findViewById(R.id.txv_bidnumber);
            imv_profile =  itemView.findViewById(R.id.imv_profile);
            txv_number =  itemView.findViewById(R.id.txv_number);
            txv_bidprice =  itemView.findViewById(R.id.txv_bidprice);
            txv_price =  itemView.findViewById(R.id.txv_price);
            txv_bid =  itemView.findViewById(R.id.txv_bid);


        }
    }

    public class HeaderViewHolder extends SectioningAdapter.HeaderViewHolder {
        TextView textView;
        NiceSpinner spiner;
        LinearLayout lyt_profile_pin,lyt_currentbid,lyt_yourbid,lyt_pin_point;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            spiner = (NiceSpinner)itemView.findViewById(R.id.spiner);
            lyt_profile_pin = (LinearLayout)itemView.findViewById(R.id.lyt_profile_pin);
            lyt_currentbid = (LinearLayout)itemView.findViewById(R.id.lyt_currentbid);
            lyt_yourbid = (LinearLayout)itemView.findViewById(R.id.lyt_yourbid);
            lyt_pin_point = (LinearLayout)itemView.findViewById(R.id.lyt_pin_point);
            if(type ==1){
                lyt_profile_pin.setVisibility(View.GONE);
                lyt_pin_point.setVisibility(View.VISIBLE);
            }else {
                lyt_profile_pin.setVisibility(View.VISIBLE);
                lyt_pin_point.setVisibility(View.GONE);
            }
        }
    }

    public class FooterViewHolder extends SectioningAdapter.FooterViewHolder {
        TextView textView;
        TextView adapterPositionTextView;

        public FooterViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            adapterPositionTextView = (TextView) itemView.findViewById(R.id.adapterPositionTextView);

            if (!ProfilePinHeaderAdapter.this.showAdapterPositions) {
                adapterPositionTextView.setVisibility(View.GONE);
            }
        }
    }


    ArrayList<Section> sections = new ArrayList<>();
    boolean showModificationControls;
    boolean showCollapsingSectionControls;
    boolean showAdapterPositions;
    boolean hasFooters;
    ArrayList<BoostModel>room_data = new ArrayList<>();
    public ProfilePinHeaderAdapter(int numSections, int numItemsPerSection, boolean hasFooters, boolean showModificationControls, boolean showCollapsingSectionControls, boolean showAdapterPositions, int type, Context context) {
        this.context = context;
        this.showModificationControls = showModificationControls;
        this.showCollapsingSectionControls = showCollapsingSectionControls;
        this.showAdapterPositions = showAdapterPositions;
        this.hasFooters = hasFooters;
        this.type = type;
        for (int i = 0; i < numSections; i++) {
            appendSection(i, numItemsPerSection);
        }
    }

    public void setRoomData(ArrayList<BoostModel>boostModels){
        room_data.clear();
        room_data.addAll(boostModels);
        notifyAllSectionsDataSetChanged();
    }

    public void setRoomData(ArrayList<BoostModel>boostModels,int county , int region){
        room_data.clear();
        room_data.addAll(boostModels);
        this.county = county;
        this.region= region;
        notifyAllSectionsDataSetChanged();
    }

    void appendSection(int index, int itemCount) {
        Section section = new Section();
        section.index = index;
        section.copyCount = 0;

        String str = "Bid By Country";
        if (index == 1)
            str = "Bid By County";
        else if (index == 2)
            str = "Bid By Region";
        section.header = str;

//
//        if (this.hasFooters) {
//            section.footer = "End of section " + index;
//        }

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
        View v = inflater.inflate(R.layout.boost_simple_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.profile_pin_header, parent, false);
        return new HeaderViewHolder(v);
    }

    @Override
    public FooterViewHolder onCreateFooterViewHolder(ViewGroup parent, int footerType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_item_simple_footer, parent, false);
        return new FooterViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindItemViewHolder(SectioningAdapter.ItemViewHolder viewHolder, int sectionIndex, int itemIndex, int itemType) {
        Section s = sections.get(sectionIndex);
        ItemViewHolder ivh = (ItemViewHolder) viewHolder;
        ivh.card_bidnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CommonActivity)(context)).showToolTip("Current Bids",v,true);
            }
        });
        if(type ==1){
            BoostModel boostModel = new BoostModel();
            boostModel.setTotal_bids(0);
            boostModel.setPrice("5.0");
            boostModel.setPosition(itemIndex);
            for(int i =0;i<room_data.size();i++){
                if(room_data.get(i).getPosition() == itemIndex){
                    boostModel = room_data.get(i);
                    break;
                }
            }
            ivh.txv_number.setText(String.valueOf(boostModel.getTotal_bids()));
            ivh.txv_price.setText("£" + boostModel.getPrice());
            ivh.txv_bidnumber.setText(String.valueOf(boostModel.getPosition()+1));
            Glide.with(context).load(boostModel.getUserModel().getBusinessModel().getBusiness_logo()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(context, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(ivh.imv_profile);
            ivh.txv_bid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CommonActivity)(context)).placeBid(itemIndex,ivh.txv_bidprice.getText().toString());
                }
            });
        }else if(type ==0){
            BoostModel boostModel = new BoostModel();
            boostModel.setTotal_bids(0);
            boostModel.setPrice("5.0");
            boostModel.setPosition(itemIndex);
            if(room_data.size()>0) {
                if(room_data.get(sectionIndex * 2 + itemIndex).isEmptyModel())
                  boostModel = room_data.get(sectionIndex * 2 + itemIndex);
            }
            ivh.txv_number.setText(String.valueOf(boostModel.getTotal_bids()));
            ivh.txv_price.setText("£" + boostModel.getPrice());
            ivh.txv_bidnumber.setText(String.valueOf(boostModel.getPosition()+1));
            Glide.with(context).load(boostModel.getUserModel().getBusinessModel().getBusiness_logo()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(context, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(ivh.imv_profile);
            ivh.txv_bid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CommonActivity)(context)).placeBid(sectionIndex*2+ itemIndex,ivh.txv_bidprice.getText().toString());
                }
            });
        }
//        ivh.textView.setText(s.items.get(itemIndex));
//        ivh.adapterPositionTextView.setText(Integer.toString(getAdapterPositionForSectionItem(sectionIndex, itemIndex)));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindHeaderViewHolder(SectioningAdapter.HeaderViewHolder viewHolder, int sectionIndex, int headerType) {
        Section s = sections.get(sectionIndex);
        final HeaderViewHolder hvh = (HeaderViewHolder) viewHolder;
        hvh.lyt_currentbid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CommonActivity)(context)).showToolTip(context.getResources().getString(R.string.current_bid),v,false);
            }
        });
        hvh.lyt_yourbid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CommonActivity)(context)).showToolTip(context.getResources().getString(R.string.your_bid),v,false);

            }
        });
        if (USE_DEBUG_APPEARANCE) {
            hvh.textView.setText(pad(sectionIndex * 2) + s.header);
            viewHolder.itemView.setBackgroundColor(0x55FF9999);
        } else {
            hvh.textView.setText(s.header);
        }

        if(sectionIndex ==0){
            hvh.spiner.setText("United Kingdom");
            List<String> dataset = new LinkedList<>(Arrays.asList("United Kingdom"));
            hvh.spiner.attachDataSource(dataset);
          //  Log.d("aaaaaaaaaaa",String.valueOf(dataset));
        }
        else if(sectionIndex==1){
            hvh.spiner.attachDataSource(Commons.county);
            hvh.spiner.setSelectedIndex(county);
          //  Log.d("bbbbbbbbb",Commons.county.get(county)  +  "   "  +String.valueOf(Commons.region.get(Commons.county.get(county))));

        }else if(sectionIndex==2) {
         //   Log.d("bbbbbbbbb",Commons.county.get(county)  +  "   "  +String.valueOf(Commons.region.get(Commons.county.get(county))));
            hvh.spiner.attachDataSource(Commons.region.get(Commons.county.get(county)));
            hvh.spiner.setSelectedIndex(region);
        }

        hvh.spiner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                if(sectionIndex == 1) {
                    county = position;
                    region = 0;
                    ((CommonActivity)(context)).getAction(county,0);
                }else if(sectionIndex ==2){
                    ((CommonActivity)(context)).getAction(county,position);
                }
            }
        });

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
