package com.mondaychicken.bacving.main.alertList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mondaychicken.bacving.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ijaebeom on 2015. 9. 10..
 */
public class alertAdapter extends RecyclerView.Adapter<alertViewHolder> {

    List<alertData> alertDatas = new ArrayList<>();
    Context context;

    public void add(alertData main, int position){
        alertDatas.add(position, main);
        notifyItemInserted(position);

    }

    @Override
    public alertViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewGroup viewMain = (ViewGroup)inflater.inflate(R.layout.alert_item, parent, false);
        alertViewHolder mainViewHolder = new alertViewHolder(viewMain, context);

        return mainViewHolder;


    }

    @Override
    public void onBindViewHolder(alertViewHolder holder, int position) {
        alertData main = alertDatas.get(position);
        alertViewHolder alert = (alertViewHolder)holder;
        alert.description.setText(main.getDescription());
        //프로필이 null임
//        Glide.with(context).load(main.getProfileimg()).into(alert.profile);
//        Glide.get(context).clearMemory();
    }

    @Override
    public int getItemCount() {
        return alertDatas.size();
    }



}
