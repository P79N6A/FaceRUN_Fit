package com.fly.run.bean;

import java.io.Serializable;

/**
 * Created by hugo on 2015/9/30 0030.
 */
public class WeatherItemBean implements Serializable {

    public String date;//日期
    public String dayTime; //白天天气
    public String night; //夜晚天气
    public String week; //周几
    public String temperature; //温度
    public String wind; //风向

    public String getDate() {
        return date;
    }

    public WeatherItemBean setDate(String date) {
        this.date = date;
        return this;
    }

    public String getDayTime() {
        return dayTime;
    }

    public WeatherItemBean setDayTime(String dayTime) {
        this.dayTime = dayTime;
        return this;
    }

    public String getNight() {
        return night;
    }

    public WeatherItemBean setNight(String night) {
        this.night = night;
        return this;
    }

    public String getWeek() {
        return week;
    }

    public WeatherItemBean setWeek(String week) {
        this.week = week;
        return this;
    }

    public String getTemperature() {
        return temperature;
    }

    public WeatherItemBean setTemperature(String temperature) {
        this.temperature = temperature;
        return this;
    }

    public String getWind() {
        return wind;
    }

    public WeatherItemBean setWind(String wind) {
        this.wind = wind;
        return this;
    }
}
