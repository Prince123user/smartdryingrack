# Android Studio: run Smart Drying Rack

Open the repository root containing `settings.gradle.kts` and `app/`. Do not open `my_app/`, which is a separate Flutter starter.

1. Use a current Android Studio version that supports Android Gradle Plugin 8.13.2. If the IDE requests a newer version for this plugin, update the IDE.
2. Set Gradle JDK to the IDE's embedded JDK (17 or newer), and use the Gradle wrapper. The wrapper pins Gradle 8.13 and verifies its distribution checksum.
3. In SDK Manager install Android SDK platform 34, build-tools 35.0.0 and platform-tools. Android Studio creates machine-local `local.properties`; do not commit it.
4. Sync, select the **app** run configuration, and run on a phone/emulator with Android 8.0/API 26 or later.
5. Enter the ESP32 address on the connection screen, or choose Explore offline to inspect the interface without hardware.

No Java/SDK path, proxy hostname or local credential is hardcoded in the repository. On Windows use `gradlew.bat`; on Linux/macOS use `./gradlew`.

```bash
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest :app:lintDebug
```

The UI render/navigation test uses Robolectric's verified Android runtime artifacts from Maven, so its first run requires internet access. It launches the actual activity, exercises Home, Control, Alerts, History, Notifications and Settings, and saves PNG screenshots in `app/build/outputs/ui-screenshots/`. These are Android runtime renders, not generated design mockups. Validate real GPU behavior, accessibility and physical commands on your target phone before release.

## Applied design

- Midnight navy background, subtle circuit grid and translucent blue panels.
- Cyan primary controls, selected navigation and outlined icons; amber warning and red stop accents.
- Detailed bundled rack artwork replaces the previous line drawing. The artwork is decorative, not a camera feed or proof of rack position.
- Home/Control show the actual reported position separately from artwork.
- Alerts distinguish new/read events; read does not mean a physical fault is resolved.
- History plots real locally recorded temperatures, never seeded sample values. It requires two samples before showing a trend.
- Notifications show actual stored events, with mark-all-read.
- Settings expose only connected capabilities. Buzzer control, push notifications and forecast integration are not invented to match reference sample data.
- An adaptive cyan rack icon includes an Android 13+ monochrome variant.

The earlier sage/green design documents are retained for history. The navy/cyan reference is the current design direction.
