package com.mondaychicken.bacving.stadium.review;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mondaychicken.bacving.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ijaebeom on 2015. 9. 10..
 */
public class reviewFragmentMainViewHolder1 extends reviewFragmentViewHolder implements View.OnClickListener{
    Context context;
    CircleImageView profile;
    TextView name, date, description;
    ImageView rate1, rate2, rate3, rate4, rate5;
    public reviewFragmentMainViewHolder1(View itemView, Context context) {
        super(itemView, context);
        this.context = context;
        profile = (CircleImageView)itemView.findViewById(R.id.stadium_review_item_profile);
        name = (TextView)itemView.findViewById(R.id.stadium_review_item_name);
        date = (TextView)itemView.findViewById(R.id.stadium_review_item_date);
        description = (TextView)itemView.findViewById(R.id.stadium_review_item_description);
        rate1 = (ImageView)itemView.findViewById(R.id.stadium_review_item_star1);
        rate2 = (ImageView)itemView.findViewById(R.id.stadium_review_item_star2);
        rate3 = (ImageView)itemView.findViewById(R.id.stadium_review_item_star3);
        rate4 = (ImageView)itemView.findViewById(R.id.stadium_review_item_star4);
        rate5 = (ImageView)itemView.findViewById(R.id.stadium_review_item_star5);

        itemView.setOnClickListener(this);
    }

    //사용 안함.
    @Override
    public void onClick(View v) {

    }
}

