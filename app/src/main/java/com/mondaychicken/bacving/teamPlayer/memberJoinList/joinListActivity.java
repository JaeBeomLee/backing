package com.mondaychicken.bacving.teamPlayer.memberJoinList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

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


/**
 * Created by leejaebeom on 2015. 12. 2..
 */
public class joinListActivity extends AppCompatActivity{

    AlertDialog alertDialog2;
    RecyclerView recyclerView;
    joinListAdapter adapter;
    LinearLayoutManager manager;
    Toolbar toolbar;
    LinearLayout none;
    int position = 0;
    private String serverResult, userNo, userName, token;
    private String server = "http://api.bacving.com/api/team/accept_list.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_member_join_list);
        recyclerView = (RecyclerView)findViewById(R.id.team_member_join_list);
        toolbar = (Toolbar)findViewById(R.id.tool_bar_join_list);
        none = (LinearLayout)findViewById(R.id.join_list_none);
        adapter = new joinListAdapter();
        manager = new LinearLayoutManager(this);

        toolbar.setTitle("가입 요청 리스트");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(position);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        LoadPreference(this);
        teamMemberList();
    }


    public void teamMemberList(){
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
        String msg = "", idx[] = null, name[] = null, grade[] = null, profileimg[] = null;
        JSONObject array[] = null;
        JSONArray members = null;
        if (result == null){
            return;
        }
        try{
            JSONObject json = new JSONObject(result);
            code = json.getInt("code");
            msg = json.getString("message");
            members = json.getJSONArray("data");
            array = new JSONObject[members.length()];
            idx = new String[members.length()];
            name = new String[members.length()];
            grade = new String[members.length()];
            profileimg = new String[members.length()];
            for (int i = 0; i<members.length(); i++){
                array[i] = members.getJSONObject(i);
                idx[i] = array[i].getString("userno");
                name[i] = array[i].getString("name");
                grade[i] = array[i].getString("grade");
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
            for(int i = 0; i< members.length(); i++){
                adapter.add(createMainList(name[i], profileimg[i], grade[i], Integer.parseInt(idx[i])), i);
            }
            if (adapter.isNoneSize()){
                none.setVisibility(View.VISIBLE);
            }
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
            data.append("token").append("=").append(token).append("&");
            data.append("page").append("=").append(1).append("&");
            data.append("user_idx").append("=").append(userNo).append("&");
            data.append("team_idx").append("=").append(teamActivity.team_idx).append("&");
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
                    alertDialog2 = DialogInit("인터넷 연결 오류", "인터넷과 연결 되 있지 않습니다 인터넷을 연결 해 주세요.");
                    alertDialog2.show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private joinListData createMainList(String name, String profile, String grade, int index ){
        joinListData join = new joinListData();
        join.setName(name);
        join.setProfileimg(profile);
        join.setGrade(grade);
        join.setIdx(index);
        return join;
    }

    private AlertDialog DialogInit(String title, String message){
        AlertDialog.Builder build = new AlertDialog.Builder(this);
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

    //SharedPreference를 이용하여 저장된 데이터를 불러오는 메서드
    private void LoadPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userNo = sharedPreferences.getString("userno", null);
        userName = sharedPreferences.getString("name", null);
        token= sharedPreferences.getString("token", null);

    }

}
