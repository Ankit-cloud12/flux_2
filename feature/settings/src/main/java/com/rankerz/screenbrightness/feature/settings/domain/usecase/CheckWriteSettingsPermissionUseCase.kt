package com.rankerz.screenbrightness.feature.settings.domain.usecase

import com.rankerz.screenbrightness.core.domain.manager.SystemPermissionsManager
import javax.inject.Inject

/**
 * Use case to check if the Write Settings permission is granted.
 */
class CheckWriteSettingsPermissionUseCase @Inject constructor(
    private val systemPermissionsManager: SystemPermissionsManager
) {
    operator fun invoke(): Boolean {
        return systemPermissionsManager.hasWriteSettingsPermission()
    }
}