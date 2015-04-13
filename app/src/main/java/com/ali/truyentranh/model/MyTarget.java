package com.ali.truyentranh.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by AliPro on 4/11/2015.
 */

public class MyTarget implements Target {
    String name = "default";

    public MyTarget() {
        File f = new File(Environment.getExternalStorageDirectory(),
                Tools.folder);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    public void setName(String name) {
        this.name = Tools.md5(name);
    }

    public String getPath(String name) {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + Tools.folder + "/" + name + ".jpg");
        if(file.exists()) {
            return Environment.getExternalStorageDirectory().getPath() + "/" + Tools.folder + "/" + name + ".jpg";
        }
        return "";
    }

    @Override
    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + Tools.folder + "/" + name + ".jpg");
                int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
                if(!file.exists() || file_size <= 0) {
                    try {
                        file.createNewFile();
                        FileOutputStream ostream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, ostream);
                        ostream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        if (placeHolderDrawable != null) {
        }
    }
}
