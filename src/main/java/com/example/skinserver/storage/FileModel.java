package com.example.skinserver.storage;

/**
 * Created by chanson.cc on 2018/4/22.
 */
public class FileModel {
    private String name;
    private String path;
    private String qrcode;

    public FileModel(String name, String path) {
        this.name = name;
        this.path = path;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }
}
