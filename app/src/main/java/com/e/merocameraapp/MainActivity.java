package com.e.merocameraapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Button btnImage;
    private ImageView image_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        btnImage=findViewById(R.id.button_image);
        image_view=findViewById(R.id.imageview);


        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            loadCamera();
            }
        });

    }
    private void  checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
    }
    private void loadCamera(){
        Intent intent =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent,1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK){
            Bundle extras=data.getExtras();
            Bitmap imageBitmap=(Bitmap) extras.get("data");

            String partFilename=currentDateFormat();
            storeCameraPhotoInSDCard(imageBitmap,partFilename);


            image_view.setImageBitmap(imageBitmap);
        }
    }
    private String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }
    private void storeCameraPhotoInSDCard(Bitmap bitmap, String currentDate){
        File outputFile = new File(Environment.getExternalStorageDirectory(), "photo_" + currentDate + ".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
