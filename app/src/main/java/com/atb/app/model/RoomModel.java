package com.atb.app.model;

public class RoomModel {
    String channelId, image, name,last_message ;
    boolean isOnline = false;
    Long lastReadTimetoken;
    Long unReadCount = 0l,lastMessageTime = 0l;
    int type, anotherUserID;
    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public Long getLastReadTimetoken() {
        return lastReadTimetoken;
    }

    public void setLastReadTimetoken(Long lastReadTimetoken) {
        this.lastReadTimetoken = lastReadTimetoken;
    }

    public Long getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(Long unReadCount) {
        this.unReadCount = unReadCount;
    }

    public Long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(Long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAnotherUserID() {
        return anotherUserID;
    }

    public void setAnotherUserID(int anotherUserID) {
        this.anotherUserID = anotherUserID;
    }
}
