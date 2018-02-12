package com.mondaychicken.bacving;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.mondaychicken.bacving.Login.preLayout;
import com.mondaychicken.bacving.gcm.QuickstartPreferences;
import com.mondaychicken.bacving.gcm.RegistrationIntentService;
import com.mondaychicken.bacving.main.MainPage;

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
import java.util.Random;

/**
 * Created by ijaebeom on 2015. 9. 27..
 */
public class splash extends AppCompatActivity {
    String email, pw;
    String server = "http://52.68.69.47/api/member/index.php";
    String serverResult;
    Handler handler;
    ImageView background;
    android.support.v7.app.AlertDialog.Builder builder;

    String logintoken;
    private BroadcastReceiver RegistrationBroadcastReceiver;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoadPreference(this);

        registBroadcastReceiver();
        getInstanceIdToken();

        handler = new Handler();
        builder = new android.support.v7.app.AlertDialog.Builder(splash.this);
        Random r = new Random();
        setContentView(com.mondaychicken.bacving.R.layout.splash);
        int imageRandom = r.nextInt(4)+1;
        background = (ImageView)findViewById(com.mondaychicken.bacving.R.id.splash_background);
        switch (imageRandom){
            case 1:
                background.setImageResource(com.mondaychicken.bacving.R.drawable.baseball);
                break;
            case 2:
                background.setImageResource(com.mondaychicken.bacving.R.drawable.basketball);
                break;
            case 3:
                background.setImageResource(com.mondaychicken.bacving.R.drawable.hiking);
                break;
            case 4:
                background.setImageResource(com.mondaychicken.bacving.R.drawable.soccer);
                break;
            case 5:
                background.setImageResource(com.mondaychicken.bacving.R.drawable.ground);
                break;
        }
//        if (email != null && pw != null){
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
////                    임시
//                    Login();
//                }
//            }, 800);
//
//        }else if (email == null || pw == null){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(splash.this, preLayout.class);
                    startActivity(intent);
                    overridePendingTransition(com.mondaychicken.bacving.R.anim.fade, com.mondaychicken.bacving.R.anim.hold);
                    splash.this.finish();
                }
            }, 2000);
//        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(RegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(RegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_GENERATING));
        LocalBroadcastManager.getInstance(this).registerReceiver(RegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(RegistrationBroadcastReceiver);
        super.onPause();
    }

    public void Login(){
//            final ProgressDialog dialog = ProgressDialog.show(splash.this, "", "잠시만 기다려 주세요",true);

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
//                    dialog.dismiss();
            }
        }.start();
    }

    private void LoginEnd(){
        String result = serverResult;

        int code = 0;
        String msg = "";
        String account = null, userno = null, userEmail= null, userPw= null, userName= null, userProfileimg= null, userStatus= null, userGrade= null, userToken= null, userPhone = null, userBirth = null, userGender = null;

        try{
            JSONObject json = new JSONObject(result);
            code = json.getInt("code");
            msg = json.getString("message");
            account = json.getString("account");
            JSONObject accountJson = new JSONObject(account);

            userno = accountJson.getString("userno");
            userEmail = accountJson.getString("email");
            userPw = accountJson.getString("pw");
            userName = accountJson.getString("name");
            userProfileimg = accountJson.getString("profileimg");
            userStatus = accountJson.getString("status");
            userGrade = accountJson.getString("grade");
            userToken = json.getString("token");
            userBirth = accountJson.getString("birth");
            userGender = accountJson.getString("gender");
            userPhone = accountJson.getString("phone");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
            code = 0;
            msg = "failed";
        }

        if (code == 700){
//            Toast.makeText(splash.this, msg, Toast.LENGTH_SHORT).show();

//            SavePreference("token", logintoken, getApplicationContext());
            SavePreference("userno", userno, this);
            SavePreference("email", userEmail, this);
            SavePreference("pw", pw, this);
            SavePreference("name", userName, this);
            SavePreference("profileimg", userProfileimg, this);
            SavePreference("statue", userStatus, this);
            SavePreference("grade", userGrade, this);
            SavePreference("birth", userBirth, this);
            SavePreference("gender", userGender, this);
            SavePreference("phone", userPhone, this);
            Intent intent = new Intent(splash.this, MainPage.class);
            intent.putExtra("token", userToken);
            startActivity(intent);
            overridePendingTransition(com.mondaychicken.bacving.R.anim.fade, com.mondaychicken.bacving.R.anim.hold);
            splash.this.finish();
            Log.d("Sucess", result);
        }else if(code == 412){
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "로그인에 문제가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(splash.this, preLayout.class);
                    startActivity(intent);
                    overridePendingTransition(com.mondaychicken.bacving.R.anim.fade, com.mondaychicken.bacving.R.anim.hold);
                    finish();
                    splash.this.finish();
                }
            });
        }else{
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "인터넷 연결에 오류가 발생했습니다. 인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    moveTaskToBack(true);
                    finish();
                    splash.this.finish();
                }
            });

//            Log.d("failed", result);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        background = null;
        try {
            this.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void ConnectServer(){
        try {
            StringBuffer data = new StringBuffer();
            data.append("email").append("=").append(email).append("&");
            data.append("pw").append("=").append(pw).append("&");
            data.append("deviceid").append("=").append(logintoken).append("&");
            data.append("type").append("=").append("2").append("&");
            data.append("mode").append("=").append("5").append("&");

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
        } catch (ConnectException e) {
            e.printStackTrace();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    builder.setTitle("인터넷 연결 오류")
                            .setMessage("인터넷 연결이 되어 있지 않습니다. 인터넷 연결을 확인해 주세요.")
                            .setPositiveButton("확인", null)
                            .show();
                }
            });
            moveTaskToBack(true);
            finish();

            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 문자값저장
    public void SavePreference(String key, String value, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    // 저장 불러오기
    private void LoadPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        email= sharedPreferences.getString("email", null);
        pw = sharedPreferences.getString("pw", null);

    }

    public void getInstanceIdToken()
    {
        if (checkPlayServices())
        {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.d("test", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public void registBroadcastReceiver()
    {
        RegistrationBroadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();

                if(action.equals(QuickstartPreferences.REGISTRATION_READY))
                {
                    Log.d("test", "READY");
                }
                else if(action.equals(QuickstartPreferences.REGISTRATION_GENERATING))
                {
                    Log.d("test", "GENERATING" );
                }
                else if(action.equals(QuickstartPreferences.REGISTRATION_COMPLETE))
                {
                    String token = intent.getStringExtra("token");
                    Log.d("test", "token : " + token);

                    logintoken = token;
                }

            }
        };
    }
}
