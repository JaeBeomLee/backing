package com.mondaychicken.bacving.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ijaebeom on 2015. 9. 4..
 */
public class headerPagerAdater extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    public headerPagerAdater(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFlag(Fragment fragment){
        mFragmentList.add(fragment);
    }
}
