package com.mondaychicken.bacving.main.soccer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import bacving.lee.bacving_main.R;

import java.util.List;

/**
 * Created by ijaebeom on 2015. 9. 10..
 */
public class mainTabFragmentAdapter2 extends RecyclerView.Adapter<mainTabFragmentMainViewHolder2> {

    List<mainTabFragmentMainData2> mainData;



    public mainTabFragmentAdapter2(List<mainTabFragmentMainData2> mainData){
        this.mainData = mainData;

    }

    public void add(mainTabFragmentMainData2 main, int position){
        mainData.add(position, main);
        notifyItemInserted(position);

    }

    @Override
    public mainTabFragmentMainViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewGroup viewMain = (ViewGroup)inflater.inflate(R.layout.main_list_view1, parent, false);
        mainTabFragmentMainViewHolder2 mainViewHolder2 = new mainTabFragmentMainViewHolder2(viewMain, parent.getContext());

        return mainViewHolder2;


    }

    @Override
    public void onBindViewHolder(mainTabFragmentMainViewHolder2 holder, int position) {
        mainTabFragmentMainData2 main = mainData.get(position);
        mainTabFragmentMainViewHolder2 mainViewHolder1 = (mainTabFragmentMainViewHolder2)holder;
        mainViewHolder1.mainTeamName.setText(main.getTeamName());
        mainViewHolder1.mainTeamLogo.setImageBitmap(main.getTeamLogo());
        mainViewHolder1.mainBackground.setImageBitmap(main.getMainBackground());
    }

    @Override
    public int getItemCount() {
        return mainData.size();
    }


}
