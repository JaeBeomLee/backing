package com.mondaychicken.bacving.main.recommend;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mondaychicken.bacving.R;
import com.mondaychicken.bacving.main.MainPage;
import com.mondaychicken.bacving.teamPlayer.teamActivity;

/**
 * Created by ijaebeom on 2015. 9. 10..
 */
public class recommendTabFragmentRecommendViewHolder1 extends recommendTabFragmentViewHolder implements View.OnClickListener{
    ImageView mainBackground, mainTeamLogo;
    TextView mainTeamName;
    Context context;
    public recommendTabFragmentRecommendViewHolder1(View itemView, Context context) {
        super(itemView, context);
        this.context = context;
        mainBackground = (ImageView)itemView.findViewById(R.id.main_background);
        mainTeamLogo = (ImageView)itemView.findViewById(R.id.main_team_logo);
        mainTeamName = (TextView)itemView.findViewById(R.id.main_team_name);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 이 리스트 하나를 클릭헸을때 반응하는 이벤트
        Intent intent = new Intent(context, teamActivity.class);
        intent.putExtra("leader_idx", recommendTabFragment.leader_idx[getLayoutPosition()]);
        intent.putExtra("team_idx", recommendTabFragment.team_idx[getLayoutPosition()]);
        context.startActivity(intent);
        MainPage.MainPage.finish();
        Log.d("touch","num : " +getLayoutPosition());
    }
}

