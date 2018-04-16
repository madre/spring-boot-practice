package com.example.skinserver.controller;

public class UtilModel {

    private long id;
    private String content;

    public UtilModel() {
        id = 999;
        content = "default";
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
