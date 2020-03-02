# Engagement SDK Migration Guide

## 3.5.x to 3.6.3

#### General

This release has enhancements. See CHANGELOG.md for more info.

##### Upgrade Steps

1. Open the `build.gradle` from your project and change the dependency to implementation 'com.phunware.engagement:mobile-engagement:3.6.3' and then sync the project.

## 3.5.x to 3.5.4

#### General

This release has enhancements. See CHANGELOG.md for more info.

### Library updates
- compileSdkVersion - 28
- targetSdkVersion - 28
- Support Library version - 28.0.0

##### Upgrade Steps

1. Open the `build.gradle` from your project and change the dependency to implementation 'com.phunware.engagement:mobile-engagement:3.5.4' and then sync the project.

## 3.5.x to 3.5.3

#### General

This release has major bug fixes. See CHANGELOG.md for more info.

### Library updates
- Support Library version - 27.1.0

##### Upgrade Steps

1. Open the `build.gradle` from your project and change the dependency to implementation 'com.phunware.engagement:mobile-engagement:3.5.3' and then sync the project.

## 3.4.0 to 3.5.x

#### General

This release has major bug fixes and feature enhancements.  See CHANGELOG.md for more info.

##### Upgrade Steps

1. Open the `build.gradle` from your project and change the dependency to compile 'com.phunware.engagement:mobile-engagement:3.5.0' and then sync the project.

## 3.3.x to 3.4.0

### Library updates
- compileSdkVersion - 27
- Support Library version - 27.1.0
- Google Play Services version - 11.8.0
- Okhttp version - 3.10.0

#### General

This release has bug fixes and feature enhancements.  See CHANGELOG.md for more info.

##### Upgrade Steps

1. Open the `build.gradle` from your project and change the dependency to compile 'com.phunware.engagement:mobile-engagement:3.4.0' and then sync the project.

## 3.2.0 to 3.3.0

#### General

This release has bug fixes and feature enhancements.  See CHANGELOG.md for more info.

##### Upgrade Steps

1. Open the `build.gradle` from your project and change the dependency to compile 'com.phunware.engagement:mobile-engagement:3.3.0' and then sync the project.

## 3.1.x to 3.2.0

#### General

This release has major changes that are needed in order to handle Android O's background limitations.

##### Upgrade Steps

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

The *BeaconRangingService* now spins up a foreground
notification when started. In doing so, we also provide a way to stylize
the notification through the same *NotificationCustomizationService* that has
been provided. By implementing the *editForegroundNotification* method the
user then modifies the style of the foreground notification being shown.

