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
    private transient Drawable drawable;
    private final Bitmap bitmap;

    public CargarImagen(Bitmap bitmap, Resources res) {
        super(res);
        this.bitmap = bitmap;
    }

    public CargarImagen(@NonNull CargarImagen e, Resources res) {
        super(res);
        drawable = e.drawable;
        bitmap = e.bitmap;
        escalaPosicionX = e.escalaPosicionX;
        escalaPosicionY = e.escalaPosicionY;
        posicionCentroX = e.posicionCentroX;
        posicionCentroY = e.posicionCentroY;
        angulo = e.angulo;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.save();
        float dx = (mMaxX + mMinX) / 2;
        float dy = (mMaxY + mMinY) / 2;

        drawable.setBounds((int) mMinX, (int) mMinY, (int) mMaxX, (int) mMaxY);
        canvas.translate(dx, dy);
        canvas.rotate(angulo * 180.0f / (float) Math.PI);
        canvas.translate(-dx, -dy);
        drawable.draw(canvas);
        canvas.restore();
    }

    @Override
    public void load(@NonNull Context context, float iniciarDireccionX, float iniciarDireccionY) {
        Resources res = context.getResources();
        getMetricas(res);

        mStartMidX = iniciarDireccionX;
        mStartMidY = iniciarDireccionY;

        drawable = new BitmapDrawable(res, bitmap);

        ancho = drawable.getIntrinsicWidth();
        altura = drawable.getIntrinsicHeight();

        float centroX;
        float centroY;
        float escalaX;
        float escalaY;
        float anguloInicial;
        if (primeraCarga) {
            centroX = iniciarDireccionX;
            centroY = iniciarDireccionY;

            float scaleFactor = (float) (Math.max(anchoPantalla, alturaPantalla) /
                    (float) Math.max(ancho, altura) * ESCALAFACTORINICIAL);
            escalaX = escalaY = scaleFactor;
            anguloInicial = 0.0f;

            primeraCarga = false;
        } else {
            centroX = posicionCentroX;
            centroY = posicionCentroY;
            escalaX = escalaPosicionX;
            escalaY = escalaPosicionY;
            anguloInicial = angulo;
        }
        setPosicion(centroX, centroY, escalaX, escalaY, anguloInicial);
    }

    @Override
    public void unload() {
        this.drawable = null;
    }
}
