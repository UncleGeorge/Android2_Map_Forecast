package com.example.n01371888_zhangzhiwang_owm;

public class Forecast {
    private String dateTime;
    private String temperature;
    private String weather;
    private String iconUrl;


    public Forecast(String dateTime, String temperature, String weather, String iconUrl) {
        this.dateTime = dateTime;
        this.temperature = temperature;
        this.weather = weather;
        this.iconUrl = iconUrl;
    }

    // Getters
    public String getDateTime() {
        return dateTime;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getWeather() {
        return weather;
    }
    public String getIconUrl() {
        return iconUrl;
    }

    // Setters
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
