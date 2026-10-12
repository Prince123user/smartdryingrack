# Android Studio: run Smart Drying Rack

Open the repository root containing `settings.gradle.kts` and `app/`. Do not open `my_app/`, which is a separate Flutter starter.

1. Use a current Android Studio version that supports Android Gradle Plugin 8.13.2. If the IDE requests a newer version for this plugin, update the IDE.
2. Set Gradle JDK to JDK 17 (recommended) or JDK 21, and use the Gradle wrapper. The wrapper pins Gradle 8.13 and verifies its distribution checksum.
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

## KspTaskJvm / Gradle sync recovery

Hilt annotation processing uses Kotlin **kapt 1.9.24**, matching the Kotlin Android plugin. KSP is not applied by this project. This avoids Hilt 2.51.1's dependency on the KSP Gradle task class that can fail to load in an incompatible local plugin environment.

If upgrading from the earlier project version:

1. Pull the latest main branch, or download a new ZIP and extract it into a new folder. Do not merge a fresh ZIP over a folder whose build files were automatically upgraded by the IDE.
2. Confirm the root and app build files use `org.jetbrains.kotlin.kapt` and the Hilt compiler uses `kapt(...)`. Remove any local additions of the KSP plugin.
3. Stop this project's Gradle daemons and clean generated output:

```powershell
# Windows PowerShell, from the repository root
.\gradlew.bat --stop
.\gradlew.bat clean :app:assembleDebug --refresh-dependencies
```

```bash
# macOS / Linux
./gradlew --stop
./gradlew clean :app:assembleDebug --refresh-dependencies
```

4. Open the same repository root in Android Studio, select JDK 17 or 21 as the Gradle JDK, disable Gradle offline mode if dependencies need downloading, and sync.

Keep the repository's pinned plugin versions instead of accepting individual Kotlin/Gradle upgrades. If sync still fails, capture the first `Caused by:` section from the full Gradle stack trace and the Android Studio version; a generic cache-recovery suggestion does not identify the cause.

## Android Studio Rabbit with a Java 25 IDE runtime

Android Studio's About dialog reports the Java runtime of the IDE. Check the **Gradle JDK** separately under **Settings → Build, Execution, Deployment → Build Tools → Gradle**.

This project pins Gradle 8.13. Set its Gradle JDK to **JDK 17 (recommended)** or **JDK 21**. Do not select a Java 25 JBR for this wrapper. In Rabbit, if the embedded JBR is Java 25, choose **Download JDK**, select version **17**, and apply it for this project. The IDE itself can continue using Java 25.

For PowerShell commands, ensure the terminal also uses a supported JDK. Replace this placeholder with the actual installed JDK 17 directory; do not copy it literally:

```powershell
$env:JAVA_HOME = "C:\path\to\jdk-17"
& "$env:JAVA_HOME\bin\java.exe" -version
.\gradlew.bat --version
.\gradlew.bat --stop
.\gradlew.bat clean :app:assembleDebug
```

The Java/Launcher/Daemon version reported by the wrapper should be 17 or 21. Avoid upgrading the wrapper to Gradle 9 only to accommodate the IDE runtime; that also requires revisiting the Android, Kotlin and Hilt plugin compatibility.
