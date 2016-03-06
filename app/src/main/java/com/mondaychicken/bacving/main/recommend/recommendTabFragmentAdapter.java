package com.mondaychicken.bacving.main.recommend;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mondaychicken.bacving.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ijaebeom on 2015. 9. 10..
 */
public class recommendTabFragmentAdapter extends RecyclerView.Adapter<recommendTabFragmentViewHolder> {

    List<recommendTabFragmentMainData> recommendData = new ArrayList<recommendTabFragmentMainData>();

    Context context;
    public void add(recommendTabFragmentMainData main, int position){
        recommendData.add(position, main);
        notifyItemInserted(position);

    }


    @Override
    public recommendTabFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup viewMain = (ViewGroup)inflater.inflate(R.layout.main_list_view1, parent, false);
        recommendTabFragmentRecommendViewHolder1 mainViewHolder1 = new recommendTabFragmentRecommendViewHolder1(viewMain, context);
        return mainViewHolder1;
    }


    @Override
    public void onBindViewHolder(final recommendTabFragmentViewHolder holder, int position) {
        final recommendTabFragmentMainData main = recommendData.get(position);
        recommendTabFragmentRecommendViewHolder1 mainViewHolder1 = (recommendTabFragmentRecommendViewHolder1)holder;
        mainViewHolder1.mainTeamName.setText(main.getTeamName());
        Glide.with(context).load(main.getMainBackground()).into(mainViewHolder1.mainBackground);
        Glide.with(context).load(main.getTeamLogo()).into(mainViewHolder1.mainTeamLogo);
        Glide.get(context).clearMemory();

    }

    @Override
    public int getItemCount() {
        return recommendData.size();
    }
}
