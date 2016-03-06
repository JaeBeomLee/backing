package com.mondaychicken.bacving.Login;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.EditText;

import bacving.lee.bacving_main.R;

/**
 * Created by ijaebeom on 15. 7. 27..
 */

//지금 안씁니다. Cerificate로 통합됩니다.
public class smsNumber extends ActionBarActivity {

    EditText phoneNumber;
    Button numButton;
    String Number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_cert_all);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#e02a2a")));
        bar.setTitle("회원인증");





//        phoneNumber = (EditText)findViewById(R.id.phone_number);
//        numButton = (Button)findViewById(R.id.num_button);
//        numButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (phoneNumber.getText().toString().length() < 11){
//                    Toast.makeText(getApplicationContext(),"전화번호가 너무 짧습니다.",Toast.LENGTH_SHORT).show();
//                }else{
//                    Intent intent = new Intent(smsNumber.this, smsCertificant.class);
//                    intent.putExtra("phone", phoneNumber.getText().toString());
//                    setResult(RESULT_OK, intent);
//                    startActivity(intent);
//                }
//            }
//        });


    }
}
