package com.mondaychicken.bacving.teamPlayer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bacving.lee.bacving_main.R;

/**
 * Created by ijaebeom on 2015. 8. 24..
 */
public class teamInformationFragment extends Fragment {

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        View v = inflater.inflate(R.layout.team_infomation_view, container,false);
        return v;
    }
}
