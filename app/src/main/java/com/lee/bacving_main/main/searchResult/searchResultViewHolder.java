package com.lee.bacving_main.main.searchResult;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lee.bacving_main.R;

/**
 * Created by ijaebeom on 2015. 9. 15..
 */
public class searchResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    ImageView searchReultImage;
    TextView searchResultName, searchResult2nd, searchResult3rd;
    public searchResultViewHolder(View itemView) {
        super(itemView);

        searchReultImage = (ImageView)itemView.findViewById(R.id.search_result_image);
        searchResultName = (TextView)itemView.findViewById(R.id.search_result_name);
        searchResult2nd = (TextView)itemView.findViewById(R.id.search_result_2nd_text);
        searchResult3rd = (TextView)itemView.findViewById(R.id.search_result_3rd_text);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
