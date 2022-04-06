package com.atb.app.model;

import android.util.Log;

import com.atb.app.commons.Commons;
import com.atb.app.model.submodel.DisableSlotModel;
import com.atb.app.model.submodel.HolidayModel;
import com.atb.app.model.submodel.OpeningTimeModel;
import com.atb.app.model.submodel.SocialModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BusinessModel {
    //approved 0 - pending, 1 - approved, other - rejected
    // 0 - not paid, 1 - paid
    int id,user_id;
    String business_logo,business_name,business_website,business_bio,business_profile_name,approval_reason,group_title;
    int paid ,approved,post_count,followers_count,follow_count;
    Long updated_at,created_at;
    ArrayList<OpeningTimeModel> openingTimeModels = new ArrayList<>();
    ArrayList<HolidayModel> holidayModels = new ArrayList<>();
    ArrayList<DisableSlotModel> disableSlotModels = new ArrayList<>();
    ArrayList<SocialModel> socialModels = new ArrayList<>();
    int reviews = 0;
    double rating = 0.0;
    public ArrayList<SocialModel> getSocialModels() {
        return socialModels;
    }
    ArrayList<ArrayList<String>> slots = new ArrayList<>();

    public String getGroup_title() {
        return group_title;
    }

    public void setGroup_title(String group_title) {
        this.group_title = group_title;
    }

    public int getReviews() {
        return reviews;
    }

    public void setReviews(int reviews) {
        this.reviews = reviews;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public ArrayList<ArrayList<String>> getSlots() {
        slots.clear();
        if(openingTimeModels.size()==0)return slots;
        for(int i =0;i<7;i++){
            ArrayList<String>arrayList = new ArrayList<>();
            slots.add(arrayList);
        }
        for(int i =0;i<7;i++){
            int next_day = (i+1)%7;
            if(openingTimeModels.get(i).getIs_available()==0)continue;
            int start = Commons.getMilionSecond(Commons.getLocaltime(openingTimeModels.get(i).getStart()));
            int end = Commons.getMilionSecond(Commons.getLocaltime(openingTimeModels.get(i).getEnd()));
            if(end<start)start-=24*3600;
            int count = (end-start)/1800;
            for(int k=0;k<count;k++){
                if(start%(24*3600)<= Commons.getMilionSecond("11:30 PM")) {
                    slots.get(i).add(Commons.gettimeFromMilionSecond(start));
                }else {
                    slots.get(next_day).add(Commons.gettimeFromMilionSecond(start));
                }
                start+=1800;
            }
        }

//        for(int i =0;i<7;i++){
//            String str = "";
//            for(int j =0;j<slots.get(i).size();j++){
//                str = str + " " + slots.get(i).get(j);
//            }
//            Log.d("aaaaaaa",String.valueOf(i) + "   " +  str);
//        }
        return slots;
    }

    public void setSocialModels(ArrayList<SocialModel> socialModels) {
        this.socialModels = socialModels;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getBusiness_logo() {
        return business_logo;
    }

    public void setBusiness_logo(String business_logo) {
        this.business_logo = business_logo;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getBusiness_website() {
        return business_website;
    }

    public void setBusiness_website(String business_website) {
        this.business_website = business_website;
    }

    public String getBusiness_bio() {
        return business_bio;
    }

    public void setBusiness_bio(String business_bio) {
        this.business_bio = business_bio;
    }

    public String getBusiness_profile_name() {
        return business_profile_name;
    }

    public void setBusiness_profile_name(String business_profile_name) {
        this.business_profile_name = business_profile_name;
    }

    public String getApproval_reason() {
        return approval_reason;
    }

    public void setApproval_reason(String approval_reason) {
        this.approval_reason = approval_reason;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
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

    public Long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Long updated_at) {
        this.updated_at = updated_at;
    }

    public Long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Long created_at) {
        this.created_at = created_at;
    }

    public ArrayList<OpeningTimeModel> getOpeningTimeModels() {
        return openingTimeModels;
    }

    public void setOpeningTimeModels(ArrayList<OpeningTimeModel> openingTimeModels) {
        this.openingTimeModels = openingTimeModels;
    }

    public ArrayList<HolidayModel> getHolidayModels() {
        return holidayModels;
    }

    public void setHolidayModels(ArrayList<HolidayModel> holidayModels) {
        this.holidayModels = holidayModels;
    }

    public ArrayList<DisableSlotModel> getDisableSlotModels() {
        return disableSlotModels;
    }

    public void setDisableSlotModels(ArrayList<DisableSlotModel> disableSlotModels) {
        this.disableSlotModels = disableSlotModels;
    }

    public void initModel(JSONObject business_info){
        try {
            setId(business_info.getInt("id"));
            setUser_id(business_info.getInt("user_id"));
            setBusiness_logo(business_info.getString("business_logo"));
            setBusiness_name(business_info.getString("business_name"));
            setBusiness_website(business_info.getString("business_website"));
            setBusiness_bio(business_info.getString("business_bio"));
            setBusiness_profile_name(business_info.getString("business_profile_name"));
            setPaid(business_info.getInt("paid"));
            setApproved(business_info.getInt("approved"));
            setApproval_reason(business_info.getString("approval_reason"));
            setUpdated_at(business_info.getLong("updated_at"));
            setCreated_at(business_info.getLong("created_at"));
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
                getOpeningTimeModels().add(openingTimeModel);
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
                getHolidayModels().add(holidayModel);
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
                getDisableSlotModels().add(disableSlotModel);
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
                getSocialModels().add(socialModel);
            }
            if(business_info.has("post_count"))
                 setPost_count(business_info.getInt("post_count"));
            if(business_info.has("followers_count"))
                setFollowers_count(business_info.getInt("followers_count"));
            if(business_info.has("follow_count"))
                setFollow_count(business_info.getInt("follow_count"));
            if(business_info.has("reviews"))
                setReviews(business_info.getInt("reviews"));
            if(business_info.has("rating"))
                setRating(business_info.getDouble("rating"));
        }catch (Exception e){
            Log.d("aaaaa",e.toString());
        }
    }
}
