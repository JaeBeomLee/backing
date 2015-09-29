package com.lee.bacving_main.main.searchResult;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by ijaebeom on 2015. 9. 15..
 */
public class searchResultData {
    Bitmap searchResultImage;
    String searchResultName, searchResult2nd, searchResult3rd;

    public void setSearchResultImage(Bitmap searchResultImage) {
        this.searchResultImage = searchResultImage;
    }

    public void setSearchResultName(String searchResultName) {
        this.searchResultName = searchResultName;
    }

    public void setSearchResult2nd(String searchResult2nd) {
        this.searchResult2nd = searchResult2nd;
    }

    public void setSearchResult3rd(String searchResult3rd) {
        this.searchResult3rd = searchResult3rd;
    }

    public Bitmap getSearchResultImage() {
        return searchResultImage;
    }

    public String getSearchResultName() {
        return searchResultName;
    }

    public String getSearchResult2nd() {
        return searchResult2nd;
    }

    public String getSearchResult3rd() {
        return searchResult3rd;
    }
}
