package com.mondaychicken.bacving.main.etc;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.mondaychicken.bacving.R;
import com.mondaychicken.bacving.cropActivity;
import com.mondaychicken.bacving.gcm.QuickstartPreferences;
import com.mondaychicken.bacving.gcm.RegistrationIntentService;
import com.mondaychicken.bacving.main.MainPage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by leejaebeom on 2015. 10. 9..
 */
public class updateProfile extends AppCompatActivity {
    Context context;

    EditText memberPhone;
    TextView memberBirthText;
    Button updateMember, man, woman;
    android.support.v7.app.AlertDialog.Builder builder;

    RelativeLayout profileEdit;
    Bitmap profileImage;
    CircleImageView profile;
    DatePicker dp;

    int permissionCheckCamera, permissionCheckStorage;
    Uri imageUri;
    int GALLARY_CODE = 1, CAMERA_CODE = 2, CROP_CODE = 3;

    int year;
    int month;
    int date;

    private AlertDialog alertDialog = null;
    private AlertDialog alertDialog2 = null;

    String phoneS = null, birthS = null, userno = null, token = null, sex = null, userEmail, userPw, profileImg, userPhone = null, userBirth = null, userGender = null, userToken= null;
    //0 == profile, 1 == cover
    int checkImage;
    int acception,teamSportsS, CAMERA, GALLERY;
    String profileUploadFilePath;
    String profileUploadFileName = "member_profile.jpg";
    String serverUpdate = "http://52.68.69.47/api/member/update.php";
    String serverLogin = "http://52.68.69.47/api/member/index.php";
    String logintoken;
    String serverResult;
    int code = 0;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private BroadcastReceiver RegistrationBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_update);

        context = updateProfile.this;

        registBroadcastReceiver();
        getInstanceIdToken();

        LoadPreference(context);

        Login();

        Toolbar toolbar = (Toolbar)findViewById(R.id.tool_bar_member_update);
        toolbar.setTitleTextColor(0xffffffff);
        toolbar.setTitle("프로필 수정");

        profile = (CircleImageView)findViewById(R.id.member_create_profile);
        profileEdit = (RelativeLayout)findViewById(R.id.member_create_profile_edit);
        updateMember = (Button)findViewById(R.id.member_update_btn);
        man = (Button)findViewById(R.id.member_man);
        woman = (Button)findViewById(R.id.member_woman);
        memberBirthText = (TextView)findViewById(R.id.member_birth_text);
        memberPhone = (EditText)findViewById(R.id.member_phone);

        memberBirthText.setText("생년월일");

        if (savedInstanceState != null){
            profileImage = savedInstanceState.getParcelable("profileBitmap");
            profileUploadFilePath = savedInstanceState.getString("profilePath");
            imageUri = savedInstanceState.getParcelable("imageUri");

            checkImage = savedInstanceState.getInt("checkImage");
            if (profileImage != null){
                profile.setImageBitmap(profileImage);
            }
        }

        profileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkImage = 0;
                SavePreference("checkImage", checkImage, updateProfile.this);
                permissionCheckCamera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);

                int MyResultID = 1;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (permissionCheckCamera == PackageManager.PERMISSION_DENIED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MyResultID);
                    }
                }

                if (permissionCheckCamera == PackageManager.PERMISSION_DENIED){
//                    alertDialog = null;
//                    alertDialog.dismiss();
                }else{
                    //DialogAdd() 내용의 다이얼로그 생성
                    alertDialog = DialogImageChoose();
                    //DialogAdd() 내용의 다이얼로그 보여줌
                    alertDialog.show();
                }
            }
        });


        updateMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneS = memberPhone.getText().toString();
                if(memberPhone.getText().toString().equals("")){
                    phoneS = null;
                }
                if ((year != 0) && (month != 0) && (date != 0)){
                    birthS = year + "-" + month + "-" +date;
                }else{
                    birthS = null;
                }
                update();
            }
        });

        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex = "1";
                man.setBackgroundResource(R.drawable.man_style_able);
                man.setTextColor(0xffffffff);
                woman.setBackgroundResource(R.drawable.woman_style_unable);
                woman.setTextColor(0xffe74c3c);
            }
        });

        woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex = "2";
                woman.setBackgroundResource(R.drawable.woman_style_able);
                woman.setTextColor(0xffffffff);
                man.setBackgroundResource(R.drawable.man_style_unable);
                man.setTextColor(0xff5677fa);
            }
        });

    }

    public void memberBirth(View v){
        switch (v.getId()){
            case R.id.member_birth:
                alertDialog = DialogDatePicker();
                alertDialog.show();
                break;
        }
    }

    public void update(){

        final ProgressDialog dialog = ProgressDialog.show(this, "", "잠시만 기다려 주세요",true);

        new Thread(){
            @Override
            public void run() {
                updateServer();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateEnd();

                    }
                });
                dialog.dismiss();
            }
        }.start();
    }
    private void updateEnd(){

        String result = serverResult;
        if (result!= null){
            Log.d("result", result);
        }

        String msg = "", gender = null, phone = null, birth = null, profileimg = null;

        if (result == null){
            return;
        }
        try{
            JSONObject json = new JSONObject(result);
            code = json.getInt("code");
            msg = json.getString("message");
            JSONObject update_date = json.getJSONObject("update_data");
            Iterator<String> iter = update_date.keys();
            switch (iter.next()){
                case "gender":
                    gender = update_date.getString("gender");
                    break;
                case "profileimg":
                    profileimg = update_date.getString("profileimg");
                    SavePreference("profileimg", profileimg, this);
                    break;
                case "birth" :
                    birth = update_date.getString("birth");
                    break;
                case "phone" :
                    phone = update_date.getString("phone");
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("failed", result);

        } catch (NullPointerException e){
            e.printStackTrace();

            Log.d("failed", result);
            code = 0;
            msg = "failed";
        }

        if (code == 700){
            Toast.makeText(this,"변경되었습니디.",Toast.LENGTH_SHORT).show();

//            SavePreference("profileimg", profileImg, this);
            finish();
            MainPage.MainPage.finish();
            Intent intent = new Intent(this, MainPage.class);
            intent.putExtra("token", userToken);
            startActivity(intent);

        }else if(code == 425){
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    alertDialog2 = DialogInit("알 수 없는 오류","알 수 없습니다.");
                    alertDialog2.show();
                }
            });
            Log.d("failed", code + " " + msg);
        }else{
            alertDialog2 = DialogInit("오류",msg);
            alertDialog2.show();
            Log.d("failed", code + " " + msg);
            Log.d("result", result);
        }
    }
    private void updateServer(){

        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphen = "--";
        String boundary = "SpecificString";
        int byteRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        File profileFile = new File(profileUploadFilePath,profileUploadFileName);
        profileImage = null;
        try {

            serverResult = null;

            URL url = new URL(serverUpdate);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            http.setRequestProperty("ENCTYPE", "multipart/form-data");
            http.setRequestProperty("Connection", "Keep-Alive");
            http.setReadTimeout(15000);
            http.setConnectTimeout(15000);

            dos = new DataOutputStream(http.getOutputStream());


            dos.writeBytes("\r\n--" + boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"user_idx\"" + lineEnd + lineEnd + userno);
            dos.writeBytes("\r\n--"+ boundary + "\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"token\"" +lineEnd + lineEnd + token);
            if (phoneS!=null){
                dos.writeBytes("\r\n--"+ boundary + "\r\n");
                dos.writeBytes("Content-Disposition: form-data; name=\"phone\"" + lineEnd + lineEnd + phoneS);
            }

            if (sex != null){
                dos.writeBytes("\r\n--" + boundary + "\r\n");
                dos.writeBytes("Content-Disposition: form-data; name=\"gender\"" + lineEnd + lineEnd + sex);
            }

            if (birthS != null){
                dos.writeBytes("\r\n--"+ boundary + "\r\n");
                dos.writeBytes("Content-Disposition: form-data; name=\"birth\"" +lineEnd + lineEnd + birthS);
            }

            if (profileFile.exists()){
                try{
                    dos.writeBytes("\r\n--"+ boundary + "\r\n");
                    dos.writeBytes("Content-Disposition: form-data; name=\"profileimg\";filename=\"" + profileUploadFileName + "\"" + lineEnd);
                    dos.writeBytes("Content-Type: application/octet-stream" + lineEnd + lineEnd);
                    FileInputStream fis = new FileInputStream(profileFile);

                    bytesAvailable = fis.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    buffer = new byte[bufferSize];

                    byteRead = fis.read(buffer, 0, bufferSize);

                    while (byteRead > 0){
                        DataOutputStream dataWrite = new DataOutputStream(http.getOutputStream());

                        dataWrite.write(buffer, 0, bufferSize);
                        bytesAvailable = fis.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        byteRead = fis.read(buffer, 0, bufferSize);
                    }

                    dos.writeBytes(lineEnd + twoHyphen + boundary + twoHyphen +lineEnd);

                    fis.close();
                }catch (FileNotFoundException e){
                    dos.writeBytes("\r\n--"+ boundary + "\r\n");
                    dos.writeBytes("Content-Disposition: form-data; name=\"profileimg\"" +lineEnd + lineEnd + null);
                }
            }else{
                dos.writeBytes("\r\n--"+ boundary + "\r\n");
                dos.writeBytes("Content-Disposition: form-data; name=\"profileimg\";filename=\"" + "" + "\"" + lineEnd);
                dos.writeBytes("Content-Type: application/octet-stream" + lineEnd + lineEnd);
            }


            InputStreamReader ims = new InputStreamReader(http.getInputStream(),"UTF-8");
            BufferedReader reader = new BufferedReader(ims);
            StringBuilder builder = new StringBuilder();
            String str;

            while ((str = reader.readLine()) != null){
                builder.append(str);
            }
            dos.flush();
            dos.close();
            ims.close();

            serverResult = builder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ConnectException e){
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


    public void Login(){
            final ProgressDialog dialog = ProgressDialog.show(this, "", "잠시만 기다려 주세요",true);
//
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
                    dialog.dismiss();
            }
        }.start();
    }

    private void LoginEnd(){
        String result = serverResult;

        int code = 0;
        String msg = "";
        String account = null,userno = null, userEmail= null, userPw= null, userName= null, userProfileimg= null, userStatus= null, userGrade= null;

        try{
            JSONObject json = new JSONObject(result);
            code = json.getInt("code");
            msg = json.getString("message");
            account = json.getString("account");
            JSONObject accountJson = new JSONObject(account);

            userno = accountJson.getString("userno");
            userProfileimg = accountJson.getString("profileimg");
            userToken = json.getString("token");
            userBirth = accountJson.getString("birth");
            userGender = accountJson.getString("gender");
            userPhone = accountJson.getString("phone");
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
            code = 0;
            msg = "failed";
        }

        if (code == 700){
            Log.d("Sucess", result);
//            Toast.makeText(splash.this, msg, Toast.LENGTH_SHORT).show();
            token = userToken;
//            profileImg는 이 엑티비티에서 사용하는 프로필 이미지 주소 변수이고 userProfileimg는 LoginEnd()에서 주는 변수이다.
            profileImg = userProfileimg;
            memberPhone.setText(userPhone);
            memberBirthText.setText(userBirth);
            switch (userGender){
                case "1":
                    sex = "1";
                    man.setBackgroundResource(R.drawable.man_style_able);
                    man.setTextColor(0xffffffff);
                    woman.setBackgroundResource(R.drawable.woman_style_unable);
                    woman.setTextColor(0xffe74c3c);
                    break;
                case "2":
                    sex = "2";
                    woman.setBackgroundResource(R.drawable.woman_style_able);
                    woman.setTextColor(0xffffffff);
                    man.setBackgroundResource(R.drawable.man_style_unable);
                    man.setTextColor(0xff5677fa);
                    break;
            }
            Glide.with(this).load(profileImg).into(profile);
            Glide.get(this).clearMemory();
        }else if(code == 412){

        }else{

//            Log.d("failed", result);

        }
    }

    private void ConnectServer(){
        try {
            StringBuffer data = new StringBuffer();
            data.append("email").append("=").append(userEmail).append("&");
            data.append("pw").append("=").append(userPw).append("&");
            data.append("deviceid").append("=").append(token).append("&");
            data.append("type").append("=").append("2").append("&");
            data.append("mode").append("=").append("5").append("&");

            URL url = new URL(serverLogin);
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setDefaultUseCaches(false);
            http.setDoInput(true);
            http.setDoOutput(true);
            http.setRequestMethod("POST");
//            http.setRequestProperty("content-type", "application/x-www-form-urlencoded");

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
        } catch (ConnectException e) {
            e.printStackTrace();
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    builder.setTitle("인터넷 연결 오류")
                            .setMessage("인터넷 연결이 되어 있지 않습니다. 인터넷 연결을 확인해 주세요.")
                            .setPositiveButton("확인", null)
                            .show();
                }
            });
            moveTaskToBack(true);
            finish();

            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Uri getImageUri(String fileName){
        if (isExStorageAvailable()){
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+ "/bacving/";

            switch (checkImage){
                case 0:
                    profileUploadFilePath = path;
                    break;
            }
            File directory = new File(path);
            if (!directory.exists()){
                directory.mkdirs();
            }
            File file = new File(path,fileName);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Uri.fromFile(file);
        }
        return null;
    }

    public boolean isExStorageAvailable(){
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }
        return false;
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

    private AlertDialog DialogImageChoose(){
        final View innerView = getLayoutInflater().inflate(R.layout.image_method_choose_layout, null);
        final AlertDialog.Builder build = new AlertDialog.Builder(context);

        // 이미지 버튼을 이용하여 갤러리를 선택할지 카메라를 선택할지 정함
        final ImageButton gallery = (ImageButton)innerView.findViewById(R.id.select_gallery);
        final ImageButton camera = (ImageButton)innerView.findViewById(R.id.select_camera);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType(MediaStore.Images.Media.CONTENT_TYPE);
                i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, GALLARY_CODE);
                alertDialog.cancel();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionCheckStorage = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                int MyResultID = 2;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (permissionCheckStorage == PackageManager.PERMISSION_DENIED) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MyResultID);
                    }
                }

                if (permissionCheckStorage == PackageManager.PERMISSION_DENIED) {
//                    alertDialog = null;
//                    alertDialog.dismiss();
                } else {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    switch (checkImage) {
                        case 0:
                            i.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri(profileUploadFileName));
                            imageUri = getImageUri(profileUploadFileName);
                            break;
                    }
                    startActivityForResult(i, CAMERA_CODE);
                    alertDialog.cancel();
                }
            }
        });
        build.setView(innerView);

        return build.create();
    }

    public void saveImageFile(Bitmap bitmap){
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+ "/bacving/";
        switch (checkImage){
            case 0:
                profileUploadFilePath = path;
                break;
        }
        
        File directory = new File(path);
        
        File file = null;
        switch (checkImage){
            case 0:
                file = new File(directory, profileUploadFileName);
                break;
        }
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Bitmap decodeBitmapToUri(Uri uri) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Calculate inSampleSize
        options.inSampleSize = 2;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap result = null;

        try {
            result = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_CANCELED){
            if (requestCode == CAMERA_CODE){
                if (imageUri == null){
                    switch (checkImage){
                        case 0:
                            imageUri = getImageUri(profileUploadFileName);
                            break;
                    }
                }

                //이지점이 편집
                Intent cropMode = new Intent(updateProfile.this, cropActivity.class);
                cropMode.putExtra("imageUri", imageUri.toString()); //이미지 위치
                cropMode.putExtra("checkImage", checkImage); // 프로필인지 커버인지
                startActivityForResult(cropMode, CROP_CODE);

            } else if (requestCode == GALLARY_CODE){
                Uri selectImage = data.getData();
                String[] filrPathColumn = {MediaStore.Images.Media.DATA};
                Cursor c = this.getContentResolver().query(selectImage, filrPathColumn, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filrPathColumn[0]);
                switch (checkImage){
                    case 0:
                        profileUploadFilePath = c.getString(columnIndex);
                        profileImage = BitmapFactory.decodeFile(profileUploadFilePath);
                        saveImageFile(profileImage);
                        imageUri = getImageUri(profileUploadFileName);
//                        profile.setImageBitmap(profileImage);
                        break;
                }
                c.close();

                //이지점이 편집
                Intent cropMode = new Intent(updateProfile.this, cropActivity.class);
                cropMode.putExtra("imageUri", imageUri.toString()); //이미지 위치
                cropMode.putExtra("checkImage", checkImage); // 프로필인지 커버인지
                startActivityForResult(cropMode, CROP_CODE);
            } else if (requestCode == CROP_CODE){
                Uri selectImage = imageUri;
                switch (checkImage){
                    case 0:
                        profileImage = decodeBitmapToUri(selectImage);
                        profile.setImageBitmap(profileImage);
                        break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//    @Override
//    protected void onDestroy() {
//        if (profileImage != null){
//            profileImage.recycle();
//        }
//        super.onDestroy();
//    }

    private AlertDialog DialogDatePicker(){

        final View innerView = getLayoutInflater().inflate(R.layout.date_picker,null);
        dp = (DatePicker)innerView.findViewById(R.id.birth_picker);
        String[] dateArr = userBirth.split("-");
        dp.updateDate(Integer.valueOf(dateArr[0]), Integer.valueOf(dateArr[1]), Integer.valueOf(dateArr[2]));

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("생년월일");
        builder.setMessage("생년월일을 선택해 주세요");
        builder.setCancelable(true);
        builder.setView(innerView);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                year = dp.getYear();
                month = dp.getMonth()+1;
                date = dp.getDayOfMonth();
                memberBirthText.setText(year+"-"+ month+"-" + date);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    // 문자값저장
    public void SavePreference(String key, int value, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    // 문자값저장
    public void SavePreference(String key, String value, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    private void LoadPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        userno = sharedPreferences.getString("userno", null);
        userEmail = sharedPreferences.getString("email", null);
        userPw = sharedPreferences.getString("pw", null);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("profileBitmap", profileImage);
        outState.putString("profilePath", profileUploadFilePath);
        outState.putInt("checkImage", checkImage);
        outState.putParcelable("imageUri",imageUri);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(RegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(RegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_GENERATING));
        LocalBroadcastManager.getInstance(this).registerReceiver(RegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(RegistrationBroadcastReceiver);
        super.onPause();
    }

    public void getInstanceIdToken()
    {
        if (checkPlayServices())
        {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.d("test", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public void registBroadcastReceiver()
    {
        RegistrationBroadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String action = intent.getAction();

                if(action.equals(QuickstartPreferences.REGISTRATION_READY))
                {
                    Log.d("test", "READY");
                }
                else if(action.equals(QuickstartPreferences.REGISTRATION_GENERATING))
                {
                    Log.d("test", "GENERATING" );
                }
                else if(action.equals(QuickstartPreferences.REGISTRATION_COMPLETE))
                {
                    String token = intent.getStringExtra("token");
                    Log.d("test", "token : " + token);

                    logintoken = token;
                }

            }
        };
    }

}
