package com.mondaychicken.bacving.main.searchResult;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mondaychicken.bacving.R;
import com.mondaychicken.bacving.stadium.stadiumActivity;
import com.mondaychicken.bacving.teamPlayer.teamActivity;

/**
 * Created by ijaebeom on 2015. 9. 15..
 */
public class searchResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    ImageView searchReultImage;
    TextView searchResultName, searchResult2nd, searchResult3rd, searchResultHeader;
    Context context;
    public searchResultViewHolder(View itemView, Context context) {
        super(itemView);

        this.context = context;
        searchReultImage = (ImageView)itemView.findViewById(R.id.search_result_image);
        searchResultName = (TextView)itemView.findViewById(R.id.search_result_name);
        searchResult2nd = (TextView)itemView.findViewById(R.id.search_result_2nd_text);
        searchResult3rd = (TextView)itemView.findViewById(R.id.search_result_3rd_text);
        searchResultHeader = (TextView)itemView.findViewById(R.id.search_result_header);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Log.d("ItemClicked",getLayoutPosition()+".");

        try{
            if (searchResultActivity.stadiumNum[getLayoutPosition()] == 3) {
                Intent intent = new Intent(context, stadiumActivity.class);
                intent.putExtra("s_idx", searchResultActivity.stadiuminfo[getLayoutPosition() - searchResultActivity.teaminfo.length].getIdx());
                context.startActivity(intent);
            }else if (searchResultActivity.teamNum[getLayoutPosition()] == 1) {
                Intent intent = new Intent(context, teamActivity.class);
                intent.putExtra("team_idx", searchResultActivity.teaminfo[getLayoutPosition()].getIdx());
                intent.putExtra("leader_idx", searchResultActivity.teaminfo[getLayoutPosition()].getLeader_idx());
                intent.putExtra("search",true);
                context.startActivity(intent);
            }
        }catch(NullPointerException e){
            Log.e("NullPointer","경기장 검색 결과가 없음");
            try{
                if (searchResultActivity.teamNum[getLayoutPosition()] == 1) {
                    Intent intent = new Intent(context, teamActivity.class);
                    intent.putExtra("team_idx", searchResultActivity.teaminfo[getLayoutPosition()].getIdx());
                    intent.putExtra("leader_idx", searchResultActivity.teaminfo[getLayoutPosition()].getLeader_idx());
                    context.startActivity(intent);
                }
            }catch(NullPointerException ex){
                Log.e("NullPointer","팀 검색 결과가 없음");
            }
        }


    }
}
