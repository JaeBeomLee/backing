package com.mondaychicken.bacving.teamPlayer.memberJoinList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mondaychicken.bacving.R;
import com.mondaychicken.bacving.teamPlayer.setting.setting;
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
 * Created by ijaebeom on 2015. 9. 10..
 */
public class joinListAdapter extends RecyclerView.Adapter<joinListViewHolder>{

    List<joinListData> joinListDatas = new ArrayList<>();
    Context context;
    AlertDialog alertDialog2;
    boolean isNone;
    private String serverResult, userNo, userName, token;
    private String server = "http://api.bacving.com/api/team/accept.php";

    public void add(joinListData main, int position){
        joinListDatas.add(position, main);
        notifyItemInserted(position);
        isNone = false;
    }

    public boolean isNoneSize(){
        if (joinListDatas.size() == 0){
            isNone = true;
        }
        return isNone;
    }

    @Override
    public joinListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewGroup viewMain = (ViewGroup)inflater.inflate(R.layout.team_member_join_list_item, parent, false);
        joinListViewHolder mainViewHolder = new joinListViewHolder(viewMain, context);

        return mainViewHolder;


    }

    @Override
    public void onBindViewHolder(joinListViewHolder holder, final int position) {
        final joinListData main = joinListDatas.get(position);
        LoadPreference(context);
        joinListViewHolder alert = (joinListViewHolder)holder;
        alert.name.setText(main.getName());
        if (main.getProfileimg() != "null"){
            Glide.with(context).load(main.getProfileimg()).into(alert.profile);
            Glide.get(context).clearMemory();
        }
        alert.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //수락 거절 서버 연동 1은 수락
                teamMemberList(main.getIdx(), 1);
                joinListDatas.remove(position);
                notifyDataSetChanged();
            }
        });
        alert.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //수락 거절 서버 연동 0은 거절 
                teamMemberList(main.getIdx(), 0);
                joinListDatas.remove(position);
                notifyDataSetChanged();
            }
        });

    }


    @Override
    public int getItemCount() {
        return joinListDatas.size();
    }

    public void teamMemberList(final int target_idx, final int bool){
        new Thread(){
            @Override
            public void run() {
                ConnectServer(target_idx, bool);
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


        }else{
            alertDialog2 = DialogInit("오류",msg);
            alertDialog2.show();
            Log.d("failed", code + " " + msg);
        }
    }
    private void ConnectServer(int target, int bool){
        try {
            serverResult = null;
            StringBuffer data = new StringBuffer();
            data.append("target_accept").append("=").append(bool).append("&");
            data.append("target_idx").append("=").append(target).append("&");
            data.append("user_idx").append("=").append(userNo).append("&");
            data.append("token").append("=").append(token).append("&");
            data.append("team_idx").append("=").append(setting.team_idx).append("&");

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

    private void LoadPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userNo = sharedPreferences.getString("userno", null);
        userName = sharedPreferences.getString("name", null);
        token = sharedPreferences.getString("token", null);
    }
}
