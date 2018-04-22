package com.example.skinserver.storage;

import com.google.zxing.WriterException;

import java.io.IOException;
import java.nio.file.Path;

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


    public void crateQrcode(StorageService storageService) {
//        String replace = this.name.replace("\\.", "_");
        String replace = name;
        String qrcodeFileName = replace + ".png";
        Path load = storageService.load(qrcodeFileName);
        if (load.toFile().exists()) {

        } else {

            try {
                QRCode.createQRCode(path, load.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
        this.qrcode = "/upload-dir/" + qrcodeFileName;

    }
}
