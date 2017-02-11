package com.example.robin.imagemanipulation_493;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static android.R.attr.data;

public class MainActivity extends AppCompatActivity {

    private ImageView imageV;
    private ImageManager IM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageV = (ImageView) findViewById(R.id.main_imageView);

        IM = new ImageManager(this.getApplicationContext());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }


    /*
     * Top right activity bar menu. Currently holds: Settings
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                //TODO: Launch settings activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
     * Image selecter when FAB is pressed. Allows to launch camera intent, or gallery pick intent.
     */
    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Delete from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    launchCameraIntent();
                } else if (options[item].equals("Choose from Gallery")) {
                    launchGalleryPickIntent();
                } else if (options[item].equals("Delete from Gallery")) {
                    launchGalleryDeleteIntent();
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }


    /*
     * Activity results. If camera launched, save the photo and display. If gallery launched,
     * show the selected photo.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RESULT_OK == resultCode) {

            switch (requestCode) {
                case Statics.CAMERA_REQUEST:
                    IM.saveImageToMediaGallery();
                    imageV.setImageBitmap(IM.loadLastStoredImage());
                    break;
                case Statics.GALLERY_ADD_REQUEST:
                    if(data != null) {
                        imageV.setImageBitmap(IM.loadImage(data));
                    }
                    break;
                case Statics.GALLERY_DEL_REQUEST:
                    if(data != null) {
                        IM.deleteImage(data);
                        imageV.setImageBitmap(null);
                        imageV.destroyDrawingCache();
                    }
            }
        }
    }

    /*
     * All intent launchers
     */
    public void launchCameraIntent() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        File photoFile = null;
        try {
            photoFile = IM.createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
            showToast("Error 0x01: Error creating file in launchCameraIntent");
        }
        if (photoFile != null) {
            Uri photoUri = FileProvider.getUriForFile(this,
                    "com.example.android.fileprovider",
                    photoFile);

            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(cameraIntent, Statics.CAMERA_REQUEST);
        }
    }

    public void launchGalleryPickIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Statics.GALLERY_ADD_REQUEST);
    }

    public void launchGalleryDeleteIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Statics.GALLERY_DEL_REQUEST);
    }

    public void showToast(String str) {
        Toast.makeText(getApplicationContext(),
                str, Toast.LENGTH_SHORT).show();
    }



}

