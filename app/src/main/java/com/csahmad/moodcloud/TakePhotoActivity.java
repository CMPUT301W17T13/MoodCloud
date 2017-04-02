package com.csahmad.moodcloud;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakePhotoActivity extends Activity {

    public static final int READ_CAMERA_REQUEST = 0;
    private static final int TAKE_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        ImageButton ib = (ImageButton) findViewById(R.id.take);



        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeAPhoto();
            }
        });
        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                finish();
            }}
        );
    }
    /**
     * Return whether this app currently has permission to use the camera.
     *
     * @return whether this app currently has permission to use the camera
     */
    public boolean haveCameraPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED)

            return false;

        return true;
    }

    /** Request permission from the user to use the camera. */
    public void requestCameraPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                READ_CAMERA_REQUEST);
    }

    public void takeAPhoto() {

        if (!haveCameraPermission()) requestCameraPermission();

        if (haveCameraPermission()) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, TAKE_IMAGE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if (requestCode == TAKE_IMAGE_REQUEST){
            if (resultCode == RESULT_OK){
                Toast.makeText(getApplicationContext(), "Photo taken!", Toast.LENGTH_LONG).show();
                ImageView imageView = (ImageView) findViewById(R.id.photo);
                Bundle extras = intent.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("IMAGE", ImageConverter.toString(imageBitmap));
                returnIntent.putExtra("BITMAP", imageBitmap);
                setResult(Activity.RESULT_OK, returnIntent);


                //finish();

            }
            else
            if (resultCode == RESULT_CANCELED)
                Toast.makeText(getApplicationContext(), "Photo was cancelled !", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "Unknown bug! Please report!", Toast.LENGTH_LONG).show();
        }
    }

}
