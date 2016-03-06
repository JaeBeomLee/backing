package com.mondaychicken.bacving.matching;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mondaychicken.bacving.R;
import com.mondaychicken.bacving.main.total.mainTabFragmentViewHolder;
import com.mondaychicken.bacving.teamPlayer.teamActivity;

/**
 * Created by ijaebeom on 2015. 9. 10..
 */
public class matchingFragmentItemViewHolder extends matchingFragmentViewHolder implements View.OnClickListener {
    ImageView logo;
    TextView name, time, place, age;
    Context context;
    public matchingFragmentItemViewHolder(View itemView, Context context) {
        super(itemView, context);
        this.context = context;
        logo = (ImageView)itemView.findViewById(R.id.matching_search_result_item_logo);
        name = (TextView)itemView.findViewById(R.id.matching_search_result_item_name);
        time = (TextView)itemView.findViewById(R.id.matching_search_result_item_time);
        place = (TextView)itemView.findViewById(R.id.matching_search_result_item_place);
        age = (TextView)itemView.findViewById(R.id.matching_search_result_item_age);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, matchingSearchResultItemActivity.class);
        intent.putExtra("match_idx", matchingSearchResultActivity.waitMatch_idx[getLayoutPosition()]);
        intent.putExtra("teamName", matchingSearchResultActivity.waitTeamName[getLayoutPosition()]);
        intent.putExtra("region_text", matchingSearchResultActivity.waitStadiumAd[getLayoutPosition()]);
        intent.putExtra("type", matchingSearchResultActivity.matchType);
        intent.putExtra("time", matchingSearchResultActivity.waitTime[getLayoutPosition()]);
        intent.putExtra("lat", matchingSearchResultActivity.latitude);
        intent.putExtra("lng", matchingSearchResultActivity.longitude);
        intent.putExtra("team_idx", matchingSearchResultActivity.team_idx);

//        type = intent.getExtras().getString("type");
//        w_idx = intent.getExtras().getString("w_idx");
//        sport_idx = intent.getExtras().getString("sport_idx");
//        stadium_idx = intent.getExtras().getString("stadium_idx");
//        stadium_text = intent.getExtras().getString("stadium_text");
//        region_code = intent.getExtras().getString("region_code");
//        longitude = intent.getExtras().getString("lng");
//        latitude = intent.getExtras().getString("lat");
//        meet_date = intent.getExtras().getString("meet_date");
                context.startActivity(intent);
    }
}

