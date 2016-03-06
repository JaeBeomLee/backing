package com.mondaychicken.bacving.main.searchResult;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.mondaychicken.bacving.R;

import java.util.List;

/**
 * Created by ijaebeom on 2015. 9. 15..
 */
public class searchResultAdapter extends RecyclerView.Adapter<searchResultViewHolder> {
    List<searchResultData> resultData;
    Context context;

    public searchResultAdapter(List<searchResultData> resultData){
        this.resultData = resultData;
    }

    public void add(searchResultData data, int position){
        resultData.add(position, data);
        notifyItemInserted(position);
    }
    @Override
    public searchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewGroup viewSearchResult = (ViewGroup)inflater.inflate(R.layout.search_result_item,parent,false);
        searchResultViewHolder resultViewHolder = new searchResultViewHolder(viewSearchResult, parent.getContext());
        return resultViewHolder;

    }

    @Override
    public void onBindViewHolder(searchResultViewHolder holder, int position) {
        searchResultData resultData = this.resultData.get(position);
        searchResultViewHolder resultViewHolder = (searchResultViewHolder)holder;
        Glide.with(context).load(resultData.getSearchResultImage()).into(resultViewHolder.searchReultImage);
        Glide.get(context).clearMemory();
        resultViewHolder.searchResultName.setText(resultData.getSearchResultName());
        resultViewHolder.searchResult2nd.setText(resultData.getSearchResult2nd());
        resultViewHolder.searchResult3rd.setText(resultData.getSearchResult3rd());
        switch (resultData.getSearchResultNum()){
            case 0:
                resultViewHolder.searchResultHeader.setText("팀");
                break;
            case 1:
                resultViewHolder.searchResultHeader.setVisibility(View.GONE);
                break;
            case 2:
                resultViewHolder.searchResultHeader.setText("경기장");
                break;
            case 3:
                resultViewHolder.searchResultHeader.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return resultData.size();
    }
}
