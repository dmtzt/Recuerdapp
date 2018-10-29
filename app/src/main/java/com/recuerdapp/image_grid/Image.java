package com.recuerdapp.image_grid;

/**
 * Created by 79192 on 20/11/2017.
 */

import android.graphics.Bitmap;

public class Image{
    private Bitmap image;
    private int idx;

    public Image(Bitmap image, int idx) {
        super();
        this.image = image;
        this.idx = idx;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }
}
