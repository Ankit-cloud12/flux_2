package com.rankerz.screenbrightness.feature.settings.domain.usecase

import com.rankerz.screenbrightness.core.domain.manager.SystemPermissionsManager
import javax.inject.Inject

/**
 * Use case to check if the overlay permission (SYSTEM_ALERT_WINDOW) is granted.
 */
class CheckOverlayPermissionUseCase @Inject constructor(
    private val systemPermissionsManager: SystemPermissionsManager
) {
    operator fun invoke(): Boolean {
        return systemPermissionsManager.hasOverlayPermission()
    }
}