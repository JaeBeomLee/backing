package com.mondaychicken.bacving.teamPlayer.schedule;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mondaychicken.bacving.R;

public class scheduleTabFragmentSubViewHoder1 extends scheduleTabFragmentViewHolder {
    Context context;
    TextView scheduleSubMonth;
    public scheduleTabFragmentSubViewHoder1(final View itemView, final Context context) {
        super(itemView, context);
        scheduleSubMonth = (TextView)itemView.findViewById(R.id.schedule_sub_month);
        this.context = context;

    }


}
