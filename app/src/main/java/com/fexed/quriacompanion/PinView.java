package com.fexed.quriacompanion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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

    public ArrayList<String> getPinNames(){
        return pinNames;
    }

    private void initialise() {
        float density = getResources().getDisplayMetrics().densityDpi;
        pin = getBitmapFromVectorDrawable(this.getContext(), R.drawable.pin);
        float w = (density/420f) * pin.getWidth();
        float h = (density/420f) * pin.getHeight();
        pin = Bitmap.createScaledBitmap(pin, (int)w, (int)h, true);
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
        for (PointF point : sPin){
            if (point != null && pin != null) {
                PointF vPin = sourceToViewCoord(point);
                float vX = vPin.x - (pin.getWidth()/2);
                float vY = vPin.y - pin.getHeight();
                canvas.drawBitmap(pin, vX, vY, paint);
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

