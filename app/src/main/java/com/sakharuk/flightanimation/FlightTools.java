package com.sakharuk.flightanimation;

import com.google.android.gms.maps.model.LatLng;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;

/**
 * Created by Aleksandr on 22.01.2016.
 */
public class FlightTools {

    public static final long FLIGHT_ANIMATION_TIME = 5000;

    public static final LatLng PRE_START_POINT = new LatLng(60.75, 28.19);
    public static final LatLng START_POINT = new LatLng(59.57, 30.19); // Saint-Petersburg
    public static final LatLng FINISH_POINT = new LatLng(51.516667,  -.083333); // London
    public static final LatLng POST_FINISH_POINT = new LatLng(50.51667, 1.083333);

    private static UnivariateFunction pathFunction;

    static {
        UnivariateInterpolator interpolator = new SplineInterpolator();
        pathFunction = interpolator.interpolate(
                new double[]{POST_FINISH_POINT.latitude, FINISH_POINT.latitude, START_POINT.latitude, PRE_START_POINT.latitude},
                new double[]{POST_FINISH_POINT.longitude, FINISH_POINT.longitude, START_POINT.longitude, PRE_START_POINT.longitude}
        );
    }

    public static double getPathValue(double x) {
        return  pathFunction.value(x);
    }
}
