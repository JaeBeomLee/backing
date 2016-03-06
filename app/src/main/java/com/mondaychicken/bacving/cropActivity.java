package com.mondaychicken.bacving;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.isseiaoki.simplecropview.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by leejaebeom on 2016. 1. 21..
 */
public class cropActivity extends AppCompatActivity implements View.OnClickListener {
    CropImageView cropImageView;
    ImageView croppedImageView;
    ImageButton cropButton, rotateButton;
    Button okButton, noButton;
    LinearLayout edit, check;
    Uri imageUri;
    int checkImage, CROP_CODE = 3;
    Bitmap cropImaageBitmap;

    String profileUploadFilePath;
    String profileUploadFileName = "team_profile.jpg";
    String coverUploadFilePath;
    String coverUploadFileName = "team_cover.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_crop);

        Intent intent = getIntent();
        imageUri = Uri.parse(intent.getExtras().getString("imageUri"));
        checkImage = intent.getExtras().getInt("checkImage");

        cropImageView = (CropImageView)findViewById(R.id.cropImageView);
        croppedImageView = (ImageView)findViewById(R.id.croppedImageView);
        cropButton = (ImageButton)findViewById(R.id.crop_button);
        rotateButton = (ImageButton)findViewById(R.id.rotate_button);
        okButton = (Button)findViewById(R.id.ok_button);
        noButton = (Button)findViewById(R.id.no_button);
        edit = (LinearLayout)findViewById(R.id.edit_btn);
        check = (LinearLayout)findViewById(R.id.check_btn);

        cropImageView.setImageBitmap(decodeBitmapToUri(imageUri));
        switch (checkImage){
            case 0:
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_1_1);
                break;
            case 1:
                cropImageView.setCropMode(CropImageView.CropMode.RATIO_16_9);
                break;
        }
        cropImageView.setHandleSizeInDp(4);
        cropImageView.setTouchPaddingInDp(16);
        cropImageView.setMinFrameSizeInDp(100);
        cropImageView.setHandleShowMode(CropImageView.ShowMode.SHOW_ALWAYS);
        cropImageView.setGuideShowMode(CropImageView.ShowMode.NOT_SHOW);


        cropButton.setOnClickListener(this);
        rotateButton.setOnClickListener(this);
        okButton.setOnClickListener(this);
        noButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.crop_button :
                cropImaageBitmap = cropImageView.getCroppedBitmap();
                croppedImageView.setImageBitmap(cropImaageBitmap);
                cropImageView.setVisibility(View.GONE);
                croppedImageView.setVisibility(View.VISIBLE);
                edit.setVisibility(View.GONE);
                check.setVisibility(View.VISIBLE);
                break;
            case R.id.rotate_button :
                cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
                break;
            case R.id.ok_button :
                saveImageFile(cropImaageBitmap);

                Intent finish = new Intent();
                setResult(RESULT_OK, finish);
                finish();
                break;
            case R.id.no_button :
                croppedImageView.setImageBitmap(null);
                croppedImageView.setVisibility(View.GONE);
                cropImageView.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
                check.setVisibility(View.GONE);
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

    public void saveImageFile(Bitmap bitmap){
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+ "/bacving/";
        switch (checkImage){
            case 0:
                profileUploadFilePath = path;
                break;
            case 1:
                coverUploadFilePath = path;
                break;
        }

        File directory = new File(path);

        File file = null;
        switch (checkImage){
            case 0:
                file = new File(directory, profileUploadFileName);
                break;
            case 1:
                file = new File(directory, coverUploadFileName);
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

}
