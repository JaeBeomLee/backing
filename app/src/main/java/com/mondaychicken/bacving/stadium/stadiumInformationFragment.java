package com.mondaychicken.bacving.stadium;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mondaychicken.bacving.R;

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
    TextView time, place, phone, info;
    String timeS, placeS, phoneS, infoS;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v  = inflater.inflate(R.layout.stadium_information,container, false);
        context = container.getContext();

        place= (TextView)v.findViewById(R.id.place_stadium);
        time = (TextView)v.findViewById(R.id.time_stadium);
        phone = (TextView)v.findViewById(R.id.phone_stadium);
        info = (TextView)v.findViewById(R.id.info_stadium);

        //엑티비티에서 서버로 받은정보를 저장해 프레그먼트애서 가져와 사용
        LoadPreference(context);
        place.setText(placeS);
        phone.setText(phoneS);
        info.setText(infoS);

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

    private void LoadPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        userno = sharedPreferences.getString("userno", null);
//        token = sharedPreferences.getString("token", null);
        placeS = sharedPreferences.getString("stadiumPlace", null);
        phoneS = sharedPreferences.getString("stadiumPhone", null);
        infoS = sharedPreferences.getString("stadiumDescription", null);
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
