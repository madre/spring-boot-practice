package com.example.skinserver.skinfile;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by chanson.cc on 2018/5/5.
 */
@Entity
public class SkinFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String originFileName;
    private String skinOutputFileName;
    private String skinId;
    private String qrcode;
    private String zip;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOriginFileName() {
        return originFileName;
    }

    public void setOriginFileName(String originFileName) {
        this.originFileName = originFileName;
    }

    public String getSkinOutputFileName() {
        return skinOutputFileName;
    }

    public void setSkinOutputFileName(String skinOutputFileName) {
        this.skinOutputFileName = skinOutputFileName;
    }

    public String getSkinId() {
        return skinId;
    }

    public void setSkinId(String skinId) {
        this.skinId = skinId;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
