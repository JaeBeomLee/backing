package com.mondaychicken.bacving.main.searchResult;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import bacving.lee.bacving_main.R;
import com.mondaychicken.bacving.stadium.stadiumActivity;

/**
 * Created by ijaebeom on 2015. 9. 15..
 */
public class searchResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    ImageView searchReultImage;
    TextView searchResultName, searchResult2nd, searchResult3rd;
    Context context;
    public searchResultViewHolder(View itemView, Context context) {
        super(itemView);

        this.context = context;
        searchReultImage = (ImageView)itemView.findViewById(R.id.search_result_image);
        searchResultName = (TextView)itemView.findViewById(R.id.search_result_name);
        searchResult2nd = (TextView)itemView.findViewById(R.id.search_result_2nd_text);
        searchResult3rd = (TextView)itemView.findViewById(R.id.search_result_3rd_text);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, stadiumActivity.class);
        context.startActivity(intent);
    }
}
