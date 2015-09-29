package com.lee.bacving_main.main;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lee.bacving_main.Login.preLayout;
import com.lee.bacving_main.R;
import com.lee.bacving_main.main.searchResult.searchResultActivity;
import com.lee.bacving_main.main.soccer.mainTabFragment2;
import com.lee.bacving_main.main.total.mainTabFragment;
import com.lee.bacving_main.main.total.mainViewPagerAdapter;


/**
 * Created by ijaebeom on 2015. 9. 4..
 */
public class MainPage extends AppCompatActivity implements DrawerLayout.DrawerListener {

    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    DrawerArrowDrawable drawerArrowDrawable;
    ViewPager headerPager, tabPager;
    TabLayout tabLayout;
    SearchView searchView;
    MenuItem searchMenu;
    String userno = null, userEmail= null, userPw= null, userName= null, userProfileimg= null, userStatus= null, userGrade= null, userToken= null;
    LinearLayout header;
    TextView headerName;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        Intent intent = getIntent();
        userno = intent.getStringExtra("userno");
        userEmail = intent.getStringExtra("email");
        userPw = intent.getStringExtra("pw");
        userName = intent.getStringExtra("name");
        userProfileimg = intent.getStringExtra("profileimg");

        userStatus = intent.getStringExtra("status");
        userGrade = intent.getStringExtra("grade");
        userToken = intent.getStringExtra("token");

        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(0xffffffff);
        LinearLayout headerView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.drawer_header, null);
        TextView UserName = (TextView)headerView.findViewById(R.id.username);
        UserName.setText(userName);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.name1, R.string.name2);
        navigationView = (NavigationView) findViewById(R.id.main_drawer_view);
        header = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.drawer_header, null);
        headerName = (TextView)header.findViewById(R.id.username);
        headerPager = (ViewPager)findViewById(R.id.header_pager);
        tabPager = (ViewPager)findViewById(R.id.tab_pager);
        tabLayout = (TabLayout)findViewById(R.id.tab_sports);
        drawerArrowDrawable = new DrawerArrowDrawable(this);


        setSupportActionBar(toolbar);
        drawerLayout.setDrawerListener(toggle);
        toolbar.setNavigationIcon(drawerArrowDrawable);
        drawerLayout.setDrawerListener(this);
        setupHeaderPager(headerPager);
        setupViewPager(tabPager);
        tabLayout.setupWithViewPager(tabPager);
        headerName.setText(userName);
        navigationView.addHeaderView(headerView);

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
                        Intent intent1 = new Intent(MainPage.this, preLayout.class);
                        startActivity(intent1);
                        overridePendingTransition(R.anim.fade, R.anim.hold);
                        MainPage.this.finish();
                    default:
                        return true;
                }

            }
        });

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
        adapter.addFrag(new mainTabFragment2(), "추천 ");
        adapter.addFrag(new mainTabFragment2(), "야구");
        adapter.addFrag(new mainTabFragment2(), "축구");
        adapter.addFrag(new mainTabFragment2(), "농구");
        viewPager.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchMenu = menu.findItem(R.id.action_search);
        final SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setAlpha(1.0f);
        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setTextColor(Color.WHITE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

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



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(toggle.onOptionsItemSelected(item)){
            return true;
        }else if(id == R.id.action_search){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        drawerArrowDrawable.setProgress(slideOffset);
    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            finish();
        }
        return super.onKeyDown(keyCode, event);

    }

    public void SavePreference(String key, String value, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
