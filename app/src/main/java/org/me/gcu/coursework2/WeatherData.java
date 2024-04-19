package org.me.gcu.coursework2;

public class WeatherData {

    private String title;

    private String mainTitle;

    private String description;

    private String pubDate;

    private String location;

    public enum ForecastType {
        THREE_DAY,
        LATEST
    }

    private ForecastType forecastType;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public ForecastType getForecastType() {
        return forecastType;
    }

    public void setForecastType(ForecastType forecastType) {
        this.forecastType = forecastType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }
}
