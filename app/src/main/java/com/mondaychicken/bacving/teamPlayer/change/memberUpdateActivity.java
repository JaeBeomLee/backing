package com.mondaychicken.bacving.teamPlayer.change;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mondaychicken.bacving.R;
import com.mondaychicken.bacving.teamPlayer.teamActivity;

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
 * Created by siyeol on 15. 11. 27..
 */
public class memberUpdateActivity extends AppCompatActivity {

    String nickname, userNo, userName, token, serverResult, team_idx, getNickname;
    int code;
    Context context;
    private AlertDialog alertDialog2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_member_update);

        context = memberUpdateActivity.this;
        LoadPreference(context);
        //화면 상의 컴포넌트 불러오기
        final EditText nickname_txt = (EditText)findViewById(R.id.team_member_update_nickname);
        Button edit_btn = (Button)findViewById(R.id.team_member_update_btn);

        Intent intent = getIntent();
        nickname = intent.getExtras().getString("nickname");
        team_idx = intent.getExtras().getString("team_idx");
        //입력창에 기존 닉네임 대입
        nickname_txt.setText(nickname);

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNickname = nickname_txt.getText().toString();
                Update();
            }
        });

    }

    public void Update(){

        final ProgressDialog dialog = ProgressDialog.show(this, "", "잠시만 기다려 주세요",true);

        new Thread(){
            @Override
            public void run() {
                UpdateUserInfo();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        UpdateEnd();

                    }
                });
                dialog.dismiss();
            }
        }.start();
    }

    private void UpdateUserInfo(){
        try {
            serverResult = null;
            StringBuffer data = new StringBuffer();
            data.append("user_idx").append("=").append(userNo).append("&");
            data.append("team_idx").append("=").append(team_idx).append("&");
            data.append("token").append("=").append(token).append("&");
            data.append("nickname").append("=").append(getNickname);

            URL url = new URL("http://api.bacving.com/api/team/member_update.php");
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

    private void UpdateEnd(){

        String result = serverResult;
        if (result!= null){
            Log.d("result", result);
        }

        String msg = "";

        if (result == null){
            return;
        }
        try{
            JSONObject json = new JSONObject(result);
            code = json.getInt("code");
            msg = json.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("failed", result);

        } catch (NullPointerException e){
            e.printStackTrace();

            Log.d("failed", result);
            code = 0;
            msg = "failed";
        }

        if (code == 700){
            alertDialog2 = DialogInit("정보 수정","닉네임 변경이 성공적으로 완료되었습니다.");
            alertDialog2.show();

            Log.d("Sucess", result);
        }else{
            alertDialog2 = DialogInit("오류",msg);
            alertDialog2.show();
            Log.d("failed", code + " " + msg);
            Log.d("result", result);

//            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
//            Log.d("failed", result);


        }
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

    private void LoadPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userNo = sharedPreferences.getString("userno", null);
        userName = sharedPreferences.getString("name", null);
        token = sharedPreferences.getString("token", null);
    }

}
