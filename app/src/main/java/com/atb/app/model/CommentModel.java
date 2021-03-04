package com.atb.app.model;

import java.util.ArrayList;

public class CommentModel {
    int id;
    String comment="";
    String userName;
    boolean like;
    ArrayList<CommentModel>replies = new ArrayList<>();
    int level =0;
    int parentPosstion =0;
    ArrayList<String>image_url = new ArrayList<>();
    public int getId() {
        return id;
    }

    public int getParentPosstion() {
        return parentPosstion;
    }

    public void setParentPosstion(int parentPosstion) {
        this.parentPosstion = parentPosstion;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public ArrayList<CommentModel> getReplies() {
        return replies;
    }

    public void setReplies(ArrayList<CommentModel> replies) {
        this.replies = replies;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public ArrayList<String> getImage_url() {
        return image_url;
    }

    public void setImage_url(ArrayList<String> image_url) {
        this.image_url.clear();
        this.image_url.addAll(image_url);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
