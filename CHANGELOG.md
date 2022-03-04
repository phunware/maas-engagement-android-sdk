# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [4.0.1][] - 2021-11-01

### Added
- Added a new `NotificationCustomizationService.shouldDisplayNotificationForMessage(...)` callback to control whether or not a notification should be displayed.

### Fixed
- Fixed downloading beacon bundles
- Fixed parsing campaign metadata and promotions
- Fixed BEACON_ENTRY and BEACON_EXIT campaign triggers 
- Fixed GEOFENCE_ENTRY and GEOFENCE_EXIT synchronization with MaaS

## [4.0.0][] - 2021-08-11

### Added

- Message Center API
- Ability to process intercepted FCM messages.

### Changed

- Migrated to AndroidX
- Simplified initialization routine
- Improved beacon scanning
- Bumped minSdkVersion to API 23 (Android 6.0)
- Bumped targetSdkVersion to API 30 (Android 11)
- Updated to modern Google play services
- Updated to modern Firebase dependencies
- Updated Phunware dependencies to 4.0.0

### Removed

- Removed all existing deprecated API

### Fixed

- Fixed several crashes
- Fixed several performance issues

## [3.6.5][] - 2020-09-03

### Fixed

- Fixed an issue where the device would lag or produce an ANR when location permissions are allowed "only while using the app".

## [3.6.3][] - 2020-03-02

### Fixed

- Improved error handling

## [3.6.2][] - 2020-01-20

### Fixed

- Fixed issues in analytics event tracking

## [3.6.1][] - 2019-05-16

### Changed

- Updated to Firebase Cloud Messaging

## [3.6.0][] - 2019-04-30

### Changed

- Updated geofence implementation to improve detection of geofence transition events

## [3.5.4][] - 2019-02-27

### Added

- Added support for Android 9.0 (Pie)
- Added support for metadata in OnDemand broadcast

### Changed

- Updated to Google Play Services 16.0.0
- Updated to Core 3.5.0
- Built with Android Support Library 28.0.0

## [3.5.3][] - 2018-09-14

### Added

- Added support to display campaign notifications without title

### Changed

- Upgraded to Core 3.4.2

### Fixed

- Fixed crash during device reboot for apps that have not initialized Engagement SDK

## [3.5.2][] - 2018-08-13

### Added

- Added Campaign type constants for public use

### Changed

- Delaying geofence requests until device has valid location

## [3.5.1][] - 2018-06-25

### Fixed

- Fixed issue with recurring notifications for campaigns with metadata

## [3.5.0][] - 2018-06-04

### Added

- Handle device reboots and restart services
- Added registration with Core Module

### Changed

- Faster geofence detections on app install

### Deprecated

- Updated deprecated location accquisition APIs

### Fixed

- Fixed time delay in starting beacon ranging
- Fixed duplicate geofence notifications on screen orientation change
- Fixed re-registering geofences
- Fixed duplicate enable push notification requests to server
- Fixed synchronization issues

## [3.4.0][] - 2018-04-10

### Changed

- Upgraded to Google Play Services 11.8.0
- Upgraded to Core 3.3.0
- Changed foreground notification message while searching for beacon campaigns

## [3.3.1][] - 2018-03-27

### Changed

- Upgraded Sql Cipher library to version 3.5.9 to support 64 bit architecture.

## [3.3.0][] - 2018-03-14

### Added

- More logging for easier troubleshooting

### Fixed

- Android Oreo compatibility fixes for receiving push notifications in the background
- Fix for receiving background location updates
- Fix for receiving engagement messages on Oreo devices when device is within geofence during app installation
- Fix for other background crashes

## [3.2.0][] - 2017-11-13

### Added

- New enablePushNotifications api off of Engagement base class *Must be called in order to receive push notifications*

### Fixed

- Android Oreo compatibility fixes for background use, please see MIGRATION.md for more details

## [3.1.2][] - 2017-08-22

### Added

- Flexible broadcast

## [3.1.1][] - 2017-08-14

### Fixed

- Various bug fixes

## [3.1.0][] - 2017-05-11

### Changed

- Messaging SDK renamed to Engagement SDK

### Fixed

- Various bug fixes

## 3.0.5 - 2017-04-19

### Changed

- Static id updates

### Fixed

- Deep-linking bug fix
- Various bug fixes

## 3.0.4 - 2017-01-23

### Fixed

- Various bug fixes

## 3.0.3 - 2017-01-04

### Fixed

- Various bug fixes

## 3.0.2 - 2016-12-08

### Added

- Single device push notifications are now supported

### Changed

- Updated SqlCipher for Android 7+ support

### Fixed

- Various bug fixes

## 3.0.1 - 2016-10-10

### Changed

- Updated Google Play Service to 9.6.1
- Updated to Core 3.0.2

### Fixed

- Various bug fixes

## 3.0.0 - 2016-08-31

### Added

- Complete re-write of Messaging SDK

[4.0.1]: https://github.com/phunware/maas-engagement-android-sdk/compare/4.0.0...4.0.1
[4.0.0]: https://github.com/phunware/maas-engagement-android-sdk/compare/v3.6.5...4.0.0
[3.6.5]: https://github.com/phunware/maas-engagement-android-sdk/compare/v3.6.3...v3.6.5
[3.6.3]: https://github.com/phunware/maas-engagement-android-sdk/compare/v3.6.2...v3.6.3
[3.6.2]: https://github.com/phunware/maas-engagement-android-sdk/compare/v3.6.1...v3.6.2
[3.6.1]: https://github.com/phunware/maas-engagement-android-sdk/compare/v3.6.0...v3.6.1
[3.6.0]: https://github.com/phunware/maas-engagement-android-sdk/compare/v3.5.4...v3.6.0
[3.5.4]: https://github.com/phunware/maas-engagement-android-sdk/compare/v3.5.3...v3.5.4
[3.5.3]: https://github.com/phunware/maas-engagement-android-sdk/compare/v3.5.2...v3.5.3
[3.5.2]: https://github.com/phunware/maas-engagement-android-sdk/compare/v3.5.1...v3.5.2
[3.5.1]: https://github.com/phunware/maas-engagement-android-sdk/compare/v3.5.0...v3.5.1
[3.5.0]: https://github.com/phunware/maas-engagement-android-sdk/compare/v3.4.0...v3.5.0
[3.4.0]: https://github.com/phunware/maas-engagement-android-sdk/compare/v3.3.1...v3.4.0
[3.3.1]: https://github.com/phunware/maas-engagement-android-sdk/compare/v3.3.0...v3.3.1
[3.3.0]: https://github.com/phunware/maas-engagement-android-sdk/compare/v3.2.0...v3.3.0
[3.2.0]: https://github.com/phunware/maas-engagement-android-sdk/compare/v3.1.2...v3.2.0
[3.1.2]: https://github.com/phunware/maas-engagement-android-sdk/compare/v3.1.1...v3.1.2
[3.1.1]: https://github.com/phunware/maas-engagement-android-sdk/compare/v3.1.0...v3.1.1
[3.1.0]: https://github.com/phunware/maas-engagement-android-sdk/releases/tag/v3.1.0
