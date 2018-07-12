package com.fly.run.bean;

import java.io.Serializable;

/**
 * Created by kongwei on 2017/3/10.
 */

public class AccountBean implements Serializable {

    public int id; //id标识
    public String account;
    public String name;
    public String sex;
    public String tag;
    public String description;
    public String profession;
    public String level;
    public String headPortrait;
    public String create_time;
    public int age;
    public int type;

    public int getId() {
        return id;
    }

    public AccountBean setId(int id) {
        this.id = id;
        return this;
    }

    public String getAccount() {
        return account;
    }

    public AccountBean setAccount(String account) {
        this.account = account;
        return this;
    }

    public String getUsername() {
        return name;
    }

    public AccountBean setUsername(String username) {
        this.name = username;
        return this;
    }

    public String getSex() {
        return sex;
    }

    public AccountBean setSex(String sex) {
        this.sex = sex;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public AccountBean setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AccountBean setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getProfession() {
        return profession;
    }

    public AccountBean setProfession(String profession) {
        this.profession = profession;
        return this;
    }

    public String getLevel() {
        return level;
    }

    public AccountBean setLevel(String level) {
        this.level = level;
        return this;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public AccountBean setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
        return this;
    }

    public String getCreate_time() {
        return create_time;
    }

    public AccountBean setCreate_time(String create_time) {
        this.create_time = create_time;
        return this;
    }

    public int getAge() {
        return age;
    }

    public AccountBean setAge(int age) {
        this.age = age;
        return this;
    }

    public int getType() {
        return type;
    }

    public AccountBean setType(int type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public AccountBean setName(String name) {
        this.name = name;
        return this;
    }
}
