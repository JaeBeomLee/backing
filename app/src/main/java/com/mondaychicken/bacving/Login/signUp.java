package com.mondaychicken.bacving.Login;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import bacving.lee.bacving_main.R;
import com.mondaychicken.bacving.main.MainPage;

public class signUp extends AppCompatActivity {
    Button man;
    Button woman;
    Button SignUpBt;
    DatePicker dp;
    TextView signUpBirthText;
    EditText signUpEmail, signUpPw1, signUpPw2, signUpName;
    String signUpResult;
    private AlertDialog dialog = null;

    int year;
    int month;
    int date;

    String email, pw1, pw2, name ,birth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);


        signUpEmail = (EditText)findViewById(R.id.signup_email);
        signUpPw1 = (EditText)findViewById(R.id.signup_pw);
        signUpPw2 = (EditText)findViewById(R.id.signup_pw2);
        signUpName = (EditText)findViewById(R.id.signup_name);




        signUpBirthText = (TextView)findViewById(R.id.signup_birth_text);
        signUpBirthText.setText("생년월일");
        man = (Button)findViewById(R.id.signup_man);
        woman = (Button)findViewById(R.id.signup_woman);
        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                man.setBackgroundResource(R.drawable.man_style_able);
                man.setTextColor(0xffffffff);
                woman.setBackgroundResource(R.drawable.woman_style_unable);
                woman.setTextColor(0xffe74c3c);
            }
        });

        woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                woman.setBackgroundResource(R.drawable.woman_style_able);
                woman.setTextColor(0xffffffff);
                man.setBackgroundResource(R.drawable.man_style_unable);
                man.setTextColor(0xff5677fa);
            }
        });

        SignUpBt = (Button)findViewById(R.id.sign_up_bt);

        SignUpBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpData();
                if (checkDataBlank() == false){
                    dialog = DialogLogin();
                    dialog.show();
                }else if (!email.contains("@") || !email.contains("build/intermediates/exploded-aar/com.google.android.gms/play-services-appstate/8.1.0/res")){
                    dialog = DialogEmailCheck();
                    dialog.show();
                }else if (pw1.length() < 6){
                    dialog = DialogPasswordLength();
                    dialog.show();
                }else if (!pw1.equals(pw2)){
                    dialog = DialogPasswordCheck();
                    dialog.show();
                }

                Intent intent = new Intent(signUp.this, MainPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
    }

//    public void Login(){
//        final ProgressDialog dialog = ProgressDialog.show(this, "", "잠시만 기다려 주세요",true);
//
//        new Thread(){
//            @Override
//            public void run() {
//                ConnectServer();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        LoginEnd();
//
//                    }
//                });
//                dialog.dismiss();
//            }
//        }.start();
//    }

//    private void LoginEnd(){
//
//        String result = signUpResult;
//
//        int code = 0;
//        String msg = "";
//        String account = null,userno = null, userEmail= null, userPw= null, userName= null, userProfileimg= null, userStatus= null, userGrade= null, userToken= null;
//        if (result == null){
//            return;
//        }
//        try{
//            JSONObject json = new JSONObject(result);
//            code = json.getInt("code");
//            msg = json.getString("message");
//            account = json.getString("account");
//            JSONObject accountJson = new JSONObject(account);
//
//            userno = accountJson.getString("userno");
//            userEmail = accountJson.getString("email");
//            userPw = accountJson.getString("pw");
//            userName = accountJson.getString("name");
//            userProfileimg = accountJson.getString("profileimg");
//            userStatus = accountJson.getString("status");
//            userGrade = accountJson.getString("grade");
//            userToken = json.getString("token");
//        } catch (JSONException e) {
//            e.printStackTrace();
//
//        } catch (NullPointerException e){
//            e.printStackTrace();
//
//            code = 0;
//            msg = "failed";
//        }
//
//        if (code == 700){
////            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//
//            SavePreference("email", loginEmail, getApplicationContext());
//            SavePreference("pw", loginPassword, getApplicationContext());
//            SavePreference("token", logintoken, getApplicationContext());
//
//            Intent intent = new Intent(this, MainPage.class);
//            intent.putExtra("userno", userno)
//                    .putExtra("email", userEmail)
//                    .putExtra("pw", userPw)
//                    .putExtra("name", userName)
//                    .putExtra("profileimg", userProfileimg)
//                    .putExtra("status", userStatus)
//                    .putExtra("grade", userGrade)
//                    .putExtra("token", userToken);
//
//            startActivity(intent);
//            preLayout.this.finish();
//            Log.d("Sucess", result);
//        }else{
//            Handler handler = new Handler(Looper.getMainLooper());
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    alertDialog2 = DialogWrong();
//                    alertDialog2.show();
//                }
//            });
//            Log.d("failed", code + " " + msg);
//
////            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
////            Log.d("failed", result);
//
//
//        }
//    }
//    private void ConnectServer(){
//        try {
//            signUpResult = null;
//            StringBuffer data = new StringBuffer();
//            data.append("email").append("=").append(loginEmail).append("&");
//            data.append("pw").append("=").append(loginPassword).append("&");
//            data.append("deviceid").append("=").append(logintoken).append("&");
//            data.append("type").append("=").append("2").append("&");
//            data.append("mode").append("=").append("5").append("&");
//
//            URL url = new URL(server);
//            HttpURLConnection http = (HttpURLConnection)url.openConnection();
//            http.setDefaultUseCaches(false);
//            http.setDoInput(true);
//            http.setDoOutput(true);
//            http.setRequestMethod("POST");
////            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");
//
//            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
//            PrintWriter writer = new PrintWriter(outStream);
//            writer.write(data.toString());
//            writer.flush();
//
//            InputStreamReader ims = new InputStreamReader(http.getInputStream(),"UTF-8");
//            BufferedReader reader = new BufferedReader(ims);
//            StringBuilder builder = new StringBuilder();
//            String str;
//
//            while ((str = reader.readLine()) != null){
//                builder.append(str);
//            }
//
//            serverResult = builder.toString();
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (ConnectException e){
//            e.printStackTrace();
//            Handler handler = new Handler(Looper.getMainLooper());
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    alertDialog2 = DialogNetwork();
//                    alertDialog2.show();
//                }
//            });
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    public void signUpBirth(View v){
        switch (v.getId()){
            case R.id.signup_birth:
                dialog = DialogDatePicker();
                dialog.show();
                break;
        }
    }



    private AlertDialog DialogDatePicker(){

        final View innerView = getLayoutInflater().inflate(R.layout.sign_up_birth_picker,null);
        dp = (DatePicker)innerView.findViewById(R.id.birth_picker);

        AlertDialog.Builder builder = new AlertDialog.Builder(signUp.this);
        builder.setTitle("생년월일");
        builder.setMessage("생년월일을 선택해 주세요");
        builder.setCancelable(true);
        builder.setView(innerView);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                year = dp.getYear();
                month = dp.getMonth()+1;
                date = dp.getDayOfMonth();
                signUpBirthText.setText(year+"년 "+ month+"월 " + date + "일생 ");
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

    private AlertDialog DialogPasswordCheck(){

        AlertDialog.Builder builder = new AlertDialog.Builder(signUp.this);
        builder.setMessage("비밀번호가 비밀번호 확인과 일치 하지 않습니다.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }
    private AlertDialog DialogPasswordLength(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("비밀번호는 최소 6자리 이상이어야 합니다");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    private AlertDialog DialogEmailCheck(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("이메일 형식이 아닙니다.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }
    protected void signUpData(){
        email = signUpEmail.getText().toString();
        pw1 = signUpPw1.getText().toString();
        pw2 = signUpPw2.getText().toString();
        name = signUpName.getText().toString();

        birth = signUpBirthText.getText().toString();
    }

    protected boolean checkDataBlank(){
        if (email.equals("") || pw1.equals("")|| pw2.equals("")|| name.equals("")|| birth.equals("생년월일")){
            return false;
        }else {
            return true;
        }
    }

    private AlertDialog DialogLogin(){
        final AlertDialog.Builder build = new AlertDialog.Builder(signUp.this);
        build.setMessage("데이터를 다 입력하지 않으셨습니다.");
        build.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        return build.create();
    }

    public void SavePreference(String key, String value, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

}
