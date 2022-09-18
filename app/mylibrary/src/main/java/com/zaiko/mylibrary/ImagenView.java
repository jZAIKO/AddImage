package com.zaiko.mylibrary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ImagenView extends View implements ControlesMultiTouch.MultiTouchObjectCanvas<EntradaMultiTouch> {
    private Path drawPath;
    // dibujo y pintura de lienzo
    private Paint drawPaint, canvasPaint;
    // color inicial
    private int paintColor = Color.TRANSPARENT;
    // canvas
    private Canvas drawCanvas;
    // canvas bitmap
    private Bitmap canvasBitmap;

    private final ArrayList<EntradaMultiTouch> imageIDs = new ArrayList<>();

    private final ControlesMultiTouch<EntradaMultiTouch> controlesMultiTouch = new ControlesMultiTouch<EntradaMultiTouch>(
            this);

    private final ControlesMultiTouch.PointInfo currTouchPoint = new ControlesMultiTouch.PointInfo();

    private final boolean mShowDebugInfo = true;

    private static final int UI_MODE_ROTATE = 1, UI_MODE_ANISOTROPIC_SCALE = 2;

    private int mUIMode = UI_MODE_ROTATE;

    private static final float SCREEN_MARGIN = 100;

    private int width;
    private int height;

    public ImagenView(Context context) {
        this(context, null);
    }

    public ImagenView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        setupDrawing();
    }

    public ImagenView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(@NonNull Context context) {
        Resources res = context.getResources();
        setBackgroundColor(Color.TRANSPARENT);

        DisplayMetrics metrics = res.getDisplayMetrics();
        int displayWidth = res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math
                .max(metrics.widthPixels, metrics.heightPixels) : Math.min(
                metrics.widthPixels, metrics.heightPixels);
        int displayHeight = res.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? Math
                .min(metrics.widthPixels, metrics.heightPixels) : Math.max(
                metrics.widthPixels, metrics.heightPixels);
    }

    /**
     * Llamar metodo para añadir la imagen
     */
    public void addImagen(@NonNull Context context, Bitmap bitmap) {
        Resources res = context.getResources();
        imageIDs.add(new CargarImagen(bitmap, res));
        float cx = getWidth() / 2;
        float cy = getHeight() / 2;
        imageIDs.get(imageIDs.size() - 1).load(context, cx, cy);
        invalidate();
    }

    /**
     * llamar metodo para obtener el total de items añadidos
     */
    public int getSize() {
        return imageIDs.size();
    }

    /**
     * llamar metodo para eliminar todos los items añadidos
     */
    public void EliminarAllImagen() {
        imageIDs.removeAll(imageIDs);
        invalidate();
    }

    /**
     * llamar metodo para eliminar uno por uno los items añadidos
     */
    public void eliminarImagen() {
        if (imageIDs.size() > 0) {
            imageIDs.remove(imageIDs.size() - 1);
        }
        invalidate();
    }

    /**
     * establesca un nuevo color para el fondo
     * @param newColor
     */
    public void setColor(String newColor) {
        invalidate();
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    /**
     * Limpiar el canvas
     */
    public void LimpiarCanvas() {
        drawCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        invalidate();

    }

    /**
     * Establecer el dibujo en transparente
     */
    public void setTranspertColor() {
        drawPaint.setColor(Color.TRANSPARENT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
        int n = imageIDs.size();
        for (int i = 0; i < n; i++)
            imageIDs.get(i).draw(canvas);
    }

    public void trackballClicked() {
        mUIMode = (mUIMode + 1) % 3;
        invalidate();
    }

    /**
     * Pass touch events to the MT controller
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        // responder a eventos de bajada, movimiento y subida
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(touchX, touchY);
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }
        invalidate();

        return controlesMultiTouch.onTouchEvent(event);
    }

    /**
     * Obtenga la imagen que está debajo del punto de un solo toque o devuelva nulo
     * (cancelando la operación de arrastre) si no hay
     */
    public EntradaMultiTouch getDraggableObjectAtPoint(@NonNull ControlesMultiTouch.PointInfo pt) {
        float x = pt.getX(), y = pt.getY();
        int n = imageIDs.size();
        for (int i = n - 1; i >= 0; i--) {
            CargarImagen im = (CargarImagen) imageIDs.get(i);
            if (im.contienePunto(x, y))
                return im;
        }
        return null;
    }

    /**
     * Seleccione un objeto para arrastrar. Se llama cada vez que se encuentra que un objeto es
     *      * bajo el punto (getDraggableObjectAtPoint() devuelve no nulo) y
     *      * se está iniciando una operación de arrastre. Llamado con nulo cuando finaliza la operación de arrastre.
     */
    public void selectObject(EntradaMultiTouch img, ControlesMultiTouch.PointInfo touchPoint) {
        currTouchPoint.set(touchPoint);
        if (img != null) {
            //Mover la imagen a la parte superior de la pila cuando se selecciona
            drawPaint.setColor(Color.TRANSPARENT);
            imageIDs.remove(img);
            imageIDs.add(img);
        }

        invalidate();
    }

    /**
     * Obtiene la posición actual y la escala de la imagen seleccionada. Llamado cuando sea
     *      * se inicia un arrastre o se reinicia.
     */
    public void getPositionAndScale(@NonNull EntradaMultiTouch img,
                                    @NonNull ControlesMultiTouch.PositionAndScale objPosAndScaleOut) {
        objPosAndScaleOut.set(img.getPosicionCentroX(), img.getPosicionCentroY(),
                (mUIMode & UI_MODE_ANISOTROPIC_SCALE) == 0,
                (img.getEscalaPosicionX() + img.getEscalaPosicionY()) / 2,
                (mUIMode & UI_MODE_ANISOTROPIC_SCALE) != 0, img.getEscalaPosicionX(),
                img.getEscalaPosicionY(), (mUIMode & UI_MODE_ROTATE) != 0,
                img.getAngulo());
    }

    /**
     * Establezca la posición y la escala de la imagen arrastrada/estirada.
     */
    public boolean setPositionAndScale(EntradaMultiTouch img,
                                       ControlesMultiTouch.PositionAndScale newImgPosAndScale, ControlesMultiTouch.PointInfo touchPoint) {
        currTouchPoint.set(touchPoint);
        boolean ok = ((CargarImagen) img).setPosicion(newImgPosAndScale);
        if (ok)
            invalidate();
        return ok;
    }

    public boolean pointInObjectGrabArea(ControlesMultiTouch.PointInfo pt, EntradaMultiTouch img) {
        return false;
    }

    //prepararse para dibujar y configurar las propiedades del trazo de pintura
    private void setupDrawing() {
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(10);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    // tamaño asignado para ver
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w > 0 && h > 0) {
            super.onSizeChanged(w, h, oldw, oldh);
            canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            drawCanvas = new Canvas(canvasBitmap);
        }
    }
}
