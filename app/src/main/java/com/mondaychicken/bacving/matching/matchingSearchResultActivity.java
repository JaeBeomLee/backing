package com.mondaychicken.bacving.matching;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mondaychicken.bacving.R;
import com.mondaychicken.bacving.gpsInfo;

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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by leejaebeom on 2015. 10. 31..
 */
public class matchingSearchResultActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    int position = 0;
    String server = "http://api.bacving.com/api/match/waiting_list.php";
    String teamServer = "http://52.68.69.47/api/team/index.php";
    //static 인 이유는 매칭 등록에 필요한 정보들이라서 넘겨줘야 하기 때문
    static String sport_idx, location, matchType, time, date, month, year, team_idx, teamName;
    String serverResult;
    static String waitMatch_idx[], waitType[], waitTeam_idx[], waitSport_idx[], waitStadiumAd[], waitTime[], waitTeamName[], waitTeamProfileUrl[], waitTeamAge[], token, userno, headerTime, tailTime;
    TextView resultTime, resultSport, resultType, resultSort;
    Context context;
    static double latitude, longitude;
    int permissionCheckGPS;
    private AlertDialog alertDialog2 = null;
    private gpsInfo gps;
    JSONObject array[] = null;
    matchingFragmentAdapter adapter;
    List<matchingFragmentData> mainList = new ArrayList<matchingFragmentData>();
    public static Activity matchingSearchResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching_search_result);

        matchingSearchResult = this;

        Intent intent = getIntent();
        sport_idx = intent.getExtras().getString("sport_idx");
        matchType = intent.getExtras().getString("match_type");
        location = intent.getExtras().getString("location");
        time = intent.getExtras().getString("time");
        date = intent.getExtras().getString("date");
        month = intent.getExtras().getString("month");
        year = intent.getExtras().getString("year");
        team_idx = intent.getExtras().getString("team_idx");
        teamName = intent.getExtras().getString("teamName");
        context = this;
        LoadPreference(context);
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar_matching_search_result);
        toolbar.setTitle("검색 결과");
        toolbar.setTitleTextColor(Color.WHITE);

        switch (time){
            case "전체" :
                headerTime = "00:00";
                tailTime = "23:59";
                break;
            case "오전" :
                headerTime = "06:00";
                tailTime = "11:59";
                break;
            case "오후" :
                headerTime = "12:00";
                tailTime = "17:59";
                break;
            case "저녁" :
                headerTime = "18:00";
                tailTime = "23:59";
                break;
            case "새벽" :
                headerTime = "00:00";
                tailTime = "5:59";
                break;
        }

        resultTime = (TextView)findViewById(R.id.matching_search_result_time);
        resultSport = (TextView)findViewById(R.id.matching_search_result_sports);
        resultType = (TextView)findViewById(R.id.matching_search_result_type);
        resultSort = (TextView)findViewById(R.id.matching_search_result_sort);
        gps = new gpsInfo(this);

        resultTime.setText(year + "년 " + month + "월 " + date + "일 " + time);
        resultType.setText(matchType);
        if (Integer.parseInt(team_idx) == 1){
            resultSport.setText("축구");
        }

        recyclerView = (RecyclerView)findViewById(R.id.matching_search_result_recycler);
        manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(position);
        recyclerView.setLayoutManager(manager);
        adapter = new matchingFragmentAdapter(mainList);
        recyclerView.setAdapter(adapter);

        permissionCheckGPS = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        int MyResultID = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionCheckGPS == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MyResultID);
            }
        }

        if (permissionCheckGPS == PackageManager.PERMISSION_DENIED) {

        } else {
            if (gps.isGetLocation()){
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

                matchingWaitingList();
            }else{
                gps.showSettingsAlert();
            }
        }

    }

    public void matchingWaitingList(){

        new Thread(){
            @Override
            public void run() {
                ConnectServer();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        watingListEnd();


                    }
                });
            }
        }.start();
    }
    private void watingListEnd(){

        String result = serverResult;
        Log.d("fragmentResult2", result);
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
            teamArray = json.getJSONArray("list_item");
            array = new JSONObject[teamArray.length()];
            waitTeamName = new String[teamArray.length()];
            waitMatch_idx = new String[teamArray.length()];
            waitSport_idx = new String[teamArray.length()];
            waitType = new String[teamArray.length()];
            waitTeam_idx = new String[teamArray.length()];
            waitTeamAge = new String[teamArray.length()];
            waitTime = new String[teamArray.length()];
            waitStadiumAd = new String[teamArray.length()];
            waitTeamProfileUrl = new String[teamArray.length()];
            for (int i = 0; i<teamArray.length(); i++){
                array[i] = teamArray.getJSONObject(i);
                waitMatch_idx[i] = array[i].getString("match_idx");
                waitSport_idx[i] = array[i].getString("sport_idx");
                waitTeamName[i] = array[i].getString("team_name");
                waitTeamProfileUrl[i] = array[i].getString("team_profileimg");
                waitType[i] = array[i].getString("type");
                waitTeam_idx[i] = array[i].getString("w_idx");
                waitTeamAge[i] = array[i].getString("a_age");
                waitTime[i] = array[i].getString("meet_date");
                waitStadiumAd[i] = array[i].getString("region_text");
            }
        } catch (JSONException e) {
            e.printStackTrace();

        } catch (NullPointerException e){
            e.printStackTrace();

            code = 0;
            msg = "failed";
        }

        if (code == 700){
            for (int j = 0; j <=array.length; j++){
                if (j == array.length){
                    adapter.add(null, j);
                    continue;
                }
                adapter.add(createMainList(waitTeamName[j], waitTeamAge[j], waitStadiumAd[j], waitTime[j], waitTeamProfileUrl[j]), j);

            }
            manager.scrollToPosition(position);

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
            if (matchType.equals("11:11")){
                data.append("sport_idx").append("=").append(1).append("&");
            }else if(matchType.equals("5:5")){
                data.append("sport_idx").append("=").append(2).append("&");
            }
            data.append("token").append("=").append(token).append("&");
            data.append("lat").append("=").append(latitude).append("&");
            data.append("lng").append("=").append(longitude).append("&");
            data.append("region_code").append("=").append(location).append("&");
            data.append("date_code").append("=").append(year+"-"+month+"-"+date).append("&");
            data.append("s_time_code").append("=").append(headerTime).append("&");
            data.append("t_time_code").append("=").append(tailTime).append("&");
            data.append("page").append("=").append(1).append("&");
            data.append("limit").append("=").append(15).append("&");
            data.append("distance").append("=").append(1000000).append("&");

            Log.d("data to server",data.toString());


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


    private matchingFragmentData createMainList(String name, String age, String place, String time, String profile ){
        matchingFragmentData main = new matchingFragmentData();
        main.setName(name);
        main.setLogo(profile);
        main.setAge(age);
        main.setPlace(place);
        main.setTime(time);
        return main;
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
                    permissionCheckGPS= PackageManager.PERMISSION_GRANTED;
                    if (gps.isGetLocation()){
                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();

                        matchingWaitingList();
                    }else{
                        gps.showSettingsAlert();
                    }
                }else {
                    permissionCheckGPS = PackageManager.PERMISSION_DENIED;
                }
            }
        }
    }
}
