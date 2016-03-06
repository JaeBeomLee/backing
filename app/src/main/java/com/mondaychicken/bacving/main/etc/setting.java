package com.mondaychicken.bacving.main.etc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.mondaychicken.bacving.R;

/**
 * Created by leejaebeom on 2015. 11. 24..
 */
public class setting extends AppCompatActivity {
    CheckBox alert;
    boolean isAlertChecked = true;
    DrawerArrowDrawable drawerArrowDrawable;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        LoadPreference(setting.this);
        alert = (CheckBox)findViewById(R.id.app_setting_alert);
        toolbar = (Toolbar)findViewById(R.id.tool_bar_app_setting);
        drawerArrowDrawable = new DrawerArrowDrawable(this);

        drawerArrowDrawable.setProgress(1);
        toolbar.setNavigationIcon(drawerArrowDrawable);
        toolbar.setTitle("설정");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        alert.setChecked(true);
        if (!isAlertChecked){
            alert.setChecked(false);
        }
        alert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isAlertChecked = isChecked;
                    SavePreference("app_setting_alert", isAlertChecked, setting.this);
                }else if(!isChecked){
                    isAlertChecked = isChecked;
                    SavePreference("app_setting_alert", isAlertChecked, setting.this);
                }
            }
        });

    }
    public void SavePreference(String key, boolean value, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    private void LoadPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        isAlertChecked = sharedPreferences.getBoolean("app_setting_alert",true);
    }
}
