package com.mondaychicken.bacving.stadium;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mondaychicken.bacving.R;
import com.mondaychicken.bacving.stadium.review.reviewFragment;
import com.mondaychicken.bacving.teamPlayer.ViewPagerAdapter;
import com.mondaychicken.bacving.teamPlayer.playerList.playerFragment;
import com.mondaychicken.bacving.teamPlayer.schedule.scheduleTabFragment;
import com.mondaychicken.bacving.teamPlayer.teamInformationFragment;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class stadiumActivity extends AppCompatActivity {
    RelativeLayout map;
    int permissionCheck;
    MapView mapView;
    String MapApiKey = "0b42f43598ab66b672d55c2364b29e5b";
    //s_idx 구장 고유번호
    String userno, token, s_idx, serverResultInfo,serverResultReview;
    String server = "http://52.68.69.47/api/stadium/detail_info.php";
    double lat, lng;

    CollapsingToolbarLayout collapseToolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    private AlertDialog alertDialog2 = null;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stadium_pager_view2);

        Intent intent = getIntent();
        LoadPreference(this);
        s_idx = intent.getExtras().getString("s_idx");

        stadiumInformation();
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar_stadium);

        collapseToolbar = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        viewPager = (ViewPager)findViewById(R.id.stadium_view_pager);
        tabLayout = (TabLayout)findViewById(R.id.stadium_tab_layout);
        setSupportActionBar(toolbar);
        collapseToolbar.setCollapsedTitleTextColor(Color.WHITE);
        collapseToolbar.setExpandedTitleColor(Color.WHITE);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new stadiumInformationFragment(), "정보");
        adapter.addFrag(new reviewFragment(), "리뷰");
        viewPager.setAdapter(adapter);
    }

    public void stadiumInformation(){

        new Thread(){
            @Override
            public void run() {
                stadiumInformationServer();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stadiumInformationEnd();

                    }
                });
            }
        }.start();
    }
    private void stadiumInformationServer(){
        try {
            serverResultInfo = null;
            StringBuffer data = new StringBuffer();
            data.append("user_idx").append("=").append(userno).append("&");
            data.append("s_idx").append("=").append(s_idx).append("&");
            data.append("token").append("=").append(token).append("&");

            URL url = new URL(server);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
//            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(data.toString());
            writer.flush();

            InputStreamReader ims = new InputStreamReader(http.getInputStream(),"UTF-8");
            BufferedReader reader = new BufferedReader(ims);
            StringBuilder builder = new StringBuilder();
            String str;

            while ((str = reader.readLine()) != null){
                builder.append(str);
            }

            serverResultInfo = builder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ConnectException e){
            e.printStackTrace();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    alertDialog2 = DialogNetwork();
                    alertDialog2.show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void stadiumInformationEnd(){

        String result = serverResultInfo;
        JSONObject infoData = null;
        stadiumInfo stadiumInfo = new stadiumInfo();
        int code = 0;
        String msg = "";

        if (result != null){
            Log.d("Result", result);
        }
        try{
            JSONObject json = new JSONObject(result);
            code = json.getInt("code");
            msg = json.getString("message");
            infoData = json.getJSONObject("info");
            stadiumInfo.setIdx(infoData.getString("idx"));
            stadiumInfo.setSport_idx(infoData.getString("sport_idx"));
            stadiumInfo.setName(infoData.getString("name"));
            stadiumInfo.setDescription(infoData.getString("description"));
            stadiumInfo.setLocation(infoData.getString("location"));
            stadiumInfo.setLat(infoData.getDouble("lat"));
            stadiumInfo.setLng(infoData.getDouble("lng"));
            stadiumInfo.setTel(infoData.getString("tel"));
            stadiumInfo.setPrice(infoData.getString("price"));
//            stadiumBackgroundUrl = json.getString("coverimg");
//            stadiumLogoUrl = json.getString("profileimg");

        } catch (JSONException e) {
            e.printStackTrace();

        } catch (NullPointerException e){
            e.printStackTrace();

            code = 0;
            msg = "failed";
        }

        if (code == 700){
            collapseToolbar.setTitle(stadiumInfo.getName());
//            time.setText();
            SavePreference("stadiumPlace", stadiumInfo.getLocation(), this);
            SavePreference("stadiumPhone", stadiumInfo.getTel(), this);
            SavePreference("stadiumDescription", stadiumInfo.getDescription(), this);

            lat = stadiumInfo.getLat();
            lng = stadiumInfo.getLng();
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
            }
//            collapseToolbar.setTitle(stadiumName);
//            Glide.get(stadiumActivity.this).clearMemory();
//            Glide.with(stadiumActivity.this).load(stadiumBackgroundUrl).into(stadiumBackground);
//            Glide.with(stadiumActivity.this).load(stadiumLogoUrl).into(stadiumLogo);
//            Glide.get(stadiumActivity.this).clearMemory();
//
//            setupViewPager(viewPager);
//            //탭 레이아웃과 뷰페이져를 같이 사용하는데 탭뷰와 달리 간편하게 메서드 하나로 연결이 가능하다
//            tabLayout.setupWithViewPager(viewPager);
//
//            // join이 2면 가입 완료 상태 가입완료 상태만 버튼이 안보여야 한다
//            // 설립자인데 0이 나온다. 0은 미가입
//            if (Integer.parseInt(join) == 2){
//                method_btn.setVisibility(View.GONE);
//                CoordinatorLayout.LayoutParams marginControl = (CoordinatorLayout.LayoutParams)viewPagerLayout.getLayoutParams();
//                marginControl.bottomMargin = 0;
//                viewPagerLayout.setLayoutParams(marginControl);
//            }
        }else{
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    alertDialog2 = DialogWrong();
                    alertDialog2.show();
                }
            });
            Log.d("failed", code + " " + msg);
        }
    }

    public void stadiumReview(){

        new Thread(){
            @Override
            public void run() {
                stadiumReviewServer();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stadiumReviewEnd();

                    }
                });
            }
        }.start();
    }
    private void stadiumReviewServer(){
        try {
            serverResultReview = null;
            StringBuffer data = new StringBuffer();
//            data.append("user_idx").append("=").append(userno).append("&");
//            data.append("s_idx").append("=").append(s_idx).append("&");
//            data.append("token").append("=").append(token).append("&");

            URL url = new URL(server);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
//            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(data.toString());
            writer.flush();

            InputStreamReader ims = new InputStreamReader(http.getInputStream(),"UTF-8");
            BufferedReader reader = new BufferedReader(ims);
            StringBuilder builder = new StringBuilder();
            String str;

            while ((str = reader.readLine()) != null){
                builder.append(str);
            }

            serverResultReview = builder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ConnectException e){
            e.printStackTrace();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    alertDialog2 = DialogNetwork();
                    alertDialog2.show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void stadiumReviewEnd(){

        String result = serverResultReview;
        JSONObject ReviewData = null;
        stadiumInfo stadiumInfo = new stadiumInfo();
        int code = 0;
        String msg = "";

        if (result != null){
            Log.d("Result", result);
        }
        try{
            JSONObject json = new JSONObject(result);
            code = json.getInt("code");
            msg = json.getString("message");
            ReviewData = json.getJSONObject("review"); // review is temporary
//            stadiumBackgroundUrl = json.getString("coverimg");
//            stadiumLogoUrl = json.getString("profileimg");

        } catch (JSONException e) {
            e.printStackTrace();

        } catch (NullPointerException e){
            e.printStackTrace();

            code = 0;
            msg = "failed";
        }

        if (code == 700){

            SavePreference("stadiumReview", ReviewData.toString(), this);

        }else{
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    alertDialog2 = DialogWrong();
                    alertDialog2.show();
                }
            });
            Log.d("failed", code + " " + msg);
        }
    }

    private AlertDialog DialogWrong(){
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("서버 오류");
        build.setMessage("서버에서 요청이 안뜨네요, 에러인가봐요?");
        build.setPositiveButton("확인", null);
        return build.create();
    }

    private AlertDialog DialogNetwork(){
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("인터넷 연결 오류");
        build.setMessage("인터넷과 연결 되 있지 않습니다 인터넷을 연결 해 주세요.");
        build.setPositiveButton("확인", null);
        return build.create();
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
                }else {
                    permissionCheck = PackageManager.PERMISSION_DENIED;
                }
            }
        }
    }

    // 문자값저장
    public void SavePreference(String key, String value, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void LoadPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userno = sharedPreferences.getString("userno", null);
        token = sharedPreferences.getString("token", null);
    }
}

class stadiumInfo{
    String idx, name, description, sport_idx, region, location, tel, price, terms, status, moddate, regdate;
    double lat, lng;

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSport_idx() {
        return sport_idx;
    }

    public void setSport_idx(String sport_idx) {
        this.sport_idx = sport_idx;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getModdate() {
        return moddate;
    }

    public void setModdate(String moddate) {
        this.moddate = moddate;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
