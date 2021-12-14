package com.atb.app.activities.chat.view;

import com.atb.app.commons.Commons;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MessageBuilder {
    private String text;
    private long timetoken;
    public static MessageBuilder newBuilder() {
        return new MessageBuilder();
    }
    private MessageBuilder() {
    }

    public MessageBuilder text(String text) {
        this.text = text;
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
        payload.addProperty("timetoken", timetoken);
        payload.addProperty("senderId", Commons.senderID);
        payload.addProperty("senderName", Commons.senderName);
        payload.addProperty("senderImage", Commons.senderImage);
        return payload;
    }
}
