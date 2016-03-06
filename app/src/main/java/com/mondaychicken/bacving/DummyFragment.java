package com.mondaychicken.bacving;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ijaebeom on 2015. 8. 12..
 */
public class DummyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(com.mondaychicken.bacving.R.layout.tab_1, container, false);

        return v;
    }
}
