package com.atb.app.fragement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.navigationItems.NotificationActivity;
import com.atb.app.activities.profile.ProfileBusinessNaviagationActivity;
import com.atb.app.activities.profile.ProfileUserNavigationActivity;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

public class ChatFragment extends Fragment implements View.OnClickListener {
    View view;
    LinearLayout lyt_chatprofile,lyt_profile;
    FrameLayout frame_noti;
    ImageView imv_notification,imv_profile,imv_chat;
    CardView card_unread_noti;
    TextView txv_name;
    ListView list_chat;
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        list_chat = (ListView)view.findViewById(R.id.list_chat);
        lyt_chatprofile = (LinearLayout) view.findViewById(R.id.lyt_chatprofile);
        lyt_profile = (LinearLayout) view.findViewById(R.id.lyt_profile);
        frame_noti = (FrameLayout) view.findViewById(R.id.frame_noti);
        imv_notification = (ImageView) view.findViewById(R.id.imv_notification);
        card_unread_noti = (CardView) view.findViewById(R.id.card_unread_noti);
        imv_profile = (ImageView) view.findViewById(R.id.imv_profile);
        imv_chat = (ImageView) view.findViewById(R.id.imv_chat);
        txv_name = (TextView) view.findViewById(R.id.txv_name);

        frame_noti.setOnClickListener(this);
        lyt_profile.setOnClickListener(this);
        lyt_chatprofile.setOnClickListener(this);

        if(Commons.g_user.getAccount_type() ==1 ){
            Glide.with(this).load(Commons.g_user.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(context, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
        }else {
            Glide.with(this).load(Commons.g_user.getImvUrl()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(context, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
        }
        setProfile(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frame_noti:
                ((CommonActivity)context).goTo(context, NotificationActivity.class,false);
                break;
            case R.id.lyt_profile:
                if(context instanceof MainActivity) {
                    if (Commons.g_user.getAccount_type() == 1)
                        startActivityForResult(new Intent(context, ProfileBusinessNaviagationActivity.class), 1);
                    else
                        ((CommonActivity) context).goTo(context, ProfileUserNavigationActivity.class, false);
                }else {
                    if(context instanceof ProfileBusinessNaviagationActivity)
                    ((ProfileBusinessNaviagationActivity)context).setColor(0);
                    else  if(context instanceof ProfileUserNavigationActivity)
                        ((ProfileUserNavigationActivity)context).setColor(0);
                }
                break;
            case R.id.lyt_chatprofile:
                if(Commons.g_user.getAccount_type()==1)
                    ((CommonActivity)context).SelectprofileDialog(context);
                break;
        }
    }

    public void setProfile(boolean flag){
        if(flag ){
            Glide.with(this).load(Commons.g_user.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(context, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_chat);
            txv_name.setText(Commons.g_user.getBusinessModel().getBusiness_name());
        }else {
            Glide.with(this).load(Commons.g_user.getImvUrl()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(context, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_chat);
            txv_name.setText(Commons.g_user.getUserName());
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        context =getActivity();
    }

}