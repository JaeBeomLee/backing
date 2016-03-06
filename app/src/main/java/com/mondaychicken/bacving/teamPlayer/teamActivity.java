package com.mondaychicken.bacving.teamPlayer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import bacving.lee.bacving_main.R;
import com.mondaychicken.bacving.teamPlayer.schedule.scheduleTabFragment;

import java.util.concurrent.ExecutionException;

public class teamActivity extends AppCompatActivity {

    String teamName;
    String teamLogoUrl, teamBackgroundUrl;
    Bitmap teamLogoBitmap = null, teamBackgroundBitmap = null;
    ViewPager viewPager;
    TabLayout tabLayout;
    TextView teamNameText;
    ImageView teamLogo, teamBackground;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
        Intent intent = getIntent();
        teamName = intent.getExtras().getString("teamName");
        teamLogoUrl = intent.getExtras().getString("teamLogo");
        teamBackgroundUrl = intent.getExtras().getString("teamBackground");
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        teamNameText = (TextView)findViewById(R.id.team_name);
        teamLogo = (ImageView)findViewById(R.id.team_logo);
        teamBackground = (ImageView)findViewById(R.id.team_background);

        teamNameText.setText(teamName);
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    teamBackgroundBitmap = Glide.with(teamActivity.this).load(teamBackgroundUrl).asBitmap().into(1024, 512).get();
                    teamLogoBitmap = Glide.with(teamActivity.this).load(teamLogoUrl).asBitmap().into(500, 500).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                teamBackground.setImageBitmap(teamBackgroundBitmap);
                teamLogo.setImageBitmap(teamLogoBitmap);
            }
        }.execute();
        setupViewPager(viewPager);
        //탭 레이아웃과 뷰페이져를 같이 사용하는데 탭뷰와 달리 간편하게 메서드 하나로 연결이 가능하다
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new teamInformationFragment(), "정보");
        adapter.addFrag(new scheduleTabFragment(), "일정/결과");
        adapter.addFrag(new playerListFragment(), "선수단");
        viewPager.setAdapter(adapter);
    }

}
