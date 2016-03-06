package com.mondaychicken.bacving.teamPlayer.setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.mondaychicken.bacving.R;

/**
 * Created by leejaebeom on 2015. 11. 4..
 */

public class setting extends AppCompatActivity {
    //종료를 위한 조치
    public static setting setActivity;

    RecyclerView recyclerView;
    settingAdapter adapter;
    LinearLayoutManager manager;
    //false는 팀장, true는 팀원
    boolean teamMember;

    int leader_idx, userNo;
    public static String team_idx, nickname;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        setActivity = setting.this;
        Intent intent = getIntent();
        leader_idx = Integer.parseInt(intent.getExtras().getString("leader_idx","0"));
        userNo = Integer.parseInt(intent.getExtras().getString("userno", "0"));
        team_idx = intent.getExtras().getString("team_idx");
        nickname = intent.getExtras().getString("nickname");
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar_setting);
        recyclerView = (RecyclerView)findViewById(R.id.setting_recycler);
        adapter = new settingAdapter();
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(position);
        recyclerView.setLayoutManager(manager);
        toolbar.setTitle("설정");
        toolbar.setTitleTextColor(Color.WHITE);
        recyclerView.setAdapter(adapter);

        addHeader("팀페이지", position);
        if (leader_idx == userNo){
            teamMember = false;
            SavePreference("teamMember", teamMember, this);
            addName("팀 삭제", position);
            addName("정보 수정", position);
            addName("가입 요청 리스트", position);
            addHeader("개인", position);
            addName("별명 설정", position);
        }else{
            teamMember = true;
            SavePreference("teamMember", teamMember, this);
            addName("탈퇴하기", position);
            addHeader("개인", position);
            addName("별명 설정", position);
        }

    }

    private void addHeader(String header, int position){
        adapter.add(createMainList(header, null), position);
        this.position++;
    }
    private void addName(String name, int position){
        adapter.add(createMainList(null, name), position);
        this.position++;
    }
    private settingData createMainList(String header, String name){
        settingData data = new settingData();
        data.setSettingHeader(header);
        data.setSettingName(name);
        return data;
    }

    public void SavePreference(String key, boolean value, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
}
