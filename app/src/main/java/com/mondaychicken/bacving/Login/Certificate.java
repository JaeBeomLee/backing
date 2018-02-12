package com.mondaychicken.bacving.Login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mondaychicken.bacving.R;

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
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by ijaebeom on 15. 8. 5..
 */
public class Certificate extends AppCompatActivity {

    String phone, email;
    Button certEmail;
    Button certSms;
    EditText certMethod;
    static EditText certNumber;
    Button methodCheckButton;
    Button certCheckButton;
    Button signUpPre;
    CheckBox privacyPolicy;
    boolean checkedButton = false;
    String secureCode = null;
    private AlertDialog alertDialog2 = null;
    TextView timerText;
    LinearLayout timerLinear;
    Timer timer;
    Handler handlerTimer;

    int time = 180000; //회원 인증 만료 시간
    String certUrl = "http://api.bacving.com/api/member/join/request.php";
    String checkUrl = "http://api.bacving.com/api/member/join/check.php";
    String serverResult;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_cert_all);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        certEmail = (Button)findViewById(R.id.cert_email);
        certSms = (Button) findViewById(R.id.cert_sms);
        certMethod = (EditText)findViewById(R.id.cert_method);
        certNumber = (EditText)findViewById(R.id.cert_num);
        methodCheckButton = (Button)findViewById(R.id.method_chk_btn);
        certCheckButton = (Button)findViewById(R.id.cert_chk_btn);
        signUpPre = (Button)findViewById(R.id.sign_up_pre);
        privacyPolicy = (CheckBox)findViewById(R.id.privacy_policy);
        timerLinear = (LinearLayout)findViewById(R.id.timer);
        timerText = (TextView)findViewById(R.id.timer_text);

        toolbar.setTitle("인증하기");
        toolbar.setTitleTextColor(0xffffffff);
        certMethod.setHint("이메일 주소");
        certEmail.setBackgroundResource(R.drawable.woman_style_able);
        certEmail.setTextColor(0xffffffff);
        certSms.setBackgroundResource(R.drawable.woman_style_unable);
        certSms.setTextColor(0xffEF5350);
        certMethod.setInputType(208);
        certMethod.setText(null);

        certEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                certMethod.setHint("이메일 주소");
                certEmail.setBackgroundResource(R.drawable.woman_style_able);
                certEmail.setTextColor(0xffffffff);
                certSms.setBackgroundResource(R.drawable.woman_style_unable);
                certSms.setTextColor(0xffEF5350);
                certMethod.setInputType(208);
                checkedButton = false;
                certMethod.setText(null);
            }
        });

        certSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                certMethod.setHint("전화 번호");
//                certMethod.setText(phone);
                certSms.setBackgroundResource(R.drawable.woman_style_able);
                certSms.setTextColor(0xffffffff);
                certEmail.setBackgroundResource(R.drawable.woman_style_unable);
                certEmail.setTextColor(0xffEF5350);
                certMethod.setInputType(2);
                certNumber.setInputType(2);
                checkedButton = true;
                certMethod.setText(null);
            }
        });

        methodCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkedButton == true){
                    certServer();
                    phone = certMethod.getText().toString();
                    inputAuthNumber(certMethod.getText().toString());
                }else if(checkedButton == false){
                    certServer();
                    email = certMethod.getText().toString();
                }


            }
        });
        certCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkServer();
            }
        });
        signUpPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (privacyPolicy.isChecked()){
                    if (secureCode != null){
                        Intent intent = new Intent(Certificate.this, signUp.class);
                        intent.putExtra("token", token);
                        intent.putExtra("secuCode", secureCode);
                        if(checkedButton == true){
                            intent.putExtra("phone", phone);
                        }else if (checkedButton == false){
                            intent.putExtra("email", email);
                        }
                        startActivity(intent);
                        finish();
                    }else{
                        alertDialog2 = DialogInit("인증 오류","인증을 먼저 진행해 주세요");
                        alertDialog2.show();
                    }
                }else{
                    alertDialog2 = DialogInit("약관 오류","이용 약관 및 회원 정보 제공에 동의 해주세요.");
                    alertDialog2.show();
                }
            }
        });
        privacyPolicy.setText(Html.fromHtml("박빙 <font color=\"blue\"><a href= \"http://www.bacving.com\" >서비스 이용약관</a></font>및 <font color=\"blue\"><a href= \"http://www.bacving.com\" >회원정보 제공</a></font>에 동의합니다."));
        privacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());


    }

    public void certServer(){
        final ProgressDialog dialog = ProgressDialog.show(this, "", "잠시만 기다려 주세요",true);

        new Thread(){
            @Override
            public void run() {
                certConnectServer();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        certServerEnd();

                    }
                });
                dialog.dismiss();
            }
        }.start();
    }
    private void certServerEnd(){

        String result = serverResult;
        int code = 0;
        String msg = "";
        String send_time = null, userno = null;

        if (result == null){
            return;
        }
        try{
            Log.d("result", result);
            JSONObject json = new JSONObject(result);
            code = json.getInt("code");
            msg = json.getString("message");
            send_time = json.getString("send_time");
        } catch (JSONException e) {
            e.printStackTrace();

        } catch (NullPointerException e){
            e.printStackTrace();

            code = 0;
            msg = "failed";
        }

        if (code == 700){
            //인증 성공
            if (checkedButton == true){
                timerLinear.setVisibility(View.VISIBLE);
                timer = new Timer();
                handlerTimer = new Handler();

                timer.cancel();
                timer = null;
                timer = new Timer();

                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        handlerTimer.post(new Runnable() {
                            @Override
                            public void run() {
                                int minutes = (int)((time/ (1000 * 60)) % 60);
                                int second = (int) ((time/ 1000) % 60);
                                String timerS = String.format("%02d:%02d", minutes, second);
                                timerText.setText(timerS);
                                time -= 1000;
                                if (time < 0){
                                    timer.cancel();
                                    timer.purge();
                                    timerText.setText("인증 시간이 초과했습니다");
                                    return;
                                }
                            }
                        });
                    }
                };
                time = 180000;
                timer.schedule(task, 1000, 1000);
            }
            alertDialog2 = DialogInit("인증번호 전송완료!","전송받으신 인증번호를 3분 안에 입력해주세요.");
            alertDialog2.show();

        }else{
            alertDialog2 = DialogInit("인증 오류",msg);
            alertDialog2.show();
            Log.d("failed", code + " " + msg);

//            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
//            Log.d("failed", result);


        }
    }
    private void certConnectServer(){
        try {
            serverResult = null;
            StringBuffer data = new StringBuffer();
            if (checkedButton == true){
                data.append("type").append("=").append("phone").append("&");
                data.append("phone").append("=").append(certMethod.getText().toString()).append("&");
            }else if(checkedButton == false){
                data.append("type").append("=").append("email").append("&");
                data.append("email").append("=").append(certMethod.getText().toString()).append("&");
            }

            URL url = new URL(certUrl);
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
                    alertDialog2 = DialogInit("인터넷 연결 오류","네트워크 연결이 올바르지 않습니다");
                    alertDialog2.show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkServer(){
        final ProgressDialog dialog = ProgressDialog.show(this, "", "잠시만 기다려 주세요",true);

        new Thread(){
            @Override
            public void run() {
                checkConnectServer();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checkServerEnd();

                    }
                });
                dialog.dismiss();
            }
        }.start();
    }
    private void checkServerEnd(){

        String result = serverResult;

        int code = 0;
        String msg = "";


        if (result == null){
            return;
        }
        try{
            Log.d("result", result);
            JSONObject json = new JSONObject(result);
            code = json.getInt("code");
            msg = json.getString("message");
            secureCode = json.getString("secuCode");
            Log.d("secu", secureCode);
        } catch (JSONException e) {
            e.printStackTrace();

        } catch (NullPointerException e){
            e.printStackTrace();

            code = 0;
            msg = "failed";
        }

        if (code == 700){
            alertDialog2 = DialogInit("인증성공","인증에 성공하였습니다");
            alertDialog2.show();
        }else{
            alertDialog2 = DialogInit("인증오류",msg);
            alertDialog2.show();
            Log.d("failed", code + " " + msg);

        }
    }
    private void checkConnectServer(){
        try {
            serverResult = null;
            StringBuffer data = new StringBuffer();
//            data.append("email").append("=").append(loginEmail).append("&");
            data.append("target").append("=").append(certMethod.getText().toString()).append("&");
            data.append("code").append("=").append(certNumber.getText().toString()).append("&");
//            data.append("type").append("=").append("2").append("&");
//            data.append("mode").append("=").append("5").append("&");

            URL url = new URL(checkUrl);
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
                    alertDialog2 = DialogInit("인터넷 연결 오류","네트워크 연결이 올바르지 않습니다");
                    alertDialog2.show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //다이얼로그 호출 함수
    private AlertDialog  DialogInit(String title, String message){
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        if(title != "") {
            build.setTitle(title);
        }
        build.setMessage(message);
        build.setPositiveButton("확인", null);
        return build.create();
    }

    // 문자값저장
    public void SavePreference(String key, String value, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }


//    private void LoadPreference(Context context) {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        loginEmail= sharedPreferences.getString("email", null);
//        loginPassword= sharedPreferences.getString("pw", null);
//    }

    public static void inputAuthNumber(String authNumber){
        if (authNumber != null && authNumber.length() == 4){
            certNumber.setText(authNumber);
        }
    }
}
