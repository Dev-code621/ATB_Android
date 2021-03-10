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

import java.util.ArrayList;

public class UserModel {
    int id, complete,bt_customer_id;
    String email, password, imvUrl, firstname, lastname, location, sex, birhtday, invitecode,bt_paypal_account;
    String userName, fb_user_id, facebook_token, description, post_search_region;
    String status_reason, stripe_customer_token, stripe_connect_account, push_tokenm, invited_by;
    double latitude, longitude;
    int range, account_type, status, online, post_count, followers_count, follow_count;
    Long update_at, created_at;
    BusinessModel businessModel = new BusinessModel();
    SettingInfoModel settingInfoModel = new SettingInfoModel();
    ArrayList<FeedInfoModel>feedInfoModels = new ArrayList<>();

    public int getBt_customer_id() {
        return bt_customer_id;
    }

    public void setBt_customer_id(int bt_customer_id) {
        this.bt_customer_id = bt_customer_id;
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
            if(!userObject.getString("business_info").equals("null")){
                JSONObject business_info = userObject.getJSONObject("business_info");
                BusinessModel businessModel = new BusinessModel();
                businessModel.setId(business_info.getInt("id"));
                businessModel.setUser_id(business_info.getInt("user_id"));
                businessModel.setBusiness_logo(business_info.getString("business_logo"));
                businessModel.setBusiness_name(business_info.getString("business_name"));
                businessModel.setBusiness_website(business_info.getString("business_website"));
                businessModel.setBusiness_bio(business_info.getString("business_bio"));
                businessModel.setBusiness_profile_name(business_info.getString("business_profile_name"));
                businessModel.setPaid(business_info.getInt("paid"));
                businessModel.setApproved(business_info.getInt("approved"));
                businessModel.setApproval_reason(business_info.getString("approval_reason"));
                businessModel.setUpdated_at(business_info.getLong("updated_at"));
                businessModel.setCreated_at(business_info.getLong("created_at"));
                JSONArray opening_times = business_info.getJSONArray("opening_times");
                for(int i =0;i<opening_times.length();i++){
                    JSONObject opening_time = opening_times.getJSONObject(i);
                    OpeningTimeModel openingTimeModel = new OpeningTimeModel();
                    openingTimeModel.setId(opening_time.getInt("id"));
                    openingTimeModel.setUser_id(opening_time.getInt("user_id"));
                    openingTimeModel.setDay(opening_time.getInt("day"));
                    openingTimeModel.setIs_available(opening_time.getInt("is_available"));
                    openingTimeModel.setStart(opening_time.getString("start"));
                    openingTimeModel.setEnd(opening_time.getString("end"));
                    openingTimeModel.setUpdated_at(opening_time.getLong("updated_at"));
                    openingTimeModel.setCreated_at(opening_time.getLong("created_at"));
                    businessModel.getOpeningTimeModels().add(openingTimeModel);
                }
                JSONArray holidays = business_info.getJSONArray("holidays");
                for(int i =0;i<holidays.length();i++){
                    JSONObject holiday = holidays.getJSONObject(i);
                    HolidayModel holidayModel = new HolidayModel();
                    holidayModel.setId(holiday.getInt("id"));
                    holidayModel.setUser_id(holiday.getInt("user_id"));
                    holidayModel.setName(holiday.getString("name"));
                    holidayModel.setDay_off(holiday.getLong("day_off"));
                    holidayModel.setUpdated_at(holiday.getLong("updated_at"));
                    holidayModel.setCreated_at(holiday.getLong("created_at"));
                    businessModel.getHolidayModels().add(holidayModel);
                }
                JSONArray disabled_slots = business_info.getJSONArray("disabled_slots");
                for(int i =0;i<disabled_slots.length();i++){
                    JSONObject disable_slot = disabled_slots.getJSONObject(i);
                    DisableSlotModel disableSlotModel = new DisableSlotModel();
                    disableSlotModel.setId(disable_slot.getInt("id"));
                    disableSlotModel.setUser_id(disable_slot.getInt("user_id"));
                    disableSlotModel.setDay_timestamp(disable_slot.getLong("day_timestamp"));
                    disableSlotModel.setStart(disable_slot.getString("start"));
                    disableSlotModel.setEnd(disable_slot.getString("end"));
                    disableSlotModel.setCreated_at(disable_slot.getLong("created_at"));
                    disableSlotModel.setUpdated_at(disable_slot.getLong("updated_at"));
                    businessModel.getDisableSlotModels().add(disableSlotModel);
                }
                JSONArray socials = business_info.getJSONArray("socials");
                for(int i =0;i<socials.length();i++){
                    JSONObject social = socials.getJSONObject(i);
                    SocialModel socialModel = new SocialModel();
                    socialModel.setId(social.getInt("id"));
                    socialModel.setUser_id(social.getInt("user_id"));
                    socialModel.setSocial_name(social.getString("social_name"));
                    socialModel.setType(social.getInt("type"));
                    socialModel.setCreated_at(social.getLong("created_at"));
                    businessModel.getSocialModels().add(socialModel);
                }
                businessModel.setPost_count(business_info.getInt("post_count"));
                businessModel.setFollowers_count(business_info.getInt("followers_count"));
                businessModel.setFollow_count(business_info.getInt("follow_count"));

            }

        }catch (Exception e){
            Log.d("aaaaaaa",e.toString());
        }
    }
}