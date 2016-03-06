package com.mondaychicken.bacving.teamPlayer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import bacving.lee.bacving_main.R;

/**
 * Created by ijaebeom on 2015. 8. 18..
 */
public class playerListHolder extends RecyclerView.ViewHolder {
    TextView profileName;
    ImageView profileImage;

    public playerListHolder(View itemView) {
        super(itemView);
        profileName = (TextView)itemView.findViewById(R.id.profile_name);
        profileImage = (ImageView)itemView.findViewById(R.id.profile_img);
    }
}
