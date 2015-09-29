package com.lee.bacving_main.teamPlayer;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.lee.bacving_main.DummyFragment;
import com.lee.bacving_main.R;

public class teamActivity extends AppCompatActivity {

    String teamName;
    ViewPager viewPager;
    TabLayout tabLayout;
    TextView teamNameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        Intent intent = getIntent();
        teamName = intent.getExtras().getString("teamName");
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        teamNameText = (TextView)findViewById(R.id.team_name);
        teamNameText.setText(teamName);
        setupViewPager(viewPager);
        //탭 레이아웃과 뷰페이져를 같이 사용하는데 탭뷰와 달리 간편하게 메서드 하나로 연결이 가능하다
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new teamInformationFragment(), "정보");
        adapter.addFrag(new DummyFragment(), "일정/결과");
        adapter.addFrag(new playerListFragment(), "선수단");
        viewPager.setAdapter(adapter);
    }

}
