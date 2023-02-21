package com.phunware.engagement.sample.activities

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import android.os.Bundle
import com.phunware.engagement.sample.R
import com.phunware.engagement.sample.fragments.ConfigFragment
import android.content.Intent
import com.phunware.engagement.Engagement
import com.phunware.engagement.sample.fragments.MessageDetailFragment
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import com.phunware.engagement.sample.fragments.GeozoneListFragment
import com.phunware.engagement.sample.fragments.MessageListFragment
import com.phunware.engagement.sample.fragments.AttributeFragment
import androidx.core.view.GravityCompat
import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import com.phunware.engagement.messages.model.Message

private const val PERMISSIONS_REQUEST = 10

internal class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var mDrawer: DrawerLayout? = null
    private var mNavigation: NavigationView? = null
    private var mDrawerToggle: ActionBarDrawerToggle? = null

    private val isAndroidSDKVersion33OrHigher: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    private val isAndroidSDKVersion31OrHigher: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    private val isAndroidSDKVersion29OrHigher: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null || !Engagement.isInitialized()) {
            Engagement.init(applicationContext)
        }

        checkPermissions()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.content, ConfigFragment.newInstance())
                .commit()
        }

        handleIntent(intent)

        mDrawer = findViewById(R.id.drawer)
        mDrawerToggle = ActionBarDrawerToggle(
            this, mDrawer,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        mDrawer?.setDrawerListener(mDrawerToggle)
        val bar = supportActionBar
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true)
            bar.setHomeButtonEnabled(true)
        }
        mNavigation = findViewById(R.id.navigation)
        mNavigation?.setNavigationItemSelectedListener(this)
        supportFragmentManager.addOnBackStackChangedListener {
            mDrawerToggle?.isDrawerIndicatorEnabled = supportFragmentManager.backStackEntryCount == 0
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_VIEW == intent.action && Engagement.MIME_MESSAGE == intent.type) {
            val intentMessage = intent.getParcelableExtra<Message>(Engagement.EXTRA_MESSAGE)
            if (intentMessage != null) {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.content, MessageDetailFragment.newInstance(intentMessage))
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun onPermissionsGranted() {
        if (!canAccessPreciseLocation()) {
            return
        }

        if (!canPostNotifications()) {
            Log.w("MainActivity", "Please grant notification Permission to receive campaign notifications.")
            return
        }

        if (isAndroidSDKVersion29OrHigher) {
            if (!canAccessBackgroundLocation()) {
                Toast.makeText(
                    this,
                    R.string.missing_background_location_permission,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // Now that we have notification and location permissions, start monitoring for campaigns by
        // enabling notifications and starting the location manager
        Engagement.enablePushNotifications()
        Engagement.locationManager().start()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mDrawerToggle?.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mDrawerToggle?.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                    return true
                }
                mDrawerToggle?.onOptionsItemSelected(item) == true || super.onOptionsItemSelected(item)
            }
            else -> mDrawerToggle?.onOptionsItemSelected(item) == true || super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> supportFragmentManager.beginTransaction()
                .replace(R.id.content, ConfigFragment())
                .commit()
            R.id.locations -> supportFragmentManager.beginTransaction()
                .replace(R.id.content, GeozoneListFragment())
                .commit()
            R.id.messages -> supportFragmentManager.beginTransaction()
                .replace(R.id.content, MessageListFragment())
                .commit()
            R.id.attributes -> supportFragmentManager.beginTransaction()
                .replace(R.id.content, AttributeFragment())
                .commit()
            else -> {}
        }
        mDrawer?.closeDrawer(GravityCompat.START)
        return true
    }

    private fun checkPermissions() {
        mutableSetOf<String>().run {
            if (isAndroidSDKVersion33OrHigher) {
                if (!canPostNotifications()) {
                    add(permission.POST_NOTIFICATIONS)
                }
            }

            if (isAndroidSDKVersion31OrHigher) {
                if (!canLookForBluetoothDevices()) {
                    add(permission.BLUETOOTH_SCAN)
                }
            }

            if (!canAccessPreciseLocation()) {
                add(permission.ACCESS_COARSE_LOCATION)
                add(permission.ACCESS_FINE_LOCATION)
            } else {
                if (isAndroidSDKVersion29OrHigher) {
                    if (!canAccessBackgroundLocation()) {
                        add(permission.ACCESS_BACKGROUND_LOCATION)
                    }
                }
            }

            if (isNotEmpty()) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    toTypedArray(),
                    PERMISSIONS_REQUEST
                )
            } else {
                onPermissionsGranted()
            }
        }
    }

    private fun canAccessPreciseLocation() =
        hasPermission(permission.ACCESS_FINE_LOCATION)

    private fun canAccessBackgroundLocation() = if (isAndroidSDKVersion29OrHigher) {
        hasPermission(permission.ACCESS_BACKGROUND_LOCATION)
    } else {
        true
    }

    private fun canLookForBluetoothDevices() = if (isAndroidSDKVersion31OrHigher) {
        hasPermission(permission.BLUETOOTH_SCAN)
    } else {
        true
    }

    private fun canPostNotifications() = if (isAndroidSDKVersion33OrHigher) {
        hasPermission(permission.POST_NOTIFICATIONS)
    } else {
        true
    }

    private fun hasPermission(perm: String): Boolean {
        return PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
                    if (isAndroidSDKVersion29OrHigher) {
                        if (!canAccessBackgroundLocation()) {
                            ActivityCompat.requestPermissions(
                                this,
                                arrayOf(permission.ACCESS_BACKGROUND_LOCATION),
                                PERMISSIONS_REQUEST
                            )
                        }
                    }
                    onPermissionsGranted()
                }
                return
            }
            else -> {}
        }
    }
}