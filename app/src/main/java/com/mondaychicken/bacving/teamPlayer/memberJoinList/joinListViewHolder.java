package com.mondaychicken.bacving.teamPlayer.memberJoinList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mondaychicken.bacving.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ijaebeom on 2015. 9. 10..
 */
public class joinListViewHolder extends RecyclerView.ViewHolder {
    Context context;
    TextView name;
    Button accept, reject;
    CircleImageView profile;
    public joinListViewHolder(View itemView, Context context) {
        super(itemView);
        name = (TextView)itemView.findViewById(R.id.team_member_join_list_name);
        profile = (CircleImageView)itemView.findViewById(R.id.team_member_join_list_profile);
        accept = (Button)itemView.findViewById(R.id.team_member_join_list_accept_btn);
        reject = (Button)itemView.findViewById(R.id.team_member_join_list_reject_btn);
        this.context = context;

    }


}
