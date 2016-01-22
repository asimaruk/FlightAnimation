package com.sakharuk.flightanimation.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.VisibleRegion;
import com.sakharuk.flightanimation.FlightTools;
import com.sakharuk.flightanimation.R;

/**
 * Created by Aleksandr on 22.01.2016.
 */
public class FlightView extends View {

    private int pathColor;
    private float circleRadius;
    private Paint circlePaint;

    private Projection oldProjection;
    private CameraPosition oldCameraPosition;

    private Bitmap backBitmap;
    private Canvas backCanvas;

    public FlightView(Context context) {
        super(context);
        init();
    }

    public FlightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FlightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Resources resources = getResources();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            pathColor = resources.getColor(R.color.colorPath);
        } else {
            pathColor = resources.getColor(R.color.colorPath, null);
        }
        circleRadius = resources.getDimension(R.dimen.path_circle_radius);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(pathColor);
        circlePaint.setStyle(Paint.Style.FILL);

        setWillNotDraw(false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        backBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        backCanvas = new Canvas(backBitmap);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(backBitmap, 0, 0, null);
    }

    public void updateMapProjection(CameraPosition cameraPosition, Projection projection) {
        if (oldCameraPosition == null || oldCameraPosition.zoom != cameraPosition.zoom) {
            backCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            setTranslationX(0);
            setTranslationY(0);
            for (double x = FlightTools.START_POINT.latitude; x > FlightTools.FINISH_POINT.latitude; x -= .2) {
                Point point = projection.toScreenLocation(new LatLng(x, FlightTools.getPathValue(x)));
                backCanvas.drawCircle(point.x, point.y, circleRadius, circlePaint);
            }
            invalidate();
        } else {
            VisibleRegion visibleRegion = projection.getVisibleRegion();
            Point point = projection.toScreenLocation(new LatLng(
                    visibleRegion.nearLeft.latitude,
                    visibleRegion.nearLeft.longitude
            ));
            Point oldPoint = oldProjection.toScreenLocation(new LatLng(
                    visibleRegion.nearLeft.latitude,
                    visibleRegion.nearLeft.longitude
            ));
            setTranslationX(getTranslationX() + (point.x - oldPoint.x));
            setTranslationY(getTranslationY() + (point.y - oldPoint.y));
        }
        oldProjection = projection;
        oldCameraPosition = cameraPosition;
    }
}
