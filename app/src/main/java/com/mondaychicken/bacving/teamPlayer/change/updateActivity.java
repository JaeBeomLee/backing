package com.mondaychicken.bacving.teamPlayer.change;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.mondaychicken.bacving.R;
import com.mondaychicken.bacving.cropActivity;
import com.mondaychicken.bacving.teamPlayer.teamActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by leejaebeom on 2015. 10. 9..
 */
public class updateActivity extends AppCompatActivity {
    //아직 완벽하게 수정되지 않았습니다
    Context context;

    EditText teamName, teamDescription;
    Spinner teamLocation1, teamLocation2;
    RadioGroup crewAccept;
    RadioButton acceptImmediately, acceptAllow;
    Button createTeam;

    LinearLayout coverEdit;
    RelativeLayout profileEdit;
    Bitmap profileImage;
    Bitmap coverImage;
    ImageView cover;
    CircleImageView profile;

    int permissionCheckCamera, permissionCheckStorage, locationPosition1, locationPosition2;
    Uri imageUri;
    int GALLARY_CODE = 1, CAMERA_CODE = 2, CROP_CODE = 3;
    ImageView profileImageEdit = null;

    private AlertDialog alertDialog = null;
    private AlertDialog alertDialog2 = null;

    String teamNameS = null, teamLocationS1 = null, teamLocationS2 = null, teamDescriptionS = null, userno = null, token = null, regionCode = null;
    String name = null ,team_info = null, description= null, author_idx= null,
            leader_idx= null, sport_idx= null, coverimg= null, profileimg= null,
            region_code= null,  accept = null, team_idx = null,
            location1[] = null, location1_min[] = null, location1Code[] = null, location2[] = null, location2code[] = null, location2_min[] = null;
    ArrayAdapter<String> location1adapter1, location1adapter2;
    //0 == profile, 1 == cover
    int checkImage;
    int acception, CAMERA, GALLERY;
    String profileUploadFilePath;
    String profileUploadFileName = "team_profile.jpg";
    String coverUploadFilePath;
    String coverUploadFileName = "team_cover.jpg";

    String server = "http://52.68.69.47/api/team/index.php";
    String serverUpdate = "http://52.68.69.47/api/team/update.php";
    String locationServer = "http://52.68.69.47/api/region.php";
    String serverResult, serverResultTeam;
    int code = 0;

    int serverResponseCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_update);
        context = updateActivity.this;
        Intent intent = getIntent();
        team_idx = intent.getExtras().getString("team_idx");
        LoadPreference(context);

        teamInformation();
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar_team_create);
        toolbar.setTitleTextColor(0xffffffff);
        toolbar.setTitle("팀 정보 수정");

        teamLocation1 = (Spinner)findViewById(R.id.team_create_location1);
        teamLocation2 = (Spinner)findViewById(R.id.team_create_location2);
        teamName = (EditText)findViewById(R.id.team_create_name);
        teamDescription = (EditText)findViewById(R.id.team_create_description);
        crewAccept = (RadioGroup)findViewById(R.id.team_create_accept);
        acceptImmediately = (RadioButton)findViewById(R.id.accept_immediately);
        acceptAllow = (RadioButton)findViewById(R.id.accept_allow);
        createTeam = (Button)findViewById(R.id.team_create_btn);
        profile = (CircleImageView)findViewById(R.id.team_create_profile);
        cover = (ImageView)findViewById(R.id.team_create_cover);
        coverEdit = (LinearLayout)findViewById(R.id.team_create_cover_edit);
        profileEdit = (RelativeLayout)findViewById(R.id.team_create_profile_edit);
        locationNetwork1();

        if (savedInstanceState != null){
            profileImage = savedInstanceState.getParcelable("profileBitmap");
            coverImage = savedInstanceState.getParcelable("coverBitmap");
            profileUploadFilePath = savedInstanceState.getString("profilePath");
            coverUploadFilePath = savedInstanceState.getString("coverPath");
            checkImage = savedInstanceState.getInt("checkImage");
            imageUri = savedInstanceState.getParcelable("imageUri");
            if (profileImage != null){
                profile.setImageBitmap(profileImage);
            }
            if (coverImage != null){
                cover.setImageBitmap(coverImage);
            }
        }

        profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkImage = 0;
                permissionCheckCamera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);

                int MyResultID = 1;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (permissionCheckCamera == PackageManager.PERMISSION_DENIED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MyResultID);
                    }
                }

                if (permissionCheckCamera == PackageManager.PERMISSION_DENIED){
//                    alertDialog = null;
//                    alertDialog.dismiss();
                }else{
                    //DialogAdd() 내용의 다이얼로그 생성
                    alertDialog = DialogImageChoose();
                    //DialogAdd() 내용의 다이얼로그 보여줌
                    alertDialog.show();
                }
            }
        });

        coverEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkImage = 1;
                permissionCheckCamera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);

                int MyResultID = 1;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (permissionCheckCamera == PackageManager.PERMISSION_DENIED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MyResultID);
                    }
                }

                if (permissionCheckCamera == PackageManager.PERMISSION_DENIED) {
//                    alertDialog = null;
//                    alertDialog.dismiss();
                } else {
                    //DialogAdd() 내용의 다이얼로그 생성
                    alertDialog = DialogImageChoose();
                    //DialogAdd() 내용의 다이얼로그 보여줌
                    alertDialog.show();
                }
            }
        });
        createTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                teamNameS = teamName.getText().toString();
                teamDescriptionS = teamDescription.getText().toString();
                switch (crewAccept.getCheckedRadioButtonId()){
                    case R.id.accept_immediately:
                        acception = 1;
                        break;
                    case R.id.accept_allow:
                        acception = 0;
                        break;
                }

                if (teamNameS == null || teamDescriptionS == null || teamLocationS2 == null){
                    alertDialog2 = DialogInit("생성 오류","팀 생성에 필요한 모든 내용을 입력 해 주세요.");
                    alertDialog2.show();
                }else{
                    Update();
                }



            }
        });

        teamLocation1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == -1){
                    teamLocationS1 = null;
                }else{
                    teamLocationS1 = location1Code[position];
                    locationNetwork2();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        teamLocation2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == -1){
                    teamLocationS2 = null;
                }else{
                    teamLocationS2 = location2code[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public void teamInformation(){

        new Thread(){
            @Override
            public void run() {
                teamInformationServer();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        teamInformationEnd();

                    }
                });
            }
        }.start();
    }
    private void teamInformationEnd(){

        String result = serverResultTeam;

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
            name = json.getString("name");
            description = json.getString("description");
            team_idx = json.getString("idx");
            sport_idx = json.getString("sport_idx");
            teamDescriptionS = json.getString("description");
            sport_idx = json.getString("sport_idx");
            coverimg = json.getString("coverimg");
            profileimg = json.getString("profileimg");

        } catch (JSONException e) {
            e.printStackTrace();

        } catch (NullPointerException e){
            e.printStackTrace();

            code = 0;
            msg = "failed";
        }

        if (code == 700){
            Glide.get(this).clearMemory();
            Glide.with(this).load(coverimg).into(cover);
            Glide.with(this).load(profileimg).into(profile);
            Glide.get(this).clearMemory();
            teamName.setText(name);
            teamDescription.setText(description);
        }else{
            alertDialog2 = DialogInit("오류",msg);
            alertDialog2.show();
            Log.d("failed", code + " " + msg);
        }
    }
    private void teamInformationServer(){
        try {
            serverResultTeam = null;
            StringBuffer data = new StringBuffer();
            data.append("mode").append("=").append(5).append("&");
            data.append("type").append("=").append(2).append("&");
            data.append("user_idx").append("=").append(userno).append("&");
            data.append("team_idx").append("=").append(team_idx).append("&");
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

            serverResultTeam = builder.toString();

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

    public void Update(){

        regionCode = location2code[teamLocation2.getSelectedItemPosition()-1];

        final ProgressDialog dialog = ProgressDialog.show(this, "", "잠시만 기다려 주세요",true);

        new Thread(){
            @Override
            public void run() {
                ConnectServer();
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
            team_info = json.getString("team_info");
            JSONObject teamInfoJson = new JSONObject(team_info);

            name = teamInfoJson.getString("name");
            description = teamInfoJson.getString("description");
            author_idx = teamInfoJson.getString("author_idx");
            leader_idx = teamInfoJson.getString("leader_idx");
            sport_idx = teamInfoJson.getString("sport_idx");
            coverimg = teamInfoJson.getString("coverimg");
            profileimg = teamInfoJson.getString("profileimg");
            region_code = teamInfoJson.getString("region_code");
            team_idx = teamInfoJson.getString("team_idx");
            accept = teamInfoJson.getString("accept");
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
            Intent intent1 = new Intent(updateActivity.this,teamActivity.class);
            intent1.putExtra("teamName", name);
            intent1.putExtra("description", description);
            intent1.putExtra("author_idx", author_idx);
            intent1.putExtra("leader_idx", leader_idx);
            intent1.putExtra("teamLogo", profileimg);
            intent1.putExtra("teamBackground", coverimg);
            intent1.putExtra("sport_idx", sport_idx);
            intent1.putExtra("region_code", region_code);
            intent1.putExtra("accept", accept);
            intent1.putExtra("team_idx", team_idx);
            startActivity(intent1);
            this.finish();
            Log.d("Sucess", result);
            if (profileImage != null){
                profileImage.recycle();
            }
            if (coverImage != null){
                coverImage.recycle();
            }
        }else if(code == 425){
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    alertDialog2 = DialogInit("팀 생성 오류","회원님께서 생성하신 같은 이름의 팀이 이미 있습니다.");
                    alertDialog2.show();
                }
            });
            Log.d("failed", code + " " + msg);
        }else{
            alertDialog2 = DialogInit("오류",msg);
            alertDialog2.show();
            Log.d("failed", code + " " + msg);
            Log.d("result", result);

//            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
//            Log.d("failed", result);


        }
    }
    private void ConnectServer(){

        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphen = "--";
        String boundary = "SpecificString";
        int byteRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        File profileFile = new File(profileUploadFilePath,profileUploadFileName);
        File coverFile = new File(coverUploadFilePath,coverUploadFileName);
        try {

            serverResult = null;

            URL url = new URL(serverUpdate);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            http.setRequestProperty("ENCTYPE", "multipart/form-data");
            http.setRequestProperty("Connection", "Keep-Alive");

            dos = new DataOutputStream(http.getOutputStream());

            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"user_idx\"" + lineEnd + lineEnd + userno);
            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"team_idx\"" + lineEnd + lineEnd + team_idx);
            dos.writeBytes("\r\n--"+ boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"token\"" +lineEnd + lineEnd + token);
            dos.writeBytes("\r\n--"+ boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"name\"" + lineEnd + lineEnd + URLEncoder.encode(teamNameS, "UTF-8"));
            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"description\"" + lineEnd + lineEnd + URLEncoder.encode(teamDescriptionS, "UTF-8"));
            dos.writeBytes("\r\n--"+ boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"accept\"" +lineEnd + lineEnd + acception);
            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"region_code\"" + lineEnd + lineEnd + regionCode);
            dos.writeBytes("\r\n--"+ boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"type\"" +lineEnd + lineEnd + 2);
            dos.writeBytes("\r\n--"+ boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"mode\"" +lineEnd + lineEnd + 1);
            Log.d("teamName", teamNameS);
            Log.d("teamDescription",teamDescriptionS);
            if (profileImage != null){
                if (profileFile.exists()){
                    try{
                        dos.writeBytes("\r\n--"+ boundary + "\r\n");
                        dos.writeBytes("Content-Disposition: form-data; name=\"profileimg\";filename=\"" + profileUploadFileName + "\"" + lineEnd);
                        dos.writeBytes("Content-Type: application/octet-stream" + lineEnd + lineEnd);
                        FileInputStream fis = new FileInputStream(profileFile);

                        bytesAvailable = fis.available();
                        bufferSize = Math.min(bytesAvailable,maxBufferSize);
                        buffer = new byte[bufferSize];

                        byteRead = fis.read(buffer, 0, bufferSize);

                        while (byteRead > 0){
                            DataOutputStream dataWrite = new DataOutputStream(http.getOutputStream());

                            dataWrite.write(buffer, 0, bufferSize);
                            bytesAvailable = fis.available();
                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            byteRead = fis.read(buffer, 0, bufferSize);
                        }

                        dos.writeBytes(lineEnd + twoHyphen + boundary + twoHyphen +lineEnd);

                        fis.close();
                    }catch (FileNotFoundException e){
                        dos.writeBytes("\r\n--"+ boundary + "\r\n");
                        dos.writeBytes("Content-Disposition: form-data; name=\"profileimg\"" +lineEnd + lineEnd + null);
                    }
                }else{
                    dos.writeBytes("\r\n--"+ boundary + "\r\n");
                    dos.writeBytes("Content-Disposition: form-data; name=\"profileimg\";filename=\"" + "" + "\"" + lineEnd);
                    dos.writeBytes("Content-Type: application/octet-stream" + lineEnd + lineEnd);
                }
            }

            if (coverImage != null){
                if (coverFile.exists()){
                    try{
                        dos.writeBytes("\r\n--"+ boundary + "\r\n");
                        dos.writeBytes("Content-Disposition: form-data; name=\"coverimg\";filename=\"" + coverUploadFileName + "\"" + lineEnd);
                        dos.writeBytes("Content-Type: application/octet-stream" + lineEnd + lineEnd);

                        FileInputStream fis2 = new FileInputStream(coverFile);

                        bytesAvailable = fis2.available();
                        bufferSize = Math.min(bytesAvailable,maxBufferSize);
                        buffer = new byte[bufferSize];

                        byteRead = fis2.read(buffer, 0, bufferSize);

                        while (byteRead > 0){
                            DataOutputStream dataWrite = new DataOutputStream(http.getOutputStream());

                            dataWrite.write(buffer, 0, bufferSize);
                            bytesAvailable = fis2.available();
                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            byteRead = fis2.read(buffer, 0, bufferSize);
                        }

                        dos.writeBytes(lineEnd + twoHyphen + boundary + twoHyphen + lineEnd);
                        fis2.close();
                    }catch (FileNotFoundException e){
                        dos.writeBytes("\r\n--"+ boundary + "\r\n");
                        dos.writeBytes("Content-Disposition: form-data; name=\"coverimg\"" +lineEnd + lineEnd + null);
                    }
                }else {
                    dos.writeBytes("\r\n--"+ boundary + "\r\n");
                    dos.writeBytes("Content-Disposition: form-data; name=\"coverimg\";filename=\"" + "" + "\"" + lineEnd);
                    dos.writeBytes("Content-Type: application/octet-stream" + lineEnd + lineEnd);
                }
            }


            dos.flush();
            dos.close();

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


    public void locationNetwork1(){

        new Thread(){
            @Override
            public void run() {
                locationServer1();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        locationEnd1();

                    }
                });
            }
        }.start();
    }
    private void locationEnd1(){

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
            JSONArray locationArray = json.getJSONArray("data");
            JSONObject[] array = new JSONObject[locationArray.length()];
            location1 = new String[locationArray.length()];
            location1_min = new String[locationArray.length()];
            location1Code = new String[locationArray.length()];
            for (int i = 0; i<locationArray.length(); i++){
                array[i] = locationArray.getJSONObject(i);
                location1[i] = array[i].getString("name");
                location1_min[i] = array[i].getString("min_name");
                location1Code[i] = array[i].getString("code");
            }
        } catch (JSONException e) {
            e.printStackTrace();

        } catch (NullPointerException e){
            e.printStackTrace();

            code = 0;
            msg = "failed";
        }

        if (code == 700){

            location1adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, location1);
            teamLocation1.setAdapter(location1adapter1);
        }else{
            alertDialog2 = DialogInit("오류",msg);
            alertDialog2.show();
            Log.d("failed", code + " " + msg);
        }
    }
    private void locationServer1(){
        try {
            serverResult = null;

            URL url = new URL(locationServer);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");

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

    public void locationNetwork2(){

        new Thread(){
            @Override
            public void run() {
                locationServer2();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        locationEnd2();

                    }
                });
            }
        }.start();
    }
    private void locationEnd2(){

        String result = serverResult;
        Log.d("fragmentResult", result);
        int code = 0;
        int region_count = 0;
        String msg = "";

        if (result == null){
            return;
        }
        try{
            JSONObject json = new JSONObject(result);
            code = json.getInt("code");
            msg = json.getString("message");
            region_count = json.getInt("region_count");
            JSONArray locationArray = json.getJSONArray("data");
            JSONObject[] array = new JSONObject[locationArray.length()];
            location2 = new String[locationArray.length()];
            location2_min = new String[locationArray.length()];
            location2code = new String[locationArray.length()];
            for (int i = 0; i<locationArray.length(); i++){
                array[i] = locationArray.getJSONObject(i);
                location2[i] = array[i].getString("name");
                location2_min[i] = array[i].getString("min_name");
                location2code[i] = array[i].getString("code");
            }
        } catch (JSONException e) {
            e.printStackTrace();

        } catch (NullPointerException e){
            e.printStackTrace();

            code = 0;
            msg = "failed";
        }

        if (code == 700){
            if(region_count == 0){
                location2 = new String[1];
                location2_min = new String[1];
                location2code = new String[1];

                location2[0] = "전국";
                location2_min[0] = "전국";
                location2code[0] = "0000000000";
            }
            location1adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, location2_min);
            teamLocation2.setAdapter(location1adapter2);
        }else{
            alertDialog2 = DialogInit("오류",msg);
            alertDialog2.show();
            Log.d("failed", code + " " + msg);
        }
    }
    private void locationServer2(){
        try {
            serverResult = null;
            StringBuffer data = new StringBuffer();
            data.append("code").append("=").append(teamLocationS1).append("&");

            URL url = new URL(locationServer);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("GET");
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

    public Uri getImageUri(String fileName) {
        if (isExStorageAvailable()){
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+ "/bacving/";
            switch (checkImage){
                case 0:
                    profileUploadFilePath = path;
                    break;
                case 1:
                    coverUploadFilePath = path;
                    break;
            }
            File directory = new File(path);
            if (!directory.exists()){
                directory.mkdirs();
            }
            File file = new File(path,fileName);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Uri.fromFile(file);
        }
        return null;
    }

    public boolean isExStorageAvailable(){
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }
        return false;
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

    private AlertDialog DialogImageChoose(){
        final View innerView = getLayoutInflater().inflate(R.layout.image_method_choose_layout, null);
        final AlertDialog.Builder build = new AlertDialog.Builder(context);

        // 이미지 버튼을 이용하여 갤러리를 선택할지 카메라를 선택할지 정함
        final ImageButton gallery = (ImageButton)innerView.findViewById(R.id.select_gallery);
        final ImageButton camera = (ImageButton)innerView.findViewById(R.id.select_camera);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType(MediaStore.Images.Media.CONTENT_TYPE);
                i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, GALLARY_CODE);
                alertDialog.cancel();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionCheckStorage = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                int MyResultID = 2;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (permissionCheckStorage == PackageManager.PERMISSION_DENIED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MyResultID);
                    }
                }

                if (permissionCheckStorage == PackageManager.PERMISSION_DENIED) {
//                    alertDialog = null;
//                    alertDialog.dismiss();
                } else {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    switch (checkImage) {
                        case 0:
                            i.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri(profileUploadFileName));
                            imageUri = getImageUri(profileUploadFileName);
                            break;
                        case 1:
                            i.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri(coverUploadFileName));
                            imageUri = getImageUri(coverUploadFileName);
                            break;
                    }
                    startActivityForResult(i, CAMERA_CODE);
                    alertDialog.cancel();
                }
            }
        });
        build.setView(innerView);

        return build.create();
    }

    public void saveImageFile(Bitmap bitmap){
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+ "/bacving/";
        switch (checkImage){
            case 0:
                profileUploadFilePath = path;
                break;
            case 1:
                coverUploadFilePath = path;
                break;
        }
        
        File directory = new File(path);
        
        File file = null;
        switch (checkImage){
            case 0:
                file = new File(directory, profileUploadFileName);
                break;
            case 1:
                file = new File(directory, coverUploadFileName);
                break;
        }
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Bitmap decodeBitmapToUri(Uri uri) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Calculate inSampleSize
        options.inSampleSize = 2;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap result = null;

        try {
            result = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }


//    // 문자값저장
//    public void savePreference(String key, String value, Context context) {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(key, value);
//        editor.commit();
//    }
//
//
    private void loadPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userno = sharedPreferences.getString("userno", null);
//        loginEmail= sharedPreferences.getString("email", null);
//        loginPassword= sharedPreferences.getString("pw", null);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CODE){
            if (resultCode == Activity.RESULT_OK){
                if (imageUri == null){
                    switch (checkImage){
                        case 0:
                            imageUri = getImageUri(profileUploadFileName);
                            break;
                        case 1:
                            imageUri = getImageUri(coverUploadFileName);
                            break;
                    }
                }

                //메모리 과부하 문제로 파일을 저장후에 Intent된 뒤 이미지를 Uri로 찾는 방식을 사용함.
                Intent cropMode = new Intent(updateActivity.this, cropActivity.class);
                cropMode.putExtra("imageUri", imageUri.toString()); //이미지 위치
                cropMode.putExtra("checkImage", checkImage); // 프로필인지 커버인지
                startActivityForResult(cropMode, CROP_CODE);
//                Uri selectImage = imageUri;
//                switch (checkImage){
//                    case 0:
//                        profileImage = decodeBitmapToUri(selectImage);
//                        profile.setImageBitmap(profileImage);
//                        break;
//                    case 1:
//                        coverImage = decodeBitmapToUri(selectImage);
//                        cover.setImageBitmap(coverImage);
//                        break;
//                }

            }
        } else if (requestCode == GALLARY_CODE){
            if (resultCode == Activity.RESULT_OK){
                Uri selectImage = data.getData();
                String[] filrPathColumn = {MediaStore.Images.Media.DATA};
                Cursor c = this.getContentResolver().query(selectImage, filrPathColumn, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filrPathColumn[0]);
                switch (checkImage){
                    case 0:
                        profileUploadFilePath = c.getString(columnIndex);
                        profileImage = BitmapFactory.decodeFile(profileUploadFilePath);
                        saveImageFile(profileImage);
                        imageUri = getImageUri(profileUploadFileName);
//                        profile.setImageBitmap(profileImage);
                        break;
                    case 1:
                        coverUploadFilePath = c.getString(columnIndex);
                        coverImage = BitmapFactory.decodeFile(coverUploadFilePath);
                        saveImageFile(coverImage);
                        imageUri = getImageUri(coverUploadFileName);
//                        cover.setImageBitmap(coverImage);
                        break;
                }
                c.close();

                Intent cropMode = new Intent(updateActivity.this, cropActivity.class);
                cropMode.putExtra("imageUri", imageUri.toString()); //이미지 위치
                cropMode.putExtra("checkImage", checkImage); // 프로필인지 커버인지
                startActivityForResult(cropMode, CROP_CODE);

            }
        } else if (requestCode == CROP_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri selectImage = imageUri;
                switch (checkImage) {
                    case 0:
                        profileImage = decodeBitmapToUri(selectImage);
                        profile.setImageBitmap(profileImage);
                        break;
                    case 1:
                        coverImage = decodeBitmapToUri(selectImage);
                        cover.setImageBitmap(coverImage);
                        break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        if (coverImage != null) {
            coverImage.recycle();
        }
        if (profileImage != null){
            profileImage.recycle();
        }
        super.onDestroy();
    }
    private void LoadPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userno = sharedPreferences.getString("userno", null);
        token = sharedPreferences.getString("token", null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("profileBitmap", profileImage);
        outState.putParcelable("coverBitmap", coverImage);
        outState.putString("profilePath", profileUploadFilePath);
        outState.putString("coverPath", coverUploadFilePath);
        outState.putParcelable("imageUri",imageUri);
        outState.putInt("checkImage", checkImage);
    }
}


