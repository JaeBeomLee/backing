package com.mondaychicken.bacving.stadium;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.mondaychicken.bacving.R;
import com.mondaychicken.bacving.map.search.mapSearch;
import com.mondaychicken.bacving.matching.matchingTeamRegister;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;
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
 * Created by leejaebeom on 2015. 10. 9..
 */
public class createStadiumActivity extends AppCompatActivity {
    Context context;

    EditText stadiumName, stadiumDescription, stadiumAddress, stadiumTel, stadiumPrice, stadiumTerms;
    Spinner stadiumSports;
    Button createTeam;

    private AlertDialog alertDialog = null;
    private AlertDialog alertDialog2 = null;

    String stadiumNameS = null, stadiumLocationS2 = null, stadiumDescriptionS = null, stadiumAddressS, stadiumTelS, stadiumPriceS, stadiumTermsS, userno = null, token = null;
    String name = null , profileimg= null, address;
    double lat, lng;
    int stadiumSportsS;
    
    RelativeLayout map;
    int permissionCheck;
    MapView mapView;
    String MapApiKey = "0b42f43598ab66b672d55c2364b29e5b";
    
    String server = "http://api.bacving.com/api/stadium/create.php";
    String serverResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stadium_create);

        context = createStadiumActivity.this;

        LoadPreference(context);

        Intent intent = getIntent();
        name = intent.getExtras().getString("name");
        profileimg = intent.getExtras().getString("profileimg");
        address = intent.getExtras().getString("address");
        stadiumTelS = intent.getExtras().getString("tel");
        stadiumLocationS2 = intent.getExtras().getString("region");
        lat = intent.getExtras().getDouble("lat");
        lng = intent.getExtras().getDouble("lng");
        String[] Sports = {"축구", "농구"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Sports);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stadiumSports = (Spinner) findViewById(R.id.stadium_create_sports);
        stadiumSports.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_stadium_create);
        toolbar.setTitleTextColor(0xffffffff);
        toolbar.setTitle("팀 생성");

        stadiumName = (EditText) findViewById(R.id.stadium_create_name);
        stadiumDescription = (EditText) findViewById(R.id.stadium_create_description);
        stadiumAddress = (EditText) findViewById(R.id.stadium_create_address);
        stadiumTel = (EditText) findViewById(R.id.stadium_create_tel);
        stadiumPrice = (EditText) findViewById(R.id.stadium_create_price);
        stadiumTerms = (EditText) findViewById(R.id.stadium_create_terms);
        createTeam = (Button) findViewById(R.id.stadium_create_btn);

        stadiumName.setText(name);
        stadiumAddress.setText(address);
        stadiumTel.setText(stadiumTelS);


//        permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
//        int MyResultID = 1;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
//                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MyResultID);
//            }
//        }
//        map = (RelativeLayout)findViewById(R.id.map_view);
//        if (permissionCheck == PackageManager.PERMISSION_DENIED){
//            map.setVisibility(View.GONE);
//        }else{
//            mapView = new MapView(this);
//            mapView.setDaumMapApiKey(MapApiKey);
//            map.addView(mapView);
//            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lat, lng), true);
//            mapView.setZoomLevel(-2, true);
//        }

        createTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stadiumNameS = stadiumName.getText().toString();
                stadiumDescriptionS = stadiumDescription.getText().toString();
                stadiumAddressS = stadiumAddress.getText().toString();
                stadiumPriceS = stadiumPrice.getText().toString();
                stadiumTelS = stadiumTel.getText().toString();
                stadiumTermsS = stadiumTerms.getText().toString();
                switch (stadiumSports.getSelectedItem().toString()) {
                    case "축구":
                        stadiumSportsS = 1;
                        break;
                }
                ;

                if (stadiumNameS == null || stadiumDescriptionS == null || stadiumLocationS2 == null || stadiumSportsS == 0 || stadiumAddressS == null) {
                    alertDialog2 = DialogInit("생성 오류", "경기장 등록에 필요한 모든 내용을 입력 해 주세요.");
                    alertDialog2.show();
                } else {
                    Login();
                }


            }
        });


    }

    private AlertDialog DialogInit(String title, String message){
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        if(title.length() > 0) {
            build.setTitle(title);
        }
        build.setMessage(message);
        build.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog2.dismiss();

            }
        });
        return build.create();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    permissionCheck = PackageManager.PERMISSION_GRANTED;
                    map.setVisibility(View.VISIBLE);
                    mapView = new MapView(this);
                    mapView.setDaumMapApiKey(MapApiKey);
                    map.addView(mapView);
                    mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lat, lng), true);
                    mapView.setZoomLevel(-2, true);
                }else {
                    permissionCheck = PackageManager.PERMISSION_DENIED;
                }
            }
        }
    }


    public void Login(){

        new Thread(){
            @Override
            public void run() {
                ConnectServer();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoginEnd();

                    }
                });
            }
        }.start();
    }
    private void LoginEnd(){

        String result = serverResult;
        Log.d("fragmentResult", result);
        int code = 0;
        String msg = "", name = null, location = "";
        JSONObject array[];

        if (result == null){
            return;
        }
        try{
            JSONObject json = new JSONObject(result);
            code = json.getInt("code");
            msg = json.getString("message");
            JSONObject input_data = json.getJSONObject("input_data");
            name = input_data.getString("name");
            location = input_data.getString("location");

        } catch (JSONException e) {
            e.printStackTrace();

        } catch (NullPointerException e){
            e.printStackTrace();

            code = 0;
            msg = "failed";
        }

        if (code == 700){
            Intent back = new Intent();
            back.putExtra("stadium", name + "(" +location+ ")");
            setResult(2, back);
            finish();
        }else{
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    alertDialog2 = DialogInit("무언가 잘못 되었습니다", "수정될 예정입니다");
                    alertDialog2.show();
                }
            });
            Log.d("failed", code + " " + msg);
        }
    }
    private void ConnectServer(){
        try {
            serverResult = null;
            StringBuffer data = new StringBuffer();
            data.append("user_idx").append("=").append(userno).append("&");
            data.append("token").append("=").append(token).append("&");
            data.append("name").append("=").append(stadiumNameS).append("&");
            data.append("description").append("=").append(stadiumDescriptionS).append("&");
            data.append("sport_idx").append("=").append(stadiumSportsS).append("&");
            data.append("region").append("=").append(stadiumLocationS2).append("&");
            data.append("location").append("=").append(stadiumAddressS).append("&");
            data.append("lat").append("=").append(lat).append("&");
            data.append("lng").append("=").append(lng).append("&");
            if (stadiumTelS != null){
                //Optional
                data.append("tel").append("=").append(stadiumTelS).append("&");
            }
            if (stadiumPriceS != null){
                //Optional
                data.append("price").append("=").append(stadiumPriceS).append("&");
            }

            if (stadiumTermsS != null){
                //Optional
                data.append("terms").append("=").append(stadiumTermsS).append("&");
            }


            URL url = new URL(server);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(data.toString());
            writer.flush();

            InputStreamReader ims = new InputStreamReader(http.getInputStream(),"UTF-8");
            BufferedReader reader = new BufferedReader(ims);
            StringBuilder builder = new StringBuilder();
            String str;

            while ((str = reader.readLine()) != null){
                builder.append(str);
            }

            serverResult = builder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ConnectException e){
            e.printStackTrace();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    alertDialog2 =  DialogInit("무언가 잘못 되었습니다", "수정될 예정입니다");
                    alertDialog2.show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 문자값저장
    public void savePreference(String key, int value, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.commit();
    }

    private void LoadPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userno = sharedPreferences.getString("userno", null);
        token = sharedPreferences.getString("token", null);
    }

}
