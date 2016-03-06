package com.mondaychicken.bacving.matching;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

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

/**
 * Created by leejaebeom on 2015. 10. 30..
 */
public class matchingSearch extends AppCompatActivity {
    String matchType, time, serverResult;
    String server = "http://aws.bacving.com/api/member/info.php";
    String mainTeamName[], userno, token, team_idx[], sport_idx[], region_code[], teamNum, sportNum, regionCode;

    DatePicker picker;
    Spinner teamList;
    int date, month, year;
    private AlertDialog alertDialog2 = null;
    Button typeBtn1, typeBtn2, timeBtn1, timeBtn2, timeBtn3, timeBtn4, timeBtn5, alertDate, finishBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching_search);
        LoadPreference(this);

        Login();

        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar_matching_search);
        toolbar.setTitle("매칭검색");
        toolbar.setTitleTextColor(Color.WHITE);
        teamList = (Spinner)findViewById(R.id.matching_search_team);
        typeBtn1 = (Button)findViewById(R.id.matching_search_type_1);
        typeBtn2 = (Button)findViewById(R.id.matching_search_type_2);
        timeBtn1 = (Button)findViewById(R.id.matching_search_time_1);
        timeBtn2 = (Button)findViewById(R.id.matching_search_time_2);
        timeBtn3 = (Button)findViewById(R.id.matching_search_time_3);
        timeBtn4 = (Button)findViewById(R.id.matching_search_time_4);
        timeBtn5 = (Button)findViewById(R.id.matching_search_time_5);
        alertDate = (Button)findViewById(R.id.matching_search_date);
        finishBtn = (Button)findViewById(R.id.matching_search_finish);

        typeBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeBtn1.setBackgroundResource(R.drawable.button_able);
                typeBtn1.setTextColor(Color.parseColor("#000000"));
                typeBtn2.setBackgroundResource(R.drawable.button_disable);
                typeBtn2.setTextColor(Color.parseColor("#aaaaaa"));
                matchType = "11:11";
            }
        });
        typeBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeBtn2.setBackgroundResource(R.drawable.button_able);
                typeBtn2.setTextColor(Color.parseColor("#000000"));
                typeBtn1.setBackgroundResource(R.drawable.button_disable);
                typeBtn1.setTextColor(Color.parseColor("#aaaaaa"));
                matchType = "5:5";
            }
        });
        timeBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeBtn1.setBackgroundResource(R.drawable.button_able);
                timeBtn1.setTextColor(Color.parseColor("#000000"));
                timeBtn2.setBackgroundResource(R.drawable.button_disable);
                timeBtn2.setTypeface(null, Typeface.NORMAL);
                timeBtn2.setTextColor(Color.parseColor("#aaaaaa"));
                timeBtn3.setBackgroundResource(R.drawable.button_disable);
                timeBtn3.setTypeface(null, Typeface.NORMAL);
                timeBtn3.setTextColor(Color.parseColor("#aaaaaa"));
                timeBtn4.setBackgroundResource(R.drawable.button_disable);
                timeBtn4.setTypeface(null, Typeface.NORMAL);
                timeBtn4.setTextColor(Color.parseColor("#aaaaaa"));
                timeBtn5.setBackgroundResource(R.drawable.button_disable);
                timeBtn5.setTypeface(null, Typeface.NORMAL);
                timeBtn5.setTextColor(Color.parseColor("#aaaaaa"));
                time = "전체";
            }
        });
        timeBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeBtn2.setBackgroundResource(R.drawable.button_able);
                timeBtn2.setTextColor(Color.parseColor("#000000"));
                timeBtn1.setBackgroundResource(R.drawable.button_disable);
                timeBtn1.setTypeface(null, Typeface.NORMAL);
                timeBtn1.setTextColor(Color.parseColor("#aaaaaa"));
                timeBtn3.setBackgroundResource(R.drawable.button_disable);
                timeBtn3.setTypeface(null, Typeface.NORMAL);
                timeBtn3.setTextColor(Color.parseColor("#aaaaaa"));
                timeBtn4.setBackgroundResource(R.drawable.button_disable);
                timeBtn4.setTypeface(null, Typeface.NORMAL);
                timeBtn4.setTextColor(Color.parseColor("#aaaaaa"));
                timeBtn5.setBackgroundResource(R.drawable.button_disable);
                timeBtn5.setTypeface(null, Typeface.NORMAL);
                timeBtn5.setTextColor(Color.parseColor("#aaaaaa"));
                time = "오전";
            }
        });
        timeBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeBtn3.setBackgroundResource(R.drawable.button_able);
                timeBtn3.setTextColor(Color.parseColor("#000000"));
                timeBtn2.setBackgroundResource(R.drawable.button_disable);
                timeBtn2.setTypeface(null, Typeface.NORMAL);
                timeBtn2.setTextColor(Color.parseColor("#aaaaaa"));
                timeBtn1.setBackgroundResource(R.drawable.button_disable);
                timeBtn1.setTypeface(null, Typeface.NORMAL);
                timeBtn1.setTextColor(Color.parseColor("#aaaaaa"));
                timeBtn4.setBackgroundResource(R.drawable.button_disable);
                timeBtn4.setTypeface(null, Typeface.NORMAL);
                timeBtn1.setTextColor(Color.parseColor("#aaaaaa"));
                timeBtn5.setBackgroundResource(R.drawable.button_disable);
                timeBtn5.setTypeface(null, Typeface.NORMAL);
                timeBtn5.setTextColor(Color.parseColor("#aaaaaa"));
                time = "오후";
            }
        });

        timeBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeBtn4.setBackgroundResource(R.drawable.button_able);
                timeBtn4.setTextColor(Color.parseColor("#000000"));
                timeBtn2.setBackgroundResource(R.drawable.button_disable);
                timeBtn2.setTypeface(null, Typeface.NORMAL);
                timeBtn2.setTextColor(Color.parseColor("#aaaaaa"));
                timeBtn3.setBackgroundResource(R.drawable.button_disable);
                timeBtn3.setTypeface(null, Typeface.NORMAL);
                timeBtn3.setTextColor(Color.parseColor("#aaaaaa"));
                timeBtn1.setBackgroundResource(R.drawable.button_disable);
                timeBtn1.setTypeface(null, Typeface.NORMAL);
                timeBtn1.setTextColor(Color.parseColor("#aaaaaa"));
                timeBtn5.setBackgroundResource(R.drawable.button_disable);
                timeBtn5.setTypeface(null, Typeface.NORMAL);
                timeBtn5.setTextColor(Color.parseColor("#aaaaaa"));
                time = "저녁";
            }
        });

        timeBtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeBtn5.setBackgroundResource(R.drawable.button_able);
                timeBtn5.setTextColor(Color.parseColor("#000000"));
                timeBtn2.setBackgroundResource(R.drawable.button_disable);
                timeBtn2.setTypeface(null, Typeface.NORMAL);
                timeBtn2.setTextColor(Color.parseColor("#aaaaaa"));
                timeBtn3.setBackgroundResource(R.drawable.button_disable);
                timeBtn3.setTypeface(null, Typeface.NORMAL);
                timeBtn3.setTextColor(Color.parseColor("#aaaaaa"));
                timeBtn4.setBackgroundResource(R.drawable.button_disable);
                timeBtn4.setTypeface(null, Typeface.NORMAL);
                timeBtn4.setTextColor(Color.parseColor("#aaaaaa"));
                timeBtn1.setBackgroundResource(R.drawable.button_disable);
                timeBtn1.setTypeface(null, Typeface.NORMAL);
                timeBtn1.setTextColor(Color.parseColor("#aaaaaa"));
                time = "새벽";
            }
        });

        alertDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog2 = DialogDatePicker();
                alertDialog2.show();
            }
        });
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(matchType == null || time == null || teamNum == null)){
                    Intent intent = new Intent(matchingSearch.this, matchingSearchResultActivity.class);
                    intent.putExtra("match_type",matchType);
                    intent.putExtra("time", time);
                    intent.putExtra("date", String.valueOf(date));
                    intent.putExtra("month", String.valueOf(month));
                    intent.putExtra("year", String.valueOf(year));
                    intent.putExtra("location", regionCode);
                    intent.putExtra("team_idx", teamNum);
                    intent.putExtra("teamName", teamList.getSelectedItem().toString());
                    intent.putExtra("sport_idx", sportNum);
                    startActivity(intent);
                }else{
                    Toast.makeText(matchingSearch.this, "모든 조건을 선택해 주세요~", Toast.LENGTH_SHORT).show();
                }

            }
        });

        teamList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == -1) {
                    teamNum = null;
                    regionCode = null;
                } else {
                    Log.d("position", String.valueOf(position));
                    if (team_idx == null){
                        teamNum = null;
                        regionCode = null;
                    }else{
                        teamNum = team_idx[position];
                        regionCode = region_code[position];
                    }
                    if (sport_idx == null){
                        sportNum = null;
                        regionCode = null;
                    }else{
                        sportNum = sport_idx[position];
                        regionCode = region_code[position];
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void Login(){

        new Thread(){
            @Override
            public void run() {
                ConnectServer();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoginEnd();

                    }
                });
            }
        }.start();
    }
    private void LoginEnd(){

        String result = serverResult;
        Log.d("fragmentResult", result);
        int code = 0;
        String msg = "";
        JSONObject array[];

        if (result == null){
            return;
        }
        try{
            JSONObject json = new JSONObject(result);
            code = json.getInt("code");
            msg = json.getString("message");
            JSONArray teamArray = json.getJSONArray("data");
            array = new JSONObject[teamArray.length()];
            mainTeamName = new String[teamArray.length()];
            team_idx = new String[teamArray.length()];
            sport_idx = new String[teamArray.length()];
            region_code = new String[teamArray.length()];
            for (int i = 0; i<teamArray.length(); i++){
                array[i] = teamArray.getJSONObject(i);
                team_idx[i] = array[i].getString("idx");
                region_code[i] = array[i].getString("region_code");
                mainTeamName[i] = array[i].getString("name");
                sport_idx[i] = array[i].getString("sport_idx");
            }
        } catch (JSONException e) {
            e.printStackTrace();

        } catch (NullPointerException e){
            e.printStackTrace();

            code = 0;
            msg = "failed";
        }

        if (code == 700){
            String[] Sports = mainTeamName;
            if (Sports == null){
                Sports = new String[]{"회원님이 리더로 있는 팀이 없습니다"};
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, Sports);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            teamList.setAdapter(adapter);
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
            serverResult = null;
            StringBuffer data = new StringBuffer();
            data.append("user_idx").append("=").append(userno).append("&");
            data.append("token").append("=").append(token).append("&");
            data.append("leader_use").append("=").append("1").append("&");

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
    private AlertDialog DialogWrong(){
        final AlertDialog.Builder build = new AlertDialog.Builder(this);
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

    private AlertDialog DialogDatePicker(){

        final View innerView = getLayoutInflater().inflate(R.layout.date_picker,null);
        picker = (DatePicker)innerView.findViewById(R.id.birth_picker);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("경기날짜를 선택해 주세요");
        builder.setCancelable(true);
        builder.setView(innerView);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                year = picker.getYear();
                month = picker.getMonth() + 1;
                date = picker.getDayOfMonth();
                alertDate.setText(year + " - " + month + " - " + date);
                alertDate.setTextColor(Color.BLACK);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }
    private void LoadPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userno = sharedPreferences.getString("userno", null);
        token = sharedPreferences.getString("token", null);
    }
}
