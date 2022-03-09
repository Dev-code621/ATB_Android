package com.atb.app.activities.chat.view;

import android.util.Log;

import com.atb.app.commons.Commons;
import com.atb.app.model.RoomModel;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class MessageBuilder {
    private String text;
    private long timetoken;
    String messageType;
    public static MessageBuilder newBuilder() {
        return new MessageBuilder();
    }
    private MessageBuilder() {
    }

    public MessageBuilder text(String text,String messageType) {
        this.text = text;
        this.messageType = messageType;
        return this;
    }

    public MessageBuilder timetoken(long timetoken) {
        this.timetoken = timetoken;
        return this;
    }

    public JsonObject build() {
        return generate();
    }

    private JsonObject generate() {
        String json = new Gson().toJson(this);
        JsonObject payload = new JsonParser().parse(json).getAsJsonObject();
        JsonObject sender = new JsonObject();

        sender.addProperty("id", Commons.senderID);
        sender.addProperty("name", Commons.senderName);
        sender.addProperty("imageUrl", Commons.senderImage);
        payload.add("sender" , sender);
        payload.addProperty("messageType" , messageType);

        return payload;

//        JsonObject payload = new JsonParser().parse(json).getAsJsonObject();
//        payload.addProperty("senderId", Commons.senderID);
//        payload.addProperty("senderName", Commons.senderName);
//        payload.addProperty("senderImage", Commons.senderImage);
//        payload.addProperty("messageType" , messageType);
//        JsonObject pn_gcm = new JsonObject();
//        JsonObject notification = new JsonObject();
//        notification.addProperty("title","You got new message from " + Commons.senderName);
//        if(messageType.equals("Image"))
//            notification.addProperty("body","Image sent");
//        else{
//            notification.addProperty("body",text);
//        }
//        notification.addProperty("sound","default");
//        pn_gcm.add("notification",notification);
//
//        JsonObject data = new JsonObject();
//        data.addProperty("title","New Message");
//        data.addProperty("body",text);
//        data.addProperty("senderId",Commons.senderID);
//        data.addProperty("type","100");
//        Gson gson = new Gson();
//        String room = gson.toJson(roomModel);
//        data.addProperty("related_id",room);
//        pn_gcm.add("data",data);
//        pn_gcm.addProperty("priority","high");
//        payload.add("pn_gcm",pn_gcm);
    }
}
