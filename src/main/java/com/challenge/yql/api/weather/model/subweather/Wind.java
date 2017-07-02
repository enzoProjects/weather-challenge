package com.challenge.yql.api.weather.model.subweather;

/**
 * Created by springfield-home on 7/1/17.
 */
public class Wind {
    private int chill;
    private int direction;
    private int speed;

    public int getChill() {
        return chill;
    }

    public void setChill(int chill) {
        this.chill = chill;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
