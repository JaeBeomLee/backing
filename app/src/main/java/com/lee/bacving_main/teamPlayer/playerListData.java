package com.lee.bacving_main.teamPlayer;


import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by ijaebeom on 2015. 8. 18..
 */
public class playerListData {
    String name;
    Bitmap image;

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getName() {
        return name;
    }
}
