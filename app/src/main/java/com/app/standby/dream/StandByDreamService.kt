package com.app.standby.dream

import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.BatteryManager
import android.service.dreams.DreamService
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.app.standby.ui.theme.StandByScreen

class StandByDreamService : DreamService(), LifecycleOwner {
    private var textView: TextView? = null

    private val lifecycleRegistry = LifecycleRegistry(this)

    private var composeView: ComposeView? = null



    override val lifecycle: Lifecycle
        get() = lifecycleRegistry


    override fun onCreate() {
        super.onCreate()
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        isFullscreen = true
        isInteractive = true

        composeView = ComposeView(this).apply {
            setContent {
                StandByScreen()
            }

        }

//        textView = TextView(this).apply {
//            textSize = 32f
//            setTextColor(android.graphics.Color.WHITE)
//            setBackgroundColor(android.graphics.Color.BLACK)
//            gravity = Gravity.CENTER
//        }
//        setContentView(textView)
        refresh()
    }

    override fun onDreamingStarted() {
        super.onDreamingStarted()

        if (!isDeviceCharging()) {
            finish()
            return
        }
        refresh()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        refresh()
    }

    override fun onDetachedFromWindow() {
//        textView = null
        super.onDetachedFromWindow()
    }

    override fun onDreamingStopped() {
        super.onDreamingStopped()
    }

    private fun refresh() {
        val charging = isDeviceCharging()
        val landscape = resources.configuration.orientation ==
                Configuration.ORIENTATION_LANDSCAPE

        Log.d("Shubh", "charging=$charging landscape=$landscape")

        if (charging && landscape) {
            isScreenBright = true
        } else {
            isScreenBright = false
            finish()
        }
    }

    private fun isDeviceCharging(): Boolean {
        val intent = registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        ) ?: return false

        val plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
        return plugged != 0
    }
}