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
    protected boolean primeraCarga = true;

    protected transient Paint paint = new Paint();

    protected int ancho;
    protected int altura;

    // anchura/altura de la pantalla
    protected int anchoPantalla;
    protected int alturaPantalla;

    protected float posicionCentroX;
    protected float posicionCentroY;
    protected float escalaPosicionX;
    protected float escalaPosicionY;
    protected float angulo;

    protected float mMinX;
    protected float mMaxX;
    protected float mMinY;
    protected float mMaxY;

    /**
     * área de la entidad que se puede escalar/rotar
     * usando un solo toque (crece desde abajo a la derecha)
     */
    protected final static int TAMANIO_GRAVEDAD_AREA = 40;
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

    public int getAncho() {
        return ancho;
    }

    public int getAltura() {
        return altura;
    }

    public float getPosicionCentroX() {
        return posicionCentroX;
    }

    public float getPosicionCentroY() {
        return posicionCentroY;
    }

    public float getEscalaPosicionX() {
        return escalaPosicionX;
    }

    public float getEscalaPosicionY() {
        return escalaPosicionY;
    }

    public float getAngulo() {
        return angulo;
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


    protected void getMetricas(@NonNull Resources resources) {
        DisplayMetrics metrics = resources.getDisplayMetrics();
        anchoPantalla =
                (resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                        ? Math.max(metrics.widthPixels, metrics.heightPixels)
                        : Math.min(metrics.widthPixels, metrics.heightPixels);
        alturaPantalla =
                (resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                        ? Math.min(metrics.widthPixels, metrics.heightPixels)
                        : Math.max(metrics.widthPixels, metrics.heightPixels);
    }

    /**
     * Establecer la posición y la escala de una imagen en coordenadas de la pantalla
     */
    public boolean setPosicion(ControlesMultiTouch.PositionAndScale posicionYscala) {
        float nuevaEscalaX;
        float nuevaEscalaY;

        if ((mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0) {
            nuevaEscalaX = posicionYscala.getScaleX();
        } else {
            nuevaEscalaX = posicionYscala.getScale();
        }

        if ((mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0) {
            nuevaEscalaY = posicionYscala.getScaleY();
        } else {
            nuevaEscalaY = posicionYscala.getScale();
        }

        return setPosicion(posicionYscala.getXOff(),
                posicionYscala.getYOff(),
                nuevaEscalaX,
                nuevaEscalaY,
                posicionYscala.getAngle());
    }

    /**
     * Establecer la posición y la escala de una imagen en coordenadas de pantalla
     */
    protected boolean setPosicion(float centroX, float centroY,
                                  float escalaX, float escalaY, float anguloInicial) {
        float ws = (float) (ancho / 2) * escalaX;
        float hs = (float) (altura / 2) * escalaY;

        mMinX = centroX - ws;
        mMinY = centroY - hs;
        mMaxX = centroX + ws;
        mMaxY = centroY + hs;

        mGrabAreaX1 = mMaxX - TAMANIO_GRAVEDAD_AREA;
        mGrabAreaY1 = mMaxY - TAMANIO_GRAVEDAD_AREA;
        mGrabAreaX2 = mMaxX;
        mGrabAreaY2 = mMaxY;

        posicionCentroX = centroX;
        posicionCentroY = centroY;
        escalaPosicionX = escalaX;
        escalaPosicionY = escalaY;
        angulo = anguloInicial;

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
        primeraCarga = false;
        load(context, posicionCentroX, posicionCentroY);
    }

    public abstract void draw(Canvas canvas);

    public abstract void load(Context context, float iniciarDireccionX, float iniciarDireccionY);

    public abstract void unload();

    public void setIsGrabAreaSelected(boolean selected) {
        mIsGrabAreaSeleccionar = selected;
    }

    public boolean isGrabAreaSelected() {
        return mIsGrabAreaSeleccionar;
    }
}
