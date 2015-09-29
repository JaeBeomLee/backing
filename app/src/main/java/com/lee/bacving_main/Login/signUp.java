package com.lee.bacving_main.Login;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.lee.bacving_main.R;
import com.lee.bacving_main.main.MainPage;

public class signUp extends AppCompatActivity {
    Button man;
    Button woman;
    Button SignUpBt;
    DatePicker dp;
    TextView signUpBirthText;
    EditText signUpEmail, signUpPw1, signUpPw2, signUpName;

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
                }else if (!email.contains("@") || !email.contains(".")){
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        final AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setMessage("데이터를 다 입력하지 않으셨습니다.");
        build.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        return build.create();
    }

}
