package com.mondaychicken.bacving.main.total;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import bacving.lee.bacving_main.R;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by ijaebeom on 2015. 9. 13..
 */

public class mainTabFragment extends Fragment{
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    int position = 0;

    String mainTeamName;
    Bitmap mainBackground, mainTeamLogo;
    static String backgroundUrl = "http://aws.bacving.com/api/temp_images/barcelona.jpg";
    static String logoUrl = "http://aws.bacving.com/api/temp_images/chelsea_logo.png";

    String subLeague, subApm, subHour, subMinute, subDate, subMonth, subYear, subNext,subStadium;
    //다이얼로그에 필요한 Bundle과 Context
    Bundle bundle;
    Context context;
    Handler handler;

    mainTabFragmentAdapter adapter;
    List<mainTabFragmentMainData> mainList = new ArrayList<mainTabFragmentMainData>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bundle = savedInstanceState;
        context = container.getContext();

        final View v = inflater.inflate(R.layout.main_list,container, false);
        recyclerView = (RecyclerView)v.findViewById(R.id.main_list_recycler);
        manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(position);
        recyclerView.setLayoutManager(manager);
        adapter = new mainTabFragmentAdapter(mainList);
        recyclerView.setAdapter(adapter);
        handler = new Handler();
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    mainBackground = Glide.with(mainTabFragment.this).load(backgroundUrl).asBitmap().into(1024, 512).get();
                    mainTeamLogo = Glide.with(mainTabFragment.this).load(logoUrl).asBitmap().into(500, 500).get();
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
                mainTeamName = "팀이름";
                subLeague = "major";
                subDate = "11";
                subMonth = "6";
                subYear = "2015";
                subApm = "오전";
                subHour = "6";
                subMinute = "30";
                subStadium = "잠실야구장";
                subNext = "수원FC";



                for (int i = 0; i < 10; i++) {
                    adapter.add(createMainList(), position);

                }
                manager.scrollToPosition(position);
            }
        }.execute();


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();



    }

    private mainTabFragmentMainData createMainList(){
        mainTabFragmentMainData main = new mainTabFragmentMainData();

        main.setTeamName(mainTeamName);
        main.setMainBackground(mainBackground);
        main.setTeamLogo(mainTeamLogo);
        main.setMatchDataApm(subApm);
        main.setMatchDataHour(subHour);
        main.setMatchDataMinute(subMinute);
        main.setMatchDataDate(subDate);
        main.setMatchDataMonth(subMonth);
        main.setMatchDataYear(subYear);
        main.setMatchStadium(subStadium);
        main.setNextMatch(subNext);
        return main;
    }

}