package com.rankerz.screenbrightness.core.data.manager

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings
import android.util.Log
import com.rankerz.screenbrightness.core.domain.manager.SystemPermissionsManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SystemPermissionsManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SystemPermissionsManager {

    override fun hasWriteSettingsPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.System.canWrite(context)
        } else {
            // Below M, this permission is granted at install time if declared in manifest
            // However, WRITE_SETTINGS is protection level signature|privileged|development|appop|retailDemo
            // so a normal app cannot get it pre-M anyway unless it's a system app.
            // For simplicity, assume false for older versions unless it's a system app scenario.
            Log.w("SystemPermissionsManager", "Write Settings check pre-M might be inaccurate for non-system apps.")
            false
        }
    }

    override fun hasOverlayPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(context)
        } else {
            // Below M, SYSTEM_ALERT_WINDOW is granted at install time if declared.
            // Check AppOpsManager as a fallback, though less reliable pre-M.
            try {
                val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
                val mode = appOps.checkOpNoThrow(
                    AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW,
                    Process.myUid(),
                    context.packageName
                )
                mode == AppOpsManager.MODE_ALLOWED || mode == AppOpsManager.MODE_DEFAULT // Default might allow based on manifest
            } catch (e: Exception) {
                Log.e("SystemPermissionsManager", "Error checking overlay permission pre-M", e)
                false // Assume false on error
            }
        }
    }

    override fun hasUsageStatsPermission(): Boolean {
        // Check actual permission using AppOpsManager
        return try {
            val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                appOps.unsafeCheckOpNoThrow( // Use unsafeCheckOpNoThrow on Q+
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    Process.myUid(),
                    context.packageName
                )
            } else {
                @Suppress("DEPRECATION")
                appOps.checkOpNoThrow( // Use checkOpNoThrow pre-Q
                    AppOpsManager.OPSTR_GET_USAGE_STATS,
                    Process.myUid(),
                    context.packageName
                )
            }
            mode == AppOpsManager.MODE_ALLOWED
        } catch (e: Exception) {
            Log.e("SystemPermissionsManager", "Error checking usage stats permission", e)
            false
        }
    }

    override fun requestWriteSettingsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.System.canWrite(context)) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                    data = Uri.parse("package:${context.packageName}")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Needed when starting from non-Activity context
                }
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("SystemPermissionsManager", "Failed to launch Write Settings screen", e)
                // Optionally notify the user via a different mechanism (e.g., Toast, Snackbar)
            }
        } else {
             Log.i("SystemPermissionsManager", "Write Settings permission already granted or not applicable.")
        }
    }

    override fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
             try {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                    data = Uri.parse("package:${context.packageName}")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("SystemPermissionsManager", "Failed to launch Overlay permission screen", e)
            }
        } else {
             Log.i("SystemPermissionsManager", "Overlay permission already granted or not applicable.")
        }
    }

     override fun requestUsageStatsPermission() {
        // Only needed if hasUsageStatsPermission() returns false
        if (!hasUsageStatsPermission()) {
             try {
                val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
                    // No package URI needed for this action
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("SystemPermissionsManager", "Failed to launch Usage Access settings screen", e)
            }
        } else {
             Log.i("SystemPermissionsManager", "Usage Stats permission already granted.")
        }
    }
}