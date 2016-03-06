package com.mondaychicken.bacving.stadium.review;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.mondaychicken.bacving.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by leejaebeom on 2016. 2. 9..
 */
public class reviewFragment extends Fragment {
    String commentary, serverResult;
    int ratingNum;
    RecyclerView list;
    LinearLayoutManager manager;
    reviewFragmentAdapter adapter;
    LinearLayout btn;
    Context context;
    private AlertDialog alertDialog2 = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        LoadPreference(context);
        try {
            JSONObject data = new JSONObject(serverResult);
            //here is json access
        } catch (JSONException e) {
            e.printStackTrace();
        }

        View v  = inflater.inflate(R.layout.stadium_review,container, false);
        list = (RecyclerView)v.findViewById(R.id.stadium_review_list);
        adapter = new reviewFragmentAdapter();
        manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(0);
        list.setLayoutManager(manager);
        list.setAdapter(adapter);
        btn = (LinearLayout)v.findViewById(R.id.stadium_review_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog2 = DialogReview();
                alertDialog2.show();
            }
        });
        return v;
    }

    private AlertDialog DialogReview(){

        final View innerView = getActivity().getLayoutInflater().inflate(R.layout.stadium_review_dialog, null);
//        picker = (DatePicker)innerView.findViewById(R.id.birth_picker);
        final RatingBar rating = (RatingBar)innerView.findViewById(R.id.stadium_review_dialog_rating);
        final EditText comment = (EditText)innerView.findViewById(R.id.stadium_review_dialog_comment);
        Button confirm = (Button)innerView.findViewById(R.id.stadium_review_dialog_btn);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setView(innerView);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentary = comment.getText().toString();
                ratingNum = rating.getNumStars();
                alertDialog2.dismiss();
            }
        });
        return builder.create();
    }

    private void LoadPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        serverResult = sharedPreferences.getString("stadiumReview", null);
    }
}
