package com.rankerz.screenbrightness.data.system.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.rankerz.screenbrightness.core.common.util.Constants
import com.rankerz.screenbrightness.core.domain.repository.SettingsRepository
import com.rankerz.screenbrightness.data.system.service.ScreenOverlayService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var settingsRepository: SettingsRepository // Inject repository

    private val receiverScope = CoroutineScope(SupervisorJob() + Dispatchers.IO) // Scope for background work

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d("BootReceiver", "Boot completed event received.")

            receiverScope.launch {
                val startOnBoot = settingsRepository.getStartOnBootEnabled().firstOrNull() ?: false
                Log.d("BootReceiver", "Start on boot preference: $startOnBoot")

                if (startOnBoot) {
                    try {
                        Log.d("BootReceiver", "Starting ScreenOverlayService...")
                        val serviceIntent = Intent(context, ScreenOverlayService::class.java).apply {
                            // Optionally add default values if needed, though service has defaults
                            // action = Constants.ACTION_SHOW_OVERLAY
                            // putExtra(Constants.EXTRA_BRIGHTNESS, 0.5f) // Example default
                            // putExtra(Constants.EXTRA_TEMPERATURE, 6500) // Example default
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            context.startForegroundService(serviceIntent)
                        } else {
                            context.startService(serviceIntent)
                        }
                        Log.d("BootReceiver", "ScreenOverlayService start command sent.")
                    } catch (e: Exception) {
                        Log.e("BootReceiver", "Failed to start ScreenOverlayService on boot", e)
                    }
                }
            }
        }
    }
}