package com.rankerz.screenbrightness.feature.settings.domain.usecase

import com.rankerz.screenbrightness.core.domain.manager.SystemPermissionsManager
import javax.inject.Inject

/**
 * Use case to request the Write Settings permission.
 * This will typically launch the system settings screen for the user to grant permission.
 */
class RequestWriteSettingsPermissionUseCase @Inject constructor(
    private val systemPermissionsManager: SystemPermissionsManager
) {
    operator fun invoke() {
        // The actual permission request logic (starting an Intent) is in the implementation
        systemPermissionsManager.requestWriteSettingsPermission()
    }
}