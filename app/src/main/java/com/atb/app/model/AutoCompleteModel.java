package com.atb.app.model;

public class AutoCompleteModel {
    int id ,is_business;
    String name, pic_url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_business() {
        return is_business;
    }

    public void setIs_business(int is_business) {
        this.is_business = is_business;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }
}
