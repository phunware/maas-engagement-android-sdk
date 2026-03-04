package com.phunware.engagement.sample.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phunware.engagement.Callback;
import com.phunware.engagement.location.LocationListener;
import com.phunware.engagement.location.LocationManager;
import com.phunware.engagement.location.model.Geozone;
import com.phunware.engagement.sample.App;
import com.phunware.engagement.sample.R;
import com.phunware.engagement.sample.adapters.GeozoneAdapter;

import java.util.List;

/**
 *
 */
public class GeozoneListFragment extends Fragment
        implements GeozoneAdapter.OnGeozoneSelectedListener, LocationListener {

    private RecyclerView mList;
    private TextView mError;
    private ProgressBar mLoading;

    private GeozoneAdapter mAdapter;
    private LocationManager locationManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(com.phunware.engagement.sample.R.layout.fragment_location_list,
                container, false);
        mList = v.findViewById(com.phunware.engagement.sample.R.id.list);
        mError = v.findViewById(com.phunware.engagement.sample.R.id.error);
        mLoading = v.findViewById(com.phunware.engagement.sample.R.id.loading);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        mList.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mLoading.setVisibility(View.VISIBLE);

        locationManager = ((App) requireActivity().getApplication()).getLocationManager();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mList.getContext());
        mList.setLayoutManager(layoutManager);
        mList.addItemDecoration(new DividerItemDecoration(mList.getContext(), layoutManager.getOrientation()));

        refreshGeozones();
    }

    @Override
    public void onResume() {
        super.onResume();
        locationManager.addLocationListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeLocationListener(this);
    }

    private void showError(Throwable e) {
        mList.setVisibility(View.GONE);
        mLoading.setVisibility(View.VISIBLE);
        mError.setVisibility(View.VISIBLE);
        mError.setText(getString(com.phunware.engagement.sample.R.string.location_load_failed,
                e.getMessage()));
    }

    public void setGeozones(List<Geozone> geozones) {
        if (geozones == null || geozones.isEmpty()) {
            showEmpty();
            return;
        }

        mAdapter = new GeozoneAdapter(geozones, this);
        mList.setAdapter(mAdapter);

        mList.setVisibility(View.VISIBLE);
        mError.setVisibility(View.GONE);
        mLoading.setVisibility(View.GONE);
    }

    private void showEmpty() {
        mLoading.setVisibility(View.GONE);
        mList.setVisibility(View.GONE);
        mError.setVisibility(View.VISIBLE);
        mError.setText(com.phunware.engagement.sample.R.string.no_locations);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            activity.setTitle(com.phunware.engagement.sample.R.string.nav_locations);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(com.phunware.engagement.sample.R.menu.map, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.map) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content,
                            new GeozoneMapFragment())
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGeozoneSelected(Geozone zone) {
        GeozoneDetailFragment f = GeozoneDetailFragment.newInstance(zone);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(com.phunware.engagement.sample.R.id.content, f)
                .addToBackStack(null)
                .commit();
    }

    private void refreshGeozones() {
        locationManager.getAvailableGeozones(new Callback<List<Geozone>>() {
            @Override
            public void onSuccess(List<Geozone> data) {
                setGeozones(data);
            }

            @Override
            public void onFailed(Throwable e) {
                Log.e("error", e.toString());
                showError(e);
            }
        });
    }

    @Override
    public void onZoneAdded(Long id) {
        refreshGeozones();
    }

    @Override
    public void onZoneUpdated(Long id) {
        refreshGeozones();
    }

    @Override
    public void onZoneRemoved(Long id) {
        refreshGeozones();
    }

    @Override
    public void onZoneEntered(Long id) {
        refreshGeozones();
    }

    @Override
    public void onZoneExited(Long id) {
        refreshGeozones();
    }

    @Override
    public void onZoneCheckIn(Long id) {
        refreshGeozones();
    }

    @Override
    public void onZoneCheckInFailure(Long id) {
    }
}
