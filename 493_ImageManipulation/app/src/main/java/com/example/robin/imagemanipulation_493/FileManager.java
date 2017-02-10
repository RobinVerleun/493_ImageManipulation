package com.example.robin.imagemanipulation_493;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * Created by robin on 09/02/17.
 */

public class FileManager {


    private final WeakReference<ImageView> imgViewRef;
    private Context context;

    public FileManager(Context _context, ImageView _img) {
        context = _context;
        imgViewRef = new WeakReference<ImageView>(_img);
    }

    private String saveImageToInternalStorage(Bitmap bitmap) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(mypath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private void loadImageFromStorage(String path) {

        try {
            File f = new File(path, "profile.jpeg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            if(imgViewRef != null) {
                final ImageView img = imgViewRef.get();

                if(img != null) {
                    img.setImageBitmap(b);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

