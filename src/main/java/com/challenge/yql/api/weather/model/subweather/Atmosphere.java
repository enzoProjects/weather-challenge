package com.challenge.yql.api.weather.model.subweather;

/**
 * Created by springfield-home on 7/1/17.
 */
public class Atmosphere {
    private int humidity;
    private float pressure;
    private int rising;
    private float visibility;

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public int getRising() {
        return rising;
    }

    public void setRising(int rising) {
        this.rising = rising;
    }

    public float getVisibility() {
        return visibility;
    }

    public void setVisibility(float visibility) {
        this.visibility = visibility;
    }
}
