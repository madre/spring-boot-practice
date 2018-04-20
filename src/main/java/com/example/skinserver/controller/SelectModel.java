package com.example.skinserver.controller;

import java.util.ArrayList;
import java.util.List;

public class SelectModel {

    private long id;
    private String content;
    private List<Country> list = new ArrayList<>();

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

    public List<Country> getList() {
        return list;
    }
    public void setListData(Country data) {
        list.add(data);
    }

    public static class Country {
        public String id;
        public String name;

        public Country(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
