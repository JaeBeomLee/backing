package com.mondaychicken.bacving.main.etc;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.mondaychicken.bacving.R;

import java.util.ArrayList;

/**
 * Created by leejaebeom on 2015. 11. 1..
 */
public class noticeAdapter extends BaseExpandableListAdapter {
    private ArrayList<String> title = null;
    private ArrayList <String> content = null;
    private LayoutInflater inflater = null;
    private ViewHolder viewHolder = null;

    Context c;
    public noticeAdapter(Context c, ArrayList<String> title, ArrayList<String> content){
        super();
        this.c = c;
        this.inflater = LayoutInflater.from(c);
        this.title = title;
        this.content = content;
    }
    @Override
    public int getGroupCount() {
        return title.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public String getGroup(int groupPosition) {
        return title.get(groupPosition);
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        return content.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null){
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.notice_item, parent,false);
            viewHolder.Title = (TextView)v.findViewById(R.id.notice_item);
            v.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)v.getTag();
        }
        viewHolder.Title.setText(getGroup(groupPosition));
        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v == null){
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.notice_content, null);
            viewHolder.content = (TextView) v.findViewById(R.id.notice_item_content);
            v.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)v.getTag();
        }
        viewHolder.content.setText(getChild(groupPosition, childPosition));
        return v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolder{
        public TextView Title;
        public TextView content;
    }
}
