package com.phunware.engagement.sample;

        import android.app.NotificationChannel;
        import android.media.RingtoneManager;
        import android.os.Build;
        import android.support.v4.app.NotificationCompat;

        import com.phunware.engagement.entities.MessageType;
        import com.phunware.engagement.messages.services.NotificationCustomizationService;

public class EditNotificationService extends NotificationCustomizationService {

    @Override
    public void editNotification(NotificationCompat.Builder notificationBuilder) {
        // Use the default notification sound for all notifications
        notificationBuilder
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.ic_motorcycle_black_24dp);
    }

    @Override
    public void editForegroundNotification(NotificationCompat.Builder notificationBuilder) {
        // Use the default notification sound for all notifications
        notificationBuilder
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.drawable.ic_motorcycle_black_24dp);
    }

    @Override
    public void editNotificationChannel(NotificationChannel notificationChannel,
            MessageType messageType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            switch (messageType) {
                case BROADCAST:
                    notificationChannel.setName("SampleBroadcastChannel");
                    break;
                case GEOZONE:
                    notificationChannel.setName("SampleGeozoneChannel");
                    break;
                case ONDEMAND:
                    notificationChannel.setName("SampleOnDemandChannel");
                    break;
                case BEACON:
                    notificationChannel.setName("SampleBeaconChannel");
                    break;
                case FOREGROUND:
                    notificationChannel.setName("SampleForegroundNotificationChannel");
                    break;
                case DEFAULT:
                default:
                    notificationChannel.setName("The default Channel name");
            }

        }
    }

}

