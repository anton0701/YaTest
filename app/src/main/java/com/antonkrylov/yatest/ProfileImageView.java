package com.antonkrylov.yatest;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

/*
    ProfileImageView - класс-наследник ImageView, в котором переопределен метод setImageBitmap,
    так что большое изображение артиста масштабируется и обрезается для показа на втором Activity.
 */
public class ProfileImageView extends ImageView {
    private Bitmap bm;

    public ProfileImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        bm = null;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (bm != null) {
            setImageBitmap(bm);
            bm = null;
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {

        int viewWidth = getWidth();
        int viewHeight = getHeight();
        if (viewWidth == 0 || viewHeight == 0) {
            this.bm = bm;
            return;
        }
        float widthRatio = (float)viewWidth/bm.getWidth();
        Bitmap scaledBm = null;
        if(widthRatio < 1f) {
            scaledBm = Bitmap.createScaledBitmap(bm, Math.round(bm.getWidth()*widthRatio), Math.round(bm.getHeight()*widthRatio), false);
        }
        else {
            scaledBm = bm;
        }
        Bitmap newBitmap = Bitmap.createBitmap(scaledBm, 0, 0, Math.min(viewWidth, scaledBm.getWidth()), Math.min(viewHeight, scaledBm.getHeight()));

        super.setImageBitmap(newBitmap);
    }



}

