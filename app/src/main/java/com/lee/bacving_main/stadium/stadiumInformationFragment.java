package com.lee.bacving_main.stadium;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lee.bacving_main.R;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;


/**
 * Created by ijaebeom on 2015. 8. 28..
 */
public class stadiumInformationFragment extends Fragment {
    Context context;
    RelativeLayout map;

    MapView mapView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.stadium_information,container, false);
        context = container.getContext();
        map = (RelativeLayout)v.findViewById(R.id.map_view);
        mapView = new MapView(context);
        mapView.setDaumMapApiKey("9e0d02655d291c8352e1578cc5617703");
        map.addView(mapView);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), true);
        mapView.setZoomLevel(-2, true);
        return v;
    }
}
