# Android O Updates for Engagement

## Notification Channels

In this next release of Engagement there will be a few major changes that are
needed in order to handle Android O's background limitations.

Major changes have occurred in the **NotificationCustomizationService**

We now have two new methods to utilize here:

```
abstract public void editForegroundNotification(
          NotificationCompat.Builder notificationBuilder);

abstract public void editNotificationChannel(
          NotificationChannel notificationChannel, MessageType messageType);
```

**editForegroundNotification**
  - Provides an endpoint for modifying the notification that will be shown to
  the user if using the Beacons library. (Note this notification is only shown
  when ranging for beacons.)

**editNotificationChannel**
  - Provides an endpoint for modifying the various Notification Channels that
  exist within the SDK.
  - The *notificationChannel* parameter is the channel being passed to the user from the SDK
  - The *messageType* parameter provides the notificationChannel for the user
  to modify the preferences of, along with what MessageType it correlates with.

  In Engagement, there are default channels that correspond to the following
  types:

  ```
  BROADCAST
   GEOZONE
   BEACON
   ONDEMAND
   FOREGROUND
   DEFAULT
 ```
   Every type of message corresponds to one of these types. If for some reason,
   no relevant messageType is retrieved from the message, the *DEFAULT* channel
   is used. The end user could ultimately pare down the number of channels to
   1 if they wanted to by routing all notifications through one channel.

## Foreground Beacon Ranging

As alluded to above, the *BeaconRangingService* now spins up a foreground
notification when started. In doing so, we also provide a way to stylize
the notification through the same *NotificationCustomizationService* that has
been provided. By implementing the *editForegroundNotification* method the
user then modifies the style of the foreground notification being shown.
