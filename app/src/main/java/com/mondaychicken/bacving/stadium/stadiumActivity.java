package com.mondaychicken.bacving.stadium;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.lee.bacving_main.DummyFragment;
import bacving.lee.bacving_main.R;

public class stadiumActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stadium_pager_view);


        viewPager = (ViewPager)findViewById(R.id.view_pager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        //탭 레이아웃과 뷰페이져를 같이 사용하는데 탭뷰와 달리 간편하게 메서드 하나로 연결이 가능하다
        tabLayout.setupWithViewPager(viewPager);


    }

    private void setupViewPager(ViewPager viewPager) {
        stadiumViewPagerAdapter adapter = new stadiumViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new stadiumInformationFragment(), "정보");
        adapter.addFrag(new DummyFragment(), "예약안내");
        adapter.addFrag(new DummyFragment(), "규정");
        viewPager.setAdapter(adapter);
    }


}
