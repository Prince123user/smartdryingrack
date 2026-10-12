package com.example.smartdryingrack.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import com.example.smartdryingrack.core.designsystem.SmartDryingRackTheme
import com.example.smartdryingrack.data.repository.TemperaturePoint
import com.example.smartdryingrack.feature.history.HistoryScreen
import com.example.smartdryingrack.feature.history.HistoryUiState
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.smartdryingrack.MainActivity
import com.example.smartdryingrack.SmartDryingRackApp
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], application = SmartDryingRackApp::class, qualifiers = "w390dp-h844dp-xhdpi")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class NavigationRenderTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    private fun capture(name: String) {
        compose.mainClock.advanceTimeBy(500)
        compose.waitForIdle()
        compose.onNodeWithText("Smart Drying Rack").assertIsDisplayed()
        println("$name header bounds: " + compose.onNodeWithText("Smart Drying Rack").fetchSemanticsNode().boundsInRoot)
        // Draw the actual activity into a native Android canvas. PixelCopy callbacks are not
        // driven by Robolectric's paused main looper, so Compose captureToImage would time out.
        val image = compose.runOnIdle {
            val root = compose.activity.window.decorView
            check(root.width > 0 && root.height > 0)
            Bitmap.createBitmap(root.width, root.height, Bitmap.Config.ARGB_8888).also { root.draw(Canvas(it)) }
        }
        val output = File("build/outputs/ui-screenshots/$name.png")
        checkNotNull(output.parentFile).mkdirs()
        output.outputStream().use { image.compress(Bitmap.CompressFormat.PNG, 100, it) }
        check(output.length() > 1000) { "Screenshot was not written." }
    }

    @Test fun appLaunchesAndEveryPrimaryNavigationRenders() {
        compose.onNodeWithText("Explore offline").performScrollTo().performClick()
        compose.onNodeWithText("Connect your ESP32 rack").assertExists()
        compose.onNodeWithContentDescription("Illustration of a motorized drying rack with hanging laundry").assertExists()
        capture("01-home")
        compose.onNodeWithText("Control").performClick()
        compose.onNodeWithText("Extend").assertExists()
        capture("02-control")
        compose.onNodeWithText("Alerts").performClick()
        compose.onNodeWithText("No new alerts").assertExists()
        capture("03-alerts")
        compose.onNodeWithText("History").performClick()
        compose.onNodeWithText("Waiting for temperature history").assertExists()
        capture("04-history")
        compose.onNodeWithContentDescription("Notifications").performClick()
        compose.onNodeWithText("No notifications yet").assertExists()
        capture("05-notifications")
        compose.onNodeWithContentDescription("Settings").performClick()
        compose.onNodeWithText("ESP32 rack").assertExists()
        capture("06-settings")
    }
    @Test fun temperatureChartRendersWithExplicitTestFixture() {
        val now = System.currentTimeMillis()
        val fixture = listOf(TemperaturePoint(now - 600000, 30f), TemperaturePoint(now - 300000, 31f), TemperaturePoint(now, 32f))
        compose.runOnUiThread {
            compose.activity.setContent {
                SmartDryingRackTheme {
                    Surface {
                        Column {
                            Text("Smart Drying Rack")
                            Text("Render test · synthetic chart fixture")
                            HistoryScreen(HistoryUiState(temperatures = fixture))
                        }
                    }
                }
            }
        }
        compose.onNodeWithText("32.0°C · latest reading").assertExists()
        capture("07-history-test-fixture")
    }
}
