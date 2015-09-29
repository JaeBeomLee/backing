package com.lee.bacving_main.main.soccer;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by ijaebeom on 2015. 9. 11..
 */
public class mainTabFragmentMainData2 {
    String teamName;
    Bitmap teamLogo, mainBackground;

    public void setTeamLogo(Bitmap teamLogo) {
        this.teamLogo = teamLogo;
    }

    public void setMainBackground(Bitmap mainBackground) {
        this.mainBackground = mainBackground;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Bitmap getTeamLogo() {
        return teamLogo;
    }

    public Bitmap getMainBackground() {
        return mainBackground;
    }

    public String getTeamName() {
        return teamName;
    }

}
