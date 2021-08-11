# Phunware Engagement SDK for Android

[![Nexus](https://img.shields.io/nexus/r/com.phunware.engagement/mobile-engagement?color=brightgreen&server=https%3A%2F%2Fnexus.phunware.com)](https://nexus.phunware.com/content/groups/public/com/phunware/engagement/mobile-engagement/)

Phunware's Engagement SDK for Android. Visit https://www.phunware.com/ for more information or [sign into the MaaS Portal](http://maas.phunware.com/) to set up Engagement.

### Requirements
* minSdk 23.
* AndroidX.

### Download
Add the following repository to your top level `build.gradle` file:
```groovy
repositories {
    maven {
            url "https://nexus.phunware.com/content/groups/public/"
        }
}
```

Add the following dependency to your app level `build.gradle` file:
```groovy
dependencies {
    implementation "com.phunware.engagement:mobile-engagement:<version>"
}
```

### Android project setup
##### Keys
To use any of the Phunware MaaS SDKs you'll need to add the following entries to your AndroidManifest.xml, making sure to replace the `value` properties with your actual App ID and Access Key:

``` xml
<meta-data
    android:name="com.phunware.maas.APPLICATION_ID"
    android:value="YOUR_APP_ID"/>

<meta-data
    android:name="com.phunware.maas.ACCESS_KEY"
    android:value="YOUR_ACCESS_KEY"/>
```

For instructions on how to obtain an App ID and an Access key, please see the `MaaS Setup` section below.

##### Permissions
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
```

### Usage
##### Initializing the SDK

```kotlin
Engagement.init(applicationContext)
```

##### Enabling push notifications
After initializing the SDK, it's ready to be used. However, you might want to display some information to the user before sending them push notifications.
For this reason, push notifications come disabled by default. After initializing the SDK and displaying the necessary information to your user, enable push notifications by calling:

```kotlin
Engagement.enablePushNotifications()
```

Note: Android doesn't require any permissions in order to enable push notifications. If you feel you don't need to display any information about this to your user, you can simply call the above method immediately after calling `init()`.

##### Enabling location features
Phunware's Engagement SDK supports location based campaigns. To enable that, you need to first make sure your app has been granted location permissions (including background location permissions).
Note: Background location permission (`ACCESS_BACKGROUND_LOCATION`) is important to make sure the SDK detects when your user has entered or exited a Geofence even when the app is in background.
After being granted permissions, enable location based campaigns by calling:

```kotlin
Engagement.locationManager().start()
```

You're all set to receive Engagement Messages in your App!

##### Accessing messages (Message Center)
You can access messages sent by MaaS by calling:

```kotlin
Engagement.fetchMessages(startDate, endDate, Callback<List<Message>>())
```

or, to access a specific message:

```kotlin
Engagement.fetchMessage(messageId, Callback<Message>() {})
```

### MaaS Setup
1. Create a new Android application in the [MaaS portal](https://maas.phunware.com/)
2. Your new MaaS application will contain an `Application ID` and an `Access key` that you'll add to the metadata entries of your `AndroidManifest.xml` as mentioned in the `Android project setup` section above.
3. Set up FCM following the instructions at https://firebase.google.com/docs/android/setup.
4. Create a project on Firebase console

    - Choose 'Add Firebase to Android app'
    - In the Firebase console, the package name should be the same as the `applicationId` in your app level's `build.gradle`.  If you're using the Sample App to test, the default `applicationId` is `com.phunware.engagement.sample`

5. In MaaS, navigate to your newly created Android app and choose Edit. Replace the API Key and Sender ID values with the values from ServerKey and SenderId on the Firebase console (under the CloudMessaging section).
6. The Firebase console will create a `google-services.json` file for the app you just created.
7. Add the `google-services.json` file from Firebase to your project's main directory (i.e.: `sample/google-services.json`).
8. Add the google-services gradle plugin to your application level build.gradle file:

  `apply plugin: 'com.google.gms.google-services'`

9. You're all set! For project setup and usage instructions, see the corresponding sections above.

Note: If you're testing with the Sample App and want to support the map view in the Location details screen, you'll need to obtain a Google Maps API Key and add it to the `com.google.android.geo.API_KEY` entry in the `AndroidManifest.xml` file.

###  Privacy
You understand and consent to Phunware’s Privacy Policy located at www.phunware.com/privacy. If your use of Phunware’s software requires a Privacy Policy of your own, you also agree to include the terms of Phunware’s Privacy Policy in your Privacy Policy to your end users.
  
### Terms
Use of this software requires review and acceptance of our terms and conditions for developer use located at http://www.phunware.com/terms/
