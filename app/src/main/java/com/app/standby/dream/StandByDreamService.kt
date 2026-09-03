package com.app.standby.dream

import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.BatteryManager
import android.service.dreams.DreamService
import android.util.Log
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.app.standby.screens.StandByScreen

class StandByDreamService : DreamService(), LifecycleOwner, SavedStateRegistryOwner,
    ViewModelStoreOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

    private val dreamViewModelStore = ViewModelStore()

    private var composeView: ComposeView? = null


    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    override val viewModelStore: ViewModelStore
        get() = dreamViewModelStore

    override fun onCreate() {
        super.onCreate()

        savedStateRegistryController.performAttach()
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.handleLifecycleEvent(
            Lifecycle.Event.ON_CREATE
        )
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        isFullscreen = true
        isInteractive = true

        composeView = ComposeView(this).apply {
            setViewTreeLifecycleOwner(this@StandByDreamService)
            setViewTreeSavedStateRegistryOwner(this@StandByDreamService)
            setViewTreeViewModelStoreOwner(this@StandByDreamService)
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
            setContent {
                StandByScreen()
            }
        }
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED
        setContentView(composeView)
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
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        composeView = null
        super.onDetachedFromWindow()
    }

    override fun onDreamingStopped() {
        super.onDreamingStopped()
    }

    override fun onDestroy() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        dreamViewModelStore.clear()
        super.onDestroy()
    }

    private fun refresh() {
        val charging = isDeviceCharging()
        val landscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

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
            null, IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        ) ?: return false

        val plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
        return plugged != 0
    }
}