package com.iamthene.driverassistant.model;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherData {
    private  String mTemperature, mIcon, mCity, mWeatherType;
    private int mCondition;

    public static WeatherData fromJson(JSONObject jsonObject) {
        try {
            WeatherData weatherData = new WeatherData();
            weatherData.mCity = jsonObject.getString("name");
            weatherData.mCondition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherData.mWeatherType = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherData.mIcon = updateWeatherIcon(weatherData.mCondition);

            double tempResult = jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundValue = (int) Math.rint(tempResult);
            weatherData.mTemperature = Integer.toString(roundValue);

            return weatherData;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String updateWeatherIcon(int mCondition) {
        if (mCondition >= 0 && mCondition <= 300)
        {
            return "thunderstorm";
        }
        else if (mCondition >= 300 && mCondition <= 500)
        {
            return "lightrain";
        }
        else if (mCondition >= 500 && mCondition <= 600)
        {
            return "shower";
        }
        else if (mCondition >= 600 && mCondition <= 700)
        {
            return "snow";
        }
        else if (mCondition >= 701 && mCondition <= 771)
        {
            return "fog";
        }
        else if (mCondition >= 772 && mCondition <= 800)
        {
            return "overcast";
        }
        else if (mCondition == 800)
        {
            return "sunny";
        }
        else if (mCondition >= 801 && mCondition <= 804)
        {
            return "cloudy";
        }

        return "sunny";
    }

    public String getmTemperature() {
        return mTemperature + "°C";
    }

    public String getmIcon() {
        return mIcon;
    }

    public String getmCity() {
        return mCity;
    }

    public String getmWeatherType() {
        return mWeatherType;
    }

    public int getmCondition() {
        return mCondition;
    }
}
