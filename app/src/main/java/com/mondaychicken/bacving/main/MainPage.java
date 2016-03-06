package com.mondaychicken.bacving.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.mondaychicken.bacving.Login.preLayout;
import com.mondaychicken.bacving.R;
import com.mondaychicken.bacving.main.alertList.alertAdapter;
import com.mondaychicken.bacving.main.alertList.alertData;
import com.mondaychicken.bacving.main.alertList.alertDivider;
import com.mondaychicken.bacving.main.etc.notice;
import com.mondaychicken.bacving.main.etc.setting;
import com.mondaychicken.bacving.main.etc.updateProfile;
import com.mondaychicken.bacving.main.recommend.recommendTabFragment;
import com.mondaychicken.bacving.main.searchResult.searchResultActivity;
import com.mondaychicken.bacving.main.total.mainTabFragment;
import com.mondaychicken.bacving.main.total.mainViewPagerAdapter;
import com.mondaychicken.bacving.matching.matchingSearch;
import com.mondaychicken.bacving.pushManager;
import com.mondaychicken.bacving.teamPlayer.change.createActivity;


/**
 * Created by ijaebeom on 2015. 9. 4..
 */
public class MainPage extends AppCompatActivity implements DrawerLayout.DrawerListener, NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    NavigationView navigationView, navigationView2;
    Menu navMenu;
    ActionBarDrawerToggle toggle, toggle2;
    DrawerLayout drawerLayout;
    DrawerArrowDrawable drawerArrowDrawable;
    ViewPager headerPager, tabPager;
    TabLayout tabLayout;
    MenuItem searchMenu;
    String userEmail= null, userPw= null, userProfileimg= null, userStatue= null, userGrade= null;
    public String userno = null, userToken= null, userName= null;
    LinearLayout header;
    TextView headerName;
    ImageView headerProfile;
    LinearLayout profileEdit;
    FloatingActionButton fab;
    private AlertDialog alertDialog = null;

    RecyclerView recyclerView;
    alertAdapter adapter;
    alertDivider divider;
    LinearLayoutManager manager;
    int position = 0;
    boolean isAlert = false;

    //외부에서 이 엑티비티를 종료하기 위해 사용
    public static Activity MainPage;
    final int log_out = Menu.FIRST;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        MainPage = this;

        LoadPreference(this);
        Intent intent = getIntent();
        userToken = intent.getStringExtra("token");
        isAlert = intent.getBooleanExtra("alert",false);
        String isDeleted = intent.getStringExtra("isDeleted");

        if(isDeleted != null){
            alertDialog = DialogInit("삭제 성공","팀이 성공적으로 삭제되었습니다.");
            alertDialog.show();
        }

        // 메인 페이지는 refresh를 위해 팀 페이지 들어가면 종료 시켜 버림. 그래서 다시 실행하면 token값이 사라짐 그래서 token값이 null이 나올 가능성이 있음.
        if (userToken!= null){
            //토큰은 여기서 저장
            SavePreference("token", userToken, getApplicationContext());
        }else if (userToken == null){
            LoadPreference(this);
        }
        //액션바 정의, 수정
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(0xffffffff);
        //네비게이션 프로필 나오는 부분 아 밑은 전부 네비게이션 부분
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawerArrowDrawable = new DrawerArrowDrawable(this);
        //액션바에서 네비게이션 열었다 닫았다를 등록하는 버튼 객체 생성
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.name1, R.string.name2);
        toggle2 = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.name3, R.string.name4);
        //네비게이션
        navigationView = (NavigationView) findViewById(R.id.main_drawer_view);
        navigationView2 = (NavigationView)findViewById(R.id.main_drawer_view2);
        //네비게이션 메뉴 불러와 저장하기
        navMenu = navigationView.getMenu();
        //툴바와 액션바를 연결 해주는 메서드
        setSupportActionBar(toolbar);
        drawerArrowDrawable.setColor(Color.WHITE);
        //네비게이션 버튼을 화살표 Drawable 아이콘으로 사용
        toolbar.setNavigationIcon(drawerArrowDrawable);
        drawerLayout.setDrawerListener(this);
        //이걸 해야 백버튼 눌렀을때 네비게이션이 닫힘은 SearchView 먹통의 원인
//        drawerLayout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
//        네비게이션 헤더
        header = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.drawer_header, null);
        //네비게이션 헤더 이름
        headerName = (TextView)header.findViewById(R.id.username);
        //네비게이션 헤더 프로필 이미지
        headerProfile = (ImageView)header.findViewById(R.id.user_profile);
        profileEdit = (LinearLayout)header.findViewById(R.id.header_profile_edit);
        profileEdit.setOnClickListener(this);
        //광고 내용이 들어가는 뷰페이저
        headerPager = (ViewPager)findViewById(R.id.header_pager);
        //팀 리스트 뷰페이져
        tabPager = (ViewPager)findViewById(R.id.tab_pager);
        tabLayout = (TabLayout)findViewById(R.id.tab_sports);
//        우측 하단의 둥근 버튼
        fab = (FloatingActionButton)findViewById(R.id.main_create_team_fab);
        // 둥근 버튼 클릭 이벤트
        fab.setOnClickListener(this);
        //광고 뷰페이져 설정 메서드
        setupHeaderPager(headerPager);
        //팀 리스트 뷰페이져 설정 메서드
        setupViewPager(tabPager);
        tabLayout.setupWithViewPager(tabPager);
        // 프로필 이름 설정
        headerName.setText(userName);
        //프로필 이미지 주소가 null이 아니면 이미지 추가
        if (userProfileimg != null){
            Glide.with(this).load(userProfileimg).into(headerProfile);
            Glide.get(this).clearMemory();
        }
        //네비게이션에 헤더뷰 연결
        navigationView.addHeaderView(header);
//        navMenu.add(0,log_out, Menu.NONE, "로그아웃").setIcon(R.drawable.ic_exit);
        //네이비게이션 메뉴 클릭했을때
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = (RecyclerView)findViewById(R.id.alert_list);
        adapter = new alertAdapter();
        divider = new alertDivider(this,LinearLayoutManager.VERTICAL);
        manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(position);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(divider);
        
//        final pushManager push = new pushManager(this,"push", null, 1);
//        String pushContent[][];
//        pushContent = push.select();
//
//        if (pushContent != null){
//            for (int i = 0; i< pushContent.length; i++){
//                adapter.add(createMainList(pushContent[i][1], pushContent[i][2], pushContent[i][3]), 0);
//            }
//        }



    }

    private void setupHeaderPager(ViewPager viewPager) {
        headerPagerAdater adapter = new headerPagerAdater(getSupportFragmentManager());
        adapter.addFlag(new headerFragment1());
        adapter.addFlag(new headerFragment1());
        adapter.addFlag(new headerFragment1());
        viewPager.setAdapter(adapter);
    }

    private void setupViewPager(ViewPager viewPager) {
        mainViewPagerAdapter adapter = new mainViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new mainTabFragment(), "전체");
        adapter.addFrag(new recommendTabFragment(), "추천");
        adapter.addFrag(new mainTabFragment(), "축구");
        adapter.addFrag(new mainTabFragment(), "농구");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchMenu = menu.findItem(R.id.action_search);
        final SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setTextColor(Color.WHITE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainPage.this, searchResultActivity.class);
                intent.putExtra("searchName", query);
                startActivity(intent);
                overridePendingTransition(R.anim.push_down_in, R.anim.hold);
                searchView.clearFocus();
                searchView.onActionViewCollapsed();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }


    //finish 함수 재정ㅇ의
    @Override
    public void finish() {
        super.finish();
        MainPage = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(toggle.onOptionsItemSelected(item)){
            return true;
        }else if (id == R.id.action_alert){
            if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            } else {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        }else if(id == R.id.action_search){
            return true;
        }

        return false;
    }


    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerArrowDrawable.setProgress(slideOffset);
        }
    }
    @Override
    public void onDrawerOpened(View drawerView) {}
    @Override
    public void onDrawerClosed(View drawerView) {}
    @Override
    public void onDrawerStateChanged(int newState) {}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            finish();
        }
        return super.onKeyDown(keyCode, event);

    }

    //원래는 잘 안쓰는데 이걸 써야 네비게이션이 백버튼 눌렀을때 닫힌다
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
        }else{
            super.onBackPressed();
        }
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        if (menuItem.isChecked()) {
            menuItem.setChecked(false);
        } else {
            menuItem.setChecked(true);
        }

        switch (menuItem.getItemId()) {
            case R.id.setting: //설정하기 메뉴를 눌렀을 경우
                Intent Setting = new Intent(MainPage.this, setting.class);
                startActivity(Setting);
                drawerLayout.closeDrawers();
                return true;
            case R.id.log_out:
                SavePreference("pw", null, getApplicationContext());
                Intent log_out = new Intent(MainPage.this, preLayout.class);
                startActivity(log_out);
                overridePendingTransition(R.anim.fade, R.anim.hold);
                drawerLayout.closeDrawers();
                MainPage.this.finish();
                return true;
            case R.id.matching:
                Intent matching = new Intent(MainPage.this, matchingSearch.class);
                startActivity(matching);
                drawerLayout.closeDrawers();
                return true;
            case R.id.notice:
                Intent notice = new Intent(MainPage.this, notice.class);
                startActivity(notice);
                drawerLayout.closeDrawers();
            default:
                return true;
        }

    }

    //일반 다이얼로그 생성
    private AlertDialog DialogInit(String title, String message){
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        if(title.length() > 0) {
            build.setTitle(title);
        }
        build.setMessage(message);
        build.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        return build.create();
    }

    private alertData createMainList(String title, String description, String profile){
        alertData data = new alertData();
        data.setTitle(title);
        data.setDescription(description);
        data.setProfileimg(profile);
        return data;
    }

    //SharedPreference를 이용하여 저장하는 메서드
    public void SavePreference(String key, String value, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    //SharedPreference를 이용하여 저장된 데이터를 불러오는 메서드
    private void LoadPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userno = sharedPreferences.getString("userno", null);
        userName = sharedPreferences.getString("name", null);
        userEmail = sharedPreferences.getString("email", null);
        userPw = sharedPreferences.getString("pw", null);
        userProfileimg = sharedPreferences.getString("profileimg", null);
        userStatue = sharedPreferences.getString("statue", null);
        userGrade = sharedPreferences.getString("grade", null);
        userToken= sharedPreferences.getString("token", null);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.header_profile_edit){
            Intent edit = new Intent(this, updateProfile.class);
            startActivity(edit);
        }else if(v.getId() == R.id.main_create_team_fab){
            Intent intent1 = new Intent(MainPage.this, createActivity.class);
            intent1.putExtra("userno", userno);
            intent1.putExtra("token", userToken);
            startActivity(intent1);
        }
    }
}
