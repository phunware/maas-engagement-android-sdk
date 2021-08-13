package com.phunware.engagement.sample.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import androidx.core.app.NotificationCompat;
import com.phunware.engagement.Engagement;
import com.phunware.engagement.messages.CampaignType;
import com.phunware.engagement.messages.MessageListener;
import com.phunware.engagement.messages.model.Message;
import com.phunware.engagement.sample.R;
import com.phunware.engagement.sample.activities.MainActivity;

/**
 * This Interceptor can be used to intercept messages and customize them.
 * This implementation , cancels the beacon entry campaign notifications
 * sent by SDK after 2 seconds,
 * recreates those notifications and sends them after 5 seconds,
 * thereby creating a dwell effect for beacon entry campaigns
 */
public class MessageInterceptor {

    private static final String TAG = MessageInterceptor.class.getSimpleName();
    private static final int CANCEL_DELAY = 2000;
    private static final int NOTIFICATION_DELAY = 5000;
    private static final String DWELL_PREFIX = "DWELL: ";
    private static final String DWELL_CHANNEL_ID = "dwell_channel_id";
    private static final String DWELL_CHANNEL_NAME = "dwell_channel_name";

    private Context context;
    private CampaignMessageListener listener = null;

    public MessageInterceptor(Context context) {
        this.context = context;
    }

    /**
     * Builds the notification
     *
     * @param msg Message used to build the notification
     * @param intent used in the notification
     * @return A notification built using the message object
     */
    private Notification getNotification(Message msg, Intent intent) {
        intent.setAction(Intent.ACTION_VIEW);
        intent.setType(Engagement.MIME_MESSAGE);
        intent.putExtra(Engagement.EXTRA_MESSAGE, msg);
        int requestId = (int) msg.campaignId;

        //Customize this Pending intent
        PendingIntent pi = PendingIntent.getActivity(context, requestId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder
                = new NotificationCompat.Builder(context, DWELL_CHANNEL_ID)
                .setContentTitle(DWELL_PREFIX + msg.notificationTitle)
                .setContentText(msg.notificationMessage)
                .setContentIntent(pi)
                .setSmallIcon(R.drawable.ic_search)
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel messageChannel =
                    new NotificationChannel(DWELL_CHANNEL_ID,
                            DWELL_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = context
                    .getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(messageChannel);
            notificationBuilder.setChannelId(DWELL_CHANNEL_ID);
        }
        return notificationBuilder.build();
    }

    /**
     * Listen for messages sent by Engagement SDK
     */
    public void listenForMessages() {
        listener = new CampaignMessageListener();
        Engagement.addOnMessageReceivedListener(listener);
    }

    public void stopListening() {
        if (listener != null) {
            Engagement.removeOnMessageReceivedListener(listener);
        }
    }

    /**
     * Implementation of the Engagement SDK Message listener
     */
    static class CampaignMessageListener implements MessageListener {

        private final Handler handler = new Handler();

        @Override
        public void onMessageReceived(Message message) {
        }

    }
}
