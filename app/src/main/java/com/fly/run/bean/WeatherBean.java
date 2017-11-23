package com.fly.run.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hugo on 2015/9/30 0030.
 */
public class WeatherBean implements Serializable {

    public String city;
    public String exerciseIndex; //运动指数
    public String dressingIndex; //穿衣指数
    public String coldIndex; //感冒指数
    public String washIndex; //洗衣指数

    public String airCondition; //空气质量
    public String pollutionIndex; //空气质量指数

    public String week; //周几
    public String temperature; //当前温度
    public String wind; //风向
    public String weather; //天气
    public String updateTime; //更新时间
    public String sunRise; //日出
    public String sunSet; //日落
    private List<WeatherItemBean> itemWeatherList = new ArrayList<WeatherItemBean>();

    public String getCity() {
        return city;
    }

    public WeatherBean setCity(String city) {
        this.city = city;
        return this;
    }

    public String getExerciseIndex() {
        return exerciseIndex;
    }

    public WeatherBean setExerciseIndex(String exerciseIndex) {
        this.exerciseIndex = exerciseIndex;
        return this;
    }

    public String getDressingIndex() {
        return dressingIndex;
    }

    public WeatherBean setDressingIndex(String dressingIndex) {
        this.dressingIndex = dressingIndex;
        return this;
    }

    public String getColdIndex() {
        return coldIndex;
    }

    public WeatherBean setColdIndex(String coldIndex) {
        this.coldIndex = coldIndex;
        return this;
    }

    public String getWashIndex() {
        return washIndex;
    }

    public WeatherBean setWashIndex(String washIndex) {
        this.washIndex = washIndex;
        return this;
    }

    public String getAirCondition() {
        return airCondition;
    }

    public WeatherBean setAirCondition(String airCondition) {
        this.airCondition = airCondition;
        return this;
    }

    public String getPollutionIndex() {
        return pollutionIndex;
    }

    public WeatherBean setPollutionIndex(String pollutionIndex) {
        this.pollutionIndex = pollutionIndex;
        return this;
    }

    public String getWeek() {
        return week;
    }

    public WeatherBean setWeek(String week) {
        this.week = week;
        return this;
    }

    public String getTemperature() {
        return temperature;
    }

    public WeatherBean setTemperature(String temperature) {
        this.temperature = temperature;
        return this;
    }

    public String getWind() {
        return wind;
    }

    public WeatherBean setWind(String wind) {
        this.wind = wind;
        return this;
    }

    public String getWeather() {
        return weather;
    }

    public WeatherBean setWeather(String weather) {
        this.weather = weather;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public WeatherBean setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getSunRise() {
        return sunRise;
    }

    public WeatherBean setSunRise(String sunRise) {
        this.sunRise = sunRise;
        return this;
    }

    public String getSunSet() {
        return sunSet;
    }

    public WeatherBean setSunSet(String sunSet) {
        this.sunSet = sunSet;
        return this;
    }

    public List<WeatherItemBean> getItemWeatherList() {
        return itemWeatherList;
    }

    public WeatherBean setItemWeatherList(List<WeatherItemBean> itemWeatherList) {
        this.itemWeatherList = itemWeatherList;
        return this;
    }
}
