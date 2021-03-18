package com.atb.app.model;

import com.atb.app.model.submodel.DisableSlotModel;
import com.atb.app.model.submodel.HolidayModel;
import com.atb.app.model.submodel.OpeningTimeModel;
import com.atb.app.model.submodel.SocialModel;

import java.util.ArrayList;

public class BusinessModel {
    //approved 0 - pending, 1 - approved, other - rejected
    // 0 - not paid, 1 - paid
    int id,user_id;
    String business_logo,business_name,business_website,business_bio,business_profile_name,approval_reason;
    int paid ,approved,post_count,followers_count,follow_count;
    Long updated_at,created_at;
    ArrayList<OpeningTimeModel> openingTimeModels = new ArrayList<>();
    ArrayList<HolidayModel> holidayModels = new ArrayList<>();
    ArrayList<DisableSlotModel> disableSlotModels = new ArrayList<>();
    ArrayList<ServiceModel> serviceModels = new ArrayList<>();
    ArrayList<SocialModel> socialModels = new ArrayList<>();

    public ArrayList<SocialModel> getSocialModels() {
        return socialModels;
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

    public ArrayList<ServiceModel> getServiceModels() {
        return serviceModels;
    }

    public void setServiceModels(ArrayList<ServiceModel> serviceModels) {
        this.serviceModels = serviceModels;
    }
}
