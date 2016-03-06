package com.mondaychicken.bacving.teamPlayer.setting;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mondaychicken.bacving.R;

public class settingHeaderViewHoder extends settingViewHolder{
    TextView header;
    Context context;
    public settingHeaderViewHoder(final View itemView, Context context) {
        super(itemView, context);
        this.context = context;
        header = (TextView)itemView.findViewById(R.id.setting_header_item);
    }

}
