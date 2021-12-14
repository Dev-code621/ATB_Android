package com.atb.app.activities.chat;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.atb.app.R;

import com.atb.app.activities.chat.view.EmptyView;
import com.atb.app.activities.chat.view.MessageBuilder;
import com.atb.app.activities.chat.view.MessageComposer;
import com.atb.app.base.CommonActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.RoomModel;
import com.atb.app.util.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNOperationType;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.history.PNHistoryItemResult;
import com.pubnub.api.models.consumer.history.PNHistoryResult;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadata;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.channel.PNGetChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.ConsoleHandler;

public class ChatActivity extends CommonActivity implements View.OnClickListener, MessageComposer.Listener , SwipeRefreshLayout.OnRefreshListener {
    ImageView imv_back,imv_profile;
    TextView txv_name;
    RoomModel roomModel = new RoomModel();
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mChatsRecyclerView;
    EmptyView chat_empty_view;
    MessageComposer mMessageComposer;
    private RecyclerView.OnScrollListener mOnScrollListener;
    List<MessageModel> messageModels = new ArrayList<>();
    ChatAdapter chatAdapter ;
    PNChannelMetadata metadata;
    long newMessageTimetoken;
    private SubscribeCallback mPubNubListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);
        imv_back = findViewById(R.id.imv_back);
        imv_back.setOnClickListener(this);
        imv_profile = findViewById(R.id.imv_profile);
        txv_name = findViewById(R.id.txv_name);
        mSwipeRefreshLayout = findViewById(R.id.chat_swipe);
        mChatsRecyclerView = findViewById(R.id.chat_recycler_view);
        chat_empty_view = findViewById(R.id.chat_empty_view);
        mMessageComposer = findViewById(R.id.chats_message_composer);
        mMessageComposer.setListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mChatsRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mMessageComposer.hideKeyboard();
                return false;
            }
        });
        if (getIntent() != null) {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle != null) {
                String user= bundle.getString("roomModel");
                Gson gson = new Gson();
                roomModel = gson.fromJson(user, RoomModel.class);
                initlayout();
                getChannelMetadata(roomModel.getChannelId());
            }
        }

    }

    void initlayout(){
        chatAdapter = new ChatAdapter( this );
        mChatsRecyclerView.setLayoutManager( new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mChatsRecyclerView.setAdapter(chatAdapter);
        Glide.with(_context).load(roomModel.getImage()).placeholder(R.drawable.profile_pic).dontAnimate().apply(RequestOptions.bitmapTransform(
                new RoundedCornersTransformation(_context, Commons.glide_radius, Commons.glide_magin, "#A8C3E7", Commons.glide_boder))).into(imv_profile);
        txv_name.setText(roomModel.getName());

        mSwipeRefreshLayout.setRefreshing(true);

        fetchHistory(0);
        subscribe();
        initListener();

    }

    // tag::SUB-2[]
    private void initListener() {
        mPubNubListener = new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
                if (status.getOperation() == PNOperationType.PNSubscribeOperation && status.getAffectedChannels()
                        .contains(roomModel.getChannelId())) {
                    mSwipeRefreshLayout.setRefreshing(false);
                    fetchHistory(0);
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("aaaaaaa",message.toString());
                            JsonObject jsonObject = (JsonObject)message.getMessage();
                            MessageModel messageModel  = new MessageModel();
                            messageModel.setSenderId(jsonObject.get("senderId").getAsString());
                            messageModel.setSenderImage(jsonObject.get("senderImage").getAsString());
                            messageModel.setSenderName(jsonObject.get("senderName").getAsString());
                            messageModel.setMessage(jsonObject.get("text").getAsString());
                            messageModel.setCreateAt(message.getTimetoken());
                            messageModels.add(messageModel);
                            chatAdapter.setData(messageModels);
                            scrollChatToBottom();
                        }});

            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
                if (presence.getChannel().equals(roomModel.getChannelId())) {
                    int members = presence.getOccupancy();
//                    runOnUiThread(() -> hostActivity.setSubtitle(fragmentContext.getResources()
//                            .getQuantityString(R.plurals.members_online, members, members)));
                }
            }

            @Override
            public void signal(@org.jetbrains.annotations.NotNull PubNub pubnub, @org.jetbrains.annotations.NotNull PNSignalResult pnSignalResult) {

            }

            @Override
            public void uuid(@org.jetbrains.annotations.NotNull PubNub pubnub, @org.jetbrains.annotations.NotNull PNUUIDMetadataResult pnUUIDMetadataResult) {

            }

            @Override
            public void channel(@org.jetbrains.annotations.NotNull PubNub pubnub, @org.jetbrains.annotations.NotNull PNChannelMetadataResult pnChannelMetadataResult) {

            }

            @Override
            public void membership(@org.jetbrains.annotations.NotNull PubNub pubnub, @org.jetbrains.annotations.NotNull PNMembershipResult pnMembershipResult) {

            }

            @Override
            public void messageAction(@org.jetbrains.annotations.NotNull PubNub pubnub, @org.jetbrains.annotations.NotNull PNMessageActionResult pnMessageActionResult) {

            }

            @Override
            public void file(@org.jetbrains.annotations.NotNull PubNub pubnub, @org.jetbrains.annotations.NotNull PNFileEventResult pnFileEventResult) {

            }

        };
        Commons.mPubNub.addListener(mPubNubListener);
    }


    public void getChannelMetadata(String channelName ){
        Commons.mPubNub.getChannelMetadata()
                .channel(channelName)
                .includeCustom(true)
                .async(new PNCallback<PNGetChannelMetadataResult>() {
                    @Override
                    public void onResponse(@Nullable final PNGetChannelMetadataResult result, @NotNull final PNStatus status) {
                        if (status.isError()) {
                            //handle error
                        } else {
                            metadata = result.getData();
                        }
                    }
                });
    }


    private void scrollChatToBottom() {
        mChatsRecyclerView.scrollToPosition(messageModels.size() - 1);
    }

    private void fetchHistory(int flag) {
        mSwipeRefreshLayout.setRefreshing(true);
        Commons.mPubNub.history()
                .channel(roomModel.getChannelId()) // where to fetch history from
                .count(20) // how many items to fetch
                .start(getEarliestTimestamp()) // where to start
                .includeTimetoken(true)
                .async(new PNCallback<PNHistoryResult>() {
                    @Override
                    public void onResponse(PNHistoryResult result, PNStatus status) {
                        new Thread(() -> {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }});
                            if (!status.isError() && !result.getMessages().isEmpty()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ArrayList<MessageModel>arrayList = new ArrayList<>();
                                        for(int i =0;i<result.getMessages().size();i++){
                                            PNHistoryItemResult message = result.getMessages().get(i);
                                            JsonObject jsonObject = (JsonObject)message.getEntry();
                                            MessageModel messageModel  = new MessageModel();
                                            messageModel.setSenderId(jsonObject.get("senderId").getAsString());
                                            messageModel.setSenderImage(jsonObject.get("senderImage").getAsString());
                                            messageModel.setSenderName(jsonObject.get("senderName").getAsString());
                                            messageModel.setMessage(jsonObject.get("text").getAsString());
                                            messageModel.setCreateAt(message.getTimetoken());

                                            arrayList.add(messageModel);
                                        }
                                        messageModels.addAll(0,arrayList);
                                        chatAdapter.setData(messageModels);
                                        if(flag ==0)
                                            scrollChatToBottom();
                                    }
                                });

                                
                            } else {

                            }
                        }).start();

                    }
                });
    }


    @Override
    public void onRefresh() {
        fetchHistory(1);

    }
    private Long getEarliestTimestamp() {
        if (messageModels.size()>0) {
            return messageModels.get(0).getCreateAt();
        }
        return null;
    }
    // end::HIS-1[]
    private void subscribe() {
        Commons.mPubNub
                .subscribe()
                .channels(Collections.singletonList(roomModel.getChannelId()))
                .withPresence()
                .execute();
    }

    // tag::SEND-2[]
    @Override
    public void onSentClick(String message) {
        if(message.length()==0) {
            showToast("Please input message");
            return;
        }

        String finalMessage = message;

        Commons.mPubNub
                .publish()
                .channel(roomModel.getChannelId())
                .shouldStore(true)
                .message(MessageBuilder.newBuilder().text(message).build())
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        if (!status.isError()) {
                            newMessageTimetoken = result.getTimetoken();
//                            MessageModel messageModel  = new MessageModel();
//                            messageModel.setSenderId(Commons.senderID);
//                            messageModel.setSenderImage(Commons.senderImage);
//                            messageModel.setSenderName(Commons.senderName);
//                            messageModel.setMessage(message);
//                            messageModel.setCreateAt(newMessageTimetoken);
//                            messageModels.add(messageModel);
//                            chatAdapter.setData(messageModels);
//                            scrollChatToBottom();
                        } else {

                        }
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imv_back:
                finish(this);
                break;
            case R.id.chat_recycler_view:
                Log.d("bafafafafafaf","afafafafaf");
                mMessageComposer.hideKeyboard();

                break;
        }
    }


    @Override
    protected void onDestroy() {
        Commons.mPubNub.removeListener(mPubNubListener);

        Map<String, Object> custom = new HashMap<>();
        JsonObject jsonObject = (JsonObject) metadata.getCustom();
        custom.put("owner_id",jsonObject.get("owner_id").getAsInt());
        custom.put("owner_image", jsonObject.get("owner_image").getAsString());
        custom.put("owner_name",jsonObject.get("owner_name").getAsString());
        custom.put("other_image", jsonObject.get("other_image").getAsString());
        custom.put("other_name",jsonObject.get("other_name").getAsString());
        custom.put("lastReadTimetoken",newMessageTimetoken);
        setChannelMetadata(metadata.getId(),metadata.getName(),String.valueOf(newMessageTimetoken),custom);
        super.onDestroy();
    }
}