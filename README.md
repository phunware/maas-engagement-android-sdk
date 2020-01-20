# MaaS Engagement SDK for Android
  
[Android MaaS Engagement Documentation](http://phunware.github.io/maas-engagement-android-sdk/)  
=======  
**Version 3.6.2**
=======  
________________  
  
  
## Overview  
This is Phunware's Android SDK for Engagement. Visit http://maas.phunware.com/ for more details and to sign up.  
  
### Build requirements  
* Android SDK 4.3+ (API level 18) or above  
  
### Documentation  
  
* [API reference](http://phunware.github.io/maas-engagement-android-sdk/)  
* Developer documentation can be found at  
[developer.phunware.com](https://developer.phunware.com/pages/viewpage.action?pageId=3410099)  
  
Attribution  
-----------  
MaaS Engagement uses the following third party components.  
  
| Component     | Description   | License  |  
| ------------- |:-------------:| -----:|  
| [okhttp](https://github.com/square/okhttp)        | An HTTP+HTTP/2 client for Android and Java applications by Square, Inc. | [Apache 2.0](https://github.com/square/okhttp/blob/master/LICENSE.txt) |  
| [autovalue](https://github.com/google/auto/tree/master/value)        | AutoValue provides an easier way to create immutable value classes | [Apache 2.0](https://github.com/google/auto/blob/master/LICENSE.txt) |  
| [autovalue GSON](https://github.com/rharter/auto-value-gson)        | An extension for Google's AutoValue that creates a simple Gson TypeAdapterFactory for each AutoValue annotated object. | [Apache 2.0](https://github.com/rharter/auto-value-gson/blob/master/LICENSE.txt) |  
  
## Setup  
-------  
1. Bring up the sample app with Android studio  
2. Create a new Android application in the [MaaS portal](https://maas.phunware.com/)  
3. In `sample/src/main/res/values/strings.xml` replace the `appId`, `accessKey` and `sigKey` values with the values from your new application in MaaS.   
  
  We also use Google maps in the sample app. In order to use the locations feature, please replace the `map_key`  in `sample/src/main/res/values/strings.xml` with your map API key.
  
4. Set up GCM at https://developers.google.com/cloud-messaging/android/client  
5. Create a project on Firebase console  
  
    -> Choose 'Add Firebase to Android app'  
  
    -> In the Firebase console, the package name should be the same as `applicationId` in `sample/build.gradle`. The default `applicationId` in the sample app is `com.phunware.engagement.sample`  
      
6. In MaaS, navigate to your newly created Android app and choose Edit. Replace the API Key and Sender ID values with the values from ServerKey and SenderId on the Firebase console (under CloudMessaging section).  
7. The Firebase console creates a `google-services.json` file and downloads it to your default Downloads folder.  
8. Replace the default `sample/google-services.json` file with the dowloaded `google-services.json` from Firebase console.  
9. Add the google-services gradle plugin to your application's gradle file:  
  
  `apply plugin: 'com.google.gms.google-services'`  
    
10. Compile the project under Android Studio and run it on the device  
  
Privacy  
-----------  
You understand and consent to Phunware’s Privacy Policy located at www.phunware.com/privacy. If your use of Phunware’s software requires a Privacy Policy of your own, you also agree to include the terms of Phunware’s Privacy Policy in your Privacy Policy to your end users.  
  
Terms  
-----------  
Use of this software requires review and acceptance of our terms and conditions for developer use located at http://www.phunware.com/terms/
