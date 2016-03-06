package com.mondaychicken.bacving.main.total;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mondaychicken.bacving.R;

public class mainTabFragmentSubViewHoder1 extends mainTabFragmentViewHolder {
    TextView mainNextMatch, mainMatchDateYear, mainMatchDateMonth,mainMatchDateDate,mainMatchDateApm, mainMatchDateHour, mainMatchDateMinute, mainMatchStadium;
    Button mainAddEventButton;
    Context context;

    public mainTabFragmentSubViewHoder1(final View itemView, final Context context) {
        super(itemView, context);
        this.context = context;
        mainNextMatch = (TextView)itemView.findViewById(R.id.main_next_match);
        mainMatchDateYear = (TextView)itemView.findViewById(R.id.main_match_date_year);
        mainMatchDateMonth = (TextView)itemView.findViewById(R.id.main_match_date_month);
        mainMatchDateDate = (TextView)itemView.findViewById(R.id.main_match_date_date);
        mainMatchDateApm= (TextView)itemView.findViewById(R.id.main_match_date_apm);
        mainMatchDateHour = (TextView)itemView.findViewById(R.id.main_match_date_hour);
        mainMatchDateMinute = (TextView)itemView.findViewById(R.id.main_match_date_minute);
        mainMatchStadium = (TextView)itemView.findViewById(R.id.main_match_stadium);
        mainAddEventButton = (Button)itemView.findViewById(R.id.main_add_event_btn);
    }


}
