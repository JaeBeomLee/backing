package com.mondaychicken.bacving.teamPlayer.schedule;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mondaychicken.bacving.R;

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

import static com.google.android.gms.internal.zzid.runOnUiThread;

/**
 * Created by ijaebeom on 2015. 9. 13..
 */
public class scheduleTabFragment extends Fragment{
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    int position = 0;

    private String scheduleYear, scheduleMonth ,scheduleDate, scheduleApm, scheduleHour, scheduleMinute, scheduleStadium, scheduleDateVisual, scheduleDayVisual, scheduleSubMonth;
    private String server = "http://api.bacving.com/api/match/myProgressList.php", serverResult;
    //다이얼로그에 필요한 Bundle과 Context
    Bundle bundle;
    Context context;

    scheduleTabFragmentAdapter adapter;
    List<scheduleTabFragmentMainData> mainList = new ArrayList<scheduleTabFragmentMainData>();
    private AlertDialog alertDialog2 = null;
    private String userNo, userName, token, userProfileimg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bundle = savedInstanceState;
        context = container.getContext();

        LoadPreference(context);
        final View v = inflater.inflate(R.layout.schedule_list,container, false);
        recyclerView = (RecyclerView)v.findViewById(R.id.main_list_recycler);
        manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(position);
        recyclerView.setLayoutManager(manager);
        adapter = new scheduleTabFragmentAdapter(mainList);
        recyclerView.setAdapter(adapter);

        teamSchedule();

        for (int i = 0; i<1; i++){
            adapter.add(createMainList(), position);
            position++;
        }
        for (int i = 0; i < 10; i++) {
            adapter.add(createMainList2(), position);
            position++;
        }
        for (int i = 0; i<1; i++){
            adapter.add(createMainList(), position);
            position++;
        }

        for (int i = 0; i < 5; i++) {
            adapter.add(createMainList2(), position);
            position++;
        }
        manager.scrollToPosition(0);

        return v;
    }

    public void teamSchedule(){

        new Thread(){
            @Override
            public void run() {
                teamScheduleServer();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        teamScheduleEnd();

                    }
                });
            }
        }.start();
    }
    private void teamScheduleServer(){
        try {
            serverResult = null;
            StringBuffer data = new StringBuffer();
            data.append("user_idx").append("=").append(userNo).append("&");
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

            serverResult = builder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ConnectException e){
            e.printStackTrace();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    alertDialog2 = DialogInit("인터넷 연결 오류", "인터넷과 연결 되 있지 않습니다 인터넷을 연결 해 주세요.");
                    alertDialog2.show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void teamScheduleEnd(){

        String result = serverResult;

        int code = 0;
        String msg = "";

        if (result == null){
            return;
        }else{
            Log.d("scheduleJSON", code + " " + msg);
        }
        try{
            JSONObject json = new JSONObject(result);
            code = json.getInt("code");
            msg = json.getString("message");
//            team_idx = json.getString("idx");
//            sport_idx = json.getString("sport_idx");
//            teamDescription = json.getString("description");
//            sport_idx = json.getString("sport_idx");
//            teamBackgroundUrl = json.getString("coverimg");
//            teamLogoUrl = json.getString("profileimg");
//            join = json.getString("join");

        } catch (JSONException e) {
            e.printStackTrace();

        } catch (NullPointerException e){
            e.printStackTrace();

            code = 0;
            msg = "failed";
        }

        if (code == 700){

        }else{
            alertDialog2 = DialogInit("에러",msg);
            alertDialog2.show();
            Log.d("failed", code + " " + msg);
        }
    }

    private scheduleTabFragmentMainData createMainList(){
        scheduleTabFragmentMainData main = new scheduleTabFragmentMainData();
        main.setscheduleYear("2015");
        main.setscheduleMonth("11");
        main.setscheduleDate("3");
        main.setscheduleApm("오후");
        main.setscheduleHour("3");
        main.setscheduleMinute("30");
        main.setScheduleStadium("광화문 광장");
        main.setScheduleScore("3 : 1");
        main.setScheduleResult("WIN");
        main.setScheduleDateVisual("3");
        main.setScheduleDayVisual("토요일");

        main.setScheduleSubMonth("11월");
        return main;
    }

    private scheduleTabFragmentMainData createMainList2(){
        scheduleTabFragmentMainData main = new scheduleTabFragmentMainData();
        main.setscheduleYear("2015");
        main.setscheduleMonth("10");
        main.setscheduleDate("13");
        main.setscheduleApm("오후");
        main.setscheduleHour("3");
        main.setscheduleMinute("30");
        main.setScheduleStadium("광화문 광장");
        main.setScheduleScore("3 : 1");
        main.setScheduleResult("WIN");
        main.setScheduleDateVisual("13");
        main.setScheduleDayVisual("토요일");

        main.setScheduleSubMonth(null);
        return main;
    }

    private void LoadPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userNo = sharedPreferences.getString("userno", null);
        userName = sharedPreferences.getString("name", null);
        token = sharedPreferences.getString("token", null);
        userProfileimg = sharedPreferences.getString("profileimg", null);
    }

    private AlertDialog DialogInit(String title, String message){
        AlertDialog.Builder build = new AlertDialog.Builder(context);
        if(title.length() > 0) {
            build.setTitle(title);
        }
        build.setMessage(message);
        build.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog2.dismiss();

            }
        });
        return build.create();
    }


}