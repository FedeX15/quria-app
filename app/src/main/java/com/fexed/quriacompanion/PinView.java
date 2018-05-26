package com.fexed.quriacompanion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.ArrayList;

public class PinView extends SubsamplingScaleImageView {
    private ArrayList<PointF> sPin = new ArrayList<>();
    private ArrayList<String> pinNames = new ArrayList<>();
    private Bitmap pin;
    private Bitmap pinel;
    private Bitmap pinhist;

    public PinView(Context context) {
        super(context);
    }

    public PinView(Context context, AttributeSet attr) {
        super(context, attr);
        initialise();
    }

    public boolean setPin(PointF sPin, String name) {
        if (pinNames.contains(name)){
            return false;
        } else {
            this.sPin.add(sPin);
            pinNames.add(name);
            initialise();
            invalidate();
            return true;
        }
    }

    public PointF getPin(String name) {

        return sPin.get(pinNames.indexOf(name));
    }

    public boolean removePin(String name){
        if (pinNames.contains(name)){
            sPin.remove(pinNames.indexOf(name));
            pinNames.remove(name);
            return true;
        } else {
            return false;
        }
    }

    public void removeAll() {
        sPin.removeAll(sPin);
        pinNames.removeAll(pinNames);
    }

    public ArrayList<String> getPinNames(){
        return pinNames;
    }

    private void initialise() {
        float density = getResources().getDisplayMetrics().densityDpi;
        pin = getBitmapFromVectorDrawable(this.getContext(), R.drawable.pin);
        float w = (density/420f) * pin.getWidth();
        float h = (density/420f) * pin.getHeight();
        pin = Bitmap.createScaledBitmap(pin, (int)w, (int)h, true);

        pinel = getBitmapFromVectorDrawable(this.getContext(), R.drawable.pinel);
        pinel = Bitmap.createScaledBitmap(pinel, (int)w, (int)h, true);

        pinhist = getBitmapFromVectorDrawable(this.getContext(), R.drawable.ic_adjust_black_24dp);
        w = (density/420f) * pinhist.getWidth();
        h = (density/420f) * pinhist.getHeight();
        pinhist = Bitmap.createScaledBitmap(pinhist, (int)w, (int)h, true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Don't draw pin before image is ready so it doesn't move around during setup.
        if (!isReady()) {
            return;
        }

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(28);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        for (PointF point : sPin){
            if (point != null && pin != null && pinel != null ) {
                String name = pinNames.get(sPin.indexOf(point));
                PointF vPin = sourceToViewCoord(point);
                if (name.contains("EL")) {
                    float vX = vPin.x - (pinel.getWidth() / 2);
                    float vY = vPin.y - pinel.getHeight();
                    canvas.drawBitmap(pinel, vX, vY, paint);
                } else if (name.contains("history")) {
                    float vX = vPin.x - (pinhist.getWidth() / 2);
                    float vY = vPin.y - pinhist.getHeight() / 2;
                    canvas.drawBitmap(pinhist, vX, vY, paint);
                    canvas.drawText(name.replace("history", ""), vX + 32, vY + 100, paint);
                } else{
                    float vX = vPin.x - (pin.getWidth()/2);
                    float vY = vPin.y - pin.getHeight();
                    canvas.drawBitmap(pin, vX, vY, paint);
                    canvas.drawText(name, vX + 32, vY + 100, paint);
                }
            }
        }
    }

    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

}

