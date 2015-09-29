package com.lee.bacving_main.main.total;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by ijaebeom on 2015. 9. 11..
 */
public class mainTabFragmentMainData {
    String teamName;
    Bitmap teamLogo, mainBackground;

    String nextMatch,  matchDataYear, matchDataMonth ,matchDataDate, matchDataApm, matchDataHour, matchDataMinute, matchStadium;

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

    public void setNextMatch(String nextMatch) {
        this.nextMatch = nextMatch;
    }

    public void setMatchDataYear(String matchDataYear) {
        this.matchDataYear = matchDataYear;
    }

    public void setMatchDataMonth(String matchDataMonth) {
        this.matchDataMonth = matchDataMonth;
    }

    public void setMatchDataDate(String matchDataDate) {
        this.matchDataDate = matchDataDate;
    }

    public void setMatchDataApm(String matchDataApm) {
        this.matchDataApm = matchDataApm;
    }

    public void setMatchDataHour(String matchDataHour) {
        this.matchDataHour = matchDataHour;
    }

    public void setMatchDataMinute(String matchDataMinute) {
        this.matchDataMinute = matchDataMinute;
    }

    public void setMatchStadium(String matchStadium) {
        this.matchStadium = matchStadium;
    }

    public String getNextMatch() {
        return nextMatch;
    }

    public String getMatchDataYear() {
        return matchDataYear;
    }

    public String getMatchDataMonth() {
        return matchDataMonth;
    }

    public String getMatchDataDate() {
        return matchDataDate;
    }

    public String getMatchDataApm() {
        return matchDataApm;
    }

    public String getMatchDataHour() {
        return matchDataHour;
    }

    public String getMatchDataMinute() {
        return matchDataMinute;
    }

    public String getMatchStadium() {
        return matchStadium;
    }
}
