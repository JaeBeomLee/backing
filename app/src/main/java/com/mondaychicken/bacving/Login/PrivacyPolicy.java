package com.mondaychicken.bacving.Login;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import bacving.lee.bacving_main.R;

/**
 * Created by ijaebeom on 15. 7. 2..
 */
public class PrivacyPolicy extends ActionBarActivity {
    Button pPButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_privacy);
        getSupportActionBar().hide();



        pPButton = (Button)findViewById(R.id.privacy_bt);
        pPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
