package com.atb.app.fragement;

import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.atb.app.R;
import com.atb.app.activities.MainActivity;
import com.atb.app.activities.chat.ChatActivity;
import com.atb.app.activities.navigationItems.NotificationActivity;
import com.atb.app.activities.profile.OtherUserProfileActivity;
import com.atb.app.activities.profile.ProfileBusinessNaviagationActivity;
import com.atb.app.activities.profile.ProfileUserNavigationActivity;
import com.atb.app.adapter.MessageAdapter;
import com.atb.app.base.BaseActivity;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.RoomModel;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.history.PNFetchMessageItem;
import com.pubnub.api.models.consumer.history.PNFetchMessagesResult;
import com.pubnub.api.models.consumer.history.PNMessageCountResult;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadata;
import com.pubnub.api.models.consumer.objects_api.channel.PNGetAllChannelsMetadataResult;

import com.google.gson.JsonObject;


import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChatFragment extends Fragment implements View.OnClickListener {
    View view;
    LinearLayout lyt_chatprofile,lyt_profile;
    FrameLayout frame_noti;
    ImageView imv_notification,imv_profile,imv_chat;
    CardView card_unread_noti;
    TextView txv_name;
    ListView list_chat;
    Context context;
    MessageAdapter messageAdapter ;
    List<RoomModel> roomModels = new ArrayList<>();
    List<PNChannelMetadata> channelMetadata = new ArrayList<>();
    List<String>channels = new ArrayList<>();
    List<Long>channels_LastReadTime = new ArrayList<>();
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

        if(Commons.userType ==1 ){
            Glide.with(this).load(Commons.g_user.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(context, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
        }else {
            Glide.with(this).load(Commons.g_user.getImvUrl()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(context, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
        }
        list_chat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Gson gson = new Gson();
                String usermodel = gson.toJson(roomModels.get(position));
                Bundle bundle = new Bundle();
                bundle.putString("roomModel",usermodel);
                ((CommonActivity)context).goTo(context, ChatActivity.class,false,bundle);
            }
        });

        if(Commons.notification_count>0)
            card_unread_noti.setVisibility(View.VISIBLE);
        else
            card_unread_noti.setVisibility(View.GONE);

    }

    public void setMessages(boolean flag){
        roomModels.clear();channels.clear();
        channels_LastReadTime.clear();
        for(int i = 0 ;i<channelMetadata.size();i++){

            PNChannelMetadata channel = channelMetadata.get(i);
            try {
                JsonObject custom = (JsonObject) channel.getCustom();
                RoomModel roomModel = new RoomModel();
                String str = channel.getId();
                String[] array = str.split("_");
                if(array.length<1)continue;
                String memberStr = custom.get("members").getAsString();
                JSONArray members = new JSONArray(memberStr);

                if(flag){
                    String business_account = String.valueOf(Commons.g_user.getId())+"#"+ String.valueOf(Commons.g_user.getBusinessModel().getId());
                    if(!str.contains(business_account))continue;
                    for(int j = 0 ;j<members.length();j++){
                        JSONObject member = members.getJSONObject(j);
                        if(!member.get("id").equals(Commons.senderID)){
                            roomModel.setName(member.getString("name"));
                            roomModel.setChannelId(channel.getId());
                            roomModel.setImage(member.getString("imageUrl"));
                            break;
                        }
                    }

                }else {
                    String arr[] = str.split("_");
                    boolean existFlag = false;
                    boolean adminChat = false;
                    for(int j = 0;j<arr.length;j++){
                        if(arr[j].equals(String.valueOf(Commons.g_user.getId()))){
                            existFlag =true;
                        }
                        if(arr[j].contains("ADMIN")){
                            adminChat = true;
                        }
                    }
                    if(existFlag){
                        for(int j = 0 ;j<members.length();j++){
                            JSONObject member = members.getJSONObject(j);
                            if(!member.get("id").equals(Commons.senderID)){
                                roomModel.setName(member.getString("name"));
                                if(adminChat){
                                    roomModel.setName(member.getString("name")+"(Admin)");
                                }
                                roomModel.setChannelId(channel.getId());
                                roomModel.setImage(member.getString("imageUrl"));
                                break;
                            }
                        }
                    }else{
                        continue;
                    }

                }

//                roomModel.setLastReadTimetoken(custom.get("lastReadTimetoken").getAsLong());
                roomModels.add(roomModel);
                channels_LastReadTime.add(roomModel.getLastReadTimetoken());
                channels.add(roomModel.getChannelId());
            }catch (Exception e){
                ((CommonActivity)(context)).closeProgress();

                Log.d("Exception==" ,e.toString());
            }

        }
        Commons.pubnub_channels = channels;
        Commons.mPubNub.fetchMessages()
                .channels(channels)
                .maximumPerChannel(1)
                .async(new PNCallback<PNFetchMessagesResult>() {
                    @Override
                    public void onResponse(@Nullable final PNFetchMessagesResult result, @NotNull final PNStatus status) {
                        ((CommonActivity)(context)).closeProgress();
                        try {

                            if (!status.isError()) {

                                final Map<String, List<PNFetchMessageItem>> channelToMessageItemsMap = result.getChannels();
                                for (RoomModel roomModel : roomModels) {
                                    List<PNFetchMessageItem> pnFetchMessageItems = channelToMessageItemsMap.get(URLEncoder.encode(roomModel.getChannelId()));
                                    try{
                                        for (final PNFetchMessageItem fetchMessageItem: pnFetchMessageItems) {
                                            if(fetchMessageItem.getMessage().getAsJsonObject().has("messageType") && fetchMessageItem.getMessage().getAsJsonObject().get("messageType").getAsString().equals("Image")){
                                                roomModel.setLast_message("Image sent");

                                            }else
                                                roomModel.setLast_message(fetchMessageItem.getMessage().getAsJsonObject().get("text").getAsString());
                                            roomModel.setLastMessageTime(fetchMessageItem.getTimetoken());
                                        }
                                    }catch (Exception e){

                                    }

                                }
                            }
                            else {
                                System.err.println("Handling error");
                            }
                        }catch (Exception e){

                        }

                        messageAdapter.setRoomData(roomModels);

                    }
                });

//        Commons.mPubNub.messageCounts()
//                .channels(channels)
//                .channelsTimetoken(channels_LastReadTime)
//                .async(new PNCallback<PNMessageCountResult>() {
//                    @Override
//                    public void onResponse(PNMessageCountResult result, PNStatus status) {
//                        ((CommonActivity)(context)).closeProgress();
//
//                        if (!status.isError()) {
//                            int index = 0;
//                            for (Map.Entry<String, Long> entry : result.getChannels().entrySet()) {
//                                entry.getKey(); // the channel name
//                                entry.getValue(); // number of messages for that channel
//                                roomModels.get(index).setUnReadCount(entry.getValue());
//                                index ++;
//                            }
//                        }
//                        else {
//                            status.getErrorData().getThrowable().printStackTrace();
//                        }
//                        messageAdapter.setRoomData(roomModels);
//
//                    }
//                });
//
//
//        messageAdapter.setRoomData(roomModels);

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
        Commons.profile_flag = flag;
//        ((CommonActivity)context).loginPubNub(flag);


        if(flag ){
            Glide.with(this).load(Commons.g_user.getBusinessModel().getBusiness_logo()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(context, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_chat);
            txv_name.setText(Commons.g_user.getBusinessModel().getBusiness_name());
            Commons.senderImage = Commons.g_user.getBusinessModel().getBusiness_logo();
            Commons.senderName = Commons.g_user.getBusinessModel().getBusiness_name();
            Commons.senderID = "business_" + String.valueOf(Commons.g_user.getBusinessModel().getId());


        }else {
            Glide.with(this).load(Commons.g_user.getImvUrl()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                    new RoundedCornersTransformation(context, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_chat);
            txv_name.setText(Commons.g_user.getUserName());
            Commons.senderID = "user_" + String.valueOf(Commons.g_user.getId());

            Commons.senderImage = Commons.g_user.getImvUrl();
            Commons.senderName = Commons.g_user.getFirstname() + " " + Commons.g_user.getLastname();
        }

        ((CommonActivity)(context)).showProgress();
        String filter = "name LIKE " + "'" +  Commons.g_user.getId() + "_*'" + "||" +  "name LIKE " + "'*_" + Commons.g_user.getId() + "'";
        if(flag){
            filter = "name LIKE " + "'" +  Commons.g_user.getId() + "#" + Commons.g_user.getBusinessModel().getId() + "_*'" + "||" +  "name LIKE " + "'*_" + Commons.g_user.getId() +  "#" + Commons.g_user.getBusinessModel().getId() +"'";
        }
        Commons.mPubNub.getAllChannelsMetadata()
                .includeCustom(true)
                .filter(filter)
                .async(new PNCallback<PNGetAllChannelsMetadataResult>() {
                    @Override
                    public void onResponse(@Nullable final PNGetAllChannelsMetadataResult result, @NotNull final PNStatus status) {

                        if (status.isError()) {
                            ((CommonActivity)(context)).closeProgress();

                        } else {
                            channelMetadata.clear();
                            channelMetadata = result.getData();
                           setMessages(flag);

                        }
                    }
                });





    }
    @Override
    public void onResume() {
        super.onResume();
        context =getActivity();
        messageAdapter = new MessageAdapter(context);
        list_chat.setAdapter(messageAdapter);
        if(roomModels.size()==0)
            setProfile(Commons.profile_flag);
    }

}