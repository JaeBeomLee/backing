package com.mondaychicken.bacving.teamPlayer.schedule;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mondaychicken.bacving.R;

import java.util.List;

/**
 * Created by ijaebeom on 2015. 9. 10..
 */
public class scheduleTabFragmentAdapter extends RecyclerView.Adapter<scheduleTabFragmentViewHolder> {

    List<scheduleTabFragmentMainData> mainData;


    public scheduleTabFragmentAdapter(List<scheduleTabFragmentMainData> mainData){
        this.mainData = mainData;

    }

    public void add(scheduleTabFragmentMainData main, int position){
        mainData.add(position, main);
        notifyItemInserted(position);

    }


    @Override
    public scheduleTabFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewGroup viewMain = (ViewGroup)inflater.inflate(R.layout.schedule_main, parent, false);
        scheduleTabFragmentMainViewHolder1 mainViewHolder1 = new scheduleTabFragmentMainViewHolder1(viewMain, parent.getContext());
        return mainViewHolder1;


    }


    @Override
    public void onBindViewHolder(final scheduleTabFragmentViewHolder holder, int position) {
        final scheduleTabFragmentMainData main = mainData.get(position);

        scheduleTabFragmentMainViewHolder1 mainViewHolder1 = (scheduleTabFragmentMainViewHolder1)holder;
        mainViewHolder1.scheduleYear.setText(main.getscheduleYear());
        mainViewHolder1.scheduleMonth.setText(main.getscheduleMonth());
        mainViewHolder1.scheduleDate.setText(main.getscheduleDate());
        mainViewHolder1.scheduleApm.setText(main.getscheduleApm());
        mainViewHolder1.scheduleHour.setText(main.getscheduleHour());
        mainViewHolder1.scheduleMinute.setText(main.getscheduleMinute());
        mainViewHolder1.scheduleStadium.setText(main.getscheduleStadium());
        mainViewHolder1.scheduleDateViual.setText(main.getScheduleDateVisual());
        mainViewHolder1.scheduleDayVisual.setText(main.getScheduleDayVisual());
        mainViewHolder1.scheduleScore.setText(main.getScheduleScore());
        mainViewHolder1.scheduleResult.setText(main.getScheduleResult());

        mainViewHolder1.scheduleSubMonth.setText(main.getScheduleSubMonth());
        if (main.getScheduleSubMonth() == null){
            mainViewHolder1.scheduleSubMonth.setVisibility(View.GONE);
        }else if (main.getScheduleResult() != null){
            mainViewHolder1.scheduleSubMonth.setVisibility(View.VISIBLE);
        }

        switch (main.getScheduleResult()){
            case "WIN" :
                mainViewHolder1.scheduleResult.setTextColor(0x18c37c);
                break;
            case "LOSE" :
                mainViewHolder1.scheduleResult.setTextColor(0xef5350);
                break;
            case "DRAW" :
                mainViewHolder1.scheduleResult.setTextColor(0x9c9c9c);
        }


    }


    @Override
    public int getItemCount() {
        return mainData.size();
    }
}
