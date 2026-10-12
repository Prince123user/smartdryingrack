# Smart Drying Rack

Android Kotlin/Compose companion for an ESP32 rack over Wi-Fi REST. The app starts offline until a real rack address is configured. It does not manufacture sensor readings, connection details, forecasts or drying statistics.

## Develop

Use JDK 17, Gradle 8.13 and Android SDK platform 34 / build-tools 35.0.0. Open the repository root in Android Studio. `my_app/` is a separate Flutter starter and is not the Android application described here.

The repository includes the full Gradle 8.13 wrapper with a pinned distribution checksum. Hilt uses Kotlin kapt 1.9.24; no KSP plugin is required. Java and Android SDK paths are machine-local; there are no hardcoded Windows paths. In Android Studio, open this repository root and choose JDK 17 (recommended) or JDK 21 as the Gradle JDK. An IDE bundled with Java 25 must use a separate supported Gradle JDK. Install SDK platform 34 and build-tools 35.0.0 through SDK Manager.

```bash
./gradlew :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
# Windows: gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
```

In the prepared Codex cloud machine:

```bash
source /workspace/.cloud-tools/activate.sh
cd /workspace/smartdryingrack
cloud_gradle :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
```

Debug APK: `app/build/outputs/apk/debug/app-debug.apk`. Minimum Android version: 8.0 (API 26).

## Connect your rack

Put the phone and rack on the same Wi-Fi network. Open Connection, enter the rack's base address, and tap **Save and connect**. The implemented firmware contract is documented in [DEVICE_API.md](docs/DEVICE_API.md): `GET /api/status`, `POST /api/commands` with request IDs, and explicit command acknowledgements. An existing firmware API specification was not supplied, so route/field adaptation may be needed.

Connection is considered online only after valid telemetry. Missing clothes-dry measurements are shown as unavailable. Commands are never presented as completed based on an acknowledgement alone.

## What works

- Real REST status polling, validated readings, retrying status after failures and explicit disconnection.
- Manual Extend/Retract/Stop, command deduplication at the app gate, emergency hold and movement timeout handling.
- Rain debouncing, wet/stale-reading extension blocking and opt-in phone schedules.
- Saved address, appearance, alert preferences, schedules and latest 100 events.
- Reference-inspired midnight navy/cyan design, detailed rack artwork, glowing controls and matching navigation.
- A real temperature-history chart, sampled every five minutes while connected, with Today/Week selection.
- Actual alert/history timelines, a real notifications feed, saved light/dark selection and responsive sensor cards.

Manual mode is selected on every process restart. New schedules are disabled until enabled. App automation only runs while its process is alive and connected; firmware must independently implement rain, motor, limit-switch and emergency-stop protection.

## Current boundaries

Weather integration, BLE/MQTT, device discovery/provisioning, system push notifications, remote authentication and firmware buzzer control are not implemented. LAN HTTP is unencrypted and restricted by address validation to private IPv4 addresses; do not expose it to the public internet. Use HTTPS with trusted certificates for other hosts.

The REST adapter is tested against an HTTP test server. A Robolectric test launches the actual Android activity and exercises the primary navigation; rendered screenshots are written under `app/build/outputs/ui-screenshots/`. Physical ESP32 operation and on-device GPU rendering still need phone validation.

## Design

The current design follows the supplied dark futuristic reference. See [Android Studio setup and design notes](docs/ANDROID_STUDIO.md). Earlier Garden Home concepts under docs are retained as design history; they are not the current interface.
