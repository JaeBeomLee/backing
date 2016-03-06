package com.mondaychicken.bacving.matching.etc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.android.gms.internal.zzid.runOnUiThread;

/**
 * Created by leejaebeom on 2015. 12. 10..
 */
public class matchingResultActivity extends AppCompatActivity{

    Toolbar toolbar;
    TextView stadium, date, time, teamAName, teamBName;
    EditText teamAScore, teamBScore;
    CircleImageView teamAProfile, teamBProfile;
    Button registerBtn;
    String server = "http://aws.bacving.com/api/match/post_result.php";
    //m_idx(매칭 인덱스), w_idx(대기 신청 팀 인덱), w_point(대기 신청 팀 스코어), p_idx( 신청 팀 인덱), p_point(신청 팀 스코어)
    String serverResult, userno, token, m_idx, w_idx, w_point, p_idx, p_point;
    String stadiumS, dateS, timeS, teamANameS, teamBNameS, AProfileUrl, BProfileUrl, AScore, BScore;
    private AlertDialog alertDialog2 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching_result_register);

        LoadPreference(this);
        Intent intent = getIntent();
        stadiumS = intent.getExtras().getString("stadium");
        dateS = intent.getExtras().getString("date");
        timeS = intent.getExtras().getString("time");
        teamANameS = intent.getExtras().getString("teamAName");
        teamBNameS = intent.getExtras().getString("teamBName");
        m_idx = intent.getExtras().getString("m_idx");
        w_idx = intent.getExtras().getString("w_idx");
        w_point = intent.getExtras().getString("w_point");
        p_idx = intent.getExtras().getString("p_idx");
        p_point = intent.getExtras().getString("p_point");

        toolbar = (Toolbar)findViewById(R.id.tool_bar_matching_result_register);
        stadium = (TextView)findViewById(R.id.matching_result_register_stadium);    //경기장 정보
        date = (TextView)findViewById(R.id.matching_result_register_date);          //경기 날짜 정보
        time = (TextView)findViewById(R.id.matching_result_register_time);          //경기 시간 정보
        teamAName = (TextView)findViewById(R.id.matching_result_register_team_a_name);  //1번팀 이름
        teamBName = (TextView)findViewById(R.id.matching_result_register_team_b_name);  //2번팀 이름
        teamAScore = (EditText)findViewById(R.id.matching_result_register_team_a_score);    //1번팀 점수
        teamBScore = (EditText)findViewById(R.id.matching_result_register_team_b_score);    //2번팀 점수
        teamAProfile = (CircleImageView)findViewById(R.id.matching_result_register_team_a_profile);     //1번팀 프로필 이미지
        teamBProfile = (CircleImageView)findViewById(R.id.matching_result_register_team_b_profile);     //2번팀 프로필 이미지
        registerBtn = (Button)findViewById(R.id.matching_result_register_btn);      //경기 결과 등록 버튼

        stadium.setText(stadiumS);
        date.setText(dateS);
        time.setText(timeS);
        teamAName.setText(teamANameS);
        teamBName.setText(teamBNameS);
        imageLoad(AProfileUrl, teamAProfile);
        imageLoad(BProfileUrl, teamBProfile);

        toolbar.setTitleTextColor(0xffffffff);
        toolbar.setTitle("경기 결과 등록");
        setSupportActionBar(toolbar);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AScore = teamAScore.getText().toString();
                BScore = teamBScore.getText().toString();

            }
        });
    }
    // 이미지뷰에 이미지 로드 메서드
    public void imageLoad(String Url, CircleImageView circleImageView){
        if (Url != null){
            Glide.with(this).load(Url).into(circleImageView);
        }
    }

    public void matchingResult(){

        new Thread(){
            @Override
            public void run() {
                //서버 연결
                ConnectServer();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        matchingResultEnd();

                    }
                });
            }
        }.start();
    }
    private void matchingResultEnd(){

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

        } catch (JSONException e) {
            e.printStackTrace();

        } catch (NullPointerException e){
            e.printStackTrace();

            code = 0;
            msg = "failed";
        }

        if (code == 700){
            //이건 조만간 없앨 부분이다 신경 안써도 됨
            Toast.makeText(this,"등록되었습니다", Toast.LENGTH_SHORT).show();

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
            data.append("m_idx").append("=").append(m_idx).append("&");
            data.append("w_idx").append("=").append(w_idx).append("&");
            data.append("w_point").append("=").append(w_point).append("&");
            data.append("p_idx").append("=").append(p_idx).append("&");
            data.append("p_point").append("=").append(p_point).append("&");

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

    private void LoadPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userno = sharedPreferences.getString("userno", null);
        token = sharedPreferences.getString("token", null);
    }

    private AlertDialog DialogWrong(){
        final AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setTitle("알 수 없는 오류");
        build.setMessage("알 수 없는 오류 입니다. 박빙에 문의해 주세요.");
        build.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog2.dismiss();

            }
        });
        return build.create();
    }

    private AlertDialog DialogNetwork(){
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
}

