package com.example.sriram.singleimage2;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by Sriram on 10-01-2018.
 */

public class UploadPojo {

    private Bitmap image;
    private int year;
    public UploadPojo (Bitmap image)
    {
        this.image=image;

    }
    public void setImage(Bitmap image)
    {
        this.image=image;
    }
    public Bitmap getImage()
    {
        return image;
    }
}

