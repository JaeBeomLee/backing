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
public class main_fragment_1 extends Fragment {
    TextView title, sub;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.main_item,container, false);
        title = (TextView)v.findViewById(R.id.main_title);
        sub = (TextView)v.findViewById(R.id.main_sub);

        title.setText("환영합니다");
        sub.setText("신개념 스포츠 매칭 서비스, 박빙");
        return v;
    }
}
