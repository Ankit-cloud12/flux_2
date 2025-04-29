# RankerZ Screen Brightness & Temperature Control

RankerZ is a native Android application designed to provide advanced control over screen brightness and color temperature, offering features beyond standard system settings.

## Features

*   **Brightness Control:** Adjust screen brightness, including levels below the system minimum (using a screen overlay).
*   **Color Temperature Control:** Adjust the screen's color temperature (warmth/coolness).
*   **User Profiles:** Create, save, and apply custom brightness and temperature profiles.
*   **Scheduling:** Schedule automatic changes to brightness/temperature based on time or sunrise/sunset (execution logic pending).
*   **Per-App Settings:** Define specific brightness/temperature settings for individual applications (detection and application logic pending).
*   **Settings:**
    *   Grant necessary permissions (Overlay, Write Settings).
    *   Configure app theme (System, Light, Dark).
    *   Enable/Disable starting the service on device boot.

## Technology Stack

*   **Language:** Kotlin
*   **UI:** Jetpack Compose (Material 3)
*   **Architecture:** Multi-module Clean Architecture (MVVM in feature modules)
*   **Dependency Injection:** Hilt
*   **Asynchronous Programming:** Kotlin Coroutines & Flow
*   **Database:** Room Persistence Library (for Profiles, Schedules, AppProfiles)
*   **Preferences:** Jetpack DataStore (for simple settings like Theme, Start on Boot)
*   **Build System:** Gradle

## Architecture Overview

The project follows a multi-module Clean Architecture pattern to promote separation of concerns, testability, and maintainability.

*   **`app`:** The main application module, responsible for tying everything together, including dependency injection setup (Hilt Application), main activity, navigation graph, and manifest declarations.
*   **`core`:** Contains shared code used across multiple modules.
    *   **`core:common`:** Utility classes, constants, base classes.
    *   **`core:data`:** Repository implementations, data source implementations (delegating to `data:local` and `data:system`).
    *   **`core:domain`:** Core business logic, including repository interfaces, use cases (interactors), and domain models.
    *   **`core:ui`:** Shared Jetpack Compose UI components, themes, typography, colors.
*   **`data`:** Data layer modules responsible for data retrieval and storage.
    *   **`data:local`:** Local data sources using Room (database) and DataStore (preferences). Contains DAOs, Entities, Database definition, Hilt modules for local sources.
    *   **`data:system`:** System-level interactions, including `Service`s (e.g., `ScreenOverlayService`), `BroadcastReceiver`s (e.g., `BootReceiver`), and managers for interacting with system settings (`SystemSettingsManager`) and permissions (`SystemPermissionsManager`).
*   **`feature`:** Individual feature modules, each following a similar internal structure (often MVVM):
    *   **`feature:brightness`**
    *   **`feature:temperature`**
    *   **`feature:scheduling`**
    *   **`feature:profiles`**
    *   **`feature:perapp`**
    *   **`feature:settings`**
    *   Each feature module typically contains `domain/usecase`, `ui/viewmodel`, `ui/state`, `ui/screens`, and potentially `ui/components` sub-packages.

## Getting Started

### Prerequisites

*   Android Studio (latest stable version recommended)
*   Android SDK (API Level 34 recommended for building)
*   Emulator or Physical Device (running Android API Level 26 or higher)

### Build & Run

1.  **Open Project:** Open the project root directory (`flux_2`) in Android Studio.
2.  **Sync Project:** Allow Android Studio to sync the project with Gradle files.
3.  **Select Device:** Choose a target emulator or connect a physical device (with USB Debugging enabled).
4.  **Run:** Select the `app` configuration and click the "Run" button (green triangle) or press `Shift + F10`.

Android Studio will build, install, and launch the application on the selected device/emulator.

## Testing

Manual testing can be performed by launching the app and interacting with the UI:

1.  **Navigation:** Verify bottom navigation works correctly.
2.  **Brightness/Temperature:** Test sliders and observe UI/screen changes (requires permissions).
3.  **Settings:** Test permission granting, theme switching, and start-on-boot toggle persistence (requires app restart/device reboot).
4.  **Profiles/Schedules/AppProfiles:** Test Add/Edit/Delete functionality via dialogs and verify data persistence (requires app restart).
5.  **Overlay:** Test overlay activation/deactivation and notification controls.
6.  **Boot:** Test the "Start on Boot" functionality by enabling the setting and rebooting the device.

*(See previous conversation logs for a more detailed manual testing checklist)*

## Current Status (as of last update)

*   **Implemented:**
    *   Core architecture and module structure.
    *   Hilt dependency injection setup.
    *   Jetpack Compose Navigation.
    *   Room persistence for Profiles, Schedules, AppProfiles.
    *   DataStore persistence for Theme & Start on Boot settings.
    *   Core brightness/temperature setting logic.
    *   Screen overlay service (basic functionality).
    *   Permission checking and request logic.
    *   Boot receiver for starting service.
    *   Basic UI for all features, including Add/Edit dialogs for Profiles & Schedules.
*   **Pending/TODO:**
    *   Implementation of schedule execution logic (using `AlarmManager` or `WorkManager`).
    *   Implementation of Per-App feature logic (foreground app detection, likely via Accessibility Service).
    *   Implementation of Sunrise/Sunset schedule type.
    *   Refinement of UI/UX (e.g., showing profile names instead of IDs, app icons, better error display).
    *   Comprehensive error handling.
    *   Unit and Integration tests.
    *   Code cleanup and documentation improvements.

## License

MIT
