package com.csahmad.moodcloud;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.Manifest;

/** The activity for editing a {@link Profile}. */
public class EditProfileActivity extends AppCompatActivity {

    public static final int READ_CAMERA_REQUEST = 0;
    public static final int TAKE_IMAGE_REQUEST = 1;

    private Profile profile;
    private Account account;

    private EditText name;
    private EditText oldPassword;
    private EditText newPassword;
    private Bitmap image;

    private ImageButton imageButton;
    private Drawable defaultImage;
    private ImageButton deleteImageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // A back button
        ImageButton imageButton = (ImageButton) findViewById(R.id.backButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                finish();
            }}
        );

        this.imageButton = (ImageButton) this.findViewById(R.id.profilePicture);
        this.defaultImage = this.imageButton.getDrawable();
        this.deleteImageButton = (ImageButton) this.findViewById(R.id.deleteProfilePicture);

        this.profile = LocalData.getSignedInProfile(this);
        this.account = LocalData.getSignedInAccount(this);

        this.name = (EditText) this.findViewById(R.id.profileName);
        this.name.setText(profile.getName());
        this.setImage(profile.getImageBitmap());

        this.oldPassword = (EditText) this.findViewById(R.id.oldPassword);
        this.newPassword = (EditText) this.findViewById(R.id.newPassword);
    }

    /**
     * If the given image is null, set this.image to null and set the image for this.imageButton to
     * this.defaultImage. Otherwise, set both this.image and the image for this.imageButton to the
     * given image. Also handles hiding/showing this.deleteImageButton.
     *
     * @param image the image to use for this.image and this.imageButton
     */
    public void setImage(Bitmap image) {

        this.image = image;

        if (image == null) {
            this.imageButton.setImageDrawable(this.defaultImage);
            this.deleteImageButton.setVisibility(View.INVISIBLE);
        }

        else {
            this.imageButton.setImageBitmap(image);
            this.deleteImageButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Save the new {@link Profile}/{@link Account} information to elasticsearch and locally, then
     * exit this Activity.
     *
     * @param v
     */
    public void onSaveProfileClick(View v) {

        String nameText = this.name.getText().toString().trim();
        String oldPasswordText = this.oldPassword.getText().toString().trim();
        String newPasswordText = this.newPassword.getText().toString().trim();

        if (!newPasswordText.equals("")) {

            if (oldPasswordText.equals("")) {

                Toast.makeText(getApplicationContext(), "Must enter current password",
                        Toast.LENGTH_LONG).show();

                return;
            }

            if (!oldPasswordText.equals(this.account.getPassword())) {

                Toast.makeText(getApplicationContext(), "Incorrect password",
                        Toast.LENGTH_LONG).show();

                return;
            }

            this.account.setPassword(newPasswordText);
        }

        if (!nameText.equals(""))
            this.profile.setName(nameText);

        this.profile.setImage(this.image);

        AccountController accountController = new AccountController();
        this.account.setProfile(this.profile);
        accountController.addOrUpdateAccounts(this.account);
        LocalData.store(this.account, this);

        ProfileController profileController = new ProfileController();
        profileController.addOrUpdateProfiles(this.profile);


        this.finish();
    }

    /**
     * Allow the user to take an image to update the clicked profile image.
     *
     * @param v
     */
    public void onProfilePictureClicked(View v) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, TAKE_IMAGE_REQUEST);
        }

        else
            this.requestCameraPermission();
    }

    /**
     * Delete the profile image and update Views to represent this.
     *
     * @param v
     */
    public void onDeleteProfilePictureClicked(View v) {

        this.setImage(null);
    }

    /** Request permission from the user to use the camera. */
    public void requestCameraPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                READ_CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        switch (requestCode) {

            case TAKE_IMAGE_REQUEST:

                if (resultCode == RESULT_OK) {
                    Bundle extras = intent.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    this.setImage(imageBitmap);
                }

                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        switch (requestCode) {

            case READ_CAMERA_REQUEST:

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, TAKE_IMAGE_REQUEST);
                }

                break;
        }
    }
}
