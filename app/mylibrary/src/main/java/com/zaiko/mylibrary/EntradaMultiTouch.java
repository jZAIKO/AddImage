package com.zaiko.mylibrary;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

import java.io.Serializable;

public abstract class EntradaMultiTouch implements Serializable {
    protected boolean mFirstLoad = true;

    protected transient Paint mPaint = new Paint();

    protected int mWidth;
    protected int mHeight;

    // width/height de la pantalla
    protected int mDisplayWidth;
    protected int mDisplayHeight;

    protected float mCentroX;
    protected float mCentroY;
    protected float mEscalaX;
    protected float mEscalaY;
    protected float mAngulo;

    protected float mMinX;
    protected float mMaxX;
    protected float mMinY;
    protected float mMaxY;

    /**
     * área de la entidad que se puede escalar/rotar
     * usando un solo toque (crece desde abajo a la derecha)
     */
    protected final static int GRAB_AREA_SIZE = 40;
    protected boolean mIsGrabAreaSeleccionar = false;
    protected boolean mIsLatestSeleccionar = false;

    protected float mGrabAreaX1;
    protected float mGrabAreaY1;
    protected float mGrabAreaX2;
    protected float mGrabAreaY2;

    protected float mStartMidX;
    protected float mStartMidY;

    private static final int UI_MODE_ROTAR = 1;
    private static final int UI_MODE_ANISOTROPIC_SCALE = 2;
    protected int mUIMode = UI_MODE_ROTAR;

    public EntradaMultiTouch() {

    }

    public EntradaMultiTouch(Resources resources) {
        getMetricas(resources);
    }

    protected void getMetricas(@NonNull Resources resources) {
        DisplayMetrics metrics = resources.getDisplayMetrics();
        mDisplayWidth =
                (resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                        ? Math.max(metrics.widthPixels, metrics.heightPixels)
                        : Math.min(metrics.widthPixels, metrics.heightPixels);
        mDisplayHeight =
                (resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                        ? Math.min(metrics.widthPixels, metrics.heightPixels)
                        : Math.max(metrics.widthPixels, metrics.heightPixels);
    }

    /**
     * Establecer la posición y la escala de una imagen en coordenadas de la pantalla
     */
    public boolean setPosicion(ControlesMultiTouch.PositionAndScale posicionYscala) {
        float newScaleX;
        float newScaleY;

        if ((mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0) {
            newScaleX = posicionYscala.getScaleX();
        } else {
            newScaleX = posicionYscala.getScale();
        }

        if ((mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0) {
            newScaleY = posicionYscala.getScaleY();
        } else {
            newScaleY = posicionYscala.getScale();
        }

        return setPosicion(posicionYscala.getXOff(),
                posicionYscala.getYOff(),
                newScaleX,
                newScaleY,
                posicionYscala.getAngle());
    }

    /**
     * Establecer la posición y la escala de una imagen en coordenadas de pantalla
     */
    protected boolean setPosicion(float centerX, float centerY,
                                  float scaleX, float scaleY, float angle) {
        float ws = (mWidth / 2) * scaleX;
        float hs = (mHeight / 2) * scaleY;

        mMinX = centerX - ws;
        mMinY = centerY - hs;
        mMaxX = centerX + ws;
        mMaxY = centerY + hs;

        mGrabAreaX1 = mMaxX - GRAB_AREA_SIZE;
        mGrabAreaY1 = mMaxY - GRAB_AREA_SIZE;
        mGrabAreaX2 = mMaxX;
        mGrabAreaY2 = mMaxY;

        mCentroX = centerX;
        mCentroY = centerY;
        mEscalaX = scaleX;
        mEscalaY = scaleY;
        mAngulo = angle;

        return true;
    }

    /**
     * Devuelve si las coordenadas de pantalla dadas están o no dentro de esta imagen
     */
    public boolean contienePunto(float touchX, float touchY) {
        return (touchX >= mMinX && touchX <= mMaxX && touchY >= mMinY && touchY <= mMaxY);
    }

    public boolean grabAreacontienePunto(float touchX, float touchY) {
        return (touchX >= mGrabAreaX1 && touchX <= mGrabAreaX2 &&
                touchY >= mGrabAreaY1 && touchY <= mGrabAreaY2);
    }

    public void reload(Context context) {
        // Hágale saber a la carga que las propiedades han cambiado, así que vuelva a cargarlas,
        // no retroceda y comience con los valores predeterminados
        mFirstLoad = false;
        load(context, mCentroX, mCentroY);
    }

    public abstract void draw(Canvas canvas);

    public abstract void load(Context context, float startMidX, float startMidY);

    public abstract void unload();

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public float getCenterX() {
        return mCentroX;
    }

    public float getCenterY() {
        return mCentroY;
    }

    public float getScaleX() {
        return mEscalaX;
    }

    public float getScaleY() {
        return mEscalaY;
    }

    public float getAngle() {
        return mAngulo;
    }

    public float getMinX() {
        return mMinX;
    }

    public float getMaxX() {
        return mMaxX;
    }

    public float getMinY() {
        return mMinY;
    }

    public float getMaxY() {
        return mMaxY;
    }

    public void setIsGrabAreaSelected(boolean selected) {
        mIsGrabAreaSeleccionar = selected;
    }

    public boolean isGrabAreaSelected() {
        return mIsGrabAreaSeleccionar;
    }


}
