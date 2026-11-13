# Boss Modz Store — Demo Notifications

This Android project creates a clearly labelled **demo** notification app for testing.
It does not connect to a store backend, place orders, process payments, or verify real transactions.

## Features
- App label: Boss Modz Store
- Boss Modz circular launcher/notification artwork
- Android 13+ notification permission prompt
- Start / Stop controls
- One demo notification immediately, then every 30 seconds while the foreground service is running
- Order confirmed / processing / ready / support update sample messages
- No trading, Big/Small, betting, or payment amount content
- GitHub Actions builds a debug APK and attaches it to the `latest` release

## Build on GitHub
1. Create an empty GitHub repository (suggested name: `boss-modz-apk`).
2. Run the Termux command supplied with this ZIP.
3. Open the repository's **Actions** tab and wait for the build to finish.
4. Download `Boss-Modz-Store.apk` from the `latest` release.

## Important
- Notifications are simulated and include a DEMO label.
- The repeating foreground service runs only after the user taps Start and stops when Stop is tapped.
- Android may restrict background services depending on device settings and OS version.
- This is a debug APK build, not a Play Store release.
