# StandBy for Android

An Android ambient display experience inspired by iPhone StandBy, built using **Kotlin, Jetpack Compose, and Android DreamService**.

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?logo=jetpackcompose&logoColor=white)
![Android](https://img.shields.io/badge/Android-DreamService-3DDC84?logo=android&logoColor=white)
![Coroutines](https://img.shields.io/badge/Kotlin-Coroutines-7F52FF)

StandBy runs through Android's native screensaver system and provides an ambient dashboard designed for phones placed in **landscape orientation while charging**.

The project explores how a StandBy-style experience can be built using public Android APIs while respecting modern background execution and screen activation restrictions.

---

## ✨ Features

### StandBy Dashboard

A landscape dashboard with independently switchable widgets:

- 🕐 **Clock**
- 📅 **Calendar**
- 🔋 **Battery status**
- ⏱️ **Timer**

Each half of the dashboard can switch between its available widgets with animated transitions.

### Full-Screen Clock

Swipe horizontally from the dashboard to access a minimal full-screen clock designed for bedside or desk usage.

### Charging + Landscape Validation

The Dream remains active only when:

- The device is connected to power
- The device is in landscape orientation

If either condition is no longer satisfied, the Dream exits instead of leaving an unintended blank screen.

### OLED Burn-In Mitigation

Since an ambient display can remain visible for extended periods, the UI incorporates:

- True-black backgrounds
- Reduced-luminance foreground colors
- Periodic pixel shifting of static content

The content is subtly repositioned over time to reduce prolonged illumination of the same OLED pixels.

---

## 🚀 Setup

StandBy runs as an Android screen saver, so it must be enabled once after install.

1. Clone the repository and open it in Android Studio.
2. Sync Gradle and run the app on a physical device.
3. Go to **Settings → Display → Screen saver** (path varies by OEM).
4. Select **StandBy** as the current screen saver.
5. Set **When to start** to *While charging* or *While charging or docked*.

Then place the device in **landscape** on a charger and let the screen time out.

**Minimum SDK:** Android 8.0 (API 26)

### Notes

- StandBy activates when the screen times out on its own. Pressing the power button turns the display off without starting a screen saver — this is enforced by the platform.
- Turn off **Developer options → Stay awake while charging** during testing, or the screen never sleeps and the screen saver never starts.
- Landscape detection uses the system orientation, so auto-rotate must be enabled.

## 📱 Screens

https://github.com/user-attachments/assets/546b0c96-9c8f-42b4-89b3-807ad5b0c10e

---

## 🛠️ Tech Stack

| Area | Technology |
| --- | --- |
| Language | Kotlin |
| UI | Jetpack Compose |
| System Integration | DreamService |
| Async | Kotlin Coroutines |
| State | Compose State |
| Device State | Android Battery APIs, BroadcastReceiver |
| Lifecycle | LifecycleRegistry, SavedStateRegistry, ViewModelStore |

---

## 💡 Why DreamService?

A naive implementation could listen for charging events and attempt to launch a fullscreen Activity. Modern Android versions, however, restrict background Activity launches, making that approach unreliable for this use case.

Android already provides a system-managed ambient display mechanism through `DreamService`, allowing StandBy to integrate with the native screensaver infrastructure instead of attempting to bypass Android's background execution restrictions.

```text
Android Screensaver System
          ↓
StandByDreamService
          ↓
Lifecycle-aware ComposeView
          ↓
StandBy UI
```

---

## 🧩 Hosting Jetpack Compose inside DreamService

One of the main technical challenges was hosting Jetpack Compose inside a `DreamService`.

Unlike `ComponentActivity`, `DreamService` does not automatically provide the ViewTree infrastructure expected by `ComposeView`.

The service therefore provides:

![LifecycleOwner](https://img.shields.io/badge/LifecycleOwner-Lifecycle_Aware-3DDC84)
![SavedStateRegistryOwner](https://img.shields.io/badge/SavedStateRegistryOwner-State_Restoration-3DDC84)
![ViewModelStoreOwner](https://img.shields.io/badge/ViewModelStoreOwner-ViewModel_Scope-3DDC84)

Without the required owners, Compose cannot establish the lifecycle and state infrastructure normally supplied by an Activity.

`StandByDreamService` attaches these owners directly to the Compose view tree:

```kotlin
setViewTreeLifecycleOwner(this@StandByDreamService)
setViewTreeSavedStateRegistryOwner(this@StandByDreamService)
setViewTreeViewModelStoreOwner(this@StandByDreamService)
```

A custom `LifecycleRegistry` is synchronized with the Dream lifecycle so that Compose becomes active while the Dream is running and is correctly destroyed with the service.

This allows a lifecycle-aware Compose hierarchy to run inside a system-managed Dream window without requiring an Activity.

---

## 🔋 Battery Monitoring

Battery information is observed through Android's `ACTION_BATTERY_CHANGED` broadcast.

A `BroadcastReceiver` is registered from Compose using `DisposableEffect` and unregistered when the battery widget leaves composition.

This allows the UI to react to battery changes without continuously polling device state.

---

## 🕐 Efficient Clock Updates

The clock uses `LocalTime` with Compose state.

Because the UI displays hours and minutes rather than seconds, it does not need to wake every second. Instead, the coroutine calculates the remaining time until the next minute boundary and sleeps until an update is actually required.

This reduces unnecessary wake-ups and recompositions for a UI intended to remain active for long periods.

---

## ⚠️ Platform Constraint

Unlike iPhone StandBy, a regular third-party Android application cannot arbitrarily wake the device and launch its own fullscreen UI when a charger is connected and the phone is rotated.

StandBy intentionally works within Android's public application APIs and uses the system `DreamService` / screensaver mechanism.

Achieving tighter system-level activation behavior would require privileged system access or modifications at the Android platform/AOSP level.

---

## 🎯 What This Project Explores

- Android `DreamService` and system-level UI
- Hosting Jetpack Compose outside an Activity
- Custom AndroidX lifecycle integration
- Compose state and side-effect APIs
- Coroutines and lifecycle-aware resource management
- Android battery and charging state
- Landscape-aware ambient UI
- OLED burn-in mitigation

---

## 🚀 Future Improvements

- User-configurable widget layouts
- DataStore persistence for preferences
- Multiple clock styles
- Configurable ambient colors
- Improved timer persistence
- Automated unit and UI tests

---

## ✅ Status

**Core StandBy experience complete.**

The current implementation includes DreamService integration, charging and landscape validation, a Compose-based ambient dashboard, full-screen clock mode, Clock/Calendar/Battery/Timer widgets, animated widget switching, and OLED burn-in mitigation.
