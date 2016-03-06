package com.mondaychicken.bacving.matching;

import android.graphics.Bitmap;

/**
 * Created by ijaebeom on 2015. 9. 11..
 */
public class matchingFragmentData {
    private String Name, place, time, age, logo;


    public void setName(String name) {
        Name = name;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return Name;
    }

    public String getPlace() {
        return place;
    }

    public String getTime() {
        return time;
    }

    public String getAge() {
        return age;
    }

    public String getLogo() {
        return logo;
    }
}

