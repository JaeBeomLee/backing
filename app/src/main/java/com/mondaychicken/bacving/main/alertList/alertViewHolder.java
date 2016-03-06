package com.mondaychicken.bacving.main.alertList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mondaychicken.bacving.R;

/**
 * Created by ijaebeom on 2015. 9. 10..
 */
public class alertViewHolder extends RecyclerView.ViewHolder {
    Context context;
    TextView description;
    ImageView profile;
    public alertViewHolder(View itemView, Context context) {
        super(itemView);
        description = (TextView)itemView.findViewById(R.id.alert_description);
        profile = (ImageView)itemView.findViewById(R.id.alert_profile);
        this.context = context;

    }


}
