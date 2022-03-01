package com.zaiko.mylibrary;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

public class CargarImagen extends EntradaMultiTouch {
    private static final double ESCALAFACTORINICIAL = 0.15;
    private transient Drawable mDrawable;
    private final Bitmap mBitmap;

    public CargarImagen(Bitmap bitmap, Resources res) {
        super(res);
        mBitmap = bitmap;
    }

    public CargarImagen(@NonNull CargarImagen e, Resources res) {
        super(res);
        mDrawable = e.mDrawable;
        mBitmap = e.mBitmap;
        mEscalaX = e.mEscalaX;
        mEscalaY = e.mEscalaY;
        mCentroX = e.mCentroX;
        mCentroY = e.mCentroY;
        mAngulo = e.mAngulo;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.save();
        float dx = (mMaxX + mMinX) / 2;
        float dy = (mMaxY + mMinY) / 2;

        mDrawable.setBounds((int) mMinX, (int) mMinY, (int) mMaxX, (int) mMaxY);
        canvas.translate(dx, dy);
        canvas.rotate(mAngulo * 180.0f / (float) Math.PI);
        canvas.translate(-dx, -dy);
        mDrawable.draw(canvas);
        canvas.restore();
    }

    @Override
    public void load(@NonNull Context context, float startMidX, float startMidY) {
        Resources res = context.getResources();
        getMetricas(res);

        mStartMidX = startMidX;
        mStartMidY = startMidY;

        mDrawable = new BitmapDrawable(res, mBitmap);

        mWidth = mDrawable.getIntrinsicWidth();
        mHeight = mDrawable.getIntrinsicHeight();

        float centroX;
        float centroY;
        float escalaX;
        float escalaY;
        float angle;
        if (mFirstLoad) {
            centroX = startMidX;
            centroY = startMidY;

            float scaleFactor = (float) (Math.max(mDisplayWidth, mDisplayHeight) /
                    (float) Math.max(mWidth, mHeight) * ESCALAFACTORINICIAL);
            escalaX = escalaY = scaleFactor;
            angle = 0.0f;

            mFirstLoad = false;
        } else {
            centroX = mCentroX;
            centroY = mCentroY;
            escalaX = mEscalaX;
            escalaY = mEscalaY;
            angle = mAngulo;
        }
        setPosicion(centroX, centroY, escalaX, escalaY, mAngulo);
    }

    @Override
    public void unload() {
        this.mDrawable = null;
    }
}
