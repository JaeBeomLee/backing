package com.lee.bacving_main.Login;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.TextView;

import com.lee.bacving_main.R;

//안쓰는 중입니다.
public class MainActivity extends ActionBarActivity {


    TextView sign_Up;
    Button LoginBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_dialog);
        getSupportActionBar().hide();

//        sign_Up = (TextView)findViewById(R.id.sign_up_text);
//        sign_Up.setText(Html.fromHtml("함께하고 싶으신가요? 그렇다면 <u>회원가입</u>!"));
//        sign_Up.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, signUp.class);
//                startActivity(intent);
//            }
//        });


    }

}
