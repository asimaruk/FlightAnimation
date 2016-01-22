package com.sakharuk.flightanimation.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by Aleksandr on 21.01.2016.
 */
public class FrameLayoutTouchInterceptor extends FrameLayout {

    public interface TouchInteceptor {
        void onTouchEvent(MotionEvent ev);
    }

    private TouchInteceptor touchInteceptor;

    public FrameLayoutTouchInterceptor(Context context) {
        super(context);
    }

    public FrameLayoutTouchInterceptor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameLayoutTouchInterceptor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FrameLayoutTouchInterceptor(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setTouchInteceptor(TouchInteceptor touchInteceptor) {
        this.touchInteceptor = touchInteceptor;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (touchInteceptor != null) {
            touchInteceptor.onTouchEvent(ev);
        }
        return false;
    }
}
