package com.mondaychicken.bacving.main.total;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mondaychicken.bacving.R;
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
import static com.google.android.gms.internal.zzid.runOnUiThread;

/**
 * Created by ijaebeom on 2015. 9. 13..
 */

public class mainTabFragment extends Fragment{
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    int position = 0;
    private AlertDialog alertDialog2 = null;

    String mainTeamName[], userno, token;
    static String mainBackgroundUrl[], mainTeamLogoUrl[], leader_idx[], team_idx[];
    JSONObject array[];
    String server = "http://api.bacving.com/api/member/info.php";
    String serverResult, teamCount;
    String subLeague, subApm, subHour, subMinute, subDate, subMonth, subYear, subNext,subStadium;
    //다이얼로그에 필요한 Context
    Context context;

    mainTabFragmentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LoadPreference(getActivity().getApplicationContext());
        context = container.getContext();
        // 이 fragment의 뷰
        final View v = inflater.inflate(R.layout.main_list,container, false);
        //밑으론 전부 리사이클러 뷰 설정
        recyclerView = (RecyclerView)v.findViewById(R.id.main_list_recycler);
        manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(position);
        recyclerView.setLayoutManager(manager);
        adapter = new mainTabFragmentAdapter();
        recyclerView.setAdapter(adapter);
        //팀 리스트 불러오는 메서드
        teamList();

        return v;
    }

    public void teamList(){

        new Thread(){
            @Override
            public void run() {
                //서버 연결
                ConnectServer();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        teamListEnd();

                    }
                });
            }
        }.start();
    }
    private void teamListEnd(){

        String result = serverResult;
        Log.d("fragmentResult", result);
        int code = 0;
        String msg = "";

        if (result == null){
            return;
        }
        try{
            JSONObject json = new JSONObject(result);
            code = json.getInt("code");
            msg = json.getString("message");
            teamCount = json.getString("team_count");
            JSONArray teamArray = json.getJSONArray("data");
            //배열크기가 좀 특이하게 되있는데 그 이유는 0번 리스트는 일정 등록카드이기 때문이다
            array = new JSONObject[teamArray.length()+1];
            mainTeamName = new String[teamArray.length()+1];
            team_idx = new String[teamArray.length()+1];
            leader_idx = new String[teamArray.length()+1];
            mainBackgroundUrl = new String[teamArray.length()+1];
            mainTeamLogoUrl = new String[teamArray.length()+1];
            for (int i = 1; i<=teamArray.length(); i++){
                array[i] = teamArray.getJSONObject(i - 1);
                JSONObject leader = array[i].getJSONObject("leader");
                leader_idx[i] = leader.getString("idx");
                team_idx[i] = array[i].getString("idx");
                mainTeamName[i] = array[i].getString("name");
                mainTeamLogoUrl[i] = array[i].getString("profile_thumb");
                mainBackgroundUrl[i] = array[i].getString("cover_thumb");
            }
        } catch (JSONException e) {
            e.printStackTrace();

        } catch (NullPointerException e){
            e.printStackTrace();

            code = 0;
            msg = "failed";
        }

        if (code == 700){
            //이건 조만간 없앨 부분이다 신경 안써도 됨
            if (Integer.parseInt(teamCount) != 0){
                subLeague = "major";
                subDate = "11";
                subMonth = "6";
                subYear = "2015";
                subApm = "오전";
                subHour = "6";
                subMinute = "30";
                subStadium = "잠실야구장";
                subNext = "수원FC";
                //여기까지
                for (int i = array.length-1; i >= 0; i--) {
                    if (i == 0){
                        adapter.add(createMainList(null, null, null), position);
                    }else{
                        adapter.add(createMainList(mainTeamName[i], mainTeamLogoUrl[i], mainBackgroundUrl[i]), position);
                    }

                }
                manager.scrollToPosition(position);
            }
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
    private void ConnectServer(){
        try {
            //주구 장창 서버연결
            serverResult = null;
            StringBuffer data = new StringBuffer();
            //파라미터 작성
            data.append("user_idx").append("=").append(userno).append("&");
            data.append("token").append("=").append(token).append("&");

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

    private AlertDialog DialogWaring(){
        final AlertDialog.Builder build = new AlertDialog.Builder(context);
        build.setMessage("이메일이나 비밀번호 입력을 제대로 안했습니다.");
        build.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog2.dismiss();

            }
        });
        return build.create();
    }

    private AlertDialog DialogWrong(){
        final AlertDialog.Builder build = new AlertDialog.Builder(context);
        build.setTitle("이메일, 비밀번호 오류");
        build.setMessage("이메일이나 비밀번호가 일치하지 않습니다. 이메일과 비밀번호를 확인해주세요");
        build.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog2.dismiss();

            }
        });
        return build.create();
    }

    private AlertDialog DialogNetwork(){
        final AlertDialog.Builder build = new AlertDialog.Builder(context);
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

    private mainTabFragmentMainData createMainList(String name, String logo, String cover){
        mainTabFragmentMainData main = new mainTabFragmentMainData();

        main.setTeamName(name);
        main.setMainBackground(cover);
        main.setTeamLogo(logo);
        main.setMatchDataApm(subApm);
        main.setMatchDataHour(subHour);
        main.setMatchDataMinute(subMinute);
        main.setMatchDataDate(subDate);
        main.setMatchDataMonth(subMonth);
        main.setMatchDataYear(subYear);
        main.setMatchStadium(subStadium);
        main.setNextMatch(subNext);
        return main;
    }

    private void LoadPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userno = sharedPreferences.getString("userno", null);
        token = sharedPreferences.getString("token", null);
    }
}
class team{

}