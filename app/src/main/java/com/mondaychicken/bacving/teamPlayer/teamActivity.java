package com.mondaychicken.bacving.teamPlayer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.mondaychicken.bacving.Login.preLayout;
import com.mondaychicken.bacving.R;
import com.mondaychicken.bacving.main.MainPage;
import com.mondaychicken.bacving.main.etc.notice;
import com.mondaychicken.bacving.main.etc.updateProfile;
import com.mondaychicken.bacving.matching.matchingSearch;
import com.mondaychicken.bacving.teamPlayer.change.createActivity;
import com.mondaychicken.bacving.teamPlayer.playerList.playerFragment;
import com.mondaychicken.bacving.teamPlayer.schedule.scheduleTabFragment;
import com.mondaychicken.bacving.teamPlayer.setting.setting;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class teamActivity extends AppCompatActivity implements View.OnClickListener{

    //종료를 위해
    public static teamActivity tActivity;

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    DrawerArrowDrawable drawerArrowDrawable;
    Button method_btn;
    String server = "http://52.68.69.47/api/team/index.php";
    String teamJoinServer = "http://52.68.69.47/api/team/index.php";
    String serverResult, teamName, teamLogoUrl, teamBackgroundUrl, userName, leader_idx, userNo, sport_idx, accept, token, join, nickname, positionS, userProfileimg= null;
    public static String teamDescription,team_idx,user_nickname;
    ViewPager viewPager;
    TabLayout tabLayout;
    ImageView teamLogo, teamBackground;
    LinearLayout viewPagerLayout;
    CollapsingToolbarLayout collapseToolbar;
    Toolbar toolbar;
    LinearLayout profileEdit;
    MenuItem menu_setting;
    // 임시용 입력 다이얼로그 텍박
    EditText nickName, position;
    Boolean isSearch = false;

    private AlertDialog alertDialog2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team);
        tActivity = teamActivity.this;
        final Intent intent = getIntent();
        //리더 고유 번호와 팀 번호는 주는 데 왜 Intent하는 거지 이거 수정 예정
        leader_idx = intent.getExtras().getString("leader_idx");
        team_idx = intent.getExtras().getString("team_idx");
        isSearch = intent.getExtras().getBoolean("search");

        //유저 번호, 유저 이름, 토큰 불러오기
        LoadPreference(this);
        //팀 정보 불러오는 메서드
        teamInformation();

        //각종 정의들
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        viewPagerLayout = (LinearLayout)findViewById(R.id.view_pager_layout);
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        teamLogo = (ImageView)findViewById(R.id.team_logo);
        teamBackground = (ImageView)findViewById(R.id.team_background);
        toolbar = (Toolbar)findViewById(R.id.tool_bar_team);
        navigationView = (NavigationView) findViewById(R.id.main_drawer_view);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        method_btn = (Button)findViewById(R.id.team_method_btn);
        drawerLayout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        LinearLayout headerView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.drawer_header, null);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.name1, R.string.name2);
        drawerLayout.setDrawerListener(toggle);
        drawerArrowDrawable = new DrawerArrowDrawable(this);
        drawerArrowDrawable.setColor(Color.WHITE);
        toolbar.setNavigationIcon(drawerArrowDrawable);
        setSupportActionBar(toolbar);
        TextView UserName = (TextView)headerView.findViewById(R.id.username);
        ImageView headerProfile = (ImageView)headerView.findViewById(R.id.user_profile);
        UserName.setText(userName);
        if (userProfileimg != null){
            Glide.with(this).load(userProfileimg).into(headerProfile);
            Glide.get(this).clearMemory();
        }
        profileEdit = (LinearLayout)headerView.findViewById(R.id.header_profile_edit);
        profileEdit.setOnClickListener(this);
        //접었다 폈다 레이아웃 이름설정, 접혔을때 텍스트 색, 펴졌을때 텍스트 색
        collapseToolbar = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        collapseToolbar.setTitle(teamName);
        collapseToolbar.setCollapsedTitleTextColor(Color.WHITE);
        collapseToolbar.setExpandedTitleColor(Color.WHITE);

        navigationView.addHeaderView(headerView);
        //네이비게이션 클릭했을 때
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }

                switch (menuItem.getItemId()) {
                    case R.id.setting: //설정하기 메뉴를 눌렀을 경우
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.log_out:
                        SavePreference("pw", null, getApplicationContext());
                        Intent intent1 = new Intent(teamActivity.this, preLayout.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.fade, R.anim.hold);
                        teamActivity.this.finish();
                        return true;
                    case R.id.matching:
                        Intent intent2 = new Intent(teamActivity.this, matchingSearch.class);
                        intent2.putExtra("up_name",teamName);
                        intent2.putExtra("up_description",teamDescription);
//                        intent2.putExtra("up_regionCode",l)
                        startActivity(intent2);
                        return true;
                    case R.id.notice:
                        Intent intent3 = new Intent(teamActivity.this, notice.class);
                        startActivity(intent3);
                    default:
                        return true;
                }

            }
        });

        method_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog2 = DialogInfo();
                alertDialog2.show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_team, menu);
        menu_setting = menu.findItem(R.id.action_setting);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(toggle.onOptionsItemSelected(item)){
            return true;
        }else if(id == R.id.action_setting){
            Intent intent = new Intent(this, setting.class);
            intent.putExtra("team_idx", team_idx);
            intent.putExtra("nickname", user_nickname);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
        }else{
            super.onBackPressed();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(isSearch){
            finish();
            return true;
        }
        if(keyCode == KeyEvent.KEYCODE_BACK){
            LoadPreference(this);
            Intent intent = new Intent(teamActivity.this, MainPage.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);

    }

    public void teamInformation(){

        new Thread(){
            @Override
            public void run() {
                teamInformationServer();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        teamInformationEnd();

                    }
                });
            }
        }.start();
    }
    private void teamInformationEnd(){

        String result = serverResult;

        int code = 0;
        String msg = "";

        if (result == null){
            return;
        }else{
            Log.d("Result", result);
        }
        try{
            JSONObject json = new JSONObject(result);
            code = json.getInt("code");
            msg = json.getString("message");
            team_idx = json.getString("idx");
            sport_idx = json.getString("sport_idx");
            teamName = json.getString("name");
            collapseToolbar.setTitle(teamName);
            teamDescription = json.getString("description");
            sport_idx = json.getString("sport_idx");
            teamBackgroundUrl = json.getString("coverimg");
            teamLogoUrl = json.getString("profileimg");
            join = json.getString("join");
            user_nickname = json.getString("nickname");
            
        } catch (JSONException e) {
            e.printStackTrace();

        } catch (NullPointerException e){
            e.printStackTrace();

            code = 0;
            msg = "failed";
        }

        if (code == 700){
            Glide.with(teamActivity.this).load(teamBackgroundUrl).into(teamBackground);
            Glide.with(teamActivity.this).load(teamLogoUrl)
//                    .placeholder(R.drawable.circle_emblem).error(R.drawable.circle_emblem).crossFade()
                    .into(teamLogo);
            Glide.get(teamActivity.this).clearMemory();

            setupViewPager(viewPager);
            //탭 레이아웃과 뷰페이져를 같이 사용하는데 탭뷰와 달리 간편하게 메서드 하나로 연결이 가능하다
            tabLayout.setupWithViewPager(viewPager);

            // join이 2면 가입 완료 상태 가입완료 상태만 버튼이 안보여야 한다
            // 설립자인데 0이 나온다. 0은 미가입
            if (Integer.parseInt(join) == 2){
                method_btn.setVisibility(View.GONE);
                CoordinatorLayout.LayoutParams marginControl = (CoordinatorLayout.LayoutParams)viewPagerLayout.getLayoutParams();
                marginControl.bottomMargin = 0;
                viewPagerLayout.setLayoutParams(marginControl);
            }else{
                //미 가입자 설정 버튼 안보이기
                menu_setting.setVisible(false);
            }
        }else{
            alertDialog2 = DialogInit("에러",msg);
            alertDialog2.show();
            Log.d("failed", code + " " + msg);
        }
    }
    private void teamInformationServer(){
        try {
            serverResult = null;
            StringBuffer data = new StringBuffer();
            data.append("mode").append("=").append(5).append("&");
            data.append("type").append("=").append(2).append("&");
            data.append("user_idx").append("=").append(userNo).append("&");
            data.append("team_idx").append("=").append(team_idx).append("&");
            data.append("token").append("=").append(token).append("&");

            URL url = new URL(server);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
//            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(data.toString());
            writer.flush();

            InputStreamReader ims = new InputStreamReader(http.getInputStream(),"UTF-8");
            BufferedReader reader = new BufferedReader(ims);
            StringBuilder builder = new StringBuilder();
            String str;

            while ((str = reader.readLine()) != null){
                builder.append(str);
            }

            serverResult = builder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ConnectException e){
            e.printStackTrace();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    alertDialog2 = DialogInit("인터넷 연결 오류", "인터넷과 연결 되 있지 않습니다 인터넷을 연결 해 주세요.");
                    alertDialog2.show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void teamJoin(){

        new Thread(){
            @Override
            public void run() {
                teamJoinServer();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        teamJoinEnd();

                    }
                });
            }
        }.start();
    }
    private void teamJoinEnd(){

        String result = serverResult;

        int code = 0;
        String msg = "";

        if (result == null){
            return;
        }else{
            Log.d("Result", result);
        }
        try{
            JSONObject json = new JSONObject(result);
            code = json.getInt("code");
            msg = json.getString("message");
//            team_idx = json.getString("idx");
//            sport_idx = json.getString("sport_idx");
//            teamDescription = json.getString("description");
//            sport_idx = json.getString("sport_idx");
//            teamBackgroundUrl = json.getString("coverimg");
//            teamLogoUrl = json.getString("profileimg");
//            join = json.getString("join");

        } catch (JSONException e) {
            e.printStackTrace();

        } catch (NullPointerException e){
            e.printStackTrace();

            code = 0;
            msg = "failed";
        }

        if (code == 700){
            alertDialog2 = DialogInit("가입 성공","성공적으로 팀에 가입되었습니다.");
            alertDialog2.show();
            //가입하기 버튼 없애기
            method_btn.setVisibility(View.GONE);
            CoordinatorLayout.LayoutParams marginControl = (CoordinatorLayout.LayoutParams)viewPagerLayout.getLayoutParams();
            marginControl.bottomMargin = 0;
            viewPagerLayout.setLayoutParams(marginControl);
        }else if(code == 422){
            alertDialog2 = DialogInit("가입오류","이미 가입 된 팀 입니다.");
            alertDialog2.show();
        }else{
            alertDialog2 = DialogInit("에러",msg);
            alertDialog2.show();
            Log.d("failed", code + " " + msg);
        }
    }
    private void teamJoinServer(){
        try {
            serverResult = null;
            StringBuffer data = new StringBuffer();
            data.append("mode").append("=").append(2).append("&");
            data.append("type").append("=").append(2).append("&");
            data.append("user_idx").append("=").append(userNo).append("&");
            data.append("team_idx").append("=").append(team_idx).append("&");
            data.append("token").append("=").append(token).append("&");
            data.append("nickname").append("=").append(nickname).append("&");
            data.append("position").append("=").append(positionS).append("&");


            URL url = new URL(teamJoinServer);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
//            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(data.toString());
            writer.flush();

            InputStreamReader ims = new InputStreamReader(http.getInputStream(),"UTF-8");
            BufferedReader reader = new BufferedReader(ims);
            StringBuilder builder = new StringBuilder();
            String str;

            while ((str = reader.readLine()) != null){
                builder.append(str);
            }

            serverResult = builder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ConnectException e){
            e.printStackTrace();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    alertDialog2 = DialogInit("인터넷 연결 오류", "인터넷과 연결 되 있지 않습니다 인터넷을 연결 해 주세요.");
                    alertDialog2.show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void afterMatching(){

        new Thread(){
            @Override
            public void run() {
                teamJoinServer();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        teamJoinEnd();

                    }
                });
            }
        }.start();
    }
    private void afterMatchingEnd(){

        String result = serverResult;

        int code = 0;
        String msg = "";

        if (result == null){
            return;
        }else{
            Log.d("Result", result);
        }
        try{
            JSONObject json = new JSONObject(result);
            code = json.getInt("code");
            msg = json.getString("message");
//            team_idx = json.getString("idx");
//            sport_idx = json.getString("sport_idx");
//            teamDescription = json.getString("description");
//            sport_idx = json.getString("sport_idx");
//            teamBackgroundUrl = json.getString("coverimg");
//            teamLogoUrl = json.getString("profileimg");
//            join = json.getString("join");

        } catch (JSONException e) {
            e.printStackTrace();

        } catch (NullPointerException e){
            e.printStackTrace();

            code = 0;
            msg = "failed";
        }

        if (code == 700){

            alertDialog2 = DialogInit("가입 성공","성공적으로 팀에 가입되었습니다.");
            alertDialog2.show();
            //가입하기 버튼 없애기
            method_btn.setVisibility(View.GONE);
            CoordinatorLayout.LayoutParams marginControl = (CoordinatorLayout.LayoutParams)viewPagerLayout.getLayoutParams();
            marginControl.bottomMargin = 0;
            viewPagerLayout.setLayoutParams(marginControl);
        }else if(code == 422){
            alertDialog2 = DialogInit("가입오류","이미 가입 된 팀 입니다.");
            alertDialog2.show();
        }else{
            alertDialog2 = DialogInit("에러",msg);
            alertDialog2.show();
            Log.d("failed", code + " " + msg);
        }
    }
    private void afterMatchingServer(){
        try {
            serverResult = null;
            StringBuffer data = new StringBuffer();
            data.append("user_idx").append("=").append(userNo).append("&");
            data.append("token").append("=").append(token).append("&");
            data.append("team_idx").append("=").append(team_idx).append("&");


            URL url = new URL(teamJoinServer);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
//            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(data.toString());
            writer.flush();

            InputStreamReader ims = new InputStreamReader(http.getInputStream(),"UTF-8");
            BufferedReader reader = new BufferedReader(ims);
            StringBuilder builder = new StringBuilder();
            String str;

            while ((str = reader.readLine()) != null){
                builder.append(str);
            }

            serverResult = builder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ConnectException e){
            e.printStackTrace();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    alertDialog2 = DialogInit("인터넷 연결 오류", "인터넷과 연결 되 있지 않습니다 인터넷을 연결 해 주세요.");
                    alertDialog2.show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private AlertDialog DialogInit(String title, String message){
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        if(title.length() > 0) {
            build.setTitle(title);
        }
        build.setMessage(message);
        build.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog2.dismiss();

            }
        });
        return build.create();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new teamInformationFragment(), "정보");
        adapter.addFrag(new scheduleTabFragment(), "일정/결과");
        adapter.addFrag(new playerFragment(), "선수단");
        viewPager.setAdapter(adapter);
    }

    private AlertDialog DialogInfo(){
        final View innerView = getLayoutInflater().inflate(R.layout.team_join,null);
        nickName = (EditText)innerView.findViewById(R.id.team_join_nickname);
        position = (EditText)innerView.findViewById(R.id.team_join_position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("닉네임, 포지션 설정");
        builder.setCancelable(true);
        builder.setView(innerView);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nickname = nickName.getText().toString();
                positionS = position.getText().toString();
                teamJoin();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    public void SavePreference(String key, String value, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void LoadPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userNo = sharedPreferences.getString("userno", null);
        userName = sharedPreferences.getString("name", null);
        token = sharedPreferences.getString("token", null);
        userProfileimg = sharedPreferences.getString("profileimg", null);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.header_profile_edit){
            Intent edit = new Intent(this, updateProfile.class);
            startActivity(edit);
        }else if(v.getId() == R.id.main_create_team_fab){
            Intent intent1 = new Intent(teamActivity.this, createActivity.class);
            intent1.putExtra("userno", userNo);
            intent1.putExtra("token", token);
            startActivity(intent1);
        }
    }
}
