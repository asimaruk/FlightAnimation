package com.sakharuk.flightanimation;

import android.app.Fragment;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sakharuk.flightanimation.databinding.FragmentFlightBinding;
import com.sakharuk.flightanimation.view.FrameLayoutTouchInterceptor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Aleksandr on 21.01.2016.
 */
public class FlightFragment extends Fragment implements OnMapReadyCallback {

    private FragmentFlightBinding binding;
    private LatLng startPoint = new LatLng(59.57, 30.19); // Saint-Petersburg
    private LatLng finishPoint = new LatLng(51.516667,  -.083333); // London
    private LatLngBounds flightBounds = LatLngBounds.builder().include(startPoint).include(finishPoint).build();
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_flight, container, false);
        binding = DataBindingUtil.bind(root);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.map.onCreate(savedInstanceState);
        binding.map.getMapAsync(this);

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        getView().post(new Runnable() {
            @Override
            public void run() {
                googleMap.addMarker(new MarkerOptions().position(startPoint));
                googleMap.addMarker(new MarkerOptions().position(finishPoint));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(flightBounds, 100));
                binding.flight.updateMapProjection(googleMap.getCameraPosition(), googleMap.getProjection());

                binding.frame.setTouchInteceptor(new FrameLayoutTouchInterceptor.TouchInteceptor() {
                    @Override
                    public void onTouchEvent(MotionEvent ev) {
                        binding.flight.updateMapProjection(googleMap.getCameraPosition(), googleMap.getProjection());
                    }
                });

                animatePlane(googleMap);

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        animatePlane(googleMap);
                    }
                });
            }
        });
    }

    private void animatePlane(final GoogleMap googleMap) {
        if (scheduledExecutorService != null && !scheduledExecutorService.isShutdown()) {
            scheduledExecutorService.shutdown();
        }
        scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {

            private double time;
            private Handler handler = new Handler(Looper.getMainLooper());

            @Override
            public void run() {
                if (time > FlightTools.FLIGHT_ANIMATION_TIME) {
                    scheduledExecutorService.shutdown();
                } else {
                    final double x = FlightTools.START_POINT.latitude +
                            (FlightTools.FINISH_POINT.latitude - FlightTools.START_POINT.latitude) *
                                    (time / FlightTools.FLIGHT_ANIMATION_TIME);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Point point = googleMap.getProjection().toScreenLocation(new LatLng(x, FlightTools.getPathValue(x)));
                            Log.d("Plane", point.toString());
                            Log.d("Plane differential", String.valueOf(FlightTools.getDifferential(x)));
                            Log.d("Plane angle", String.valueOf(Math.atan(FlightTools.getDifferential(x))));
                            binding.airplane.setTranslationX(point.x - binding.airplane.getWidth() / 2);
                            binding.airplane.setTranslationY(point.y - binding.airplane.getHeight() / 2);
                            binding.airplane.setRotation(FlightTools.getPlaneRotationAndle(x));
                        }
                    });
                }
                time += 17;
            }
        }, 0, 17, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.map.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.map.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        binding.map.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.map.onLowMemory();
    }
}
