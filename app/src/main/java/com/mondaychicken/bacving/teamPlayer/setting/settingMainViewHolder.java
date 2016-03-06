package com.mondaychicken.bacving.teamPlayer.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import static com.google.android.gms.internal.zzid.runOnUiThread;
import com.mondaychicken.bacving.R;
import com.mondaychicken.bacving.main.MainPage;
import com.mondaychicken.bacving.teamPlayer.memberJoinList.joinListActivity;
import com.mondaychicken.bacving.teamPlayer.teamActivity;
import com.mondaychicken.bacving.teamPlayer.change.updateActivity;
import com.mondaychicken.bacving.teamPlayer.change.memberUpdateActivity;

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
 * Created by ijaebeom on 2015. 9. 10..
 */
public class settingMainViewHolder extends settingViewHolder implements View.OnClickListener{
    TextView main;
    Context context;
    boolean teamMember;
    private AlertDialog alertDialog = null;
    String server = "http://52.68.69.47/api/team/team_delete.php";
    String serverResult;
    String userNo = "";
    String token = "";
    int num = 1, endNum = num + 2;

    public settingMainViewHolder(View itemView, Context context) {
        super(itemView, context);
        this.context = context;
        main = (TextView)itemView.findViewById(R.id.setting_main_item);
        LoadPreference(context);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (teamMember == false){

            if (getLayoutPosition() == num){
                //팀 삭제, 1
                alertDialog = AnswerDialogInit();
                alertDialog.show();
            }else if (getLayoutPosition() == (num + 1)){
                //정보 수정, 2
                Intent intent = new Intent(context, updateActivity.class);
                intent.putExtra("team_idx", setting.team_idx);
                context.startActivity(intent);
            }else if (getLayoutPosition() == (num + 2)){
                //가입 요청자 리스트, 3
                Intent intent = new Intent(context, joinListActivity.class);
                context.startActivity(intent);
            }else if (getLayoutPosition() == (endNum + 2)){
                //별명 설정, 5
                Log.d("id", String.valueOf(getItemId()));
                Intent intent2 = new Intent(context, memberUpdateActivity.class);
                intent2.putExtra("nickname", setting.nickname);
                intent2.putExtra("team_idx", setting.team_idx);
                context.startActivity(intent2);
            }
        }
        Log.d("touch", "num : " + getLayoutPosition());
    }
    //질의응답 식 다이얼로그 생성
    private AlertDialog AnswerDialogInit() {
        AlertDialog.Builder build = new AlertDialog.Builder(context);
        build.setTitle("팀 삭제");
        build.setMessage("정말 팀을 삭제하시겠습니까?");
        build.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                teamDeleteAction();
                alertDialog.dismiss();

            }
        });
        build.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        return build.create();
    }

    public void teamDeleteAction(){

        new Thread(){
            @Override
            public void run() {
                teamDelete();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        teamDeleteEnd();

                    }
                });
            }
        }.start();
    }
    //팀 삭제
    private void teamDelete(){
        try {
            serverResult = null;
            StringBuffer data = new StringBuffer();
            data.append("user_idx").append("=").append(userNo).append("&");
            data.append("team_idx").append("=").append(setting.team_idx).append("&");
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
                    alertDialog = DialogInit("인터넷 연결 오류", "인터넷과 연결 되 있지 않습니다 인터넷을 연결 해 주세요.",context);
                    alertDialog.show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void teamDeleteEnd(){

        String result = serverResult;

        int code = 0;
        String msg = "";

        if (result == null){
            return;
        }else{
            Log.d("Result", result);
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
            Intent intent = new Intent(context, MainPage.class);
            intent.putExtra("isDeleted","y");
            context.startActivity(intent);
            teamActivity teamActivity = (teamActivity) com.mondaychicken.bacving.teamPlayer.teamActivity.tActivity;
            teamActivity.finish();
            setting setActivity = (setting)setting.setActivity;
            setActivity.finish();
            //alertDialog = DialogInit("삭제 성공","팀이 삭제되었습니다.", MainPage.this);
            //alertDialog.show();
        }else{
            alertDialog = DialogInit("팀 삭제 오류",msg,context);
            alertDialog.show();
            Log.d("failed", code + " " + msg);
        }
    }

    //일반 다이얼로그 생성
    private AlertDialog DialogInit(String title, String message, Context context1){
        AlertDialog.Builder build = new AlertDialog.Builder(context1);
        if(title.length() > 0) {
            build.setTitle(title);
        }
        build.setMessage(message);
        build.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        return build.create();
    }

    private void LoadPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        teamMember= sharedPreferences.getBoolean("teamMember", false);
        userNo = sharedPreferences.getString("userno", null);
        token = sharedPreferences.getString("token", null);
    }
}

