package com.phunware.engagement.sample.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phunware.engagement.Callback;
import com.phunware.engagement.location.LocationListener;
import com.phunware.engagement.location.LocationManager;
import com.phunware.engagement.location.model.Geozone;
import com.phunware.engagement.sample.R;
import com.phunware.engagement.sample.views.GeozoneMapView;

import java.util.List;

/**
 * Displays all available geozones on a map, colored based on their current inside/monitored/default
 * status.
 */
public class GeozoneMapFragment extends Fragment implements LocationListener {

    private static final String TAG = GeozoneMapFragment.class.getSimpleName();

    private GeozoneMapView mMap;
    private LocationManager mLocationManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        mMap = (GeozoneMapView) v.findViewById(R.id.map);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMap.onCreate(savedInstanceState);

        loadGeozones();
    }

    @Override
    public void onDestroy() {
        mMap.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMap.onResume();
        mLocationManager.addLocationListener(this);
    }

    @Override
    public void onPause() {
        mLocationManager.removeLocationListener(this);
        mMap.onPause();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMap.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMap.onLowMemory();
    }

    /**
     * Loads the geozones from the Engagement SDKs so they can be added to the map.
     */
    private void loadGeozones() {
        mLocationManager.getAvailableGeozones(new Callback<List<Geozone>>() {
            @Override
            public void onSuccess(List<Geozone> data) {
                if (!isResumed()) {
                    return;
                }
                mMap.addGeozones(data);
            }

            @Override
            public void onFailed(Throwable e) {
                Log.e(TAG, "There was an error loading available geozones.", e);
            }
        });
    }

    /*
     * LocationListener callback methods
     */

    @Override
    public void onZoneAdded(final Long id) {
        mLocationManager.getGeozone(id, new Callback<Geozone>() {
            @Override
            public void onSuccess(Geozone data) {
                if (!isResumed()) {
                    return;
                }
                mMap.addGeozone(data);
            }

            @Override
            public void onFailed(Throwable e) {
                Log.e(TAG, "Failed to get added geozone: " + id, e);
            }
        });
    }

    @Override
    public void onZoneUpdated(final Long id) {
        mLocationManager.getGeozone(id, new Callback<Geozone>() {
            @Override
            public void onSuccess(Geozone data) {
                if (!isResumed()) {
                    return;
                }
                mMap.updateGeozone(data);
            }

            @Override
            public void onFailed(Throwable e) {
                Log.e(TAG, "Failed to get updated geozone: " + id, e);
            }
        });
    }

    @Override
    public void onZoneRemoved(final Long id) {
        mLocationManager.getGeozone(id, new Callback<Geozone>() {
            @Override
            public void onSuccess(Geozone data) {
                if (!isResumed()) {
                    return;
                }
                mMap.removeGeozone(data);
            }

            @Override
            public void onFailed(Throwable e) {
                Log.e(TAG, "Failed to get removed geozone: " + id, e);
            }
        });
    }

    /*
     * We don't need to worry about the rest of the listener methods.
     */

    @Override
    public void onZoneEntered(Long id) {

    }

    @Override
    public void onZoneExited(Long id) {

    }

    @Override
    public void onZoneCheckIn(Long id) {

    }

    @Override
    public void onZoneCheckInFailure(Long id) {

    }
}
