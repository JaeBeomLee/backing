package com.mondaychicken.bacving.teamPlayer.schedule;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mondaychicken.bacving.R;

/**
 * Created by ijaebeom on 2015. 9. 10..
 */
public class scheduleTabFragmentMainViewHolder1 extends scheduleTabFragmentViewHolder implements View.OnClickListener{
    Context context;
    TextView scheduleYear, scheduleMonth, scheduleDate, scheduleApm, scheduleHour, scheduleMinute, scheduleStadium, scheduleDateViual, scheduleDayVisual, scheduleSubMonth, scheduleScore, scheduleResult;
    public scheduleTabFragmentMainViewHolder1(View itemView, Context context) {
        super(itemView, context);
        this.context = context;
        scheduleYear = (TextView)itemView.findViewById(R.id.schedule_year);
        scheduleMonth = (TextView)itemView.findViewById(R.id.schedule_month);
        scheduleDate = (TextView)itemView.findViewById(R.id.schedule_date);
        scheduleApm = (TextView)itemView.findViewById(R.id.schedule_apm);
        scheduleHour = (TextView)itemView.findViewById(R.id.schedule_hour);
        scheduleMinute = (TextView)itemView.findViewById(R.id.schedule_minute);
        scheduleStadium = (TextView)itemView.findViewById(R.id.schedule_stadium);
        scheduleDateViual = (TextView)itemView.findViewById(R.id.schedule_date_visual);
        scheduleDayVisual = (TextView)itemView.findViewById(R.id.schedule_day_visual);
        scheduleScore = (TextView)itemView.findViewById(R.id.schedule_score);
        scheduleResult = (TextView)itemView.findViewById(R.id.schedule_result);

        scheduleSubMonth = (TextView)itemView.findViewById(R.id.schedule_sub_month);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}

