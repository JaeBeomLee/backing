package com.mondaychicken.bacving.Login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.mondaychicken.bacving.R;
import com.mondaychicken.bacving.gcm.QuickstartPreferences;
import com.mondaychicken.bacving.gcm.RegistrationIntentService;
import com.mondaychicken.bacving.main.MainPage;
import com.viewpagerindicator.CirclePageIndicator;

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
 * Created by ijaebeom on 15. 7. 3..
 */
public class preLayout extends AppCompatActivity implements ViewSwitcher.ViewFactory{

    Button preLogin, preSignup, loginBt, preFacebookLogin;
    TextView privacyPolicy;
    ImageSwitcher background;
    ViewPager pager;
    CirclePageIndicator indicator;

    private AlertDialog alertDialog = null;
    private AlertDialog alertDialog2 = null;
    EditText loginEmailText;
    EditText loginPasswordText;
    LinearLayout logo;

    String loginEmail;
    String loginPassword;
    String logintoken;
    String server = "http://52.68.69.47/api/member/index.php";
    String serverResult;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private BroadcastReceiver RegistrationBroadcastReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main2);

        registBroadcastReceiver();
        getInstanceIdToken();

        LoadPreference(this);

        background = (ImageSwitcher)findViewById(R.id.pre_background);
        pager = (ViewPager)findViewById(R.id.pre_pager);

        background.setFactory(this);
        background.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        background.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
        setupViewPager(pager);

        indicator = (CirclePageIndicator)findViewById(R.id.pre_indicator);
        indicator.setViewPager(pager);
        indicator.setSnap(true);
        pager.setOffscreenPageLimit(5);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        background.setImageResource(R.drawable.basketball);
                        break;
                    case 1:
                        background.setImageResource(R.drawable.baseball);
                        break;
                    case 2:
                        background.setImageResource(R.drawable.soccer);
                        break;
                    case 3:
                        background.setImageResource(R.drawable.hiking);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        preLogin = (Button)findViewById(R.id.pre_login);
        preSignup = (Button)findViewById(R.id.pre_signup);
        preFacebookLogin = (Button)findViewById(R.id.pre_facebook_login);
        privacyPolicy = (TextView)findViewById(R.id.privacy_policy_txt);

        logo = (LinearLayout)findViewById(R.id.logo);
        privacyPolicy.setText(Html.fromHtml("<u>이용약관</u>"));
        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(preLayout.this, PrivacyPolicy.class);
                startActivity(intent);
            }
        });
        //로그인 다이얼로그 띄우기
       preLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = DialogLogin();
                alertDialog.show();
            }
        });

        //회원가입 페이지로 이동
        preSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(preLayout.this, Certificate.class);
                intent.putExtra("token", logintoken);
                startActivity(intent);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        LoginPagerAdapter adapter = new LoginPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new main_fragment_1());
        adapter.addFrag(new main_fragment_2());
        adapter.addFrag(new main_fragment_3());
        adapter.addFrag(new main_fragment_4());
        viewPager.setAdapter(adapter);
    }



    // 로그인 다이얼로그
    private AlertDialog DialogLogin(){
        final View innerView = getLayoutInflater().inflate(R.layout.login_dialog, null);
        final AlertDialog.Builder build = new AlertDialog.Builder(this);

        loginBt = (Button)innerView.findViewById(R.id.login_bt);
        loginEmailText =(EditText)innerView.findViewById(R.id.login_id);
        loginPasswordText = (EditText)innerView.findViewById(R.id.login_pw);

        //로그아웃 했을때 직전까지 로그인 되있던 이메일 입력
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
                }


            }
        });
        return build.create();
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

    //잘못 입력했음을 알려주는 다이얼로그
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
    // 네트워크 오류 다이얼로그
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

    //로그인 메서드
    public void Login(){
        final ProgressDialog dialog = ProgressDialog.show(this, "", "잠시만 기다려 주세요",true);

        new Thread(){
            @Override
            public void run() {
                //서버 연결 메서드
                ConnectServer();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //서버 연결이 끝난 뒤 처리해주는 메서드
                        LoginEnd();

                    }
                });
                dialog.dismiss();
            }
        }.start();
    }
    private void LoginEnd(){

        //리퀘스트 받은 텍스트 저장
        String result = serverResult;

        int code = 0;
        String msg = "";
        String account = null, userno = null, userEmail= null, userPw= null, userName= null, userProfileimg= null, userStatus= null, userGrade= null, userToken= null, userPhone = null, userBirth = null, userGender = null;

        if (result == null){
            return;
        }
        try{
            //json 형태의 리퀘스트를 분해, 저장
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
            userBirth = json.getString("birth");
            userGender = json.getString("gender");
            userPhone = json.getString("phone");
        } catch (JSONException e) {
            e.printStackTrace();

        } catch (NullPointerException e){
            e.printStackTrace();

            code = 0;
            msg = "failed";
        }

        if (code == 700){
            //code 700 제대로 된 송수신을 의미
            //기본적으로 모든 앱 내부에서 사용되는 email, pw, 유저 고유번호 이름 을 저장
            SavePreference("userno", userno, this);
            SavePreference("email", loginEmail, this);
            SavePreference("pw", loginPassword, this);
            SavePreference("name", userName, this);
            SavePreference("profileimg", userProfileimg, this);
            SavePreference("statue", userStatus, this);
            SavePreference("grade", userGrade, this);
            SavePreference("birth", userBirth, this);
            SavePreference("gender", userGender, this);
            SavePreference("phone", userPhone, this);

            //제대로 로그인 된 것이므로 메인 페이지로 이동
            Intent intent = new Intent(this, MainPage.class);
            intent.putExtra("token", userToken);
            startActivity(intent);
            preLayout.this.finish();
            Log.d("Sucess", result);
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

    @Override
    public View makeView() {
        ImageView back = new ImageView(this);
        back.setBackgroundColor(0x000000);
        back.setScaleType(ImageView.ScaleType.CENTER_CROP);
        back.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        back.setImageResource(R.drawable.basketball);
        return back;

    }
}

