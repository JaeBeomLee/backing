package com.lee.bacving_main.Login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.lee.bacving_main.R;
import com.lee.bacving_main.gcm.QuickstartPreferences;
import com.lee.bacving_main.gcm.RegistrationIntentService;
import com.lee.bacving_main.main.MainPage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ijaebeom on 15. 7. 3..
 */
public class preLayout extends AppCompatActivity {

    Button preLogin, preSignup, loginBt;
    Animation animation;

    private AlertDialog alertDialog = null;
    private AlertDialog alertDialog2 = null;
    EditText loginEmailText;
    EditText loginPasswordText;
    LinearLayout logo;

    String loginEmail;
    String loginPassword;
    String logintoken;
    String server = "http://52.68.69.47/api/member/index.php";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    String serverResult;
    android.support.v7.app.AlertDialog.Builder builder;

    private BroadcastReceiver RegistrationBroadcastReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);



        preLogin = (Button)findViewById(R.id.pre_login);
        preSignup = (Button)findViewById(R.id.pre_signup);

        logo = (LinearLayout)findViewById(R.id.logo);

        builder = new android.support.v7.app.AlertDialog.Builder(preLayout.this);

        preLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = DialogLogin();
                alertDialog.show();
            }
        });

        preSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(preLayout.this, Certificate.class);
                startActivity(intent);
            }
        });
    }


    private AlertDialog DialogLogin(){
        final View innerView = getLayoutInflater().inflate(R.layout.login_dialog, null);
        final AlertDialog.Builder build = new AlertDialog.Builder(this);

        LoadPreference(getApplicationContext());
        loginBt = (Button)innerView.findViewById(R.id.login_bt);
        loginEmailText =(EditText)innerView.findViewById(R.id.login_id);
        loginPasswordText = (EditText)innerView.findViewById(R.id.login_pw);

        loginEmailText.setText(loginEmail);

        build.setView(innerView);
        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginEmail = loginEmailText.getText().toString();
                loginPassword = loginPasswordText.getText().toString();


                if (loginEmail.equals("") || loginPassword.equals("")) {
                    alertDialog2 = DialogWaring();
                    alertDialog2.show();
                } else {

                    Login();


//                    alertDialog.dismiss();
                }


            }
        });
        return build.create();
    }

    private AlertDialog DialogWaring(){
        final AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setMessage("이메일이나 비밀번호 입력을 제대로 안했습니다.");
        build.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog2.dismiss();

            }
        });
        return build.create();
    }

    public void Login(){
        final ProgressDialog dialog = ProgressDialog.show(this, "", "잠시만 기다려 주세요",true);

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
                dialog.dismiss();
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
//            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

            SavePreference("email", loginEmail, getApplicationContext());
            SavePreference("pw", loginPassword, getApplicationContext());
            SavePreference("token", logintoken, getApplicationContext());

            Intent intent = new Intent(this, MainPage.class);
            intent.putExtra("userno", userno)
                    .putExtra("email", userEmail)
                    .putExtra("pw", userPw)
                    .putExtra("name", userName)
                    .putExtra("profileimg", userProfileimg)
                    .putExtra("status", userStatus)
                    .putExtra("grade", userGrade)
                    .putExtra("token", userToken);

            startActivity(intent);
            preLayout.this.finish();
            Log.d("Sucess", result);
        }else{
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    builder.setMessage("이메일이나 비밀번호가 일치하지 않습니다. 이메일과 비밀번호를 확인해주세요")
                            .setPositiveButton("확인", null)
                            .show();
                }
            });
//            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
//            Log.d("failed", result);


        }
    }
    private void ConnectServer(){
        try {
            StringBuffer data = new StringBuffer();
            data.append("email").append("=").append(loginEmail).append("&");
            data.append("pw").append("=").append(loginPassword).append("&");
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
        } catch (ConnectException e){
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

            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            finish();
        }
        return super.onKeyDown(keyCode, event);

    }

    // 문자값저장
    public void SavePreference(String key, String value, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }


    private void LoadPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        loginEmail= sharedPreferences.getString("email", null);
        loginPassword= sharedPreferences.getString("pw", null);

    }
}

