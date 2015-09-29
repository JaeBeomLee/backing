package com.lee.bacving_main.main.soccer;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lee.bacving_main.R;
import com.lee.bacving_main.teamPlayer.teamActivity;
import com.lee.bacving_main.main.total.mainTabFragmentViewHolder;

/**
 * Created by ijaebeom on 2015. 9. 10..
 */
public class mainTabFragmentMainViewHolder2 extends mainTabFragmentViewHolder implements View.OnClickListener {
    ImageView mainBackground, mainTeamLogo;
    TextView mainTeamName;
    Context context;
    public mainTabFragmentMainViewHolder2(View itemView, Context context) {
        super(itemView, context);
        this.context = context;
        mainBackground = (ImageView)itemView.findViewById(R.id.main_background);
        mainTeamLogo = (ImageView)itemView.findViewById(R.id.main_team_logo);
        mainTeamName = (TextView)itemView.findViewById(R.id.main_team_name);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, teamActivity.class);
        context.startActivity(intent);
    }
}

