package com.mondaychicken.bacving.stadium.review;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mondaychicken.bacving.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ijaebeom on 2015. 9. 10..
 */
public class reviewFragmentAdapter extends RecyclerView.Adapter<reviewFragmentViewHolder> {

    List<reviewFragmentMainData> mainData = new ArrayList<>();
    Context context;

    public void add(reviewFragmentMainData main, int position){
        mainData.add(position, main);
        notifyItemInserted(position);

    }


    @Override
    public reviewFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewGroup viewMain = (ViewGroup)inflater.inflate(R.layout.stadium_review_item, parent, false);
        reviewFragmentMainViewHolder1 mainViewHolder1 = new reviewFragmentMainViewHolder1(viewMain, parent.getContext());
        return mainViewHolder1;


    }


    @Override
    public void onBindViewHolder(final reviewFragmentViewHolder holder, int position) {
        final reviewFragmentMainData main = mainData.get(position);

        reviewFragmentMainViewHolder1 mainViewHolder1 = (reviewFragmentMainViewHolder1)holder;
        mainViewHolder1.name.setText(main.getName());
        mainViewHolder1.date.setText(main.getDate());
        mainViewHolder1.description.setText(main.getComment());
        Glide.with(context).load(main.getProfileImg()).into(mainViewHolder1.profile);
        Glide.get(context).clearMemory();

        switch (main.getRatingNum()){
            case 1:
                mainViewHolder1.rate1.setImageResource(R.drawable.rate_on);
                break;
            case 2:
                mainViewHolder1.rate1.setImageResource(R.drawable.rate_on);
                mainViewHolder1.rate2.setImageResource(R.drawable.rate_on);
                break;
            case 3:
                mainViewHolder1.rate1.setImageResource(R.drawable.rate_on);
                mainViewHolder1.rate2.setImageResource(R.drawable.rate_on);
                mainViewHolder1.rate3.setImageResource(R.drawable.rate_on);
                break;
            case 4:
                mainViewHolder1.rate1.setImageResource(R.drawable.rate_on);
                mainViewHolder1.rate2.setImageResource(R.drawable.rate_on);
                mainViewHolder1.rate3.setImageResource(R.drawable.rate_on);
                mainViewHolder1.rate4.setImageResource(R.drawable.rate_on);
                break;
            case 5:
                mainViewHolder1.rate1.setImageResource(R.drawable.rate_on);
                mainViewHolder1.rate2.setImageResource(R.drawable.rate_on);
                mainViewHolder1.rate3.setImageResource(R.drawable.rate_on);
                mainViewHolder1.rate4.setImageResource(R.drawable.rate_on);
                mainViewHolder1.rate5.setImageResource(R.drawable.rate_on);
                break;
        }
//        mainViewHolder1.scheduleYear.setText(main.getscheduleYear());



    }


    @Override
    public int getItemCount() {
        return mainData.size();
    }
}
