package com.lee.bacving_main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.lee.bacving_main.Login.preLayout;
import com.lee.bacving_main.main.MainPage;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoadPreference(getApplicationContext());
        handler = new Handler();
        builder = new android.support.v7.app.AlertDialog.Builder(splash.this);

        if (email != null && pw != null){
            Login();
        }else if (email == null || pw == null){
            Random r = new Random();
            setContentView(R.layout.splash);
            int imageRandom = r.nextInt(4)+1;
            background = (ImageView)findViewById(R.id.splash_background);
            switch (imageRandom){
                case 1:
                    background.setImageResource(R.drawable.baseball);
                    break;
                case 2:
                    background.setImageResource(R.drawable.basketball);
                    break;
                case 3:
                    background.setImageResource(R.drawable.hiking);
                    break;
                case 4:
                    background.setImageResource(R.drawable.soccer);
                    break;
                case 5:
                    background.setImageResource(R.drawable.ground);
                    break;
            }

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent =  new Intent(splash.this, preLayout.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade, R.anim.hold);
                    splash.this.finish();
                }
            }, 2000);
        }
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
        String account = null,userno = null, userEmail= null, userPw= null, userName= null, userProfileimg= null, userStatus= null, userGrade= null, userToken= null;

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
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
            code = 0;
            msg = "failed";
        }

        if (code == 700){
//            Toast.makeText(splash.this, msg, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(splash.this, MainPage.class);
            intent.putExtra("userno", userno)
                    .putExtra("email", userEmail)
                    .putExtra("pw", userPw)
                    .putExtra("name", userName)
                    .putExtra("profileimg", userProfileimg)
                    .putExtra("status", userStatus)
                    .putExtra("grade", userGrade)
                    .putExtra("token", userToken);

            startActivity(intent);
            overridePendingTransition(R.anim.fade, R.anim.hold);
            splash.this.finish();
            Log.d("Sucess", result);
        }else{
//            Intent intent = new Intent(splash.this, preLayout.class);
//            startActivity(intent);
//            Toast.makeText(splash.this, result, Toast.LENGTH_SHORT).show();
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

    private void ConnectServer(){
        try {
            StringBuffer data = new StringBuffer();
            data.append("email").append("=").append(email).append("&");
            data.append("pw").append("=").append(pw).append("&");
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
    // 저장 불러오기
    private void LoadPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        email= sharedPreferences.getString("email", null);
        pw = sharedPreferences.getString("pw", null);

    }

}
