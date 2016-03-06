package com.mondaychicken.bacving.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import bacving.lee.bacving_main.R;


/**
 * Created by ijaebeom on 15. 8. 5..
 */
public class Certificate extends AppCompatActivity {

    String phone;
//    RadioGroup certType;
//    RadioButton certEmail;
//    RadioButton certSms;
    Button certEmail;
    Button certSms;
    EditText certMethod;
    static EditText certNumber;
    Button methodCheckButton;
    Button certCheckButton;
    Button signUpPre;
    CheckBox privacyPolicy;
    boolean checkedButton = false;
//    TelephonyManager phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_cert_all);

        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
//        certType = (RadioGroup)findViewById(R.id.cert_type);
        certEmail = (Button)findViewById(R.id.cert_email);
        certSms = (Button) findViewById(R.id.cert_sms);
        certMethod = (EditText)findViewById(R.id.cert_method);
        certNumber = (EditText)findViewById(R.id.cert_num);
        methodCheckButton = (Button)findViewById(R.id.method_chk_btn);
        certCheckButton = (Button)findViewById(R.id.cert_chk_btn);
        signUpPre = (Button)findViewById(R.id.sign_up_pre);
        privacyPolicy = (CheckBox)findViewById(R.id.privacy_policy);
//        phoneNumber = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        toolbar.setTitle("인증하기");
        toolbar.setTitleTextColor(0xffffffff);

//         phone = phoneNumber.getLine1Number();
        certEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                certMethod.setHint("이메일 주소");
                certEmail.setBackgroundResource(R.drawable.woman_style_able);
                certEmail.setTextColor(0xffffffff);
                certSms.setBackgroundResource(R.drawable.woman_style_unable);
                certSms.setTextColor(0xffEF5350);
                certMethod.setInputType(1);
                certNumber.setInputType(1);
                checkedButton = false;
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
                certMethod.setInputType(3);
                certNumber.setInputType(3);
                checkedButton = true;
            }
        });

        methodCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkedButton == true){
                    inputAuthNumber(certMethod.getText().toString());
                }else if(checkedButton == false){

                }
            }
        });
        signUpPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Certificate.this, signUp.class);
                startActivity(intent);
            }
        });
        privacyPolicy.setText(Html.fromHtml("박빙 <font color=\"blue\"><a href= \"http://www.bacving.com\" >서비스 이용약관</a></font>및 <font color=\"blue\"><a href= \"http://www.bacving.com\" >회원정보 제공</a></font>에 동의합니다."));
        privacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());


    }

    public static void inputAuthNumber(String authNumber){
        if (authNumber != null){
           certNumber.setText(authNumber);
        }
    }
}
