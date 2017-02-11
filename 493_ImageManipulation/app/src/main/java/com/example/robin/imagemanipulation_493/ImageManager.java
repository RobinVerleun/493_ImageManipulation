package com.example.robin.imagemanipulation_493;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by robin on 09/02/17.
 */

public class ImageManager {

    private Context context;
    public String mCurrentPhotoPath;

    public ImageManager(Context _context) {
        context = _context;
    }

    public boolean saveImageToMediaGallery() {

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        try {
            ContentValues cv = new ContentValues();
            cv.put(MediaStore.Images.Media.TITLE, String.valueOf(UUID.randomUUID()));
            cv.put(MediaStore.Images.Media.DISPLAY_NAME, "Whatever");
            cv.put(MediaStore.Images.Media.DATE_MODIFIED, System.currentTimeMillis());
            cv.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
            cv.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

            Uri url = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
            OutputStream os = context.getContentResolver().openOutputStream(url);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, os);
            os.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Bitmap loadLastStoredImage() {

        Bitmap b = null;
        try {
            File f = new File(mCurrentPhotoPath);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return b;
    }

    public Bitmap loadImage(String path) {
        return BitmapFactory.decodeFile(path);
    }

    public Bitmap loadImage(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();

        return BitmapFactory.decodeFile(picturePath);
    }

    public void deleteImage(Intent data) {
        Uri selectedImage = data.getData();
        context.getContentResolver().delete(selectedImage, null, null);
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }



}

