package com.mondaychicken.bacving.teamPlayer.playerList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mondaychicken.bacving.R;

/**
 * Created by ijaebeom on 2015. 9. 10..
 */
public class playerFragmentViewHolder extends RecyclerView.ViewHolder {
    Context context;
    TextView name, nickname;
    ImageView profile;
    public playerFragmentViewHolder(View itemView, Context context) {
        super(itemView);
        name = (TextView)itemView.findViewById(R.id.team_member_name);
        nickname = (TextView)itemView.findViewById(R.id.team_member_nickname);
        profile = (ImageView)itemView.findViewById(R.id.team_member_profile);
        this.context = context;

    }


}
