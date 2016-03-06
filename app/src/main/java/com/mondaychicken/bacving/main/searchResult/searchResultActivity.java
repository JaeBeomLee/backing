package com.mondaychicken.bacving.main.searchResult;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import com.mondaychicken.bacving.R;
import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ijaebeom on 2015. 9. 15..
 */
public class searchResultActivity extends AppCompatActivity {

    LinearLayoutManager manager;
    List<searchResultInputData> searchInputData = new ArrayList<searchResultInputData>();
    List<searchResultData> searchResultData = new ArrayList<searchResultData>();
    searchResultInputData inputData;
    searchResultAdapter adapter;
    RecyclerView recyclerView;
    Bitmap bit;
    private AlertDialog alertDialog2 = null;
    private String teamResult, stadiumResult, userNo, userName, token;
    private String teamServer = "http://aws.bacving.com/api/team/index.php";
    private String stadiumServer = "http://aws.bacving.com/api/stadium/search.php";
    static stadiumInfo stadiuminfo[] = null;
    static teamInfo teaminfo[] = null;
    static int teamNum[] = null;
    static int stadiumNum[] = null;
    int codeTeam = 0;
    int codeStadium = 0;

    //이 변수는 경기장과 팀을 다른 배열로 받기 때문에 자칫하면 리스트 배열이 중복되어지는 문제가 발샐할 수 있다 그래서 그런 겹침 문제를 없애기 위해 팀 배열 크기를 알아내 순서를 팀배열 크기 아후로 넣기 위해 만들었다
    int arraySize = 0;
    DrawerArrowDrawable drawerArrowDrawable;
    String searchName;
    int position = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_activity);
        LoadPreference(this);
        Intent intent = getIntent();
        searchName = intent.getStringExtra("searchName");
        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar_research);
        recyclerView = (RecyclerView)findViewById(R.id.search_recycler);
        adapter = new searchResultAdapter(searchResultData);
        manager  = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(position);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        toolbar.setTitle(searchName);
        drawerArrowDrawable = new DrawerArrowDrawable(this);
        drawerArrowDrawable.setProgress(1.0f);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(drawerArrowDrawable);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchResultActivity.this.finish();
            }
        });
        //검색
        teamSearch(searchName);
        // 이 밑의 주석은 전부 임시입니다
        //searchResultInputData는 나중에 서버에서 받아온 데이터를 저장 하는 곳이며 그 저장하는 데이터를 임시로 표현한 것이다
//        inputData= new searchResultInputData();
//        inputData.setSearchResultImage(bitTemp);
//        inputData.setSearchResultName("박빙FC");
//        inputData.setSearchResult2nd("잠실운동장");
//        inputData.setSearchResult3rd("031-274-9002");
//        try {
//            //에러 남
//            bit = Glide.with(this).load("https://upload.wikimedia.org/wikipedia/commons/thumb/4/48/Dell_Logo.svg/200px-Dell_Logo.svg.png").asBitmap().into(100, 100).get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        inputData.setSearchResultImage(bit);
//        //data 객체 하나를 저장해 Array배열 n번에 저장한다.
//        searchInputData.add(inputData);
//
//        for (int i = 0; i<=20; i++){
//            adapter.add(createResultList(),position);
//        }
//        manager.scrollToPosition(position);
    }

    public searchResultData createResultList(int num, String profileUrl, String name, String second, String third){
        //실질적인 데이터가 저장되는 곳이다.
        searchResultData resultData = new searchResultData();
        //num은 팀인지 구장인지 분류해주는 역할 팀 헤더는 0, 팀은 1, 구장 헤더는 2, 구장은 3
        resultData.setSearchResultNum(num);
        resultData.setSearchResultImage(profileUrl);
        resultData.setSearchResultName(name);
        resultData.setSearchResult2nd(second);
        resultData.setSearchResult3rd(third);
        return resultData;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_search){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);

    }
    //팀 검색 요청
    public void teamSearch(final String query){
        new Thread() {
            @Override
            public void run() {
                teamConnectServer(query);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        teamSearchQueryEnd();

                    }
                });
            }
        }.start();
    }

    private void teamConnectServer(String query){
        try {
            teamResult = null;
            StringBuffer data = new StringBuffer();
            data.append("type=2&mode=6&query=");
            data.append(query);

            URL url = new URL(teamServer);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(data.toString());
            writer.flush();

            InputStreamReader ims = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(ims);
            StringBuilder builder = new StringBuilder();
            String str;

            while ((str = reader.readLine()) != null) {
                builder.append(str);
            }

            teamResult = builder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ConnectException e) {
            e.printStackTrace();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    alertDialog2 = DialogInit("인터넷 연결 오류", "인터넷과 연결 되 있지 않습니다 인터넷을 연결 해 주세요.");
                    alertDialog2.show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void teamSearchQueryEnd(){
        String result = teamResult;
        Log.d("teamResult", result);
        String msg = "";
        JSONObject array[] = null;
        JSONObject leader[]= null;
        JSONArray teams = null;
        if (result == null){
            stadiumSearch(searchName);
            teaminfo = new teamInfo[0];
            return;
        }
        try{
            JSONObject json = new JSONObject(result);
            codeTeam = json.getInt("code");

            if(codeTeam == 421){
//                alertDialog2 = DialogInit("검색 오류","검색 결과가 없습니다.");
//                alertDialog2.show();
                stadiumSearch(searchName);
                teaminfo = new teamInfo[0];
                return;
            }else if(codeTeam == 700){
                //Log.d("Server Result",json.toString());
                teams = json.getJSONArray("teams");
                array = new JSONObject[teams.length()];
                leader = new JSONObject[teams.length()];
                teaminfo = new teamInfo[teams.length()];
                teamNum = new int[teams.length()];
                for (int i = 0; i<teams.length(); i++){
                    array[i] = teams.getJSONObject(i);
                    leader[i] = array[i].getJSONObject("leader");
                    teaminfo[i] = new teamInfo();
                    teaminfo[i].setName(array[i].getString("name"));
                    teaminfo[i].setIdx(array[i].getString("idx"));
                    teaminfo[i].setProfileimg(array[i].getString("profileimg"));
                    teaminfo[i].setRegion_name(array[i].getString("region_name"));
                    teaminfo[i].setLeader_idx(leader[i].getString("idx"));
                    //검색 된 팀 이름 출력
                    Log.d("Result",teaminfo[i].getName());
                    //데이터 입력
                    if (i == 0){
                        adapter.add(createResultList(0, teaminfo[i].getProfileimg(), teaminfo[i].getName(), teaminfo[i].getRegion_name(), null ), i);
                        teamNum[i] = 1;
                    }else{
                        adapter.add(createResultList(1, teaminfo[i].getProfileimg(), teaminfo[i].getName(), teaminfo[i].getRegion_name(), null ), i);
                        teamNum[i] = 1;
                    }
                    arraySize++;
                }
                Log.d("InfoLength",""+teaminfo.length);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("InfoLength", "" + teaminfo.length);
        } catch (NullPointerException e){
            e.printStackTrace();

            codeTeam = 0;
            msg = "failed";
            //오류 내용 확인을 위한 로그
            Log.e("NullPointer",e.toString());
            Log.d("InfoLength", "" + teaminfo.length);
        }
        stadiumSearch(searchName);
    }

    //경기장 검색 요청
    public void stadiumSearch(final String query){
        new Thread() {
            @Override
            public void run() {
                stadiumConnectServer(query);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        stadiumSearchQueryEnd();

                    }
                });
            }
        }.start();
    }

    private void stadiumConnectServer(String query){
        try {
            stadiumResult = null;
            StringBuffer data = new StringBuffer();
            data.append("&query=").append(query);

            URL url = new URL(stadiumServer);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");

            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(data.toString());
            writer.flush();

            InputStreamReader ims = new InputStreamReader(http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(ims);
            StringBuilder builder = new StringBuilder();
            String str;

            while ((str = reader.readLine()) != null) {
                builder.append(str);
            }

            stadiumResult = builder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ConnectException e) {
            e.printStackTrace();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    alertDialog2 = DialogInit("인터넷 연결 오류", "인터넷과 연결 되 있지 않습니다 인터넷을 연결 해 주세요.");
                    alertDialog2.show();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stadiumSearchQueryEnd(){
        String result = stadiumResult;
        Log.d("stadiumResult", result);
        String msg = "";
        JSONObject array[] = null;
        JSONArray stadiums = null;

        if (result == null){
            return;
        }
        try{
            JSONObject json = new JSONObject(result);
            codeStadium = json.getInt("code");
            if(codeStadium == 700){
                Log.d("Server Result",json.toString());
                stadiums = json.getJSONArray("stadium_list");
                array = new JSONObject[stadiums.length()];
                stadiuminfo = new stadiumInfo[stadiums.length()];
                stadiumNum = new int[stadiums.length()+arraySize];

                for (int i = 0; i<stadiums.length(); i++){
                    array[i] = stadiums.getJSONObject(i);
                    stadiuminfo[i] = new stadiumInfo();
                    stadiuminfo[i].setIdx(array[i].getString("idx"));
                    stadiuminfo[i].setName(array[i].getString("name"));
                    stadiuminfo[i].setLocation(array[i].getString("location"));
                    //검색 된 팀 이름 출력
                    Log.d("Result", stadiuminfo[i].getName());
                    if (i == 0){
                        adapter.add(createResultList(2, null, stadiuminfo[i].getName(), stadiuminfo[i].getLocation(), null), i+arraySize);
                        stadiumNum[i+arraySize] = 3;
                    }else{
                        adapter.add(createResultList(3, null, stadiuminfo[i].getName(), stadiuminfo[i].getLocation(), null), i+arraySize);
                        stadiumNum[i+arraySize] = 3;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();

        } catch (NullPointerException e){
            e.printStackTrace();

            codeStadium = 0;
            msg = "failed";
            //오류 내용 확인을 위한 로그
            Log.e("NullPointer",e.toString());
        }
        if((codeTeam == 421) && (codeStadium == 421)){
                alertDialog2 = DialogInit("검색 오류","검색 결과가 없습니다.");
                alertDialog2.show();
        }
    }
    //Dialog 생성 함수
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
    //저장 된 정보 불러오기
    private void LoadPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userNo = sharedPreferences.getString("userno", null);
        userName = sharedPreferences.getString("name", null);
        token = sharedPreferences.getString("token", null);
    }
}

class teamInfo{
    private String name, idx, profileimg, region_name, leader_idx;

    public void setLeader_idx(String leader_idx) {
        this.leader_idx = leader_idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfileimg(String profileimg) {
        this.profileimg = profileimg;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }

    public String getLeader_idx() {
        return leader_idx;
    }

    public String getIdx() {
        return idx;
    }

    public String getName() {
        return name;
    }

    public String getProfileimg() {
        return profileimg;
    }

    public String getRegion_name() {
        return region_name;
    }
}

class stadiumInfo{
    private String name, idx, profileimg, location;

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfileimg(String profileimg) {
        this.profileimg = profileimg;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIdx() {
        return idx;
    }

    public String getName() {
        return name;
    }

    public String getProfileimg() {
        return profileimg;
    }

    public String getLocation() {
        return location;
    }

}




