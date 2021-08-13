package com.phunware.engagement.sample.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phunware.engagement.sample.R;
import com.phunware.engagement.sample.models.Config;

public class ConfigAdapter extends RecyclerView.Adapter<ConfigAdapter.ViewHolder> {

    private static final int ITEM_TYPE_STAT = 0;
    private static final int ITEM_TYPE_COUNT = 5;

    private Config config;
    private String deviceId;

    public ConfigAdapter(Config config, String deviceId) {
        this.config = config;
        this.deviceId = deviceId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM_TYPE_STAT:
                v = inflater.inflate(R.layout.row_two_line, parent, false);
                break;
            default:
                throw new IllegalArgumentException("Unknown view type: " + viewType);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (position) {
            case 0: // App Name
                holder.title.setText("App Name");
                holder.subtitle.setText(config.name);
                break;
            case 1: // App Id
                holder.title.setText(R.string.title_app_id);
                holder.subtitle.setText(String.valueOf(config.appId));
                break;
            case 2: // Device Id
                holder.title.setText(R.string.title_device_id);
                holder.subtitle.setText(deviceId);
                break;
            case 3: // SDK Version
                holder.title.setText(R.string.title_sdk_version);
                holder.subtitle.setText(R.string.engagement_sdk_version);
                break;
            case 4: // Sample App Version
                holder.title.setText(R.string.title_app_version);
                holder.subtitle.setText(R.string.engagement_sdk_version);
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
            title = (TextView) itemView.findViewById(R.id.title);
            subtitle = (TextView) itemView.findViewById(R.id.subtitle);
        }
    }
}
