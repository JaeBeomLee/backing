package com.mondaychicken.bacving.teamPlayer;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mondaychicken.bacving.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ijaebeom on 2015. 8. 24..
 */
public class teamInformationFragment extends Fragment {

    Context context;
    TextView informationDescription;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        View v = inflater.inflate(R.layout.team_infomation_view, container, false);

        String description = teamActivity.teamDescription;
        informationDescription = (TextView)v.findViewById(R.id.team_information_description);
        informationDescription.setText(description);


        return v;
    }

}
