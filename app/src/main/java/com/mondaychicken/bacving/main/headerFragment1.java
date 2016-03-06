package com.mondaychicken.bacving.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mondaychicken.bacving.R;

/**
 * Created by ijaebeom on 2015. 9. 4..
 */
public class headerFragment1 extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.header1, container, false);

        return v;
    }
}
