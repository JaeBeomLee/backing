package com.mondaychicken.bacving.matching;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mondaychicken.bacving.R;

/**
 * Created by ijaebeom on 2015. 9. 10..
 */
public class matchingFragmentRegisterViewHolder extends matchingFragmentViewHolder implements View.OnClickListener {
    Context context;
    int size;
    public matchingFragmentRegisterViewHolder(View itemView, Context context, int size) {
        super(itemView, context);
        this.context = context;
        this.size = size;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d("num", String.valueOf(getLayoutPosition()));
        if (getLayoutPosition() == size-1){
            Intent intent = new Intent(context, matchingTeamRegister.class);
            intent.putExtra("sport_idx", matchingSearchResultActivity.sport_idx);
            intent.putExtra("match_type",matchingSearchResultActivity.matchType);
            intent.putExtra("location", matchingSearchResultActivity.location);
            intent.putExtra("team_idx", matchingSearchResultActivity.team_idx);
            intent.putExtra("time", matchingSearchResultActivity.time);
            intent.putExtra("date", matchingSearchResultActivity.date);
            intent.putExtra("month", matchingSearchResultActivity.month);
            intent.putExtra("year", matchingSearchResultActivity.year);
            intent.putExtra("team_name", matchingSearchResultActivity.teamName);
            context.startActivity(intent);
        }
    }
}

