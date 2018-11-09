package com.fly.run.bean;

import java.io.Serializable;

public class UpTokenInfo implements Serializable {

    private int code;
    private String key;
    private String message;
    private String randomFileName;
    private String token;

    public int getCode() {
        return code;
    }

    public UpTokenInfo setCode(int code) {
        this.code = code;
        return this;
    }

    public String getKey() {
        return key;
    }

    public UpTokenInfo setKey(String key) {
        this.key = key;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public UpTokenInfo setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getRandomFileName() {
        return randomFileName;
    }

    public UpTokenInfo setRandomFileName(String randomFileName) {
        this.randomFileName = randomFileName;
        return this;
    }

    public String getToken() {
        return token;
    }

    public UpTokenInfo setToken(String token) {
        this.token = token;
        return this;
    }
}
