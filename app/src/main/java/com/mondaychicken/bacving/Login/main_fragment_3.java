package com.mondaychicken.bacving.Login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mondaychicken.bacving.R;

/**
 * Created by leejaebeom on 2016. 1. 13..
 */
public class main_fragment_3 extends Fragment {
    TextView title, sub;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.main_item,container, false);
        title = (TextView)v.findViewById(R.id.main_title);
        sub = (TextView)v.findViewById(R.id.main_sub);

        title.setText("강력한 검색 기능");
        sub.setText("원하는 조건에 최적화 된 매칭을 제공 받으세요");
        return v;
    }
}
