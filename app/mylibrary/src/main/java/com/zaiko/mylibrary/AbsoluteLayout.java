package com.zaiko.mylibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class AbsoluteLayout extends ViewGroup {

    public AbsoluteLayout(Context context) {
        super(context);
    }

    public AbsoluteLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int totalHijos = getChildCount();
        int maximoAltura = 0;
        int maximoAnchura = 0;

        // Descubre lo grande que todas quieren ser
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        // Encuentra al niño más a la derecha y más abajo.
        for (int i = 0; i < totalHijos; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                int childRight;
                int childBottom;

                AbsoluteLayout.LayoutParams lp
                        = (AbsoluteLayout.LayoutParams) child.getLayoutParams();

                childRight = lp.x + child.getMeasuredWidth();
                childBottom = lp.y + child.getMeasuredHeight();

                maximoAnchura = Math.max(maximoAnchura, childRight);
                maximoAltura = Math.max(maximoAltura, childBottom);
            }
        }

        // Cuenta para relleno también
        maximoAnchura += getPaddingLeft() + getPaddingRight();
        maximoAltura += getPaddingTop() + getPaddingBottom();

        // Comprobar con la altura y el ancho mínimos
        maximoAltura = Math.max(maximoAltura, getSuggestedMinimumHeight());
        maximoAnchura = Math.max(maximoAnchura, getSuggestedMinimumWidth());

        setMeasuredDimension(resolveSize(maximoAnchura, widthMeasureSpec),
                resolveSize(maximoAltura, heightMeasureSpec));
    }

    /**
     * Devuelve un conjunto de parámetros de diseño con un ancho de
     * {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT},
     * una altura de {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT}
     * y con las coordenadas (0, 0).
     */
    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0, 0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t,
                            int r, int b) {
        int count = getChildCount();
        int paddingL = getPaddingLeft();
        int paddingT = getPaddingTop();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {

                AbsoluteLayout.LayoutParams lp =
                        (AbsoluteLayout.LayoutParams) child.getLayoutParams();

                int childLeft = paddingL + lp.x;
                int childTop = paddingT + lp.y;
                child.layout(childLeft, childTop,
                        childLeft + child.getMeasuredWidth(),
                        childTop + child.getMeasuredHeight());
            }
        }
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new AbsoluteLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof AbsoluteLayout.LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public int x;
        public int y;

        public LayoutParams(int width, int height, int x, int y) {
            super(width, height);
            this.x = x;
            this.y = y;
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

    }
}

