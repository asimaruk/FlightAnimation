package com.sakharuk.flightanimation;

import com.google.android.gms.maps.model.LatLng;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.FiniteDifferencesDifferentiator;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
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

    public static final int DEFAULT_PLANE_ANGLE = 90;

    private static UnivariateFunction pathFunction;
    private static UnivariateDifferentiableFunction differentiablePathFunction;

    static {
        UnivariateInterpolator interpolator = new SplineInterpolator();
        pathFunction = interpolator.interpolate(
                new double[]{POST_FINISH_POINT.latitude, FINISH_POINT.latitude, START_POINT.latitude, PRE_START_POINT.latitude},
                new double[]{POST_FINISH_POINT.longitude, FINISH_POINT.longitude, START_POINT.longitude, PRE_START_POINT.longitude}
        );
        differentiablePathFunction = new FiniteDifferencesDifferentiator(5, 0.01).differentiate(pathFunction);
    }

    public static double getPathValue(double x) {
        return  pathFunction.value(x);
    }

    public static double getDifferential(double x) {
        DerivativeStructure xDerivativeStructure = new DerivativeStructure(1, 1, 0, x);
        DerivativeStructure yDerivativeStructure = differentiablePathFunction.value(xDerivativeStructure);
        return yDerivativeStructure.getPartialDerivative(1);
    }

    public static final int getPlaneRotationAndle(double x) {
        return (int) (180 + Math.toDegrees(Math.atan(FlightTools.getDifferential(x))));
    }
}
