package com.phunware.engagement.sample.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.phunware.engagement.Engagement;
import com.phunware.engagement.entities.Message;
import com.phunware.engagement.messages.MessageManager;
import com.phunware.engagement.sample.R;
import com.phunware.engagement.sample.fragments.AttributeFragment;
import com.phunware.engagement.sample.fragments.ConfigFragment;
import com.phunware.engagement.sample.fragments.GeozoneListFragment;
import com.phunware.engagement.sample.fragments.LogFragment;
import com.phunware.engagement.sample.fragments.MessageDetailFragment;
import com.phunware.engagement.sample.fragments.MessageListFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawer;
    private NavigationView mNavigation;

    private ActionBarDrawerToggle mDrawerToggle;

    private static final int PERMISSIONS_REQUEST_LOCATION = 10;
    private static final String TAG_LOG_FRAGMENT = "LogFragmentTag";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        mDrawer = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(mDrawerToggle);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setHomeButtonEnabled(true);
        }
        mNavigation = (NavigationView) findViewById(R.id.navigation);
        mNavigation.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    @Override
                    public void onBackStackChanged() {
                        mDrawerToggle.setDrawerIndicatorEnabled(
                                getSupportFragmentManager().getBackStackEntryCount() == 0);
                    }
                });
    }

    private void onPermissionsGranted() {

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();

        switch (getIntent().getAction()) {
            case Intent.ACTION_VIEW:
                trans.replace(R.id.content, new ConfigFragment());

                Message intentMessage = getIntent().getParcelableExtra(MessageManager.EXTRA_MESSAGE);
                Engagement.analytics().trackCampaignAppLaunched(intentMessage.campaignId(),
                        intentMessage.campaignType());

                boolean hasExtras = getIntent()
                        .getBooleanExtra(MessageManager.EXTRA_HAS_EXTRAS, false);
                if (hasExtras) {
                    // Add the list fragment to the view *outside* the transaction we're
                    // going to commit so that it's before the detail view on the back stack.
                    trans.replace(R.id.content, new MessageListFragment());


                    long messageId = intentMessage.campaignId();

                    trans.replace(R.id.content,
                            MessageDetailFragment.newInstance(messageId)).addToBackStack(null);
                }
                break;
            default:
                trans.replace(R.id.content, new ConfigFragment());
        }

        trans.commitAllowingStateLoss();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(com.phunware.engagement.sample.R.menu.global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_enable_push_notifications:
                Engagement.enablePushNotifications(this.getApplicationContext());
                Toast.makeText(this.getApplicationContext(), "Push Notifications now Enabled",
                        Toast.LENGTH_LONG).show();
                return true;
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                    return true;
                }
            default:
                return mDrawerToggle.onOptionsItemSelected(item)
                        || super.onOptionsItemSelected(item);
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
            case R.id.logs:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content, new LogFragment(), TAG_LOG_FRAGMENT)
                        .commit();
                break;
            default:
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean canAccessFineLocation() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean canAccessCoarseLocation() {
        return (hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION));
    }

    private boolean hasPermission(String perm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));
        }
        return true;
    }

    private void checkPermissions() {
        if (!canAccessFineLocation() || !canAccessCoarseLocation()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            onPermissionsGranted();
        }
    }
 
    @Override
    public void onRequestPermissionsResult(int requestCode,
            @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do then
                    // contacts-related task you need to do.
                    onPermissionsGranted();
                }
                return;

            default:
                break;
        }
    }
}
