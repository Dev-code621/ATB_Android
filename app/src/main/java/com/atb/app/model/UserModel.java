package com.atb.app.model;

import android.util.Log;

import com.atb.app.activities.LoginActivity;
import com.atb.app.activities.MainActivity;
import com.atb.app.commons.Commons;
import com.atb.app.model.submodel.DisableSlotModel;
import com.atb.app.model.submodel.FeedInfoModel;
import com.atb.app.model.submodel.HolidayModel;
import com.atb.app.model.submodel.OpeningTimeModel;
import com.atb.app.model.submodel.SettingInfoModel;
import com.atb.app.model.submodel.SocialModel;
import com.atb.app.preference.PrefConst;
import com.atb.app.preference.Preference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public  class UserModel {

    int id, complete;
    String email, password, imvUrl, firstname, lastname, location, sex, birhtday, invitecode;
    String userName, fb_user_id, facebook_token, description, post_search_region= "null";
    String status_reason, stripe_customer_token, stripe_connect_account, push_tokenm, invited_by;
    double latitude, longitude;
    int range, account_type, status, online, post_count, followers_count, follow_count;
    Long update_at, created_at;
    BusinessModel businessModel = new BusinessModel();
    SettingInfoModel settingInfoModel = new SettingInfoModel();
    ArrayList<FeedInfoModel>feedInfoModels = new ArrayList<>();
    ArrayList<FollowerModel>followerModels = new ArrayList<>();
    String bt_customer_id,bt_paypal_account;

    public void setBt_customer_id(String bt_customer_id) {
        this.bt_customer_id = bt_customer_id;
    }

    public String getBt_customer_id() {
        return bt_customer_id;
    }

    public ArrayList<FollowerModel> getFollowerModels() {
        return followerModels;
    }

    public void setFollowerModels(ArrayList<FollowerModel> followerModels) {
        this.followerModels = followerModels;
    }

    public String getBt_paypal_account() {
        return bt_paypal_account;
    }

    public void setBt_paypal_account(String bt_paypal_account) {
        this.bt_paypal_account = bt_paypal_account;
    }

    public SettingInfoModel getSettingInfoModel() {
        return settingInfoModel;
    }

    public void setSettingInfoModel(SettingInfoModel settingInfoModel) {
        this.settingInfoModel = settingInfoModel;
    }

    public ArrayList<FeedInfoModel> getFeedInfoModels() {
        return feedInfoModels;
    }

    public void setFeedInfoModels(ArrayList<FeedInfoModel> feedInfoModels) {
        this.feedInfoModels = feedInfoModels;
    }

    public BusinessModel getBusinessModel() {
        return businessModel;
    }

    public void setBusinessModel(BusinessModel businessModel) {
        this.businessModel = businessModel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImvUrl() {
        return imvUrl;
    }

    public void setImvUrl(String imvUrl) {
        this.imvUrl = imvUrl;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirhtday() {
        return birhtday;
    }

    public void setBirhtday(String birhtday) {
        this.birhtday = birhtday;
    }

    public String getInvitecode() {
        return invitecode;
    }

    public void setInvitecode(String invitecode) {
        this.invitecode = invitecode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFb_user_id() {
        return fb_user_id;
    }

    public void setFb_user_id(String fb_user_id) {
        this.fb_user_id = fb_user_id;
    }

    public String getFacebook_token() {
        return facebook_token;
    }

    public void setFacebook_token(String facebook_token) {
        this.facebook_token = facebook_token;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPost_search_region() {
        return post_search_region;
    }

    public void setPost_search_region(String post_search_region) {
        this.post_search_region = post_search_region;
    }

    public String getStatus_reason() {
        return status_reason;
    }

    public void setStatus_reason(String status_reason) {
        this.status_reason = status_reason;
    }

    public String getStripe_customer_token() {
        return stripe_customer_token;
    }

    public void setStripe_customer_token(String stripe_customer_token) {
        this.stripe_customer_token = stripe_customer_token;
    }

    public String getStripe_connect_account() {
        return stripe_connect_account;
    }

    public void setStripe_connect_account(String stripe_connect_account) {
        this.stripe_connect_account = stripe_connect_account;
    }

    public String getPush_tokenm() {
        return push_tokenm;
    }

    public void setPush_tokenm(String push_tokenm) {
        this.push_tokenm = push_tokenm;
    }

    public String getInvited_by() {
        return invited_by;
    }

    public void setInvited_by(String invited_by) {
        this.invited_by = invited_by;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getAccount_type() {
        return account_type;
    }

    public void setAccount_type(int account_type) {
        this.account_type = account_type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getPost_count() {
        return post_count;
    }

    public void setPost_count(int post_count) {
        this.post_count = post_count;
    }

    public int getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(int followers_count) {
        this.followers_count = followers_count;
    }

    public int getFollow_count() {
        return follow_count;
    }

    public void setFollow_count(int follow_count) {
        this.follow_count = follow_count;
    }

    public Long getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(Long update_at) {
        this.update_at = update_at;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }

    public void initModel(JSONObject userObject){
        try {
            setId(userObject.getInt("id"));
            setUserName(userObject.getString("user_name"));
            setComplete(userObject.getInt("complete"));
            setFb_user_id(userObject.getString("fb_user_id"));
            setFacebook_token(userObject.getString("facebook_token"));
            setEmail(userObject.getString("user_email"));
            setPassword(userObject.getString("user_password"));
            setImvUrl(userObject.getString("pic_url"));
            setFirstname(userObject.getString("first_name"));
            setLastname(userObject.getString("last_name"));
            setLocation(userObject.getString("country"));
            setBirhtday(userObject.getString("birthday"));
            setSex(userObject.getString("gender"));
            setDescription(userObject.getString("description"));
            setPost_search_region(userObject.getString("post_search_region"));
            setAccount_type(userObject.getInt("account_type"));
            setStatus(userObject.getInt("status"));
            setStatus_reason(userObject.getString("status_reason"));
            setOnline(userObject.getInt("online"));
            setUpdate_at(userObject.getLong("updated_at"));
            setCreated_at(userObject.getLong("created_at"));
//                userModel.setLatitude(userObject.getDouble("latitude"));
//                userModel.setLongitude(userObject.getDouble("longitude"));
            setStripe_customer_token(userObject.getString("stripe_customer_token"));
            setStripe_connect_account(userObject.getString("stripe_connect_account"));
            setPush_tokenm(userObject.getString("push_token"));
            setInvitecode(userObject.getString("invite_code"));
            setInvited_by(userObject.getString("invited_by"));
            setPost_count(userObject.getInt("post_count"));
            setFollow_count(userObject.getInt("follow_count"));
            setFollowers_count(userObject.getInt("followers_count"));
            if(userObject.has("business_info"))
                if(!userObject.getString("business_info").equals("null")){
                    JSONObject business_info = userObject.getJSONObject("business_info");
                    BusinessModel businessModel = new BusinessModel();
                    businessModel.initModel(business_info);
                    setBusinessModel(businessModel);
                }

        }catch (Exception e){
            Log.d("aaaaaaa",e.toString());
        }
    }
}