package com.csahmad.moodcloud;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
    File imageFile;
    public static final int MEDIA_TYPE_IMAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        ImageButton ib = (ImageButton) findViewById(R.id.take);
        //ImageView moodPhoto;


        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeAPhoto();
            }
        });
    }
    public void takeAPhoto() {
        //String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MoodCloudPictures";
        //File folder = new File(path);
        //if (!folder.exists())
          //  folder.mkdir();
        //String imagePathAndFileName = path + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg";
        //File imageFile = new File(imagePathAndFileName);
        //imageFileUri = Uri.fromFile(getOutputMediaFile(MEDIA_TYPE_IMAGE));
        //imageFileUri = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        //imageFileUri = Uri.fromFile(imageFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFile);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED)

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED)

            startActivityForResult(intent, 12345);

        else
            Toast.makeText(getApplicationContext(), "You denied permission", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if (requestCode == 12345){
            if (resultCode == RESULT_OK){
                Toast.makeText(getApplicationContext(), "Photo taken!", Toast.LENGTH_LONG).show();
                ImageView moodPhoto = (ImageView) findViewById(R.id.photo);
                Log.d("file name", imageFile.toString());
                Log.d("file exists", Boolean.toString(imageFile.exists()));
                Drawable image = Drawable.createFromPath(imageFile.toString());
                if (image == null) Log.d("gggggggggggg", "is null");
                else Log.d("sdfs", image.toString());
                moodPhoto.setImageDrawable(Drawable.createFromPath(imageFile.toString()));
                //moodPhoto.setImageURI(imageFileUri);
            }
            else
            if (resultCode == RESULT_CANCELED)
                Toast.makeText(getApplicationContext(), "Photo was cancelled !", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "Unknown bug! Please report!", Toast.LENGTH_LONG).show();
        }
    }
    /*private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
    }*/
    private File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        //File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
           //     Environment.DIRECTORY_PICTURES), "MoodCloud");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        //if (! mediaStorageDir.exists()){
          //  if (! mediaStorageDir.mkdirs()){
            //    Log.d("MyCameraApp", "failed to create directory");
              //  return null;
            //}
        //}

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            Log.d("hhhhhhhhhhhhhh", this.getFilesDir().toString());
            mediaFile = new File(this.getFilesDir().toString() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
            Log.d("exists now", Boolean.toString(mediaFile.exists()));
        }  else {
            return null;
        }

        return mediaFile;
    }
}
