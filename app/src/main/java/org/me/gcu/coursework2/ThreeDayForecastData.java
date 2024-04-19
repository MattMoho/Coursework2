package org.me.gcu.coursework2;



public class ThreeDayForecastData {
    private String title1;
    private String title2;
    private String title3;
    private String minMaxTemperature1;
    private String minMaxTemperature2;
    private String minMaxTemperature3;
    private String weatherCondition1;
    private String weatherCondition2;
    private String weatherCondition3;
    private int imageResource1;
    private int imageResource2;
    private int imageResource3;

    public ThreeDayForecastData() {
        // Default constructor
    }

    public ThreeDayForecastData(String title1, String title2, String title3,
                                String minMaxTemperature1, String minMaxTemperature2, String minMaxTemperature3,
                                String weatherCondition1, String weatherCondition2, String weatherCondition3,
                                int imageResource1, int imageResource2, int imageResource3) {
        this.title1 = title1;
        this.title2 = title2;
        this.title3 = title3;
        this.minMaxTemperature1 = minMaxTemperature1;
        this.minMaxTemperature2 = minMaxTemperature2;
        this.minMaxTemperature3 = minMaxTemperature3;
        this.weatherCondition1 = weatherCondition1;
        this.weatherCondition2 = weatherCondition2;
        this.weatherCondition3 = weatherCondition3;
        this.imageResource1 = imageResource1;
        this.imageResource2 = imageResource2;
        this.imageResource3 = imageResource3;
    }

    // Getters and setters for each field
    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getTitle3() {
        return title3;
    }

    public void setTitle3(String title3) {
        this.title3 = title3;
    }

    public String getMinMaxTemperature1() {
        return minMaxTemperature1;
    }

    public void setMinMaxTemperature1(String minMaxTemperature1) {
        this.minMaxTemperature1 = minMaxTemperature1;
    }

    public String getMinMaxTemperature2() {
        return minMaxTemperature2;
    }

    public void setMinMaxTemperature2(String minMaxTemperature2) {
        this.minMaxTemperature2 = minMaxTemperature2;
    }

    public String getMinMaxTemperature3() {
        return minMaxTemperature3;
    }

    public void setMinMaxTemperature3(String minMaxTemperature3) {
        this.minMaxTemperature3 = minMaxTemperature3;
    }

    public String getWeatherCondition1() {
        return weatherCondition1;
    }

    public void setWeatherCondition1(String weatherCondition1) {
        this.weatherCondition1 = weatherCondition1;
    }

    public String getWeatherCondition2() {
        return weatherCondition2;
    }

    public void setWeatherCondition2(String weatherCondition2) {
        this.weatherCondition2 = weatherCondition2;
    }

    public String getWeatherCondition3() {
        return weatherCondition3;
    }

    public void setWeatherCondition3(String weatherCondition3) {
        this.weatherCondition3 = weatherCondition3;
    }

    public int getImageResource1() {
        return imageResource1;
    }

    public void setImageResource1(int imageResource1) {
        this.imageResource1 = imageResource1;
    }

    public int getImageResource2() {
        return imageResource2;
    }

    public void setImageResource2(int imageResource2) {
        this.imageResource2 = imageResource2;
    }

    public int getImageResource3() {
        return imageResource3;
    }

    public void setImageResource3(int imageResource3) {
        this.imageResource3 = imageResource3;
    }
}
