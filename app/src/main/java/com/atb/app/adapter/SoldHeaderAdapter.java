package com.atb.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.android.volley.Response;
import com.atb.app.R;
import com.atb.app.activities.navigationItems.ItemSoldActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.TransactionEntity;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class SoldHeaderAdapter extends SectioningAdapter {

    static final String TAG = SoldHeaderAdapter.class.getSimpleName();
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
        ImageView imv_image,imv_profile;
        TextView txv_name,txv_itemnumber,txv_price,txv_time;

        public ItemViewHolder(View itemView) {
            super(itemView);
            imv_image =  itemView.findViewById(R.id.imv_image);
            imv_profile =  itemView.findViewById(R.id.imv_profile);
            txv_name =  itemView.findViewById(R.id.txv_name);
            txv_itemnumber =  itemView.findViewById(R.id.txv_itemnumber);
            txv_price =  itemView.findViewById(R.id.txv_price);
            txv_time =  itemView.findViewById(R.id.txv_time);

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

            if (!SoldHeaderAdapter.this.showAdapterPositions) {
                adapterPositionTextView.setVisibility(View.GONE);
            }
        }
    }


    ArrayList<Section> sections = new ArrayList<>();
    boolean showModificationControls;
    boolean showCollapsingSectionControls;
    boolean showAdapterPositions;
    boolean hasFooters;
    ArrayList<TransactionEntity> _roomDatas = new ArrayList<>();
    HashMap<String, ArrayList<TransactionEntity> > hashMap = new HashMap();
    public SoldHeaderAdapter(Context context,boolean hasFooters, boolean showModificationControls, boolean showCollapsingSectionControls, boolean showAdapterPositions,ArrayList<TransactionEntity> data) {
        this._context = context;
        this.showModificationControls = showModificationControls;
        this.showCollapsingSectionControls = showCollapsingSectionControls;
        this.showAdapterPositions = showAdapterPositions;
        this.hasFooters = hasFooters;
        if(data.size()>0) {
            _roomDatas = data;
            for (int i = 0; i < _roomDatas.size(); i++) {
                String key = Commons.getMonths(_roomDatas.get(i).getCreated_at());
                if(hashMap.get(key)==null){
                    hashMap.put(key,new ArrayList<>());
                }
                hashMap.get(key).add(_roomDatas.get(i));
            }

            int index = 0;
            for ( String hash_key : hashMap.keySet() ) {
                appendSection(index,hash_key,hashMap.get(hash_key).size());
                index++;
            }
        }
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
        View v = inflater.inflate(R.layout.list_item_simple_item, parent, false);
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindItemViewHolder(SectioningAdapter.ItemViewHolder viewHolder, int sectionIndex, int itemIndex, int itemType) {
        Section s = sections.get(sectionIndex);
        ItemViewHolder holder = (ItemViewHolder) viewHolder;
//        ivh.textView.setText(s.items.get(itemIndex));
//        ivh.adapterPositionTextView.setText(Integer.toString(getAdapterPositionForSectionItem(sectionIndex, itemIndex)));
        int index =0;
        for ( String hash_key : hashMap.keySet() ) {
            if(sectionIndex==index){
                final TransactionEntity transactionEntity= hashMap.get(hash_key).get(itemIndex);
                Glide.with(_context).load(transactionEntity.getImv_url()).placeholder(R.drawable.image_thumnail).dontAnimate().apply(RequestOptions.bitmapTransform(
                        new RoundedCornersTransformation(_context, 20, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(holder.imv_image);
                holder.txv_name.setText(transactionEntity.getTitle());
                holder.txv_price.setText("£" + String.valueOf(Math.abs(transactionEntity.getAmount())));
                holder.txv_time.setText(Commons.getDisplayDate4(transactionEntity.getCreated_at()) +  " ORDER " + transactionEntity.getTransaction_id());

            }
            index++;
        }
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
    public void onBindGhostHeaderViewHolder(SectioningAdapter.GhostHeaderViewHolder viewHolder, int sectionIndex) {
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
