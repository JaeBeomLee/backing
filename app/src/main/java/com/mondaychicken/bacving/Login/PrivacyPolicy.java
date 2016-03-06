package com.mondaychicken.bacving.Login;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

import com.mondaychicken.bacving.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by ijaebeom on 15. 7. 2..
 */
public class PrivacyPolicy extends AppCompatActivity {
    Toolbar toolbar;
    TextView privacyPolicy;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy);
        toolbar = (Toolbar)findViewById(R.id.tool_bar_privacy_policy);
//        privacyPolicy = (TextView)findViewById(R.id.privacy_policy_description);

        toolbar.setTitle("박빙 이용약관");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        new MyTask().execute();

    }
    private class MyTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            Elements div;
            Document doc;
            String result = null;
            try {
                doc = Jsoup.connect("https://www.bacving.com/terms/").get();
                div = doc.select(".terms");
                result = div.html().toString().replace("<p class=\"0\">&nbsp;</p>", " ");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            ((TextView)findViewById (R.id.privacy_policy_description)).setText (Html.fromHtml(result));
        }
    }
}

