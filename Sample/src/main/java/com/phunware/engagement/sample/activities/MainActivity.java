package com.phunware.engagement.sample.activities;

import android.Manifest;
import android.Manifest.permission;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.phunware.engagement.Engagement;
import com.phunware.engagement.messages.model.Message;
import com.phunware.engagement.sample.R;
import com.phunware.engagement.sample.fragments.AttributeFragment;
import com.phunware.engagement.sample.fragments.ConfigFragment;
import com.phunware.engagement.sample.fragments.GeozoneListFragment;
import com.phunware.engagement.sample.fragments.MessageDetailFragment;
import com.phunware.engagement.sample.fragments.MessageListFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawer;
    private NavigationView mNavigation;
    private ActionBarDrawerToggle mDrawerToggle;
    private static final int PERMISSIONS_REQUEST_LOCATION = 10;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, ConfigFragment.newInstance())
                .commit();
        }

        handleIntent(getIntent());

        mDrawer = findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(mDrawerToggle);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setHomeButtonEnabled(true);
        }
        mNavigation = findViewById(R.id.navigation);
        mNavigation.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().addOnBackStackChangedListener(() ->
            mDrawerToggle.setDrawerIndicatorEnabled(getSupportFragmentManager().getBackStackEntryCount() == 0)
        );
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Message intentMessage = intent.getParcelableExtra(Engagement.EXTRA_MESSAGE);
            if (intentMessage != null) {
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, MessageDetailFragment.newInstance(intentMessage))
                    .addToBackStack(null)
                    .commit();
            }
        }
    }

    private void onPermissionsGranted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ) {
            Toast.makeText(this, R.string.missing_background_location_permission,Toast.LENGTH_LONG).show();
        }

        Engagement.locationManager().start();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                    return true;
                }
            default:
                return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, new ConfigFragment())
                        .commit();
                break;
            case R.id.locations:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, new GeozoneListFragment())
                        .commit();
                break;
            case R.id.messages:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, new MessageListFragment())
                        .commit();
                break;
            case R.id.attributes:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, new AttributeFragment())
                        .commit();
                break;
            default:
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));
    }

    private void checkPermissions() {
        if (hasPermission(permission.ACCESS_FINE_LOCATION)) {
            onPermissionsGranted();
        } else {
            requestPermissions(new String[]{permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !hasPermission(permission.ACCESS_BACKGROUND_LOCATION)) {
                        requestPermissions(new String[]{ permission.ACCESS_BACKGROUND_LOCATION }, PERMISSIONS_REQUEST_LOCATION);
                    } else {
                        onPermissionsGranted();
                    }
                }
                return;

            default:
                break;
        }
    }
}
