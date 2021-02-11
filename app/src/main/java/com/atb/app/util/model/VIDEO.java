package com.atb.app.util.model;


import java.io.Serializable;

public class VIDEO implements Serializable {
    String path;
    long id;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getId() {
        return id;
    }


    public void setId(long id) {
        this.id = id;
    }
}
