package com.mondaychicken.bacving.teamPlayer.playerList;

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
import com.mondaychicken.bacving.teamPlayer.teamActivity;

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

import static com.google.android.gms.internal.zzid.runOnUiThread;

/**
 * Created by ijaebeom on 2015. 9. 13..
 */
public class playerFragment extends Fragment{
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    int position = 0;

    private String server = "http://api.bacving.com/api/team/index.php";
    private String serverResult, userNo, userName, token;
    AlertDialog alertDialog2;
    private String scheduleSubMonth;
    //다이얼로그에 필요한 Bundle과 Context
    Bundle bundle;
    Context context;
    int positionNum = 0;
    playerFragmentAdapter adapter;
    List<playerFragmentData> mainList = new ArrayList<playerFragmentData>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bundle = savedInstanceState;
        context = container.getContext();

        final View v = inflater.inflate(R.layout.team_list_view,container, false);
        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(position);
        recyclerView.setLayoutManager(manager);
        adapter = new playerFragmentAdapter(mainList);
        recyclerView.setAdapter(adapter);

        LoadPreference(context);

        teamMemberList();
        return v;
    }

    public void teamMemberList(){

        if(adapter.getItemCount() > 0){
            manager.scrollToPosition(0);
            return;
        }

        new Thread(){
            @Override
            public void run() {
                ConnectServer();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        teamMemberListEnd();

                    }
                });
            }
        }.start();
    }
    private void teamMemberListEnd(){

        String result = serverResult;
        Log.d("fragmentResult", result);
        int code = 0;
        String msg = "", idx[] = null, name[] = null, nickname[] = null, profileimg[] = null;
        JSONObject array[] = null;
        JSONArray members = null;
        if (result == null){
            return;
        }
        try{
            JSONObject json = new JSONObject(result);
            code = json.getInt("code");
            msg = json.getString("message");
            members = json.getJSONArray("members");
            array = new JSONObject[members.length()];
            idx = new String[members.length()];
            name = new String[members.length()];
            nickname = new String[members.length()];
            profileimg = new String[members.length()];
            for (int i = 0; i<members.length(); i++){
                array[i] = members.getJSONObject(i);
                idx[i] = array[i].getString("idx");
                name[i] = array[i].getString("name");
                nickname[i] = array[i].getString("nickname");
                profileimg[i] = array[i].getString("profileimg");
            }
        } catch (JSONException e) {
            e.printStackTrace();

        } catch (NullPointerException e){
            e.printStackTrace();

            code = 0;
            msg = "failed";
        }

        if (code == 700){

            for (int i = 0; i < array.length; i++) {
                adapter.add(createMainList(idx[i], name[i], nickname[i], profileimg[i]), positionNum);
            }
            manager.scrollToPosition(0);

        }else{
            alertDialog2 = DialogInit("오류",msg);
            alertDialog2.show();
            Log.d("failed", code + " " + msg);
        }
    }
    private void ConnectServer(){
        try {
            serverResult = null;
            StringBuffer data = new StringBuffer();
            data.append("type").append("=").append(2).append("&");
            data.append("mode").append("=").append(7).append("&");
            data.append("token").append("=").append(token).append("&");
            data.append("user_idx").append("=").append(userNo).append("&");
            data.append("team_idx").append("=").append(teamActivity.team_idx).append("&");

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
                    alertDialog2 = DialogInit("인터넷 연결 오류", "인터넷과 연결 되 있지 않습니다 인터넷을 연결 해 주세요.");
                    alertDialog2.show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private playerFragmentData createMainList(String idx, String name, String nickname, String profileimg){
        playerFragmentData main = new playerFragmentData();
        main.setIdx(idx);
        main.setName(name);
        main.setNickname(nickname);
        main.setProfileimg(profileimg);
        return main;
    }

    private void LoadPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userNo = sharedPreferences.getString("userno", null);
        userName = sharedPreferences.getString("name", null);
        token = sharedPreferences.getString("token", null);
    }

}