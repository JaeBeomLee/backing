package com.mondaychicken.bacving.matching;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mondaychicken.bacving.R;
import com.mondaychicken.bacving.main.total.mainTabFragmentMainData;
import com.mondaychicken.bacving.main.total.mainTabFragmentMainViewHolder1;
import com.mondaychicken.bacving.main.total.mainTabFragmentSubViewHoder1;
import com.mondaychicken.bacving.main.total.mainTabFragmentViewHolder;

import java.util.List;

/**
 * Created by ijaebeom on 2015. 9. 10..
 */
public class matchingFragmentAdapter extends RecyclerView.Adapter<matchingFragmentViewHolder> {

    List<matchingFragmentData> Data;
    private static final int MAINHOLDER = 0;
    private static final int SUBHOLDER = 1;
    Context context;


    public matchingFragmentAdapter(List<matchingFragmentData> Data){
        this.Data = Data;

    }

    public void add(matchingFragmentData main, int position){
        Data.add(position, main);
        notifyItemInserted(position);

    }

    @Override
    public matchingFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType){
            case MAINHOLDER :
                ViewGroup viewMain = (ViewGroup)inflater.inflate(R.layout.matching_search_result_item, parent, false);
                 matchingFragmentItemViewHolder mainViewHolder1 = new matchingFragmentItemViewHolder(viewMain, context);
                return mainViewHolder1;
            case SUBHOLDER :
                ViewGroup viewSub = (ViewGroup)inflater.inflate(R.layout.matching_search_result_item_register, parent, false);
                matchingFragmentRegisterViewHolder subViewHoder1 = new matchingFragmentRegisterViewHolder(viewSub, context, Data.size());
                return subViewHoder1;
            default:
                ViewGroup viewSub0 = (ViewGroup)inflater.inflate(R.layout.matching_search_result_item_register, parent, false);
                matchingFragmentRegisterViewHolder subViewHoder0 = new matchingFragmentRegisterViewHolder(viewSub0, context, Data.size());
                return subViewHoder0;
        }
    }

    @Override
    public void onBindViewHolder(matchingFragmentViewHolder holder, int position) {
                matchingFragmentData matching = Data.get(position);
        switch (holder.getItemViewType()) {
            case MAINHOLDER:
                matchingFragmentItemViewHolder matchingViewHolder = (matchingFragmentItemViewHolder) holder;
                matchingViewHolder.name.setText(matching.getName());
                matchingViewHolder.place.setText(matching.getPlace());
                matchingViewHolder.time.setText(matching.getTime());
                matchingViewHolder.age.setText(matching.getAge());
                Glide.with(context).load(matching.getLogo()).into(matchingViewHolder.logo);
                Glide.get(context).clearMemory();
                break;
            case SUBHOLDER:
                matchingFragmentRegisterViewHolder matchingFragmentRegisterViewHolder = (matchingFragmentRegisterViewHolder) holder;
                break;

        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if (position == Data.size()-1){
            viewType = SUBHOLDER;
        }else {
            viewType = MAINHOLDER;
        }
        return viewType;
    }


    @Override
    public int getItemCount() {
        return Data.size();
    }


}
