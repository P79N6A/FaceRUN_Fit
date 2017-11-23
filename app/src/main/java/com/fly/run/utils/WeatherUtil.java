package com.fly.run.utils;

import com.fly.run.bean.WeatherBean;
import com.fly.run.bean.WeatherItemBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinzhendi-031 on 2016/11/18.
 */
public class WeatherUtil {

    public static String WeatherKey = "18a55bf68f7a4";
    //    public static String httpWeaher = "http://apicloud.mob.com/v1/weather/query?key=18a55bf68f7a4&city=成都&province=四川";
    public static String httpWeaher = "http://apicloud.mob.com/v1/weather/query?key=18a55bf68f7a4&city=%1$s&province=%2$s";
    public static JSONObject weatherData;
    public static WeatherBean weatherBean;

    public static String getWeatherUrl(String province, String city) {
        String url = String.format(httpWeaher, city, province);
        return url;
    }

    public static WeatherBean parsonWeatherData(JSONObject jsonData) {
        if (jsonData == null)
            return null;
        String mStrAir = jsonData.optString("airCondition");
        String mStrCity = jsonData.optString("city");
        String mStrColdIndex = jsonData.optString("coldIndex");
        String mStrDressingIndex = jsonData.optString("dressingIndex");
        String mStrExerciseIndex = jsonData.optString("exerciseIndex");
        String mStrPollutionIndex = jsonData.optString("pollutionIndex");
        String mStrWashIndex = jsonData.optString("washIndex");
        String mStrWeek = jsonData.optString("week");
        String mStrWind = jsonData.optString("wind");
        String mStrUpdateTime = jsonData.optString("updateTime");
        String mStrSunRise = jsonData.optString("sunrise");
        String mStrSunSet = jsonData.optString("sunset");
        String mStrTemperature = jsonData.optString("temperature");
        String mStrWeather = jsonData.optString("weather");
        JSONArray futureArray = jsonData.optJSONArray("future");
        List<WeatherItemBean> itemWeatherList = new ArrayList<WeatherItemBean>();
        for (int i = 0; i < futureArray.length(); i++) {
            WeatherItemBean bean = new WeatherItemBean();
            JSONObject item = futureArray.optJSONObject(i);
            String date = item.optString("date");
            String dayTime = item.optString("dayTime");
            String night = item.optString("night");
            String temperature = item.optString("temperature");
            String week = item.optString("week");
            String wind = item.optString("wind");
            bean.setDate(date).setDayTime(dayTime).setNight(night)
                    .setTemperature(temperature).setWeek(week).setWind(wind);
            itemWeatherList.add(bean);
        }
        WeatherBean weatherBean = new WeatherBean();
        weatherBean.setCity(mStrCity).setTemperature(mStrTemperature).setWeather(mStrWeather)
                .setAirCondition(mStrAir).setColdIndex(mStrColdIndex).setDressingIndex(mStrDressingIndex)
                .setExerciseIndex(mStrExerciseIndex).setPollutionIndex(mStrPollutionIndex).setWashIndex(mStrWashIndex)
                .setWeek(mStrWeek).setWind(mStrWind).setUpdateTime(mStrUpdateTime).setSunRise(mStrSunRise)
                .setSunSet(mStrSunSet).setItemWeatherList(itemWeatherList);
        return weatherBean;
    }
}
