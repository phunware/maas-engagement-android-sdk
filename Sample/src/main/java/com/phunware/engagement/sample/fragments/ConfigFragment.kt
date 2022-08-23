package com.phunware.engagement.sample.fragments

import android.content.Context
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.firebase.installations.FirebaseInstallations
import com.phunware.engagement.sample.R
import com.phunware.engagement.sample.adapters.ConfigAdapter
import com.phunware.engagement.sample.models.Config
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

private const val METADATA_APPLICATION_ID = "com.phunware.maas.APPLICATION_ID"
private const val METADATA_ACCESS_KEY = "com.phunware.maas.ACCESS_KEY"

/**
 * Fragment that displays configuration information.
 */
internal class ConfigFragment : Fragment() {
    private var mRecyclerView: RecyclerView? = null
    private val coroutineIoScope = CoroutineScope(Dispatchers.IO)

    companion object {
        @JvmStatic
        fun newInstance(): ConfigFragment {
            return ConfigFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_config, container, false)
        mRecyclerView = v.findViewById<View>(R.id.list) as RecyclerView
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        coroutineIoScope.launch {
            val deviceId = try {
                val advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(requireContext())
                advertisingIdInfo.id
            } catch (e: Exception) {
                Log.w("ConfigFragment", "Failed to get Advertising ID. Using Firebase Installation ID instead.")
                val firebaseInstallationId = FirebaseInstallations.getInstance().id.await()
                firebaseInstallationId
            }
            val packageName = requireActivity().packageName
            val packageManager = requireActivity().packageManager
            val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            val metaData = appInfo.metaData
            val applicationId = metaData.getInt(METADATA_APPLICATION_ID).toString()
            val accessKey = metaData.getString(METADATA_ACCESS_KEY)

            withContext(Dispatchers.Main) {
                context?.let {
                    val config = Config(
                        "Sample",
                        applicationId,
                        accessKey
                    )
                    val adapter = ConfigAdapter(config, deviceId)
                    mRecyclerView?.adapter = adapter
                }
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mRecyclerView!!.layoutManager = LinearLayoutManager(activity)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is androidx.appcompat.app.AppCompatActivity) {
            context.setTitle(R.string.app_name)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}