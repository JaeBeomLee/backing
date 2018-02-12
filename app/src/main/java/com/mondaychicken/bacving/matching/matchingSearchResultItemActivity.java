package com.mondaychicken.bacving.matching;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mondaychicken.bacving.R;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
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

/**
 * Created by leejaebeom on 2015. 11. 10..
 */
public class matchingSearchResultItemActivity extends AppCompatActivity {
    RelativeLayout map;
    int permissionCheck;
    MapView mapView;
    String server = "http://api.bacving.com/api/match/request.php";
    String serverResult, userno, token;
    private AlertDialog alertDialog2 = null;
    TextView name, time, place, matchType;
    Button matching;
    String type = null, teamName, match_idx = null, stadium_text = null, region_text = null, meet_date = null, team_idx = null;
    double lat , lng ;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching_search_result_item_activity);
        //이름 시간 장소 타입 가 필요함
        //이름, 시간, 장소는 서버, 타입은 내가 입력한 값 넘겨주기
        Intent intent = getIntent();
        type = intent.getExtras().getString("type");
        match_idx = intent.getExtras().getString("match_idx");
        teamName = intent.getExtras().getString("teamName");
        stadium_text = intent.getExtras().getString("stadium_text");
        region_text = intent.getExtras().getString("region_text");
        lng = intent.getExtras().getDouble("lng");
        lat = intent.getExtras().getDouble("lat");
        meet_date = intent.getExtras().getString("time");
        team_idx = intent.getExtras().getString("team_idx");
        LoadPreference(this);

        name = (TextView)findViewById(R.id.matching_search_result_item_act_name);
        time = (TextView)findViewById(R.id.matching_search_result_item_act_time);
        place = (TextView)findViewById(R.id.matching_search_result_item_act_place);
        matchType = (TextView)findViewById(R.id.matching_search_result_item_act_type);
        matching = (Button)findViewById(R.id.matching_search_result_item_act_btn);

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
            mapView.setDaumMapApiKey("9e0d02655d291c8352e1578cc5617703");
            map.addView(mapView);
            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lat,lng), true);
            mapView.setZoomLevel(-2, true);
        }

        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar_stadium);
        CollapsingToolbarLayout collapseToolbar = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        collapseToolbar.setTitle("");
        collapseToolbar.setCollapsedTitleTextColor(Color.WHITE);
        collapseToolbar.setExpandedTitleColor(Color.WHITE);

        name.setText(teamName);
        time.setText(meet_date);
        place.setText(stadium_text);
        matchType.setText(type);
        matching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matching();
            }
        });
    }

    public void matching(){

        new Thread(){
            @Override
            public void run() {
                ConnectServer();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        matchingEnd();


                    }
                });
            }
        }.start();
    }
    private void matchingEnd(){

        String result = serverResult;
        Log.d("fragmentResult", result);
        int code = 0;
        String msg = "";
        JSONArray teamArray = null;
        if (result == null){
            return;
        }
        try{
            JSONObject json = new JSONObject(result);
            code = json.getInt("code");
            msg = json.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();

        } catch (NullPointerException e){
            e.printStackTrace();

            code = 0;
            msg = "failed";
        }

        if (code == 700){
            finish();
            matchingSearchResultActivity.matchingSearchResult.finish();
            Toast.makeText(this,"신청이 완료 되었습니다!", Toast.LENGTH_SHORT).show();
        }else{
            alertDialog2 = DialogWrong();
            alertDialog2.show();
            Log.d("failed", code + " " + msg);
        }
    }


    private void ConnectServer(){
        try {
            serverResult = null;
            StringBuffer data = new StringBuffer();
            data.append("user_idx").append("=").append(userno).append("&");
            data.append("token").append("=").append(token).append("&");
            data.append("match_idx").append("=").append(match_idx).append("&");
            data.append("request_idx").append("=").append(team_idx).append("&");

            URL url = new URL(server);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");

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

            serverResult = builder.toString();

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

    private AlertDialog DialogWrong() {
        final AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("입력값 오류");
        build.setMessage("입력해야 할 모든 사항을 입력하지 않았습니다");
        build.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog2.dismiss();

            }
        });
        return build.create();
    }

    private AlertDialog DialogNetwork() {
        final AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("인터넷 연결 오류");
        build.setMessage("인터넷과 연결 되 있지 않습니다 인터넷을 연결 해 주세요.");
        build.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog2.dismiss();

            }
        });
        return build.create();
    }
    private void LoadPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userno = sharedPreferences.getString("userno", null);
        token = sharedPreferences.getString("token", null);
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
