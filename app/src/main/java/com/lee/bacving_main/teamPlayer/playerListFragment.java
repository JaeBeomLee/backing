package com.lee.bacving_main.teamPlayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lee.bacving_main.main.MainPage;
import com.lee.bacving_main.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ijaebeom on 2015. 8. 18..
 */
public class playerListFragment extends Fragment implements RecyclerView.OnItemTouchListener, View.OnClickListener{

    //선수 목록 추가 다이얼로그 변수
    private AlertDialog alertDialog = null;
    //갤러리와 사진 촬영 선택 다이얼로그 변
    private AlertDialog alertDialog2 = null;
    //선수 목록에 쓰일 이름 번호 포지션 변수
    String profileName;
    //선수 목록에 쓰일 이미지 Uri변수
    Bitmap profileImage;
    //listLayout 위치값
    int position = 0;
    //intent써서 onResultActivity에서 사용될 코드 값 (갤러리, 카메라)
    int GALLARY_CODE = 1, CAMERA_CODE = 2;

    //우측 하단에 띄워져 있는 버튼
    FloatingActionButton fab;
    //RecyclerView에서 사용되는 레이아웃 메니저
    LinearLayoutManager manager;
    RecyclerView recyclerView;
    //listLayout에서 사용되는 리스트를 제어하는 adapter
    playerListAdapter adapter;
    //이름, 번호, 포지션, 이미지 등의 데이터를 담고 있는 listData형 List를 만들어 하나의 ArrayList로 변수 선언
    List<playerListData> result = new ArrayList<playerListData>();

    //다이얼로그에 필요한 Bundle과 Context
    Bundle bundle;
    Context context;

    //DialogAdd() 안에 있어야 하지만 onActivityResult()에서도 사용되기 때문에 전역변수로 만들어 줌
    ImageView profileImageEdit = null;

    GestureDetectorCompat gestureDetector;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bundle = savedInstanceState;
        context = container.getContext();
        View v= inflater.inflate(R.layout.team_list_view, container, false);
        fab = (FloatingActionButton)v.findViewById(R.id.fab);

        recyclerView = (RecyclerView)v.findViewById(R.id.recyclerView);
        manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.scrollToPosition(position);
        recyclerView.setLayoutManager(manager);
        adapter = new playerListAdapter(result);
        recyclerView.setAdapter(adapter);
        //우측 하단의 FloatingActionButton을 눌렀을때 나타나는 현상
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DialogAdd() 내용의 다이얼로그 생성
                alertDialog = DialogAdd();
                //DialogAdd() 내용의 다이얼로그 보여줌
                alertDialog.show();
            }
        });
        gestureDetector =
                new GestureDetectorCompat(getContext(), new RecyclerViewDemoOnGestureListener());
        return v;
    }

    // 선수단 리스트 한명씩 만드는 다이얼로그 이름, 이미지 내용이 들어감 이미지는 DialogImageChoose()를 통해 갤러리와 카메라를 선택해 이미지 사용
    private AlertDialog DialogAdd(){
        final View innerView = getLayoutInflater(bundle).inflate(R.layout.list_item_add_layout, null);
        final AlertDialog.Builder build = new AlertDialog.Builder(context);

        //이름, 번호, 포지션을 정하주는 EditText 선언
        final TextView profileNameEdit = (TextView)innerView.findViewById(R.id.profile_name_edit);
        profileImageEdit = (ImageView)innerView.findViewById(R.id.profile_img_edit);

        //프로필 이미지 설정이 안되있을 때 이미지를 클릭해 이미지를 선택.
        //이 때 DialogImageChoose()다이얼로그로 넘어가 갤러리와 카메라를 선택.
        profileImageEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog2 = DialogImageChoose();
                alertDialog2.show();
            }
        });
        build.setView(innerView);
        //확인 버튼 활성화 및 버튼을 눌렀을때 나타나는 현상 설정
        build.setPositiveButton("확인", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //각각의 EditText에서 정한 값을 String 변수에 저장
                profileName = profileNameEdit.getText().toString();
                //어뎁터를 이용해 createList()의 listData값과 리스트 위치값을 넘겨줌.
                adapter.add(createList(), position);
                //List의 보여주는 위치를 재설정.
                manager.scrollToPosition(position);

            }
        });
        return build.create();
    }

    // 프로필 이미지를 설정할때 갤러리를 사용할지 카메라를 사용할지 선택하는 다이얼로그
    private AlertDialog DialogImageChoose(){
        final View innerView = getLayoutInflater(bundle).inflate(R.layout.image_method_choose_layout, null);
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
                alertDialog2.cancel();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(MediaStore.EXTRA_OUTPUT, "profile.jpg");
                startActivityForResult(i, CAMERA_CODE);
                alertDialog2.cancel();
            }
        });
        build.setView(innerView);

        return build.create();
    }

    // 리스트를 만들어 주는 메서드
    private playerListData createList() {

        playerListData data = new playerListData();
        data.setName(profileName);
        data.setImage(profileImage);
        return data;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLARY_CODE){
            if (resultCode == Activity.RESULT_OK){
                //추후 변경됩니다.
                profileImageEdit.setImageBitmap(profileImage);
            }
        }else if (requestCode == CAMERA_CODE){
            if (resultCode == Activity.RESULT_OK){
                //추후 변경됩니다.
                profileImageEdit.setImageBitmap(profileImage);
            }
        }
    }

    public Uri getPhotoFileUri(String fileName){
        if (isExternalStorageAvailable()){
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"bacving");
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdir()){
                Log.d("박빙", "failed to create directory");
            }
            File file = new File(mediaStorageDir.getPath(), fileName);

            return Uri.fromFile(file);
        }
        return null;
    }

    public boolean isExternalStorageAvailable(){
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.player_container_item){
            Intent intent = new Intent(getActivity(), MainPage.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    private class RecyclerViewDemoOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
            onClick(view);
            return super.onSingleTapConfirmed(e);
        }
    }
}
