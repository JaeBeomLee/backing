package com.mondaychicken.bacving.teamPlayer;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bacving.lee.bacving_main.R;

import java.util.List;

/**
 * Created by ijaebeom on 2015. 8. 18..
 */
public class playerListAdapter extends RecyclerView.Adapter<playerListHolder> {
    List<playerListData> items;

    public playerListAdapter(List<playerListData> items){
        this.items = items;
    }
    public void add(playerListData item , int position){
        items.add(position,item);
        notifyItemInserted(position);


        Log.d("item size", String.valueOf(items.size()));
    }

    @Override
    public playerListHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout,parent,false);
        return new playerListHolder(v);
    }

    @Override
    public void onBindViewHolder(playerListHolder holder, int position) {
        playerListData data = items.get(position);
        holder.profileName.setText(data.getName());
        holder.profileImage.setImageBitmap(data.getImage());
    }

    @Override
    public int getItemCount() {
    if (items == null) {
            return 0;
        }else{
            return items.size();
        }
    }
}
