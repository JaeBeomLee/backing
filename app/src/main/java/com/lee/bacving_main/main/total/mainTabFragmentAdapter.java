package com.lee.bacving_main.main.total;

import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.lee.bacving_main.R;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by ijaebeom on 2015. 9. 10..
 */
public class mainTabFragmentAdapter extends RecyclerView.Adapter<mainTabFragmentViewHolder> {

    List<mainTabFragmentMainData> mainData;

    private static final int MAINHOLDER = 0;
    private static final int SUBHOLDER = 1;

    public mainTabFragmentAdapter(List<mainTabFragmentMainData> mainData){
        this.mainData = mainData;

    }

    public void add(mainTabFragmentMainData main, int position){
        mainData.add(position, main);
        notifyItemInserted(position);

    }


    @Override
    public mainTabFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case MAINHOLDER :
                ViewGroup viewMain = (ViewGroup)inflater.inflate(R.layout.main_list_view1, parent, false);
                mainTabFragmentMainViewHolder1 mainViewHolder1 = new mainTabFragmentMainViewHolder1(viewMain, parent.getContext());
                return mainViewHolder1;
            case SUBHOLDER :
                ViewGroup viewSub = (ViewGroup)inflater.inflate(R.layout.main_list_view2, parent, false);
                mainTabFragmentSubViewHoder1 subViewHoder1 = new mainTabFragmentSubViewHoder1(viewSub, parent.getContext());
                return subViewHoder1;
            default:
                ViewGroup viewSub0 = (ViewGroup)inflater.inflate(R.layout.main_list_view2, parent, false);
                mainTabFragmentSubViewHoder1 subViewHoder0 = new mainTabFragmentSubViewHoder1(viewSub0, parent.getContext());
                return subViewHoder0;
        }
    }


    @Override
    public void onBindViewHolder(final mainTabFragmentViewHolder holder, int position) {
        final mainTabFragmentMainData main = mainData.get(position);

        switch (holder.getItemViewType()){
            case MAINHOLDER:
                mainTabFragmentMainViewHolder1 mainViewHolder1 = (mainTabFragmentMainViewHolder1)holder;
                mainViewHolder1.mainTeamName.setText(main.getTeamName());
//                mainViewHolder1.mainTeamLogo.setImageBitmap(main.getTeamLogo());
//                mainViewHolder1.mainBackground.setImageBitmap(main.getMainBackground());
                break;
            case SUBHOLDER:
                mainTabFragmentSubViewHoder1 subViewHoder1 = (mainTabFragmentSubViewHoder1)holder;
                subViewHoder1.mainNextMatch.setText(main.getNextMatch());
                subViewHoder1.mainMatchDateYear.setText(main.getMatchDataYear() + "년");
                subViewHoder1.mainMatchDateMonth.setText(main.getMatchDataMonth() + "월");
                subViewHoder1.mainMatchDateDate.setText(main.getMatchDataDate() + "일");
                subViewHoder1.mainMatchDateApm.setText(main.getMatchDataApm());
                subViewHoder1.mainMatchDateHour.setText(main.getMatchDataHour());
                subViewHoder1.mainMatchDateMinute.setText(main.getMatchDataMinute());
                subViewHoder1.mainMatchStadium.setText(main.getMatchStadium());
                subViewHoder1.mainAddEventButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("touch", "touched");

                        int year, month, date, hour, minute, Apm = 0;
                        String stadium = main.matchStadium;
                        if (main.getMatchDataApm() == "오전") {
                            Apm = 0;
                        } else if (main.getMatchDataApm() == "오후") {
                            Apm = 1;
                        }
                        String Title = main.getNextMatch();
                        year = Integer.parseInt(main.getMatchDataYear());
                        month = Integer.parseInt(main.getMatchDataMonth());
                        date = Integer.parseInt(main.getMatchDataDate());
                        hour = Integer.parseInt(main.getMatchDataHour());
                        minute = Integer.parseInt(main.getMatchDataMinute());
                        Calendar begincalendar = Calendar.getInstance();
                        begincalendar.set(Calendar.YEAR, year);
                        begincalendar.set(Calendar.MONTH, month);
                        begincalendar.set(Calendar.DATE, date);
                        begincalendar.set(Calendar.AM_PM, Apm);
                        begincalendar.set(Calendar.HOUR_OF_DAY, hour);
                        begincalendar.set(Calendar.MINUTE, minute);


                        Calendar endcalendar = Calendar.getInstance();
                        endcalendar.set(Calendar.YEAR, year);
                        endcalendar.set(Calendar.MONTH, month);
                        endcalendar.set(Calendar.DATE, date);
                        endcalendar.set(Calendar.AM_PM, Apm);
                        endcalendar.set(Calendar.HOUR_OF_DAY, hour+2);
                        endcalendar.set(Calendar.MINUTE, minute);

                        Intent intent = new Intent(Intent.ACTION_EDIT);
                        intent.setType("vnd.android.cursor.item/event");
                        intent.putExtra("title", Title);
                        intent.putExtra("beginTime", begincalendar.getTimeInMillis());
                        intent.putExtra("endTime", endcalendar.getTimeInMillis());
                        intent.putExtra("eventLocation", stadium);

                        ((mainTabFragmentSubViewHoder1) holder).context.startActivity(intent);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if (position == 0){
            viewType = SUBHOLDER;
        }else {
            viewType = MAINHOLDER;
        }
        return viewType;
    }


    @Override
    public int getItemCount() {
        return mainData.size();
    }
}
