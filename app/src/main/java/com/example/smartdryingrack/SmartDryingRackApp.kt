package com.example.smartdryingrack

import android.app.Application
import com.example.smartdryingrack.automation.AutomationManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SmartDryingRackApp : Application() {
    @Inject lateinit var automationManager: AutomationManager

    override fun onCreate() {
        super.onCreate()
        automationManager.start()
    }
}
