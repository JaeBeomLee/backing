package com.mondaychicken.bacving.main.searchResult;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import bacving.lee.bacving_main.R;

import java.util.List;

/**
 * Created by ijaebeom on 2015. 9. 15..
 */
public class searchResultAdapter extends RecyclerView.Adapter<searchResultViewHolder> {
    List<searchResultData> resultData;

    public searchResultAdapter(List<searchResultData> resultData){
        this.resultData = resultData;
    }

    public void add(searchResultData data, int position){
        resultData.add(position, data);
        notifyItemInserted(position);
    }
    @Override
    public searchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewGroup viewSearchResult = (ViewGroup)inflater.inflate(R.layout.search_result_item,parent,false);
        searchResultViewHolder resultViewHolder = new searchResultViewHolder(viewSearchResult, parent.getContext());
        return resultViewHolder;
    }

    @Override
    public void onBindViewHolder(searchResultViewHolder holder, int position) {
        searchResultData resultData = this.resultData.get(position);
        searchResultViewHolder resultViewHolder = (searchResultViewHolder)holder;
        resultViewHolder.searchReultImage.setImageBitmap(resultData.getSearchResultImage());
        resultViewHolder.searchResultName.setText(resultData.getSearchResultName());
        resultViewHolder.searchResult2nd.setText(resultData.getSearchResult2nd());
        resultViewHolder.searchResult3rd.setText(resultData.getSearchResult3rd());

    }

    @Override
    public int getItemCount() {
        return resultData.size();
    }
}
