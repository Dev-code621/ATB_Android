package com.atb.app.model;

import java.util.ArrayList;

public class NewsFeedEntity {
    int id;
    boolean type;
    boolean select = false;
    ArrayList<NewsFeedEntity>  postEntities = new ArrayList();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public ArrayList<NewsFeedEntity> getPostEntities() {
        return postEntities;
    }

    public void setPostEntities(ArrayList<NewsFeedEntity> postEntities) {
        this.postEntities = postEntities;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
