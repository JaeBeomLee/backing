package com.mondaychicken.bacving.main.etc;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;


import com.mondaychicken.bacving.R;

import java.util.ArrayList;

/**
 * Created by leejaebeom on 2015. 11. 1..
 */
public class notice extends AppCompatActivity {
    Toolbar toolbar;
    ExpandableListView list;

    private ArrayList <String> title = null;
    private ArrayList <String> content = null;
//    private ArrayList <ArrayList<String>> child = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice);
        toolbar = (Toolbar)findViewById(R.id.tool_bar_notice);
        list = (ExpandableListView)findViewById(R.id.notice_list_view);
        toolbar.setTitle("공지사항");
        toolbar.setTitleTextColor(Color.WHITE);
        title = new ArrayList<String>();
        content = new ArrayList<String>();

        addNotice("test1", "this is test\nnext Line");
        addNotice("test2", "this is test\nnext Line2");
        addNotice("test2", "this is test\nnext Line2");
        addNotice("test2", "this is test\nnext Line2");
        addNotice("test2", "this is test\nnext Line2");
        addNotice("test2", "this is test\nnext Line2");
        addNotice("test2", "this is test\nnext Line2");
        addNotice("test2", "this is test\nnext Line2");
        addNotice("test2", "this is test\nnext Line2");
        addNotice("test2", "this is test\nnext Line2");
        addNotice("test2", "this is test\nnext Line2");
        addNotice("test2", "this is test\nnext Line2");
        addNotice("test2", "this is test\nnext Line2");
        addNotice("test2", "this is test\nnext Line2");
        addNotice("test2", "this is test\nnext Line2");
        addNotice("test2", "this is test\nnext Line2");
        list.setAdapter(new noticeAdapter(this, title, content));
    }

    public void addNotice(String NoticeTitle, String NoticeContent){
        title.add(NoticeTitle);
        content.add(NoticeContent);
//        child.add(content);
    }
}
