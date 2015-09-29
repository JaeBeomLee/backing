package com.lee.bacving_main.main.searchResult;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.lee.bacving_main.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by ijaebeom on 2015. 9. 15..
 */
public class searchResultActivity extends AppCompatActivity {

    LinearLayoutManager manager;
    List<searchResultInputData> searchInputData = new ArrayList<searchResultInputData>();
    List<searchResultData> searchResultData = new ArrayList<searchResultData>();
    searchResultInputData inputData;
    searchResultAdapter adapter;
    RecyclerView recyclerView;
    Bitmap bit;

    DrawerArrowDrawable drawerArrowDrawable;
    String searchName;
    int position = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_activity);

        Intent intent = getIntent();
        searchName = intent.getStringExtra("searchName");
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar_research);
        recyclerView = (RecyclerView)findViewById(R.id.search_recycler);
        adapter = new searchResultAdapter(searchResultData);
        manager  = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(position);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        toolbar.setTitle(searchName);
        drawerArrowDrawable = new DrawerArrowDrawable(this);
        drawerArrowDrawable.setProgress(1.0f);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(drawerArrowDrawable);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchResultActivity.this.finish();
            }
        });
        //임시입니다
        //searchResultInputData는 나중에 서버에서 받아온 데이터를 저장 하는 곳이며 그 저장하는 데이터를 임시로 표현한 것이다
        inputData= new searchResultInputData();
//        inputData.setSearchResultImage(bitTemp);
        inputData.setSearchResultName("박빙FC");
        inputData.setSearchResult2nd("잠실운동장");
        inputData.setSearchResult3rd("031-274-9002");
//        try {
//            //에러 남
//            bit = Glide.with(this).load("https://upload.wikimedia.org/wikipedia/commons/thumb/4/48/Dell_Logo.svg/200px-Dell_Logo.svg.png").asBitmap().into(100, 100).get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
        inputData.setSearchResultImage(bit);
        //data 객체 하나를 저장해 Array배열 n번에 저장한다.
        searchInputData.add(inputData);

        for (int i = 0; i<=20; i++){
            adapter.add(createResultList(),position);
        }
        manager.scrollToPosition(position);
    }

    public searchResultData createResultList(){
        //실질적인 데이터가 저장되는 곳이다.
        searchResultData resultData = new searchResultData();
        //Array에서 0번에 저장되어 있는 searchResultInputData 객체를 꺼내 객체속 데이터를 입력해 넣어준다
        resultData.setSearchResultImage(searchInputData.get(0).getSearchResultImage());
        resultData.setSearchResultName(searchInputData.get(0).getSearchResultName());
        resultData.setSearchResult2nd(searchInputData.get(0).getSearchResult2nd());
        resultData.setSearchResult3rd(searchInputData.get(0).getSearchResult3rd());
        return resultData;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.action_search){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);

    }
}
