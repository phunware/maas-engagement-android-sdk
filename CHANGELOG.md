# Engagement SDK Changelog

## Version 3.6.5 (Thursday, Sep 3nd, 2020)

#### Bug fixes / performance enhancements
* Fixed an issue where the device would lag or produce an ANR when location permissions are allowed "only while using the app".

## Version 3.6.3 (Monday, Mar 2nd, 2020)

#### Bug fixes / performance enhancements
* Improved error handling

## Version 3.6.2 (Monday, Jan 20th, 2020)

#### Bug fixes / performance enhancements
* Fixed issues in analytics event tracking

## Version 3.6.1 (Thursday, May 16th, 2019)

#### Bug fixes / performance enhancements
* Updated to Firebase Cloud Messaging

## Version 3.6.0 (Tuesday, Apr 30th, 2019)

#### Features
* Updated geofence implementation to improve detection of geofence transition events

## Version 3.5.4 (Wednesday, Feb 27th, 2019)

#### Features
* Added support for Android 9.0 (Pie)
* Added support for metadata in OnDemand broadcast
* Updated to Google Play Services 16.0.0
* Updated to Core 3.5.0
* Built with Android Support Library 28.0.0

## Version 3.5.3 (Friday, Sep 14th, 2018)

#### Bug fixes / performance enhancements
* Added support to display campaign notifications without title
* Fixed crash during device reboot for apps that have not initialized Engagement SDK
* Upgraded to Core 3.4.2

## Version 3.5.2 (Monday, Aug 13th, 2018)
#### Features
* Delaying geofence requests until device has valid location
* Added Campaign type constants for public use

## Version 3.5.1 (Monday, Jun 25th, 2018)
#### Bug fixes / performance enhancements
* Fixed issue with recurring notifications for campaigns with metadata

## Version 3.5.0 (Monday, Jun 4th, 2018)
#### Features
* Faster geofence detections on app install
* Handle device reboots and restart services
* Added registration with Core Module
* Updated deprecated location accquisition APIs

#### Bug fixes / performance enhancements
* Fixed time delay in starting beacon ranging
* Fixed duplicate geofence notifications on screen orientation change
* Fixed re-registering geofences
* Fixed duplicate enable push notification requests to server
* Fixed synchronization issues

## Version 3.4.0 (Tuesday, Apr 10th, 2018)
#### Features
* Upgraded to Google Play Services 11.8.0
* Upgraded to Core 3.3.0

#### Bug fixes / performance enhancements
* Changed foreground notification message while searching for beacon campaigns

## Version 3.3.1 (Tuesday, Mar 27th, 2018)
#### Bug fixes / performance enhancements
* Upgraded Sql Cipher library to version 3.5.9 to support 64 bit architecture.

## Version 3.3.0 (Wednesday, Mar 14th, 2018)
#### Bug fixes / performance enhancements
* Android Oreo compatibility fixes for receiving push notifications in the background
* Fix for receiving background location updates
* Fix for receiving engagement messages on Oreo devices when device is within geofence during app installation
* Fix for other background crashes

#### Developer stuff
* More logging for easier troubleshooting

## Version 3.2.0 (2017-11-13)
#### Features
* New enablePushNotifications api off of Engagement base class *Must be called in order to receive push notifications*

#### Bug fixes / performance enhancements
* Android Oreo compatibility fixes for background use, please see migration.md for more details

## Version 3.1.2 (2017-8-22)
#### Features
* Flexible broadcast

## Version 3.1.1 (2017-8-14)
#### Bug fixes / performance enhancements
* Various bug fixes

## Version 3.1.0 (2017-5-11)
#### Bug fixes / performance enhancements
* Various bug fixes

#### Developer stuff
* Messaging SDK renamed to Engagement SDK

## Version 3.0.5 (2017-4-19)
#### Features
* Static id updates

#### Bug fixes / performance enhancements
* Deep-linking bug fix
* Various bug fixes

## Version 3.0.4 (2017-1-23)
#### Bug fixes / performance enhancements
* Various bug fixes

## Version 3.0.3 (2017-1-04)
#### Bug fixes / performance enhancements
* Various bug fixes

## Version 3.0.2 (2016-12-08)
#### Features
* Single device push notifications are now supported
* Updated SqlCipher for Android 7+ support

#### Bug fixes / performance enhancements
* Various bug fixes

## Version 3.0.1 (2016-10-10)
#### Features
* Updated Google Play Service to 9.6.1
* Updated to Core 3.0.2

#### Bug fixes / performance enhancements
* Various bug fixes

## Version 3.0.0 (2016-08-31)
#### Features
* Complete re-write of Messaging SDK
