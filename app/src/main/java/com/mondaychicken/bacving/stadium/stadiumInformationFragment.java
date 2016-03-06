package com.mondaychicken.bacving.stadium;


import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import bacving.lee.bacving_main.R;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;


/**
 * Created by ijaebeom on 2015. 8. 28..
 */
public class stadiumInformationFragment extends Fragment {
    Context context;
    RelativeLayout map;
    int permissionCheck;
    MapView mapView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.stadium_information,container, false);
        context = container.getContext();
        permissionCheck = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int MyResultID = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MyResultID);
            }
        }
            map = (RelativeLayout)v.findViewById(R.id.map_view);
        if (permissionCheck == PackageManager.PERMISSION_DENIED){
            map.setVisibility(View.GONE);
        }else{
            mapView = new MapView(context);
            mapView.setDaumMapApiKey("9e0d02655d291c8352e1578cc5617703");
            map.addView(mapView);
            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), true);
            mapView.setZoomLevel(-2, true);
        }
        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    permissionCheck = PackageManager.PERMISSION_GRANTED;
                    map.setVisibility(View.VISIBLE);
                    mapView = new MapView(context);
                    mapView.setDaumMapApiKey("9e0d02655d291c8352e1578cc5617703");
                    map.addView(mapView);
                    mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633), true);
                    mapView.setZoomLevel(-2, true);
                }else {
                    permissionCheck = PackageManager.PERMISSION_DENIED;
                }
            }
        }
    }


}
