package com.janhelmich.crius;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

public class PointerDrawable extends Drawable {
    private final Paint paint = new Paint();
    private boolean enabled;

    @Override
    public void draw(@NonNull Canvas canvas) {
        float cx = canvas.getWidth()/2;
        float cy = canvas.getHeight()/2;
        if (enabled) {
            paint.setColor(Color.GREEN);
            canvas.drawCircle(cx, cy, 10, paint);
        } else {
            paint.setColor(Color.RED);
            canvas.drawText("X", cx, cy, paint);
        }
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@NonNull ColorFilter colorFilter) {

    }

    @SuppressLint("WrongConstant")
    @Override
    public int getOpacity() {
        return 0;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
