package com.mondaychicken.bacving.main.soccer;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bacving.lee.bacving_main.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ijaebeom on 2015. 9. 13..
 */
public class mainTabFragment2 extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    int position = 0;

    String mainTeamName;
    Bitmap mainBackground, mainTeamLogo;


    //다이얼로그에 필요한 Bundle과 Context
    Bundle bundle;
    Context context;

    GestureDetectorCompat gestureDetector;

    mainTabFragmentAdapter2 adapter;
    List<mainTabFragmentMainData2> mainList = new ArrayList<mainTabFragmentMainData2>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bundle = savedInstanceState;
        context = container.getContext();

        View v = inflater.inflate(R.layout.main_list,container, false);
        recyclerView = (RecyclerView)v.findViewById(R.id.main_list_recycler);
        manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(position);
        recyclerView.setLayoutManager(manager);
        adapter = new mainTabFragmentAdapter2(mainList);
        recyclerView.setAdapter(adapter);

        mainTeamName = "팀이름";

        for (int i = 0; i < 10; i++) {
            adapter.add(createMainList(), position);

        }
        manager.scrollToPosition(position);
        return v;
    }

    private mainTabFragmentMainData2 createMainList(){
        mainTabFragmentMainData2 main = new mainTabFragmentMainData2();
        //데이터가 없어서 에러납니다
        main.setTeamName(mainTeamName);
        main.setMainBackground(mainBackground);
        main.setTeamLogo(mainTeamLogo);
        return main;
    }




}
