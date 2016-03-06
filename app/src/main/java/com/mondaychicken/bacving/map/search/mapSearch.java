package com.mondaychicken.bacving.map.search;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mondaychicken.bacving.R;
import com.mondaychicken.bacving.matching.matchingTeamRegister;
import com.mondaychicken.bacving.stadium.createStadiumActivity;

import net.daum.mf.map.api.CalloutBalloonAdapter;
import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Created by leejaebeom on 2015. 11. 28..
 */
public class mapSearch extends AppCompatActivity implements MapView.MapViewEventListener, MapView.POIItemEventListener{
    RelativeLayout map;
    int permissionCheck;
    MapView mapView;
    String MapApiKey = "0b42f43598ab66b672d55c2364b29e5b";
    double lat = 37.53737528;
    double lng = 127.00557633;
    private HashMap<Integer, Item> mTagItemMap = new HashMap<Integer, Item>();
    String name, profileimg, address, newAddress, zipcode, tel, region;
    Button mapSearch, mapChoose;
    EditText mapKeyword;
    MapPOIItem poiItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stadium_create_map);
        Intent intent = getIntent();

        mapSearch = (Button)findViewById(R.id.stadium_create_map_search);
        mapChoose = (Button)findViewById(R.id.stadium_create_map_choose);
        mapKeyword = (EditText)findViewById(R.id.stadium_create_map_keyword);

        mapKeyword.setText(intent.getExtras().getString("keyword"));
        mapSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = mapKeyword.getText().toString();
                if (query == null || query.length() == 0) {
                    Toast.makeText(mapSearch.this, "검색어를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                hideSoftKeyboard(); // 키보드 숨김
                MapPoint.GeoCoordinate geoCoordinate = mapView.getMapCenterPoint().getMapPointGeoCoord();
                double latitude = geoCoordinate.latitude; // 위도
                double longitude = geoCoordinate.longitude; // 경도
                int radius = 10000; // 중심 좌표부터의 반경거리. 특정 지역을 중심으로 검색하려고 할 경우 사용. meter 단위 (0 ~ 10000)
                int page = 1; // 페이지 번호 (1 ~ 3). 한페이지에 15개
                String apikey = MapApiKey;

                Searcher searcher = new Searcher(); // net.daum.android.map.openapi.search.Searcher
                searcher.searchKeyword(getApplicationContext(), query, latitude, longitude, radius, page, apikey, new OnFinishSearchListener() {
                    @Override
                    public void onSuccess(List<Item> itemList) {
                        mapView.removeAllPOIItems(); // 기존 검색 결과 삭제
                        showResult(itemList); // 검색 결과 보여줌

                    }

                    @Override
                    public void onFail() {
//                        showToast("API_KEY의 제한 트래픽이 초과되었습니다.");
                    }
                });
            }
        });
        mapChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name != null){
                    mapView = null;
                    Intent intent = new Intent(mapSearch.this, createStadiumActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("profileimg", profileimg);
                    intent.putExtra("address", address);
                    intent.putExtra("newAddress", newAddress);
                    intent.putExtra("zipcode", zipcode);
                    intent.putExtra("tel", tel);
                    intent.putExtra("lng", lng);
                    intent.putExtra("lat", lat);
                    intent.putExtra("region", region);
                    startActivityForResult(intent, 2);
                }else{
                    Toast.makeText(mapSearch.this, "말풍선을 클릭해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
        permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
        int MyResultID = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MyResultID);
            }
        }
        map = (RelativeLayout)findViewById(R.id.map_view);
        if (permissionCheck == PackageManager.PERMISSION_DENIED){
            map.setVisibility(View.GONE);
        }else{
            mapView = new MapView(this);
            mapView.setDaumMapApiKey(MapApiKey);
            map.addView(mapView);
            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lat, lng), true);
            mapView.setZoomLevel(-2, true);
            mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
            mapView.setMapViewEventListener(this);
            mapView.setPOIItemEventListener(this);
        }
    }
    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mapKeyword.getWindowToken(), 0);
    }

    private void showResult(List<Item> itemList) {
        MapPointBounds mapPointBounds = new MapPointBounds();

        for (int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);

            poiItem = new MapPOIItem();
            poiItem.setItemName(item.title);
            poiItem.setTag(i);
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(item.latitude, item.longitude);
            poiItem.setMapPoint(mapPoint);
            mapPointBounds.add(mapPoint);
            poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem.setCustomImageResourceId(R.drawable.map_pin_blue);
            poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
            poiItem.setCustomSelectedImageResourceId(R.drawable.map_pin_red);
            poiItem.setCustomImageAutoscale(false);
            poiItem.setCustomImageAnchor(0.5f, 1.0f);
            mapView.addPOIItem(poiItem);
            mTagItemMap.put(poiItem.getTag(), item);
        }


        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds));

        MapPOIItem[] poiItems = mapView.getPOIItems();
        if (poiItems.length > 0) {
            mapView.selectPOIItem(poiItems[0], false);
        }
    }
    @Override
    public void finish() {
        mapView = null;
        super.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    permissionCheck = PackageManager.PERMISSION_GRANTED;
                    map.setVisibility(View.VISIBLE);
                    mapView = new MapView(this);
                    mapView.setDaumMapApiKey(MapApiKey);
                    map.addView(mapView);
                    mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lat, lng), true);
                    mapView.setZoomLevel(-2, true);
                    mapView.setCalloutBalloonAdapter(new CustomCalloutBalloonAdapter());
                    mapView.setMapViewEventListener(this);
                    mapView.setPOIItemEventListener(this);
                }else {
                    permissionCheck = PackageManager.PERMISSION_DENIED;
                }
            }
        }
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {
        name = null;
        profileimg = null;
        address = null;
        newAddress = null;
        zipcode = null;
        tel = null;
        lat = 0.0f;
        lng = 0.0f;
        region = null;
    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

        Item item = mTagItemMap.get(mapPOIItem.getTag());
        name = item.title;
        profileimg = item.imageUrl;

        if (item.newAddress.equals("")) {
            address = item.address;
        }else{
            address = item.newAddress;
        }
        zipcode = item.zipcode;
        tel = item.phone;
        lat = item.latitude;
        lng = item.longitude;
        region = item.addressBCode;

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }

    class CustomCalloutBalloonAdapter implements CalloutBalloonAdapter {

        private final View mCalloutBalloon;

        public CustomCalloutBalloonAdapter() {
            mCalloutBalloon = getLayoutInflater().inflate(R.layout.custom_callout_balloon, null);
        }

        @Override
        public View getCalloutBalloon(MapPOIItem poiItem) {
            if (poiItem == null) return null;
            Item item = mTagItemMap.get(poiItem.getTag());
            if (item == null) return null;

            ImageView imageViewBadge;
            TextView textViewDesc;
            imageViewBadge = (ImageView) mCalloutBalloon.findViewById(R.id.badge);
            TextView textViewTitle = (TextView) mCalloutBalloon.findViewById(R.id.title);
            textViewDesc = (TextView) mCalloutBalloon.findViewById(R.id.desc);
            textViewTitle.setText(item.title);
            textViewDesc.setText("한번 클릭해주세요");
            imageViewBadge.setImageDrawable(createDrawableFromUrl(item.imageUrl));
            return mCalloutBalloon;
        }

        @Override
        public View getPressedCalloutBalloon(MapPOIItem poiItem) {
            return null;
        }

    }

    private Drawable createDrawableFromUrl(String url) {
        try {
            InputStream is = (InputStream) this.fetch(url);
            Drawable d = Drawable.createFromStream(is, "src");
            return d;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object fetch(String address) throws MalformedURLException,IOException {
        URL url = new URL(address);
        Object content = url.getContent();
        return content;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2){
            Intent intent = new Intent();
            intent.putExtra("stadium", data.getStringExtra("stadium"));
            setResult(1, intent);
            finish();
        }
    }
}
