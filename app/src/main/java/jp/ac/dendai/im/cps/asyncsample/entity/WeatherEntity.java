package jp.ac.dendai.im.cps.asyncsample.entity;

import java.util.List;

public class WeatherEntity {
    private String title;
    private String publicTime;
    private List<ForeCast> forecasts;
    private Location location;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublicTime() {
        return publicTime;
    }

    public void setPublicTime(String publicTime) {
        this.publicTime = publicTime;
    }

    public List<ForeCast> getForecasts() {
        return forecasts;
    }

    public void setForecasts(List<ForeCast> forecasts) {
        this.forecasts = forecasts;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
