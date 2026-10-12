# Smart Drying Rack (Android, Kotlin, Compose)

Phase 1 build: a working app that runs end to end on a **mock rack**, with the architecture ready for the real ESP32.

## Open it
1. Android Studio (Koala or newer) > Open > this folder. Let Gradle sync. Android Studio creates the Gradle wrapper jar from `gradle-wrapper.properties`.
2. If sync complains about versions, accept the suggested upgrades. Versions were chosen to be mutually compatible, not to be the newest.
3. Run on a phone or emulator (minSdk 26). Unit tests: `Run > app/src/test`.

## What works now
- Home: connection state, rack state with animated rack, temperature, humidity, rain sensor, clothes-dry percent, forecast (demo data), drying progress.
- Control: Auto/Manual, Extend, Retract, Emergency stop, rack details, schedule (times, days, enable), forecast and buzzer toggles, demo tools (simulate rain, sensor error, disconnect, clothes dry).
- Alerts: filtered timeline, mark read, clear all. History: operation counts and recent events.
- Automation: rain debouncing (3 readings), rain protection, forecast protection, schedule, command priority, repeat/conflict blocking, 10 s movement timeout, emergency hold.

## Architecture
`UI > ViewModel > Repository > RackController`. Only `MockRackController` exists. Add `WifiRackController` (REST or MQTT) and rebind it in `di/HardwareModule.kt`; no UI or automation code changes.

## Not built yet (next phases)
Room persistence and weekly chart, DataStore settings and Settings screen, Retrofit weather (OpenWeatherMap key via `local.properties`, not source), notifications and FCM, WorkManager, Wi-Fi/MQTT/BLE controllers and the device setup screen, light/system theme, Filipino strings, tablet navigation rail, instrumentation tests.
Until Room/DataStore land, schedule, settings and events reset when the app restarts.

## Hardware notes
- Your ESP32 can talk Wi-Fi directly, so build the Wi-Fi controller before BLE.
- "Clothes dry %" needs a sensor. A load cell with an HX711 is not on your shopping list.
- Drying-time estimate on Home is a placeholder, not a measurement.
