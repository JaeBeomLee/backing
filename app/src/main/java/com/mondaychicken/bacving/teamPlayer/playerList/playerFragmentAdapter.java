package com.mondaychicken.bacving.teamPlayer.playerList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mondaychicken.bacving.R;

import java.util.List;

/**
 * Created by ijaebeom on 2015. 9. 10..
 */
public class playerFragmentAdapter extends RecyclerView.Adapter<playerFragmentViewHolder> {

    List<playerFragmentData> playerData;
    Context context;
    public playerFragmentAdapter(List<playerFragmentData> playerData){
        this.playerData = playerData;

    }

    public void add(playerFragmentData main, int position){
        playerData.add(position, main);
        notifyItemInserted(position);

    }

    @Override
    public playerFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewGroup viewMain = (ViewGroup)inflater.inflate(R.layout.team_member_item, parent, false);
        playerFragmentViewHolder mainViewHolder = new playerFragmentViewHolder(viewMain, context);

        return mainViewHolder;


    }

    @Override
    public void onBindViewHolder(playerFragmentViewHolder holder, int position) {
        playerFragmentData main = playerData.get(position);
        playerFragmentViewHolder player = (playerFragmentViewHolder)holder;
        player.name.setText(main.getName());
        player.nickname.setText(main.getNickname());
        Glide.with(context).load(main.getProfileimg())
//                .placeholder(R.drawable.circle_emblem).error(R.drawable.circle_emblem).crossFade()
                .into(player.profile);
        Glide.get(context).clearMemory();
    }

    @Override
    public int getItemCount() {
        return playerData.size();
    }


}
