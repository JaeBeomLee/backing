package com.mondaychicken.bacving.matching;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mondaychicken.bacving.R;
import com.mondaychicken.bacving.gpsInfo;
import com.mondaychicken.bacving.map.search.mapSearch;

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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by leejaebeom on 2015. 10. 30..
 */
public class matchingTeamRegister extends AppCompatActivity{
    String matchType, location, time, date, month, year, hour, minute, teamName, team_idx;
    Button typeBtn1, typeBtn2, registerDate, registerTime, finishBtn;
    String server = "http://aws.bacving.com/api/stadium/info.php";
    String registerServer = "http://aws.bacving.com/api/match/waiting.php";
    String stadiumName[], userno, token, stadium_idx[], sport_idx, serverResult, stadiumNum;
    double latitude, longitude;
    int permissionCheckGPS;
    DatePicker picker;
    TimePicker timePicker;
    private gpsInfo gps;
    private AlertDialog alertDialog2 = null;
    String[] StadiumArray;
    List<String> StadiumList;
    AutoCompleteTextView stadiumList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching_team_register);

        final Intent intent = getIntent();
        sport_idx = intent.getExtras().getString("sport_idx");
        matchType = intent.getExtras().getString("match_type");
        location = intent.getExtras().getString("location");
        time = intent.getExtras().getString("time");
        date = intent.getExtras().getString("date");
        month = intent.getExtras().getString("month");
        year = intent.getExtras().getString("year");
        teamName = intent.getExtras().getString("teamName");
        team_idx = intent.getExtras().getString("team_idx");
        LoadPreference(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_matching_search);
        toolbar.setTitle("매칭 등록");
        toolbar.setTitleTextColor(Color.WHITE);
        typeBtn1 = (Button) findViewById(R.id.matching_team_register_type1);
        typeBtn2 = (Button) findViewById(R.id.matching_team_register_type2);
        finishBtn = (Button) findViewById(R.id.matching_register_finish);
        registerDate = (Button)findViewById(R.id.matching_team_register_date);
        registerTime = (Button)findViewById(R.id.matching_team_register_time);
        stadiumList = (AutoCompleteTextView) findViewById(R.id.matching_register_stadium);
        gps = new gpsInfo(this);

        switch (matchType){
            case "11:11":
                typeBtn1.setBackgroundResource(R.drawable.button_able);
                typeBtn1.setTextColor(Color.parseColor("#000000"));
                typeBtn2.setBackgroundResource(R.drawable.button_disable);
                typeBtn2.setTextColor(Color.parseColor("#aaaaaa"));
                matchType = "11:11";
                break;
            case "5:5":
                typeBtn2.setBackgroundResource(R.drawable.button_able);
                typeBtn2.setTextColor(Color.parseColor("#000000"));
                typeBtn1.setBackgroundResource(R.drawable.button_disable);
                typeBtn1.setTextColor(Color.parseColor("#aaaaaa"));
                matchType = "5:5";
        }


        registerDate.setText(year + "-" + month + "-" + date);
        registerDate.setTextColor(Color.BLACK);

        permissionCheckGPS = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

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

                stadiumInfo();
            }else{
                gps.showSettingsAlert();
            }
        }

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

        registerDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog2 = DialogDatePicker();
                alertDialog2.show();
            }
        });

        registerTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog2 = DialogTimePicker();
                alertDialog2.show();
            }
        });

//        stadiumList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (position == -1) {
//                    stadiumNum = null;
//                } else {
//                    stadiumNum = stadium_idx[position];
//                    Log.d("Stadium position", String.valueOf(position));
//                    Log.d("Stadium id", String.valueOf(id));
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });


        stadiumList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == -1) {
                    stadiumNum = null;
                } else {
                    //아직 덜함.
                    String item = stadiumList.getText().toString();
                    Log.d("Stadium what is this", stadiumList.getText().toString());

                    if (item.contains("등록하기")){
                        Intent intent1 = new Intent(matchingTeamRegister.this, mapSearch.class);
                        String []keyword = item.split(" ");
                        intent1.putExtra("keyword", keyword[0]);
                        stadiumList.setText("");
                        startActivityForResult(intent1, 1);
                    }else {
                        //선택 된 경기장 인덱스 구하기
                        for(int i = 0; i < StadiumArray.length; i++){
                            if(item.equals(StadiumArray[i])){
                                Log.d("Selected Stadium1",stadium_idx[i].toString());
                                Log.d("Selected Stadium2",String.valueOf(i));
                                Log.d("Selected Stadium3", StadiumArray[i].toString());
                                stadiumNum = stadium_idx[i].toString();
                                return;
                            }
                        }

                    }

                }
            }
        });
        stadiumList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                StadiumList.set(StadiumList.size()-1,s.toString() + " 등록하기");
                StadiumArray = StadiumList.toArray(new String[StadiumList.size()]);
                stadiumAdd();
                
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(matchType == null || location == null || time == null)) {
//                    Intent intent = new Intent(matchingTeamRegister.this, matchingSearchResultActivity.class);
//                    startActivity(intent);
                    register();
                }

            }
        });
    }

    public void stadiumAdd(){
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, StadiumArray);
        stadiumList.setAdapter(adapter);
    }
    public void stadiumInfo() {

        new Thread() {
            @Override
            public void run() {
                ConnectServer();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stadiumInfoEnd();

                    }
                });
            }
        }.start();
    }

    private void stadiumInfoEnd() {

        String result = serverResult;
        Log.d("fragmentResult", result);
        int code = 0;
        String msg = "";
        JSONObject array[];

        if (result == null) {
            return;
        }
        try {
            JSONObject json = new JSONObject(result);
            code = json.getInt("code");
            msg = json.getString("message");
            JSONArray teamArray = json.getJSONArray("stadium");
            array = new JSONObject[teamArray.length()];
            stadiumName = new String[teamArray.length()];
            stadium_idx = new String[teamArray.length()];
            for (int i = 0; i < teamArray.length(); i++) {
                array[i] = teamArray.getJSONObject(i);
                stadium_idx[i] = array[i].getString("idx");
                stadiumName[i] = array[i].getString("name");
            }
        } catch (JSONException e) {
            e.printStackTrace();

        } catch (NullPointerException e) {
            e.printStackTrace();

            code = 0;
            msg = "failed";
        }

        if (code == 700) {
            StadiumList = new ArrayList<String>(Arrays.asList(stadiumName));
            StadiumList.add(" ");
            StadiumArray = StadiumList.toArray(new String[StadiumList.size()]);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, StadiumArray);
            stadiumList.setAdapter(adapter);
            stadiumList.setThreshold(2);
        } else {
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

    private void ConnectServer() {
        try {
            serverResult = null;
            StringBuffer data = new StringBuffer();
            data.append("user_idx").append("=").append(userno).append("&");
            data.append("token").append("=").append(token).append("&");
            data.append("sport_idx").append("=").append(sport_idx).append("&");
            data.append("type").append("=").append("team").append("&");
            data.append("lat").append("=").append(latitude).append("&");
            data.append("lng").append("=").append(longitude).append("&");
            data.append("distance").append("=").append(700).append("&");

            URL url = new URL(server);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(data.toString());
            writer.flush();

            InputStreamReader ims = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(ims);
            StringBuilder builder = new StringBuilder();
            String str;

            while ((str = reader.readLine()) != null) {
                builder.append(str);
            }

            serverResult = builder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ConnectException e) {
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

    public void register() {

        new Thread() {
            @Override
            public void run() {
                registerServer();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        registerEnd();

                    }
                });
            }
        }.start();
    }

    private void registerEnd() {

        String result = serverResult;
        Log.d("fragmentResult", result);
        int code = 0;
        String msg = "";
        JSONObject array[];
        String type = null, w_idx = null, sport_idx = null, stadium_idx = null, stadium_text = null, region_code = null, region_text = null, longitude = null, latitude = null, meet_date = null;
        if (result == null) {
            return;
        }
        try {
            JSONObject json = new JSONObject(result);
            code = json.getInt("code");
            msg = json.getString("message");
            JSONObject data = json.getJSONObject("input_data");
            type = data.getString("type");
            w_idx = data.getString("w_idx");
            sport_idx = data.getString("sport_idx");
            stadium_idx = data.getString("stadium_idx");
            stadium_text = data.getString("stadium_text");
            region_code = data.getString("region_code");
            region_text = data.getString("region_text");
            longitude = data.getString("longitude");
            latitude = data.getString("latitude");
            meet_date = data.getString("meet_date");


        } catch (JSONException e) {
            e.printStackTrace();

        } catch (NullPointerException e) {
            e.printStackTrace();

            code = 0;
            msg = "failed";
        }

        if (code == 700) {
            Toast.makeText(this, "매칭 등록이 완료 되었습니다. 곧 매칭 신청이 들어올꺼에요!", Toast.LENGTH_SHORT).show();
            matchingSearchResultActivity.matchingSearchResult.finish();
            Intent intent = new Intent(this,matchingSearchResultActivity.class);
            intent.putExtra("match_type",matchType);
            intent.putExtra("location", location);
            intent.putExtra("time", time);
            intent.putExtra("date", String.valueOf(date));
            intent.putExtra("month", String.valueOf(month));
            intent.putExtra("year", String.valueOf(year));
            intent.putExtra("team_idx", team_idx);
            intent.putExtra("teamName", teamName);
            intent.putExtra("sport_idx", this.sport_idx);
            startActivity(intent);
            finish();
        } else {
            alertDialog2 = DialogWrong();
            alertDialog2.show();
            Log.d("failed", code + " " + msg);
        }
    }

    private void registerServer() {
        try {
            serverResult = null;
            StringBuffer data = new StringBuffer();
            data.append("user_idx").append("=").append(userno).append("&");
            data.append("token").append("=").append(token).append("&");
            data.append("type").append("=").append("team").append("&");
            data.append("w_idx").append("=").append(team_idx).append("&");
            data.append("sport_idx").append("=").append(sport_idx).append("&");
            data.append("stadium_idx").append("=").append(stadiumNum).append("&");
            data.append("latitude").append("=").append((float)latitude).append("&");
            data.append("longitude").append("=").append((float)longitude).append("&");
            data.append("region_text").append("=").append("임시").append("&");
            data.append("region_code").append("=").append("").append("&");
            data.append("meet_date").append("=").append(year + "-" + month + "-" +date + " " + hour + ":" + minute +":00").append("&");

            Log.d("data to server", data.toString());

            URL url = new URL(registerServer);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(data.toString());
            writer.flush();

            InputStreamReader ims = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(ims);
            StringBuilder builder = new StringBuilder();
            String str;

            while ((str = reader.readLine()) != null) {
                builder.append(str);
            }

            serverResult = builder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ConnectException e) {
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
    private AlertDialog DialogWrong() {
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
                year = String.valueOf(picker.getYear());
                month = String.valueOf(picker.getMonth() + 1);
                date = String.valueOf(picker.getDayOfMonth());
                registerDate.setText(year + " - " + month + " - " + date);
                registerDate.setTextColor(Color.BLACK);
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

    private AlertDialog DialogTimePicker(){
        hour = null;
        minute = null;
        final Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        final View innerView = getLayoutInflater().inflate(R.layout.time_picker,null);
        timePicker = (TimePicker)innerView.findViewById(R.id.time_picker);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hour = String.valueOf(hourOfDay);
                matchingTeamRegister.this.minute = String.valueOf(minute);
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("경기 시간을 선택해 주세요");
        builder.setCancelable(true);
        builder.setView(innerView);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (hour == null){
                    hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
                }
                if (minute == null){
                    minute = String.valueOf(calendar.get(Calendar.MINUTE));
                }
                registerTime.setText(hour + ":" + matchingTeamRegister.this.minute);
                registerTime.setTextColor(Color.BLACK);
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

                        stadiumInfo();
                    }else{
                        gps.showSettingsAlert();
                    }
                }else {
                    permissionCheckGPS = PackageManager.PERMISSION_DENIED;
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == 1){
                stadiumList.setText(data.getStringExtra("stadium"));
            }
    }
}

