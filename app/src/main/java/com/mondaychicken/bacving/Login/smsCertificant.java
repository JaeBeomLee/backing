package com.mondaychicken.bacving.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.mondaychicken.bacving.R;

/**
 * Created by ijaebeom on 15. 7. 27..
 */

//지금 안씁니다. Cerificate로 통합됩니다.
public class smsCertificant extends AppCompatActivity {
    String phone;
    TextView phoneText;
    static EditText certNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_sms_cert);
        Intent intent = getIntent();
        phoneText = (TextView)findViewById(R.id.phone_num_txt);
        certNum = (EditText)findViewById(R.id.cert_num);
        phone = intent.getStringExtra("phone");

        StringBuffer sb = new StringBuffer(phone);
        sb.insert(3,"-");
        sb.insert(8,"-");

        phoneText.setText(sb);

    }
//    public static void inputAuthNumber(String authNumber){
//        if (authNumber != null){
//            certNum.setText(authNumber);
//        }
//    }
}
