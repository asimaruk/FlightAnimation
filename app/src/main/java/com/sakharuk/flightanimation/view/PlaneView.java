package com.sakharuk.flightanimation.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Aleksandr on 22.01.2016.
 */
public class PlaneView extends ImageView {
    public PlaneView(Context context) {
        super(context);
    }

    public PlaneView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlaneView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlaneView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
