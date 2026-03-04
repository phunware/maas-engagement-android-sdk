package com.phunware.engagement.sample.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.phunware.engagement.sample.BuildConfig;
import com.phunware.engagement.sample.R;
import com.phunware.engagement.sample.models.Config;

public class ConfigAdapter extends RecyclerView.Adapter<ConfigAdapter.ViewHolder> {

    private static final int ITEM_TYPE_STAT = 0;
    private static final int ITEM_TYPE_COUNT = 6;

    private final Config config;
    private final String deviceId;
    private final String registeredDeviceId;

    public ConfigAdapter(Config config, String deviceId, String registeredDeviceId) {
        this.config = config;
        this.deviceId = deviceId;
        this.registeredDeviceId = registeredDeviceId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == ITEM_TYPE_STAT) {
            v = inflater.inflate(R.layout.row_two_line, parent, false);
        } else {
            throw new IllegalArgumentException("Unknown view type: " + viewType);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        switch (position) {
            case 0: // App Id
                holder.title.setText(R.string.title_app_id);
                holder.subtitle.setText(config.getAppId());
                break;
            case 1: // Device Id
                holder.title.setText(R.string.title_device_id);
                holder.subtitle.setText(deviceId);
                break;
            case 2: // Registered Device Id
                holder.title.setText(R.string.title_registered_device_id);
                holder.subtitle.setText(registeredDeviceId);
                break;
            case 3: // SDK Version
                holder.title.setText(R.string.title_sdk_version);
                holder.subtitle.setText(com.phunware.engagement.R.string.engagement_sdk_version);
                break;
            case 4: // Sample App Version
                holder.title.setText(R.string.title_app_version);
                holder.subtitle.setText(BuildConfig.VERSION_NAME);
                break;
            case 5: // Environment
                holder.title.setText(R.string.title_environment);
                holder.subtitle.setText(config.getEnvironment());
                break;
            default:
                // who cares
        }
    }

    @Override
    public int getItemCount() {
        return ITEM_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        //all items are of the same type right now
        return ITEM_TYPE_STAT;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView title;
        final TextView subtitle;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
        }
    }
}
