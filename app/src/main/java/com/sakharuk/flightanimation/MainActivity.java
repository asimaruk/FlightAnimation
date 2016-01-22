package com.sakharuk.flightanimation;

import android.app.Fragment;
import android.app.FragmentManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sakharuk.flightanimation.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static final String FLIGHT_MAP_FRAGMENT_TAG = "flight_map_fragment_tag";

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();

        FragmentManager fragmentManager = getFragmentManager();
        Fragment flightFragment = fragmentManager.findFragmentByTag(FLIGHT_MAP_FRAGMENT_TAG);
        if (flightFragment == null) {
            flightFragment = Fragment.instantiate(this, FlightFragment.class.getCanonicalName());
            fragmentManager.beginTransaction().add(R.id.root, flightFragment, FLIGHT_MAP_FRAGMENT_TAG).commit();
        }
    }
}
