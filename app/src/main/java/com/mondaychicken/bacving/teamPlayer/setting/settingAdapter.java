package com.mondaychicken.bacving.teamPlayer.setting;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mondaychicken.bacving.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ijaebeom on 2015. 9. 10..
 */
public class settingAdapter extends RecyclerView.Adapter<settingViewHolder> {

    List<settingData> data = new ArrayList<settingData>();

    private static final int MAINHOLDER = 0;
    private static final int SUBHOLDER = 1;
    Context context;
    public void add(settingData main, int position){
        data.add(position, main);
        notifyItemInserted(position);

    }


    @Override
    public settingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType){
            case MAINHOLDER :
                ViewGroup viewMain = (ViewGroup)inflater.inflate(R.layout.setting_main, parent, false);
                settingMainViewHolder mainViewHolder1 = new settingMainViewHolder(viewMain, context);
                return mainViewHolder1;
            case SUBHOLDER :
                ViewGroup viewSub = (ViewGroup)inflater.inflate(R.layout.setting_header, parent, false);
                settingHeaderViewHoder subViewHoder1 = new settingHeaderViewHoder(viewSub, context);
                return subViewHoder1;
            default:
                ViewGroup viewSub0 = (ViewGroup)inflater.inflate(R.layout.setting_header, parent, false);
                settingHeaderViewHoder subViewHoder0 = new settingHeaderViewHoder(viewSub0, context);
                return subViewHoder0;
        }
    }


    @Override
    public void onBindViewHolder(final settingViewHolder holder, int position) {
        final settingData settingData = data.get(position);

        switch (holder.getItemViewType()){
            case MAINHOLDER:
                settingMainViewHolder mainViewHolder1 = (settingMainViewHolder)holder;
                mainViewHolder1.main.setText(settingData.getSettingName());
                break;
            case SUBHOLDER:
                settingHeaderViewHoder subViewHoder1 = (settingHeaderViewHoder)holder;
                subViewHoder1.header.setText(settingData.getSettingHeader());
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if (data.get(position).getSettingName() == null){
            viewType = SUBHOLDER;
        }else if (data.get(position).getSettingHeader() == null){
            viewType = MAINHOLDER;
        }
        return viewType;
    }


    @Override
    public int getItemCount() {
        return data.size();
    }
}
