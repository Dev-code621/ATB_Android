package com.atb.app.model;

import android.util.Log;

import com.atb.app.commons.Commons;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommentModel {
    int id,postid,commenter_user_id;
    String comment="";
    String userName,read_created,userImage;
    ArrayList<CommentModel>replies = new ArrayList<>();
    int level =1;
    int parentPosstion =0;
    ArrayList<String>image_url = new ArrayList<>();
    boolean like,hidden;

    public int getCommenter_user_id() {
        return commenter_user_id;
    }

    public void setCommenter_user_id(int commenter_user_id) {
        this.commenter_user_id = commenter_user_id;
    }

    public String getRead_created() {
        return read_created;
    }

    public void setRead_created(String read_created) {
        this.read_created = read_created;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public int getPostid() {
        return postid;
    }

    public void setPostid(int postid) {
        this.postid = postid;
    }

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

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public void initModel(JSONObject jsonObject ,int parentnumber){
        try {
            setId(jsonObject.getInt("id"));
            if(jsonObject.has("post_id"))
                setPostid(jsonObject.getInt("post_id"));
            if(jsonObject.has("commenter_user_id"))
                setCommenter_user_id(jsonObject.getInt("commenter_user_id"));
            else
                setCommenter_user_id(jsonObject.getInt("reply_user_id"));
            if(jsonObject.has("comment"))
                setComment(jsonObject.getString("comment"));
            else
                setComment(jsonObject.getString("reply"));

            setComment(StringEscapeUtils.unescapeJava(getComment()));
            JSONArray imageData = jsonObject.getJSONArray("data");
            image_url.clear();
            for(int i =0;i<imageData.length();i++)
                image_url.add(imageData.getString(i));
            if(jsonObject.has("level"))setLevel(0);
            setUserName(jsonObject.getString("user_name"));
            setUserImage(jsonObject.getString("user_img"));
            setLike(jsonObject.getBoolean("liked"));
            setHidden(jsonObject.getBoolean("hidden"));
            setParentPosstion(parentnumber);
            setRead_created(jsonObject.getString("read_created"));
            JSONArray replies_array = jsonObject.getJSONArray("replies");
            for(int i =0;i<replies_array.length();i++){
                CommentModel commentModel = new CommentModel();
                commentModel.initModel(replies_array.getJSONObject(i),parentnumber);
                replies.add(commentModel);
            }

        }catch (Exception e){
            Log.d("Exception",e.toString());
        }
    }
}
