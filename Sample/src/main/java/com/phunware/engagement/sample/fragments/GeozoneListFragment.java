package com.phunware.engagement.sample.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.phunware.engagement.Callback;
import com.phunware.engagement.Engagement;
import com.phunware.engagement.location.LocationListener;
import com.phunware.engagement.location.model.Geozone;
import com.phunware.engagement.sample.R;
import com.phunware.engagement.sample.adapters.GeozoneAdapter;
import com.phunware.engagement.sample.views.DividerItemDecoration;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_location_list, container, false);
        mList = (RecyclerView) v.findViewById(R.id.list);
        mError = (TextView) v.findViewById(R.id.error);
        mLoading = (ProgressBar) v.findViewById(R.id.loading);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        mList.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
        mLoading.setVisibility(View.VISIBLE);

        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mList.addItemDecoration(new DividerItemDecoration(getActivity()));

        refreshGeozones();
    }

    @Override
    public void onResume() {
        super.onResume();
        Engagement.locationManager().addLocationListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Engagement.locationManager().addLocationListener(this);
    }

    private void showError(Throwable e) {
        mList.setVisibility(View.GONE);
        mLoading.setVisibility(View.VISIBLE);
        mError.setVisibility(View.VISIBLE);
        mError.setText(getString(R.string.location_load_failed, e.getMessage()));
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
        mError.setText(R.string.no_locations);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            activity.setTitle(R.string.nav_locations);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.map, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.map:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content, new GeozoneMapFragment())
                        .addToBackStack(null)
                        .commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onGeozoneSelected(Geozone zone) {
        GeozoneDetailFragment f = GeozoneDetailFragment.newInstance(zone);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, f)
                .addToBackStack(null)
                .commit();
    }

    private void refreshGeozones() {
        Engagement.locationManager().getAvailableGeozones(new Callback<List<Geozone>>() {
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
