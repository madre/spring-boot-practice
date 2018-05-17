package com.example.skinserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by chanson.cc on 2018/5/17.
 */
@ConfigurationProperties(prefix = "local")
public class ConfigBean {

    private String name;
    private String ipAddress;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
